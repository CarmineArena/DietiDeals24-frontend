package com.example.dietideals24frontend.Controller.OfferController.Retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;

public interface OfferRegistrationService {
    @POST("/offer/createOffer")
    Call<Void> saveOffer(@Body OfferDTO offerDTO);
}