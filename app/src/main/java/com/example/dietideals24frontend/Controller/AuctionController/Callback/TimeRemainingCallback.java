package com.example.dietideals24frontend.Controller.AuctionController.Callback;

import com.example.dietideals24frontend.Model.DTO.AuctionStatusDTO;

public interface TimeRemainingCallback {
    boolean onTimeRetrievedSuccessfull(AuctionStatusDTO auctionStatusDTO);
    boolean onTimeRetrievedFailure(String errorMessage);
}