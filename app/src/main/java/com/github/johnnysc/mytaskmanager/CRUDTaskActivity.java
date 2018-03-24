package com.github.johnnysc.mytaskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.johnnysc.mytaskmanager.model.Category;
import com.github.johnnysc.mytaskmanager.model.CategoryType;
import com.github.johnnysc.mytaskmanager.model.Task;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Here we can create, read, update and delete the task.
 * It gets extra params as task type {@link CategoryType#CATEGORY_TYPES}
 * and action type {@link TaskActionType} to define is it new or existing task
 * Depending on that, need to show or hide extra actions like delete & modify
 */
public class CRUDTaskActivity extends BaseActivity {

    public static final int CREATE = 0;
    public static final int READ = 1;
    public static final int EDIT = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CREATE, READ, EDIT})
    public @interface TaskActionType {
    }

    private static final String EXTRA_TASK_TYPE = "extra_task_type";
    private static final String EXTRA_ACTION_TYPE = "extra_action_type";
    private static final String EXTRA_TASK_ID = "extra_task_id";
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final int INPUT_MIN_LENGTH = 4;

    @CategoryType.TaskType
    private int mTaskType;
    @TaskActionType
    private int mActionType;
    private long mTaskId;

    private Toolbar mToolbar;
    private TextView mDateTextView;
    private TextInputEditText mTitleEditText;
    private TextInputEditText mBodyEditText;
    private FloatingActionButton mActionButton;
    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpinnerAdapter;
    private TextWatcher mTextWatcher;
    private String mInitialTitle;
    private String mInitialBody;

    public static Intent newIntent(Context context,
                                   @CategoryType.TaskType int taskType,
                                   @TaskActionType int actionType,
                                   long taskId) {
        Intent intent = new Intent(context, CRUDTaskActivity.class);
        intent.putExtra(EXTRA_TASK_TYPE, taskType);
        intent.putExtra(EXTRA_ACTION_TYPE, actionType);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }

    public static Intent newIntent(Context context,
                                   @CategoryType.TaskType int taskType,
                                   @TaskActionType int actionType) {
        return newIntent(context, taskType, actionType, -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_task);
        initExtraData();
        initUi();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mActionType == EDIT) {
            mActionType = READ;
            mActionButton.setImageResource(android.R.drawable.ic_menu_edit);
            mTitleEditText.setEnabled(false);
            mBodyEditText.setEnabled(false);
            mTitleEditText.setText(mInitialTitle);
            mBodyEditText.setText(mInitialBody);
            // TODO: 24.03.18 hide delete item in toolbar
        } else {
            super.onBackPressed();
        }
    }

    private void initUi() {
        initToolbar();
        initSpinner();
        initDate();
        initInputs();
        initActionButton();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(isActionTypeCreate() ? R.string.create_task : R.string.view_task);
    }

    private void initSpinner() {
        mSpinner = findViewById(R.id.spinner);
        initSpinnerAdapter();
        mSpinner.setAdapter(mSpinnerAdapter);
        mSpinner.setSelection(mTaskType);
        mSpinner.setEnabled(isActionTypeCreate());
    }

    private void initSpinnerAdapter() {
        String[] data = new String[4];
        for (int i = 0; i < CATEGORY_STRINGS.size(); i++) {
            data[i] = getString(CATEGORY_STRINGS.get(i));
        }
        mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, data);
    }

    private void initDate() {
        mDateTextView = findViewById(R.id.date_text_view);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = isActionTypeCreate()
                ? new Date()
                : new Date(getTaskByPrimaryKey(mTaskId).getId());
        String text = getString(R.string.created) + " " + dateFormat.format(date);
        mDateTextView.setText(text);
    }

    private void initInputs() {
        mTitleEditText = findViewById(R.id.title_edit_text);
        mBodyEditText = findViewById(R.id.body_edit_text);
        mTextWatcher = new InputTextWatcher();
        mInitialTitle = "";
        mInitialBody = "";
        if (!isActionTypeCreate()) {
            Task task = getTaskByPrimaryKey(mTaskId);
            mInitialTitle = task.getTitle();
            mInitialBody = task.getBody();
        }
        mTitleEditText.setText(mInitialTitle);
        mBodyEditText.setText(mInitialBody);
        mTitleEditText.setEnabled(isActionTypeCreate());
        mBodyEditText.setEnabled(isActionTypeCreate());
        mTitleEditText.addTextChangedListener(mTextWatcher);
        mBodyEditText.addTextChangedListener(mTextWatcher);
    }

    private void initActionButton() {
        mActionButton = findViewById(R.id.action_fab);
        int buttonResId = isActionTypeCreate()
                ? android.R.drawable.ic_menu_save
                : android.R.drawable.ic_menu_edit;
        mActionButton.setImageResource(buttonResId);
        mActionButton.setEnabled(false);
        mActionButton.setOnClickListener(view -> handleActionButtonClick());
    }

    private void handleActionButtonClick() {
        if (isActionTypeCreate()) {
            Task task = new Task();
            task.setTitle(mTitleEditText.getText().toString());
            task.setBody(mBodyEditText.getText().toString());
            task.setId(new Date().getTime());
            mRealm.executeTransaction(realm -> {
                Category category = getCategoryByPrimaryKey(mSpinner.getSelectedItemPosition());
                category.getTasks().add(task);
            });
            setResultOkAndFinish();
        } else if (mActionType == READ) {
            mTitleEditText.setEnabled(true);
            mBodyEditText.setEnabled(true);
            mActionType = EDIT;
            mActionButton.setImageResource(android.R.drawable.ic_menu_save);
            // TODO: 24.03.18 make delete item in menu visible
        } else {
            mRealm.executeTransaction(realm -> {
                Task task = getTaskByPrimaryKey(mTaskId);
                task.setTitle(mTitleEditText.getText().toString());
                task.setBody(mBodyEditText.getText().toString());
            });
            setResultOkAndFinish();
        }
    }

    private void setResultOkAndFinish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateActionButtonVisibility() {
        boolean noChanges = mInitialTitle.equals(mTitleEditText.getText().toString())
                && mInitialBody.equals(mBodyEditText.getText().toString());
        boolean inputsNonEmpty = mTitleEditText.getText().length() > INPUT_MIN_LENGTH
                && mBodyEditText.getText().length() > INPUT_MIN_LENGTH;
        boolean enabled = !noChanges && inputsNonEmpty;
        mActionButton.setEnabled(enabled);
    }

    private boolean isActionTypeCreate() {
        return mActionType == CREATE;
    }

    private void initExtraData() {
        mTaskType = getIntent().getIntExtra(EXTRA_TASK_TYPE, 0);
        mActionType = getIntent().getIntExtra(EXTRA_ACTION_TYPE, 0);
        mTaskId = getIntent().getLongExtra(EXTRA_TASK_ID, -1L);
    }

    private class InputTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            updateActionButtonVisibility();
        }
    }
}