package com.github.johnnysc.mytaskmanager.main.data.model;

import android.support.annotation.StringRes;

/**
 * @author Asatryan on 02.12.18
 */
public final class TaskCategoryDataModel {

    @CategoryType.TaskType
    private final int mTaskType;

    @StringRes
    private final int mTitleResId;

    private final int mTaskCount;

    public TaskCategoryDataModel(@CategoryType.TaskType int taskType,
                                 @StringRes int titleResId,
                                 int taskCount) {
        mTaskType = taskType;
        mTitleResId = titleResId;
        mTaskCount = taskCount;
    }

    public int getTaskType() {
        return mTaskType;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getTaskCount() {
        return mTaskCount;
    }
}