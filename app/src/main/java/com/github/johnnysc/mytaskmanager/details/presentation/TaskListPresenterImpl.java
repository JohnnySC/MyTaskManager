package com.github.johnnysc.mytaskmanager.details.presentation;

import android.support.annotation.StringRes;

import com.github.johnnysc.mytaskmanager.details.domain.TaskListInteractor;
import com.github.johnnysc.mytaskmanager.main.data.model.CategoryType;
import com.github.johnnysc.mytaskmanager.main.data.model.Task;
import com.github.johnnysc.mytaskmanager.main.presentation.BasePresenter;

import java.util.List;

/**
 * @author Asatryan on 02.12.18
 */
public final class TaskListPresenterImpl extends BasePresenter<TaskListView> implements TaskListPresenter, TaskListInteractor.DataCallback {

    private final TaskListInteractor mTaskListInteractor;

    public TaskListPresenterImpl(TaskListInteractor taskListInteractor) {
        mTaskListInteractor = taskListInteractor;
    }

    @Override
    public void clear() {
        super.clear();
        mTaskListInteractor.setDataCallback(null);
    }

    @Override
    public void getCategoryType(@CategoryType.TaskType int taskType) {
        mTaskListInteractor.getTaskCategory(taskType);
    }

    @Override
    public void getTaskList(@CategoryType.TaskType int taskType) {
        mTaskListInteractor.setDataCallback(this);
        mTaskListInteractor.getTaskList(taskType);
    }

    @Override
    public void moveTask(@CategoryType.TaskType int taskType, int oldPosition, int newPosition) {
        mTaskListInteractor.moveTask(taskType, oldPosition, newPosition);
        doSafely(() -> getView().notifyItemMoved(oldPosition, newPosition));
    }

    @Override
    public void removeTask(@CategoryType.TaskType int taskType, int position) {
        mTaskListInteractor.removeTask(taskType, position);
    }

    @Override
    public void setTaskDone(@CategoryType.TaskType int taskType, long id, boolean done) {
        mTaskListInteractor.setTaskDone(taskType, id, done);
        refreshData();
    }

    @Override
    public void provideTaskList(List<Task> taskList) {
        doSafely(() -> getView().showTasks(taskList));
    }

    @Override
    public void provideTaskCategoryTitle(@StringRes int titleResId) {
        doSafely(() -> getView().showTitle(titleResId));
    }

    @Override
    public void provideRemovedTaskPosition(int position) {
        doSafely(() -> getView().notifyItemRemoved(position));
    }

    @Override
    public void refreshData() {
        doSafely(() -> getView().notifyDataSetChanged());
    }
}