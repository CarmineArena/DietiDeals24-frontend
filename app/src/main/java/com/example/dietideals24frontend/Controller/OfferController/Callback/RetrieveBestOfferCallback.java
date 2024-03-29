package com.example.dietideals24frontend.Controller.OfferController.Callback;

import com.example.dietideals24frontend.Model.DTO.OfferDTO;

public interface RetrieveBestOfferCallback {
    boolean onBestOfferRetrievalSuccess(OfferDTO offerDTO);
    boolean onBestOfferRetrievalFailure(String errorMessage);
}