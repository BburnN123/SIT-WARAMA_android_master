package com.sit.warama.estimote;

import android.app.Notification;
import android.app.NotificationChannel;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sit.warama.MainActivity;

import androidx.core.app.NotificationCompat;


public class NotificationsManager {

    private Context context;
    private NotificationManager notificationManager;
    private int notificationId = 1;

    public NotificationsManager(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private Notification buildNotification(String title, String text) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel contentChannel = new NotificationChannel(
                    "content_channel", "Things near you", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(contentChannel);
        }

        return new NotificationCompat.Builder(context, "content_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
    }

    public void notificationEnterNotify(String zone)
    {
        notificationManager.notify(notificationId, buildNotification("Alert", "You're about to enter into the " + zone + " zone!"));
    }

    public void notificationExitNotify(String zone)
    {
        notificationManager.notify(notificationId, buildNotification("Alert", "You're about to exiting the " + zone + " zone!"));
    }

}
