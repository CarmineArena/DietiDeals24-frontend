package com.example.dietideals24frontend.Controller.OfferController.Interface;

import com.example.dietideals24frontend.Model.DTO.OfferDTO;
import com.example.dietideals24frontend.Controller.OfferController.Callback.RegisterOfferCallback;
import com.example.dietideals24frontend.Controller.OfferController.Callback.RetrieveBestOfferCallback;

public interface OfferRequestInterface {
    void sendFindBestOfferRequest(Integer itemId, Integer auctionId, final RetrieveBestOfferCallback callback);
    void sendRegisterOfferRequest(OfferDTO offerDTO, final RegisterOfferCallback callback);
}