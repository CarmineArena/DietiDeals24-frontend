package com.example.dietideals24frontend.Controller.OfferController.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;

public interface RequestBestOfferService {
    @GET("/offer/getBestOffer")
    Call<OfferDTO> getBestOffer(
            @Query("itemId") Integer itemId,
            @Query("auctionId") Integer auctionId);
}