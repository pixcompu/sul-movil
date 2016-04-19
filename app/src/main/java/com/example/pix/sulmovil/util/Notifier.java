package com.example.pix.sulmovil.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.pix.sulmovil.ui.MainActivity;


/**
 * Created by PIX on 15/04/2016.
 */
public class Notifier {

    public static void showMessage(Context currentContext, String message){
        Toast.makeText(currentContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void showNotification(Context currentContext, String title, String message) {
        Intent notifyIntent = new Intent(currentContext, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(currentContext, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(currentContext)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) currentContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
