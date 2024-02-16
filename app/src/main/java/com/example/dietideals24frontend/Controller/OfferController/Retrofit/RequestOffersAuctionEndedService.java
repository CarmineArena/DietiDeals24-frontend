package com.example.dietideals24frontend.Controller.OfferController.Retrofit;

import retrofit2.Call;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;

public interface RequestOffersAuctionEndedService {
    @GET("/auction/silent/getOfferList/pending")
    Call<List<OfferDTO>> getOfferList(@Query("itemId") Integer itemId);
}