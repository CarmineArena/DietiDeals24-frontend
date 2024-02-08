package com.example.dietideals24frontend.Controller.OfferController.Callback;

import java.util.List;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;

public interface RetrieveOffersCallback {
    boolean onRetrieveOffersSuccess(List<OfferDTO> offerDTOs);
    boolean onRetrieveOffersFailure(String errorMessage);
}