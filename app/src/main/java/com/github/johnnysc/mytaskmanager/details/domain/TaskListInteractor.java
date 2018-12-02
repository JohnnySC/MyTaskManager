package com.github.johnnysc.mytaskmanager.details.domain;

import android.support.annotation.StringRes;

import com.github.johnnysc.mytaskmanager.main.data.model.CategoryType;
import com.github.johnnysc.mytaskmanager.main.data.model.Task;
import com.github.johnnysc.mytaskmanager.main.domain.DataCallbackSetter;

import java.util.List;

/**
 * @author Asatryan on 02.12.18
 */
public interface TaskListInteractor extends DataCallbackSetter<TaskListInteractor.DataCallback> {

    void getTaskList(@CategoryType.TaskType int taskType);

    void moveTask(@CategoryType.TaskType int taskType, int oldPosition, int newPosition);

    void removeTask(@CategoryType.TaskType int taskType, int position);

    void getTaskCategory(@CategoryType.TaskType int taskType);

    void setTaskDone(@CategoryType.TaskType int taskType, long id, boolean done);

    interface DataCallback {

        void provideTaskList(List<Task> taskList);

        void provideTaskCategoryTitle(@StringRes int titleResId);

        void provideRemovedTaskPosition(int position);

        void refreshData();
    }
}