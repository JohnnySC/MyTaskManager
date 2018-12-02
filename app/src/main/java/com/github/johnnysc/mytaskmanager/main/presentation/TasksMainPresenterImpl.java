package com.github.johnnysc.mytaskmanager.main.presentation;

import com.github.johnnysc.mytaskmanager.main.data.model.TaskCategoryDataModel;
import com.github.johnnysc.mytaskmanager.main.domain.TasksMainInteractor;

import java.util.List;

/**
 * @author Asatryan on 02.12.18
 */
public final class TasksMainPresenterImpl extends BasePresenter<TasksMainView> implements TasksMainPresenter, TasksMainInteractor.DataCallback {

    private final TasksMainInteractor mTasksMainInteractor;

    public TasksMainPresenterImpl(TasksMainInteractor tasksMainInteractor) {
        mTasksMainInteractor = tasksMainInteractor;
    }

    @Override
    public void getTasksData() {
        mTasksMainInteractor.setDataCallback(this);
        mTasksMainInteractor.getTasksData();
    }

    @Override
    public void clear() {
        super.clear();
        mTasksMainInteractor.setDataCallback(null);
    }

    @Override
    public void provideTasksData(List<TaskCategoryDataModel> taskCategoryDataModels) {
        doSafely(() -> getView().showTasksInfo(taskCategoryDataModels));
    }
}