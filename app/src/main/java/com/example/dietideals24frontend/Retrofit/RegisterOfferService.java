package com.example.dietideals24frontend.Retrofit;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;

public interface RegisterOfferService {
    @POST("/offer/createOffer")
    Call<Void> saveOffer(@Body OfferDTO offerDTO);
}