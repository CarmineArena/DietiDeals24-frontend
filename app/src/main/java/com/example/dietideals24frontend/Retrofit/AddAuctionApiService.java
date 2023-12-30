package com.example.dietideals24frontend.Retrofit;

import com.example.dietideals24frontend.Model.DTO.AuctionDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddAuctionApiService {
    @POST("/auction/addSilentAuction")
    Call<Void> registerAuction(@Body AuctionDTO auctionDTO);
}