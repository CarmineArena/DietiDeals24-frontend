package com.example.dietideals24frontend.Controller.AuctionNotificationController.Retrofit;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuctionNotificationService {
    @GET("/auction/english/notificationsForUser/pending")
    Call<List<String>> getEnglishAuctionPendingNotificationsForUser(@Query("userId") Integer userId);

    @GET("/auction/silent/notificationsForUser/pending")
    Call<List<String>> getSilentAuctionPendingNotificationsForUser(@Query("userId") Integer userId);
}