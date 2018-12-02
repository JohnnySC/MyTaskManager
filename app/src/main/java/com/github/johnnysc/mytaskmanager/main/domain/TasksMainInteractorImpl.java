package com.github.johnnysc.mytaskmanager.main.domain;

/**
 * @author Asatryan on 02.12.18
 */
public final class TasksMainInteractorImpl implements TasksMainInteractor {

    private final TasksRepository mTasksRepository;
    private DataCallback mDataCallback;

    public TasksMainInteractorImpl(TasksRepository tasksRepository) {
        mTasksRepository = tasksRepository;
    }

    @Override
    public void getTasksData() {
       if (mDataCallback != null) {
           mDataCallback.provideTasksData(mTasksRepository.getTasksCategoryDataModels());
       }
    }

    @Override
    public void setDataCallback(DataCallback dataCallback) {
        mDataCallback = dataCallback;
    }
}