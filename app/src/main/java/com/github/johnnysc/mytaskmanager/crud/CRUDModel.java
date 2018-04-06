package com.github.johnnysc.mytaskmanager.crud;

import com.github.johnnysc.mytaskmanager.bean.CategoryType;

/**
 * @author Asatryan on 05.04.18.
 */

interface CRUDModel {

    void onCreate(@CategoryType.TaskType int taskType,
                  @CRUDModelImpl.TaskActionType int actionType,
                  long taskId);

    void onActionButtonClicked(String titleText,
                               String bodyText,
                               boolean done,
                               boolean notify,
                               int spinnerPosition);

    void handleBackPressed(boolean isDoneChecked);

    void doOnDatePicked(int day, int month, int year);

    void doOnTimePicked(int hour, int minute);

    void deleteTask();
}