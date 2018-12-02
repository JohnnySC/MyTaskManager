package com.github.johnnysc.mytaskmanager.main.data.model

import android.support.annotation.StringRes

/**
 * @author Asatryan on 02.12.18
 */
data class TaskTypeDataModel(@CategoryType.TaskType val taskType: Int,
                             @StringRes val titleResId: Int,
                             val taskCount: Int)