package com.github.johnnysc.mytaskmanager.main.presentation;

/**
 * @author Asatryan on 02.12.18
 */
public interface TasksMainPresenter extends LifeCycleEvents<TasksMainView> {

    void getTasksData();
}