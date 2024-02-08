package com.example.dietideals24frontend.Controller.AuctionController.Retrofit;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CloseAuctionService {
    @POST("/auction/endAuction")
    Call<Void> closeAuction(@Query("auctionId") Integer auctionId);
}