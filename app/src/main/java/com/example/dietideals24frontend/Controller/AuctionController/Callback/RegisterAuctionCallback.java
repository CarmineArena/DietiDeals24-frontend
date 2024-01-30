package com.example.dietideals24frontend.Controller.AuctionController.Callback;

import com.example.dietideals24frontend.Model.DTO.AuctionDTO;

public interface RegisterAuctionCallback {
    boolean onAuctionRegistrationSuccess(AuctionDTO auctionDTO);
    boolean onAuctionRegistrationFailure(String errorMessage);
}