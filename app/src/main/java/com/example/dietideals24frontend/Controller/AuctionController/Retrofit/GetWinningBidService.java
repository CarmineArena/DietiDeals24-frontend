package com.example.dietideals24frontend.Controller.AuctionController.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetWinningBidService {
    @GET("/auction/getWinningBid")
    Call<Float> getWinningBid(@Query("itemId") Integer itemId);
}