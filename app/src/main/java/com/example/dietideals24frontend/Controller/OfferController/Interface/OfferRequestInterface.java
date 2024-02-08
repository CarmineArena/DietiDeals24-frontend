package com.example.dietideals24frontend.Controller.OfferController.Interface;

import com.example.dietideals24frontend.Model.DTO.OfferDTO;
import com.example.dietideals24frontend.Controller.OfferController.Callback.*;

public interface OfferRequestInterface {
    // void sendFindBestOfferRequest(Integer itemId, Integer auctionId, final RetrieveBestOfferCallback callback);
    void sendRegisterOfferRequest(OfferDTO offerDTO, final RegisterOfferCallback callback);
    void sendGetOffersRequest(Integer itemId, Integer auctionId, final RetrieveOffersCallback callback);
}