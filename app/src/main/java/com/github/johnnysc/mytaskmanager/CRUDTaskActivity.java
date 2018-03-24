package com.github.johnnysc.mytaskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
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

import io.realm.RealmResults;

/**
 * Here we can create, read, update and delete the task.
 * It gets extra params as task type {@link CategoryType#CATEGORY_TYPES}
 * and action type {@link TaskActionType} to define is it new or existing task
 * Depending on that, need to show or hide extra actions like delete & modify
 */
public class CRUDTaskActivity extends BaseActivity {

    //region ActionType
    public static final int CREATE = 0;
    public static final int READ = 1;
    public static final int EDIT = 2;
    //region fields
    private static final String EXTRA_ACTION_TYPE = "extra_action_type";
    //endregion
    private static final String EXTRA_TASK_ID = "extra_task_id";
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final int INPUT_MIN_LENGTH = 4;
    @TaskActionType
    private int mActionType;
    private long mTaskId;
    private TextView mDateTextView;
    private TextInputEditText mTitleEditText;
    private TextInputEditText mBodyEditText;
    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpinnerAdapter;
    private AppCompatCheckBox mCheckBox;
    private TextWatcher mTextWatcher;
    private String mInitialTitle;
    private String mInitialBody;
    private boolean mCheckBoxInitialState;
    private MenuItem mDeleteMenuItem;

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

    //endregion

    public static Intent newIntent(Context context,
                                   @CategoryType.TaskType int taskType,
                                   @TaskActionType int actionType) {
        return newIntent(context, taskType, actionType, -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_task);
        initUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crud_menu, menu);
        mDeleteMenuItem = menu.findItem(R.id.delete_task);
        mDeleteMenuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.delete_task) {
            deleteTask();
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
            mTitleEditText.setFocusable(false);
            mBodyEditText.setFocusable(false);
            mTitleEditText.setText(mInitialTitle);
            mBodyEditText.setText(mInitialBody);
            mCheckBox.setChecked(mCheckBoxInitialState);
            mActionButton.setEnabled(true);
            mDeleteMenuItem.setVisible(false);
        } else {
            if (mCheckBoxInitialState != mCheckBox.isChecked()) {
                setResultOkAndFinish();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void initUi() {
        initTitle();
        initToolbar();
        initSpinner();
        initDate();
        initCheckBox();
        initInputs();
        initActionButton();
    }

    private void initTitle() {
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

    private void initCheckBox() {
        mCheckBox = findViewById(R.id.done_check_box);
        if (isActionTypeCreate()) {
            mCheckBoxInitialState = false;
        } else {
            Task task = getTaskByPrimaryKey(mTaskId);
            mCheckBoxInitialState = task.isDone();
        }
        mCheckBox.setChecked(mCheckBoxInitialState);
        mCheckBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (!isActionTypeCreate()) {
                mRealm.executeTransaction(realm -> getTaskByPrimaryKey(mTaskId).setDone(checked));
            }
        });
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
        mTitleEditText.setFocusableInTouchMode(isActionTypeCreate());
        mBodyEditText.setFocusableInTouchMode(isActionTypeCreate());
        mTitleEditText.addTextChangedListener(mTextWatcher);
        mBodyEditText.addTextChangedListener(mTextWatcher);
    }

    private void initActionButton() {
        initFab();
        int buttonResId = isActionTypeCreate()
                ? android.R.drawable.ic_menu_save
                : android.R.drawable.ic_menu_edit;
        mActionButton.setImageResource(buttonResId);
        mActionButton.setEnabled(!isActionTypeCreate());
        mActionButton.setOnClickListener(view -> handleActionButtonClick());
    }

    private void handleActionButtonClick() {
        if (isActionTypeCreate()) {
            Task task = new Task();
            task.setTitle(mTitleEditText.getText().toString());
            task.setBody(mBodyEditText.getText().toString());
            task.setId(new Date().getTime());
            task.setDone(mCheckBox.isChecked());
            mRealm.executeTransaction(realm -> {
                Category category = getCategoryByPrimaryKey(mSpinner.getSelectedItemPosition());
                category.getTasks().add(task);
            });
            setResultOkAndFinish();
        } else if (mActionType == READ) {
            mTitleEditText.setEnabled(true);
            mBodyEditText.setEnabled(true);
            mTitleEditText.setFocusableInTouchMode(true);
            mBodyEditText.setFocusableInTouchMode(true);
            mActionType = EDIT;
            mActionButton.setImageResource(android.R.drawable.ic_menu_save);
            mDeleteMenuItem.setVisible(true);
        } else {
            mRealm.executeTransaction(realm -> {
                Task task = getTaskByPrimaryKey(mTaskId);
                task.setTitle(mTitleEditText.getText().toString());
                task.setBody(mBodyEditText.getText().toString());
                task.setDone(mCheckBox.isChecked());

            });
            setResultOkAndFinish();
        }
    }

    private void deleteTask() {
        mRealm.executeTransaction(realm -> {
            RealmResults<Task> rows = realm.where(Task.class).equalTo("id", mTaskId).findAll();
            rows.clear();
        });
        setResultOkAndFinish();
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

    protected void initExtraData() {
        super.initExtraData();
        mActionType = getIntent().getIntExtra(EXTRA_ACTION_TYPE, 0);
        mTaskId = getIntent().getLongExtra(EXTRA_TASK_ID, -1L);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CREATE, READ, EDIT})
    public @interface TaskActionType {
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