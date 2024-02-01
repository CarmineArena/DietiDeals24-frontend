package com.example.dietideals24frontend.Utility.Task;

import java.util.List;
import java.io.IOException;
import android.os.AsyncTask;
import com.example.dietideals24frontend.Controller.AuctionNotificationController.Retrofit.AuctionNotificationService;
import com.example.dietideals24frontend.Controller.AuctionNotificationController.Callback.AuctionNotificationCallback;

public class AuctionNotificationTask extends AsyncTask<Void, Void, List<String>> {
    private final AuctionNotificationService notificationService;
    private final AuctionNotificationCallback notificationCallback;
    private final Integer userId;

    public AuctionNotificationTask(Integer userId,
            AuctionNotificationService notificationService,
            AuctionNotificationCallback notificationCallback) {
        this.userId               = userId;
        this.notificationService  = notificationService;
        this.notificationCallback = notificationCallback;
    }

    @Override
    protected List<String> doInBackground(Void... voids) {
        try {
            retrofit2.Response<List<String>> response = notificationService.getPendingNotificationsForUser(userId).execute();
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