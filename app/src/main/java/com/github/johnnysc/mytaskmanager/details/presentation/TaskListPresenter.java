package com.github.johnnysc.mytaskmanager.details.presentation;

import com.github.johnnysc.mytaskmanager.main.data.model.CategoryType;
import com.github.johnnysc.mytaskmanager.main.presentation.LifeCycleEvents;

/**
 * @author Asatryan on 02.12.18
 */
public interface TaskListPresenter extends LifeCycleEvents<TaskListView> {

    void getCategoryType(@CategoryType.TaskType int taskType);

    void getTaskList(@CategoryType.TaskType int taskType);

    void moveTask(@CategoryType.TaskType int taskType, int oldPosition, int newPosition);

    void removeTask(@CategoryType.TaskType int taskType, int position);

    void setTaskDone(@CategoryType.TaskType int taskType, long id, boolean done);
}