package com.example.dietideals24frontend.Retrofit;

import com.example.dietideals24frontend.Model.DTO.OfferDTO;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestBestOfferService {
    @GET("/offer/getBestOffer")
    Call<OfferDTO> getBestOffer(
            @Query("itemId") Integer itemId,
            @Query("auctionId") Integer auctionId);
}