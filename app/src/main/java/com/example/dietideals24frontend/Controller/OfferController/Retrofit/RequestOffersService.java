package com.example.dietideals24frontend.Controller.OfferController.Retrofit;

import java.util.List;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestOffersService {
    @GET("/offer/getOffers")
    Call<List<OfferDTO>> getOffers(
            @Query("itemId") Integer itemId,
            @Query("auctionId") Integer auctionId);
}