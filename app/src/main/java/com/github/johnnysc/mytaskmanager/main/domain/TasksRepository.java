package com.github.johnnysc.mytaskmanager.main.domain;

import com.github.johnnysc.mytaskmanager.main.data.model.TaskCategoryDataModel;

import java.util.List;

/**
 * @author Asatryan on 02.12.18
 */
public interface TasksRepository {

    List<TaskCategoryDataModel> getTasksCategoryDataModels();
}