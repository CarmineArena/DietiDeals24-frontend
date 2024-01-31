package com.example.dietideals24frontend.Controller.AuctionNotificationController;

import java.util.List;
import retrofit2.Retrofit;
import android.content.Context;

import com.example.dietideals24frontend.Utility.Task.AuctionNotificationTask;
import com.example.dietideals24frontend.View.Notification.AuctionNotificationManager;
import com.example.dietideals24frontend.Controller.AuctionNotificationController.Retrofit.*;
import com.example.dietideals24frontend.Controller.AuctionNotificationController.Callback.*;
import com.example.dietideals24frontend.Controller.AuctionNotificationController.Interface.AuctionNotificationInterface;

public class AuctionNotificationController implements AuctionNotificationInterface {
    private final Retrofit retrofitService;
    private final Context context;


    public AuctionNotificationController(Retrofit retrofitService, Context context) {
        this.context = context;
        this.retrofitService = retrofitService;
    }

    @Override
    public void notifyUser() {
        AuctionNotificationService service = retrofitService.create(AuctionNotificationService.class);
        AuctionNotificationTask task = new AuctionNotificationTask(service, new AuctionNotificationCallback() {
            @Override
            public void onNotificationsReceived(List<String> notifications) {
                if (notifications != null) {
                    AuctionNotificationManager manager = new AuctionNotificationManager(context);

                    for (String notification : notifications) {
                        manager.showNotification(1, "Auction Expired", notification);
                    }
                }
            }

            @Override
            public void onApiError() {
                // TODO: HANDLE ERROR CASE (IT SHOULD NOT HAPPEN)
            }
        });
        task.execute();
    }
}