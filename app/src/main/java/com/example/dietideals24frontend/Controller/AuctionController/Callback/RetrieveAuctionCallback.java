package com.example.dietideals24frontend.Controller.AuctionController.Callback;

import com.example.dietideals24frontend.Model.DTO.AuctionDTO;
import com.example.dietideals24frontend.Utility.Exception.UnhandledOptionException;

public interface RetrieveAuctionCallback {
    boolean onRetrieveAuctionSuccess(AuctionDTO retrievedAuction) throws UnhandledOptionException;
    boolean onRetrieveAuctionFailure(String errorMessage);
}