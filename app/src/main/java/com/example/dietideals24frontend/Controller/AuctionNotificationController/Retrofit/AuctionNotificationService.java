package com.example.dietideals24frontend.Controller.AuctionNotificationController.Retrofit;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AuctionNotificationService {
    @GET("/auction/notifications/pending")
    Call<List<String>> getPendingNotifications();
}