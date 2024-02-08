package com.example.dietideals24frontend.View.Notification;

import android.content.Context;
import android.annotation.SuppressLint;
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
        createChannel();
    }

    private void createChannel() {
        CharSequence name  = "Auction Notification";
        String description = "Used to notify the end user of a terminated auction and a possible win of an Item.";
        int importance     = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @SuppressLint("MissingPermission")
    public void showNotification(int notificationId, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        // .setSmallIcon(R.drawable.notification_icon) // TODO: METTERE L'ICONA DELL'APP

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}