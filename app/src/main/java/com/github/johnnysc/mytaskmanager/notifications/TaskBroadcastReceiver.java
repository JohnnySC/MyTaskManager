package com.github.johnnysc.mytaskmanager.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Receives notification with deadline
 *
 * @author Asatryan on 25.03.18.
 */

public class TaskBroadcastReceiver extends BroadcastReceiver {

    private static final String EXTRA_NOTIFICATION_ID = "extra_id";
    private static final String EXTRA_NOTIFICATION_ITEM = "extra_item";
    private static final String EXTRA_CANCEL = "extra_cancel";

    public static Intent newIntent(Context context,
                                   final int id,
                                   final Notification notification,
                                   boolean cancel) {
        Intent intent = new Intent(context, TaskBroadcastReceiver.class);
        intent.putExtra(EXTRA_NOTIFICATION_ID, id);
        intent.putExtra(EXTRA_NOTIFICATION_ITEM, notification);
        intent.putExtra(EXTRA_CANCEL, cancel);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle extras = intent.getExtras();
        if (extras == null || manager == null) return;
        Notification notification = extras.getParcelable(EXTRA_NOTIFICATION_ITEM);
        int id = extras.getInt(EXTRA_NOTIFICATION_ID);
        boolean cancel = extras.getBoolean(EXTRA_CANCEL);
        if (cancel) {
            manager.cancel(id);
        } else {
            manager.notify(id, notification);
        }
    }
}