package com.example.dietideals24frontend.Retrofit;

import com.example.dietideals24frontend.Model.RequestedAuctionDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddAuctionApiService {
    @POST("/auction/addSilentAuction")
    Call<Void> registerAuction(@Body RequestedAuctionDTO requestedAuctionDTO);
}