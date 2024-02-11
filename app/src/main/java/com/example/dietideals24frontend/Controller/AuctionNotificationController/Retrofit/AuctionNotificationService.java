package com.example.dietideals24frontend.Controller.AuctionNotificationController.Retrofit;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuctionNotificationService {
    @GET("/auction/notificationsForUser/pending")
    Call<List<String>> getPendingNotificationsForUser(@Query("userId") Integer userId);
}