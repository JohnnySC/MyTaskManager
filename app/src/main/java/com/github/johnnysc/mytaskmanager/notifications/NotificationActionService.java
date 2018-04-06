package com.github.johnnysc.mytaskmanager.notifications;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.johnnysc.mytaskmanager.bean.CategoryType;
import com.github.johnnysc.mytaskmanager.crud.CRUDModelImpl;
import com.github.johnnysc.mytaskmanager.crud.CRUDTaskActivity;


/**
 * Need this service to go and read the task from notification
 *
 * @author Asatryan on 02.04.18.
 */

public class NotificationActionService extends IntentService {

    public static final String EXTRA_TASK_TYPE = "extra_task_type";
    public static final String EXTRA_TASK_ID = "extra_task_id";

    public static Intent newIntent(Context context,
                                   final long taskId,
                                   final @CategoryType.TaskType int taskType) {
        Intent intent = new Intent(context, NotificationActionService.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        intent.putExtra(EXTRA_TASK_TYPE, taskType);
        return intent;
    }

    public NotificationActionService() {
        super(NotificationActionService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) return;
        int taskType = extras.getInt(EXTRA_TASK_TYPE);
        long taskId = extras.getLong(EXTRA_TASK_ID);
        Intent newIntent = CRUDTaskActivity.newIntent(this, taskType, CRUDModelImpl.READ, taskId);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }
}