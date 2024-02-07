package com.example.dietideals24frontend.Controller.AuctionController.Retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;

public interface AddAuctionService {
    @POST("/auction/addAuction")
    Call<Void> registerAuction(@Body AuctionDTO auctionDTO);
}