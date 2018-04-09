package com.github.johnnysc.mytaskmanager.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;

import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.bean.Task;

import io.realm.Realm;

/**
 * @author Asatryan on 09.04.18.
 */

public class NotificationJobService extends JobService {

    public static final String EXTRA_NOTIFICATION_TASK_ID = "extra_id";
    public static final String EXTRA_NOTIFICATION_TASK_TYPE = "extra_type";

    @Override
    public boolean onStartJob(JobParameters params) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        final PersistableBundle extras = params.getExtras();
        final long taskId = extras.getLong(EXTRA_NOTIFICATION_TASK_ID);
        Notification notification = getNotification(taskId, extras.getInt(EXTRA_NOTIFICATION_TASK_TYPE));
        if (notificationManager != null) {
            notificationManager.notify((int) taskId, notification);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    private Notification getNotification(long taskId, int taskType) {
        Intent actionIntent = NotificationActionService.newIntent(this, taskId, taskType);
        actionIntent.setAction(String.valueOf(taskId)); //needs to show different notifications
        PendingIntent actionPendingIntent = PendingIntent.getService(this, 0, actionIntent, PendingIntent.FLAG_ONE_SHOT);
        Task task = getTaskByPrimaryKey(taskId);
        return new Notification.Builder(this)
                .setContentTitle(task.getTitle())
                .setContentText(task.getBody())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(actionPendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();
    }

    private Task getTaskByPrimaryKey(long id) {
        return Realm.getDefaultInstance().where(Task.class).equalTo("id", id).findFirst();
    }
}