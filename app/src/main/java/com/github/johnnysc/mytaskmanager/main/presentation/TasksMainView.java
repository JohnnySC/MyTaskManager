package com.github.johnnysc.mytaskmanager.main.presentation;

import com.github.johnnysc.mytaskmanager.main.data.model.TaskTypeDataModel;

import java.util.List;

/**
 * @author Asatryan on 02.12.18
 */
public interface TasksMainView {

    void showTasksInfo(List<TaskTypeDataModel> taskCategoryDataModels);
}