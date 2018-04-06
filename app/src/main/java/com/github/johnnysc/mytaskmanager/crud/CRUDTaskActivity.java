package com.github.johnnysc.mytaskmanager.crud;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.johnnysc.mytaskmanager.BaseActivity;
import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.bean.CategoryType;
import com.github.johnnysc.mytaskmanager.dateandtime.DatePickerCallback;
import com.github.johnnysc.mytaskmanager.dateandtime.DatePickerFragment;
import com.github.johnnysc.mytaskmanager.dateandtime.TimePickerCallback;
import com.github.johnnysc.mytaskmanager.dateandtime.TimePickerFragment;
import com.github.johnnysc.mytaskmanager.notifications.NotificationActionService;
import com.github.johnnysc.mytaskmanager.notifications.TaskBroadcastReceiver;

/**
 * Here we can Create, Read, Update and Delete the task.
 * It gets extra params as task type {@link CategoryType#CATEGORY_TYPES}
 * and action type {@link CRUDModelImpl.TaskActionType} to define is it new or an existing task
 * Depending on that, need to show or hide extra actions like delete & modify
 *
 * @author Asatryan on 05.04.18.
 */
public class CRUDTaskActivity extends BaseActivity implements CRUDView, DatePickerCallback, TimePickerCallback {

    private static final String EXTRA_ACTION_TYPE = "extra_action_type";
    private static final String EXTRA_TASK_ID = "extra_task_id";

    private static final int INPUT_MIN_LENGTH = 4;

    private AppCompatSpinner mSpinner;
    private AppCompatCheckBox mDoneCheckBox;
    private TextView mCreatedDateTextView;
    private AppCompatCheckBox mNotifyCheckBox;
    private TextView mDeadlineTextView;
    private TextInputEditText mTitleEditText;
    private TextInputEditText mBodyEditText;

    private MenuItem mDeleteMenuItem;
    private AlarmManager mAlarmManager;
    private CRUDPresenter mPresenter;

    public static Intent newIntent(Context context,
                                   @CategoryType.TaskType int taskType,
                                   @CRUDModelImpl.TaskActionType int actionType,
                                   long taskId) {
        Intent intent = new Intent(context, CRUDTaskActivity.class);
        intent.putExtra(EXTRA_TASK_TYPE, taskType);
        intent.putExtra(EXTRA_ACTION_TYPE, actionType);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }

    public static Intent newIntent(Context context,
                                   @CategoryType.TaskType int taskType,
                                   @CRUDModelImpl.TaskActionType int actionType) {
        return newIntent(context, taskType, actionType, -1);
    }

    //region lifecycle

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_task);
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mPresenter = new CRUDPresenterImpl(this);
        int actionType = getIntent().getIntExtra(EXTRA_ACTION_TYPE, 0);
        long taskId = getIntent().getLongExtra(EXTRA_TASK_ID, -1L);
        mPresenter.onCreate(mTaskType, actionType, taskId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crud_menu, menu);
        mDeleteMenuItem = menu.findItem(R.id.delete_task);
        setDeleteMenuItemVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.delete_task) {
            mPresenter.deleteTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mPresenter.handleBackPressed(mDoneCheckBox.isChecked());
    }

    //endregion

    @Override
    public void doOnDatePicked(DatePicker view, int year, int month, int day) {
        mPresenter.doOnDatePicked(day, month, year);
        TimePickerFragment newFragment = TimePickerFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void doOnTimePicked(TimePicker view, int hourOfDay, int minute) {
        mPresenter.doOnTimePicked(hourOfDay, minute);
    }

    @Override
    public void handleOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setTitleResId(int titleResId) {
        setTitle(titleResId);
    }

    @Override
    public void initUi() {
        super.initToolbar();
        mSpinner = findViewById(R.id.spinner);
        mCreatedDateTextView = findViewById(R.id.date_text_view);
        mDoneCheckBox = findViewById(R.id.done_check_box);
        mDeadlineTextView = findViewById(R.id.time_text_view);
        mNotifyCheckBox = findViewById(R.id.alarm_check_box);
        mTitleEditText = findViewById(R.id.title_edit_text);
        mBodyEditText = findViewById(R.id.body_edit_text);
        initFab();
    }

    @Override
    public void initSpinner(int selectionPosition, boolean enabled) {
        final String[] data = new String[4];
        for (int i = 0; i < CATEGORY_STRINGS.size(); i++) {
            data[i] = getString(CATEGORY_STRINGS.get(i));
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, data);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setSelection(selectionPosition);
        mSpinner.setEnabled(enabled);
    }

    @Override
    public void initDateCreated(int createdOnResId, String date) {
        String created = getString(createdOnResId) + date;
        mCreatedDateTextView.setText(created);
    }

    @Override
    public void setDoneCheckBoxChecked(boolean checked) {
        mDoneCheckBox.setChecked(checked);
    }

    @Override
    public void setDoneCheckBoxOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mDoneCheckBox.setOnCheckedChangeListener(listener);
    }

    @Override
    public void setDeadlineTimeClickable(boolean clickable) {
        mDeadlineTextView.setClickable(clickable);
    }

    @Override
    public void setDeadlineTimeOnClickListener() {
        mDeadlineTextView.setOnClickListener(v -> {
            DatePickerFragment newFragment = DatePickerFragment.newInstance();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
    }

    @Override
    public void setDeadlineTimeText(@Nullable String text) {
        String deadline = text == null ? getString(R.string.choose_date) : text;
        mDeadlineTextView.setText(deadline);
    }

    @Override
    public void setNotifyCheckBoxEnabled(boolean enabled) {
        mNotifyCheckBox.setEnabled(enabled);
    }

    @Override
    public void setNotifyCheckBoxChecked(boolean checked) {
        mNotifyCheckBox.setChecked(checked);
    }

    @Override
    public void setInputsTexts(String title, String body) {
        mTitleEditText.setText(title);
        mBodyEditText.setText(body);
    }

    @Override
    public void setInputsEnabled(boolean enabledAndFocusableInTouchMode) {
        mTitleEditText.setEnabled(enabledAndFocusableInTouchMode);
        mBodyEditText.setEnabled(enabledAndFocusableInTouchMode);
        mTitleEditText.setFocusableInTouchMode(enabledAndFocusableInTouchMode);
        mBodyEditText.setFocusableInTouchMode(enabledAndFocusableInTouchMode);
    }

    @Override
    public void addInputsTextWatcher(TextWatcher watcher) {
        mTitleEditText.addTextChangedListener(watcher);
        mBodyEditText.addTextChangedListener(watcher);
    }

    @Override
    public void updateActionButtonVisibility(String initialTitle, String initialBody) {
        boolean noChanges = mTitleEditText.getText().toString().equals(initialTitle)
                && mBodyEditText.getText().toString().equals(initialBody);
        boolean inputsNonEmpty = mTitleEditText.getText().length() > INPUT_MIN_LENGTH
                && mBodyEditText.getText().length() > INPUT_MIN_LENGTH;
        boolean enabled = !noChanges && inputsNonEmpty;
        setActionButtonEnabled(enabled);
    }

    @Override
    public void setActionButtonImageRes(int imageResId) {
        mActionButton.setImageResource(imageResId);
    }

    @Override
    public void setActionButtonEnabled(boolean enabled) {
        mActionButton.setEnabled(enabled);
    }

    @Override
    public void setActionButtonOnClickListener() {
        mActionButton.setOnClickListener(v -> mPresenter.onActionButtonClicked(
                mTitleEditText.getText().toString(),
                mBodyEditText.getText().toString(),
                mDoneCheckBox.isChecked(),
                mNotifyCheckBox.isChecked(),
                mSpinner.getSelectedItemPosition()));
    }

    @Override
    public void setDeleteMenuItemVisible(boolean visible) {
        mDeleteMenuItem.setVisible(visible);
    }

    @Override
    public void scheduleNotification(int id, long futureInMillis, long taskId, int taskType) {
        if (mAlarmManager == null) return;
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, makePendingIntent(id, taskId, taskType, false));
    }

    @Override
    public void cancelNotification(int id, long taskId, int taskType) {
        if (mAlarmManager == null) return;
        mAlarmManager.cancel(makePendingIntent(id, taskId, taskType, true));
    }

    @Override
    public void setResultOkAndFinish() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private PendingIntent makePendingIntent(int id, long taskId, int taskType, boolean cancel) {
        Intent notificationIntent = TaskBroadcastReceiver.newIntent(this, id, getNotification(taskId, taskType), cancel);
        notificationIntent.setAction(String.valueOf(id)); //needs to show different notifications
        return PendingIntent.getBroadcast(this, id, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
    }

    private Notification getNotification(long taskId, int taskType) {
        Intent actionIntent = NotificationActionService.newIntent(this, taskId, taskType);
        actionIntent.setAction(String.valueOf(taskId)); //needs to show different notifications
        PendingIntent actionPendingIntent = PendingIntent.getService(this, 0, actionIntent, PendingIntent.FLAG_ONE_SHOT);
        return new Notification.Builder(this)
                .setContentTitle(mTitleEditText.getText().toString())
                .setContentText(mBodyEditText.getText().toString())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(actionPendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();
    }
}