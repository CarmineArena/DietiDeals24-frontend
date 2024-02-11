package com.example.dietideals24frontend.Controller.AuctionController.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.dietideals24frontend.Model.DTO.AuctionStatusDTO;

public interface GetTimeRemainingService {
    @GET("/auction/getRemainingTime")
    Call<AuctionStatusDTO> getTimeRemaining(@Query("auctionId") Integer auctionId, @Query("userId") Integer userId);
}