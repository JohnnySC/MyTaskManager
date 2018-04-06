package com.github.johnnysc.mytaskmanager.crud;

import android.text.TextWatcher;
import android.widget.CompoundButton;

/**
 * @author Asatryan on 05.04.18.
 */

public final class CRUDPresenterImpl implements CRUDPresenter {

    private final CRUDView mView;
    private final CRUDModel mModel;

    public CRUDPresenterImpl(CRUDView view) {
        mView = view;
        mModel = new CRUDModelImpl(this);
    }

    @Override
    public void onCreate(int taskType, int actionType, long taskId) {
        mModel.onCreate(taskType, actionType, taskId);
    }

    @Override
    public void onActionButtonClicked(String titleText, String bodyText, boolean done, boolean notify, int spinnerPosition) {
        mModel.onActionButtonClicked(titleText, bodyText, done, notify, spinnerPosition);
    }

    @Override
    public void handleBackPressed(boolean isDoneChecked) {
        mModel.handleBackPressed(isDoneChecked);
    }

    @Override
    public void doOnDatePicked(int day, int month, int year) {
        mModel.doOnDatePicked(day, month, year);
    }

    @Override
    public void doOnTimePicked(int hour, int minute) {
        mModel.doOnTimePicked(hour, minute);
    }

    @Override
    public void deleteTask() {
        mModel.deleteTask();
    }

    @Override
    public void initUi() {
        mView.initUi();
    }

    @Override
    public void setTitleResId(int titleResId) {
        mView.setTitleResId(titleResId);
    }

    @Override
    public void initSpinner(int selectionPosition, boolean enabled) {
        mView.initSpinner(selectionPosition, enabled);
    }

    @Override
    public void initDateCreated(int createdOnResId, String date) {
        mView.initDateCreated(createdOnResId, date);
    }

    @Override
    public void setDoneCheckBoxChecked(boolean checked) {
        mView.setDoneCheckBoxChecked(checked);
    }

    @Override
    public void setDoneCheckBoxOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mView.setDoneCheckBoxOnCheckedChangeListener(listener);
    }

    @Override
    public void setDeadlineTimeClickable(boolean clickable) {
        mView.setDeadlineTimeClickable(clickable);
    }

    @Override
    public void setDeadlineTimeOnClickListener() {
        mView.setDeadlineTimeOnClickListener();
    }

    @Override
    public void setDeadlineTimeText(String text) {
        mView.setDeadlineTimeText(text);
    }

    @Override
    public void setNotifyCheckBoxEnabled(boolean enabled) {
        mView.setNotifyCheckBoxEnabled(enabled);
    }

    @Override
    public void setNotifyCheckBoxChecked(boolean checked) {
        mView.setNotifyCheckBoxChecked(checked);
    }

    @Override
    public void setInputsTexts(String title, String body) {
        mView.setInputsTexts(title, body);
    }

    @Override
    public void setInputsEnabled(boolean enabledAndFocusableInTouchMode) {
        mView.setInputsEnabled(enabledAndFocusableInTouchMode);
    }

    @Override
    public void addInputsTextWatcher(TextWatcher watcher) {
        mView.addInputsTextWatcher(watcher);
    }

    @Override
    public void updateActionButtonVisibility(String initialTitle, String initialBody) {
        mView.updateActionButtonVisibility(initialTitle, initialBody);
    }

    @Override
    public void setActionButtonImageRes(int imageResId) {
        mView.setActionButtonImageRes(imageResId);
    }

    @Override
    public void setActionButtonEnabled(boolean enabled) {
        mView.setActionButtonEnabled(enabled);
    }

    @Override
    public void setActionButtonOnClickListener() {
        mView.setActionButtonOnClickListener();
    }

    @Override
    public void setDeleteMenuItemVisible(boolean visible) {
        mView.setDeleteMenuItemVisible(visible);
    }

    @Override
    public void handleOnBackPressed() {
        mView.handleOnBackPressed();
    }

    @Override
    public void scheduleNotification(int id, long futureInMillis, long taskId, int taskType) {
        mView.scheduleNotification(id, futureInMillis, taskId, taskType);
    }

    @Override
    public void cancelNotification(int id, long taskId, int taskType) {
        mView.cancelNotification(id, taskId, taskType);
    }

    @Override
    public void setResultOkAndFinish() {
        mView.setResultOkAndFinish();
    }
}