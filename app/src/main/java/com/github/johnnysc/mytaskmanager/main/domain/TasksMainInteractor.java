package com.github.johnnysc.mytaskmanager.main.domain;

import com.github.johnnysc.mytaskmanager.main.data.model.TaskCategoryDataModel;

import java.util.List;

/**
 * @author Asatryan on 02.12.18
 */
public interface TasksMainInteractor extends DataCallbackSetter<TasksMainInteractor.DataCallback> {

    void getTasksData();

    interface DataCallback {

        void provideTasksData(List<TaskCategoryDataModel> taskCategoryDataModels);
    }
}