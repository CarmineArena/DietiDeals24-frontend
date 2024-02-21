package com.example.dietideals24frontend.View.Notification;

import android.content.Context;
import com.example.dietideals24frontend.R;

import android.util.Log;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AuctionNotificationManager {
    /* [DESCRIPTION]
        - A channel_id connects a notification to a specific notification channel.
        - I stated that auction related notifications are associated to channel 1.
    **/
    private static final String CHANNEL_ID = "1";
    private final Context context;

    public AuctionNotificationManager(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        CharSequence name = "Auction Notification";
        String description = "Used to notify the end user of a terminated auction and a possible win of an Item.";
        int importance     = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        } else {
            Log.e("NOTIFICATION CHANNEL", "Could not create notification channel!");
        }
    }

    public void showNotification(String title, String content) {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.dietideals24img)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } catch (SecurityException e) {
            Log.e("NotificationManager", "SecurityException: " + e.getMessage());
        }
    }
}