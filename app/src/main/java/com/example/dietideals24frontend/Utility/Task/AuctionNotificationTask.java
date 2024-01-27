package com.example.dietideals24frontend.Utility.Task;

import java.util.List;
import java.io.IOException;
import android.os.AsyncTask;

import com.example.dietideals24frontend.Retrofit.AuctionNotificationService;
import com.example.dietideals24frontend.Retrofit.Callback.AuctionNotificationCallback;

public class AuctionNotificationTask extends AsyncTask<Void, Void, List<String>> {
    private final AuctionNotificationService notificationService;
    private final AuctionNotificationCallback notificationCallback;

    public AuctionNotificationTask(
            AuctionNotificationService notificationService,
            AuctionNotificationCallback notificationCallback) {
        this.notificationService  = notificationService;
        this.notificationCallback = notificationCallback;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        try {
            retrofit2.Response<List<String>> response = notificationService.getPendingNotifications().execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<String> notifications) {
        if (notifications != null) {
            notificationCallback.onNotificationsReceived(notifications);
        } else {
            notificationCallback.onApiError();
        }
    }
}