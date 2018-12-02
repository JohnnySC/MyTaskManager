package com.github.johnnysc.mytaskmanager.details.domain;

import com.github.johnnysc.mytaskmanager.main.data.model.CategoryType;
import com.github.johnnysc.mytaskmanager.main.data.model.Task;
import com.github.johnnysc.mytaskmanager.main.domain.TasksRepository;

/**
 * @author Asatryan on 02.12.18
 */
public final class TaskListInteractorImpl implements TaskListInteractor {

    private final TasksRepository mTasksRepository;
    private DataCallback mDataCallback;

    public TaskListInteractorImpl(TasksRepository tasksRepository) {
        mTasksRepository = tasksRepository;
    }

    @Override
    public void getTaskList(@CategoryType.TaskType int taskType) {
        if (mDataCallback != null) {
            mDataCallback.provideTaskList(mTasksRepository.getTasksSorted(taskType));
        }
    }

    @Override
    public void moveTask(@CategoryType.TaskType int taskType, int oldPosition, int newPosition) {
        mTasksRepository.moveTask(taskType, oldPosition, newPosition);
    }

    @Override
    public void removeTask(@CategoryType.TaskType int taskType, int position) {
        final Task task = mTasksRepository.getTask(taskType, position);
        if (task.isDone()) {
            mTasksRepository.removeTask(task.getId());
            if (mDataCallback != null) {
                mDataCallback.provideRemovedTaskPosition(position);
            }
        } else {
            if (mDataCallback != null) {
                mDataCallback.refreshData();
            }
        }
    }

    @Override
    public void getTaskCategory(@CategoryType.TaskType int taskType) {
        if (mDataCallback != null) {
            mDataCallback.provideTaskCategoryTitle(mTasksRepository.getCategoryTitle(taskType));
        }
    }

    @Override
    public void setTaskDone(@CategoryType.TaskType int taskType, long id, boolean done) {
        mTasksRepository.setTaskDone(id, done);
        mTasksRepository.sortTaskList(taskType);
    }

    @Override
    public void setDataCallback(DataCallback dataCallback) {
        mDataCallback = dataCallback;
    }
}