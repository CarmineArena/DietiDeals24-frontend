package com.example.dietideals24frontend.Controller.AuctionNotificationController;

import java.util.List;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.content.Context;
import androidx.annotation.NonNull;

import com.example.dietideals24frontend.View.Notification.AuctionNotificationManager;
import com.example.dietideals24frontend.Controller.AuctionNotificationController.Retrofit.*;
import com.example.dietideals24frontend.Controller.AuctionNotificationController.Interface.AuctionNotificationInterface;

public class AuctionNotificationController implements AuctionNotificationInterface {
    private final Retrofit retrofitService;
    private final Context context;
    private final Integer userId;
    private static final int REQUEST_CODE = 101;

    public AuctionNotificationController(Integer userId, Retrofit retrofitService, Context context) {
        this.userId          = userId;
        this.context         = context;
        this.retrofitService = retrofitService;
    }

    @Override
    public void getEnglishAuctionNotifications() {
        AuctionNotificationService service = retrofitService.create(AuctionNotificationService.class);
        service.getEnglishAuctionPendingNotificationsForUser(userId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("NotificationController", "Notification Received: " + response.body().size());

                    AuctionNotificationManager manager = new AuctionNotificationManager(context);
                    for (String notification : response.body()) {
                        Log.i("ENGLISH AUCTION NOTIFICATION RECEIVED", notification);
                        manager.showNotification("Asta Terminata", notification);
                    }
                } else {
                    Log.d("NotificationController", "Notification Received: 0");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                Log.e("Notify User", "Something went wrong!");
                Log.e("Notify User Error", t.toString());
            }
        });
    }

    @Override
    public void getSilentAuctionNotifications() {
        AuctionNotificationService service = retrofitService.create(AuctionNotificationService.class);
        service.getSilentAuctionPendingNotificationsForUser(userId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("NotificationController", "Notification Received: " + response.body().size());

                    AuctionNotificationManager manager = new AuctionNotificationManager(context);
                    for (String notification : response.body()) {
                        Log.i("SILENT AUCTION NOTIFICATION RECEIVED", notification);
                        manager.showNotification("Asta Terminata", notification);
                    }
                } else {
                    Log.d("NotificationController", "Notification Received: 0");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                Log.e("Notify User", "Something went wrong!");
                Log.e("Notify User Error", t.toString());
            }
        });
    }
}