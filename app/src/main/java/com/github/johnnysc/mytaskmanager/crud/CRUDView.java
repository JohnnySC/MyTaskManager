package com.github.johnnysc.mytaskmanager.crud;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextWatcher;
import android.widget.CompoundButton;

import com.github.johnnysc.mytaskmanager.main.data.model.CategoryType;

/**
 * @author Asatryan on 05.04.18.
 */

interface CRUDView {

    void initUi();

    void setTitleResId(@StringRes int titleResId);

    /**
     * @param selectionPosition the position of selection (mTaskType)
     * @param enabled           is enabled or disabled at start
     */
    void initSpinner(int selectionPosition, boolean enabled);

    /**
     * Need to concat 2 parts with getString(createdOnResId)
     *
     * @param createdOnResId first part
     * @param date           second part
     */
    void initDateCreated(@StringRes int createdOnResId, String date);

    void setDoneCheckBoxChecked(boolean checked);

    void setDoneCheckBoxOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener);

    void setDeadlineTimeClickable(boolean clickable);

    void setDeadlineTimeOnClickListener();

    void setDeadlineTimeText(String text);

    void setNotifyCheckBoxEnabled(boolean enabled);

    void setNotifyCheckBoxChecked(boolean checked);

    void setInputsTexts(String title, String body);

    void setInputsEnabled(boolean enabledAndFocusableInTouchMode);

    void addInputsTextWatcher(TextWatcher watcher);

    void updateActionButtonVisibility(String initialTitle, String initialBody);

    void setActionButtonImageRes(@DrawableRes int imageResId);

    void setActionButtonEnabled(boolean enabled);

    void setActionButtonOnClickListener();

    void setDeleteMenuItemVisible(boolean visible);

    void handleOnBackPressed();

    void scheduleNotification(int id, long futureInMillis,
                              long taskId,
                              @CategoryType.TaskType int taskType);

    void cancelNotification(int id);

    void setResultOkAndFinish();
}