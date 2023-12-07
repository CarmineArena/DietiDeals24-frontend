package com.example.dietideals24frontend.retrofit;

import com.example.dietideals24frontend.modelDTO.Auction;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddAuctionApiService {
    @POST("/addAuction")
    Call<Auction> registerAuction(@Body Auction auction);
}