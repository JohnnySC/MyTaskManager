package com.github.johnnysc.mytaskmanager.crud;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;

import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.bean.Category;
import com.github.johnnysc.mytaskmanager.bean.CategoryType;
import com.github.johnnysc.mytaskmanager.bean.Task;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Asatryan on 05.04.18.
 */

public final class CRUDModelImpl implements CRUDModel {

    public static final int CREATE = 0;
    public static final int READ = 1;
    public static final int EDIT = 2;

    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";

    private final java.text.DateFormat SIMPLE_DATE_FORMAT;
    private final CRUDPresenter mPresenter;
    private final Realm mRealm;

    @CategoryType.TaskType
    private int mTaskType;
    @TaskActionType
    private int mActionType;
    private long mTaskId;

    private Task mCurrentTask;
    private InitialData mInitialData;
    private Date mNotifyDate;
    private DateAndTime mDateAndTime;

    CRUDModelImpl(CRUDPresenter presenter) {
        mPresenter = presenter;
        mRealm = Realm.getDefaultInstance();
        SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT);
    }

    @Override
    public void onCreate(int taskType, int actionType, long taskId) {
        mTaskType = taskType;
        mActionType = actionType;
        mTaskId = taskId;
        final boolean isActionTypeCreate = isActionTypeCreate();
        mPresenter.setTitleResId(isActionTypeCreate ? R.string.create_task : R.string.view_task);
        mPresenter.initUi();
        if (!isActionTypeCreate) {
            mCurrentTask = getTaskByPrimaryKey(mTaskId);
        }
        mInitialData = new InitialData(isActionTypeCreate ? new Task() : mCurrentTask);
        mPresenter.initSpinner(taskType, isActionTypeCreate);
        mPresenter.initDateCreated(R.string.created, mInitialData.getInitialDateCreated());
        mPresenter.setDoneCheckBoxChecked(mInitialData.isInitialDone());
        mPresenter.setDoneCheckBoxOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isActionTypeCreate) {
                        mRealm.executeTransaction(realm -> mCurrentTask.setDone(isChecked));
                    }
                }
        );
        mPresenter.setDeadlineTimeOnClickListener();
        if (mInitialData.getInitialNotifyDate() != null) {
            mPresenter.setDeadlineTimeText(SIMPLE_DATE_FORMAT.format(mInitialData.getInitialNotifyDate()));
        }
        mPresenter.setDeadlineTimeClickable(isActionTypeCreate);
        mPresenter.setNotifyCheckBoxChecked(mInitialData.isInitialNotify());
        mPresenter.setNotifyCheckBoxEnabled(isActionTypeCreate);
        mPresenter.setInputsTexts(mInitialData.getInitialTitle(), mInitialData.getInitialBody());
        mPresenter.setInputsEnabled(isActionTypeCreate);
        mPresenter.addInputsTextWatcher(new InputTextWatcher());
        int buttonResId = isActionTypeCreate
                ? android.R.drawable.ic_menu_save
                : android.R.drawable.ic_menu_edit;
        mPresenter.setActionButtonImageRes(buttonResId);
        mPresenter.setActionButtonEnabled(!isActionTypeCreate);
        mPresenter.setActionButtonOnClickListener();
    }

    @Override
    public void onActionButtonClicked(String titleText,
                                      String bodyText,
                                      boolean done,
                                      boolean notify,
                                      int spinnerPosition) {
        if (isActionTypeCreate()) {
            mRealm.executeTransaction(realm -> {
                Task task = new Task();
                task.setTitle(titleText);
                task.setBody(bodyText);
                mTaskId = new Date().getTime();
                task.setId(mTaskId);
                task.setDone(done);
                if (mNotifyDate != null && notify) {
                    task.setNotify(true);
                    task.setDeadline(mNotifyDate.getTime());
                }
                Category category = getCategoryByPrimaryKey(spinnerPosition);
                category.getTasks().add(task);
            });
            if (notify && mNotifyDate != null) {
                mPresenter.scheduleNotification((int) mTaskId, mNotifyDate.getTime(), mTaskId, mTaskType);
            }
            mPresenter.setResultOkAndFinish();
        } else if (mActionType == READ) {
            mPresenter.setInputsEnabled(true);
            mPresenter.setInputsEnabled(true);
            mActionType = EDIT;
            mPresenter.setActionButtonImageRes(android.R.drawable.ic_menu_save);
            mPresenter.setDeleteMenuItemVisible(true);
            mPresenter.setNotifyCheckBoxEnabled(true);
            mPresenter.setDeadlineTimeClickable(true);
        } else { //EDIT
            mRealm.executeTransaction(realm -> {
                Task task = getTaskByPrimaryKey(mTaskId);
                task.setTitle(titleText);
                task.setBody(bodyText);
                task.setDone(done);
                if (notify) { //if user wants notify
                    if (mNotifyDate == null) { // if no new notify date
                        task.setNotify(false); // just cancel it
                    } else {
                        task.setNotify(true); // here everything is ok
                        task.setDeadline(mNotifyDate.getTime()); // so we update the date
                    }
                } else {
                    task.setNotify(false); //no notify, no problem
                }
            });
            if (notify && mNotifyDate != null) {
                mPresenter.scheduleNotification((int) mTaskId, mNotifyDate.getTime(), mTaskId, mTaskType);
            } else if (!notify) {
                mPresenter.cancelNotification((int) mTaskId, mTaskId, mTaskType);
            }
            mPresenter.setResultOkAndFinish();
        }
    }

    @Override
    public void handleBackPressed(boolean isDoneChecked) {
        if (mActionType == EDIT) {
            mActionType = READ;
            mPresenter.setActionButtonImageRes(android.R.drawable.ic_menu_edit);
            mPresenter.setActionButtonEnabled(true);
            mPresenter.setDeleteMenuItemVisible(false);
            mPresenter.setInputsEnabled(false);
            mPresenter.setInputsTexts(mInitialData.getInitialTitle(), mInitialData.getInitialBody());
            mPresenter.setNotifyCheckBoxEnabled(false);
            mPresenter.setNotifyCheckBoxChecked(mInitialData.isInitialNotify());
            Date initialNotifyDate = mInitialData.getInitialNotifyDate();
            String deadline = initialNotifyDate == null ? null : SIMPLE_DATE_FORMAT.format(initialNotifyDate);
            mPresenter.setDeadlineTimeText(deadline);
        } else {
            if (mInitialData.isInitialDone() == isDoneChecked) { //no changes for done
                mPresenter.handleOnBackPressed(); //simple backPressed
            } else {
                mPresenter.setResultOkAndFinish();
            }
        }
    }

    @Override
    public void doOnDatePicked(int day, int month, int year) {
        mDateAndTime = new DateAndTime();
        mDateAndTime.setDate(day, month, year);
    }

    @Override
    public void doOnTimePicked(int hour, int minute) {
        mDateAndTime.setTime(hour, minute);
        mNotifyDate = mDateAndTime.getDate();
        mPresenter.setDeadlineTimeText(SIMPLE_DATE_FORMAT.format(mNotifyDate));
    }

    @Override
    public void deleteTask() {
        mRealm.executeTransaction(realm -> {
            RealmResults<Task> rows = realm.where(Task.class).equalTo("id", mTaskId).findAll();
            rows.clear();
        });
        mPresenter.setResultOkAndFinish();
    }

    //region private methods
    private boolean isActionTypeCreate() {
        return mActionType == CREATE;
    }

    private Category getCategoryByPrimaryKey(int id) {
        return mRealm.where(Category.class).equalTo("id", id).findFirst();
    }

    private Task getTaskByPrimaryKey(long id) {
        return mRealm.where(Task.class).equalTo("id", id).findFirst();
    }
    //endregion

    //region private classes
    private class InputTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //nothing to do here
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mPresenter.updateActionButtonVisibility(mInitialData.getInitialTitle(), mInitialData.getInitialBody());
        }
    }

    private class InitialData {
        private final String initialDateCreated;
        private final boolean initialDone;
        private final boolean initialNotify;
        private final Date initialNotifyDate;
        private final String initialTitle;
        private final String initialBody;

        InitialData(Task task) {
            initialDone = task.isDone();
            initialNotify = task.isNotify();
            initialNotifyDate = task.getDeadline() > 0 ? new Date(task.getDeadline()) : null;
            initialTitle = task.getTitle();
            initialBody = task.getBody();
            Date date = isActionTypeCreate() ? new Date() : new Date(task.getId());
            initialDateCreated = " " + SIMPLE_DATE_FORMAT.format(date);
        }

        boolean isInitialDone() {
            return initialDone;
        }

        boolean isInitialNotify() {
            return initialNotify;
        }

        @Nullable
        Date getInitialNotifyDate() {
            return initialNotifyDate;
        }

        String getInitialTitle() {
            return initialTitle;
        }

        String getInitialBody() {
            return initialBody;
        }

        String getInitialDateCreated() {
            return initialDateCreated;
        }
    }

    private class DateAndTime {
        private int day, month, year, hour, minute;

        void setDate(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        void setTime(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        java.util.Date getDate() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(this.year, this.month, this.day, this.hour, this.minute);
            return calendar.getTime();
        }
    }
    //endregion

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CREATE, READ, EDIT})
    public @interface TaskActionType {
    }
}