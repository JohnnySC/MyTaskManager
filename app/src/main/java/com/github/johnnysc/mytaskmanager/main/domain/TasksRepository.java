package com.github.johnnysc.mytaskmanager.main.domain;

import com.github.johnnysc.mytaskmanager.main.data.model.CategoryType;
import com.github.johnnysc.mytaskmanager.main.data.model.Task;
import com.github.johnnysc.mytaskmanager.main.data.model.TaskTypeDataModel;

import java.util.List;

/**
 * @author Asatryan on 02.12.18
 */
public interface TasksRepository {

    List<TaskTypeDataModel> getTasksCategoryDataModels();

    List<Task> getTasksSorted(@CategoryType.TaskType int taskType);

    void moveTask(@CategoryType.TaskType int taskType, int oldPosition, int newPosition);

    Task getTask(@CategoryType.TaskType int taskType, int position);

    void setTaskDone(long id, boolean done);

    void sortTaskList(@CategoryType.TaskType int taskType);

    void removeTask(long id);

    int getCategoryTitle(@CategoryType.TaskType int taskType);
}