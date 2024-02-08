package com.example.dietideals24frontend.Controller.AuctionController.Callback;

public interface CloseAuctionCallback {
    boolean onCloseSuccess();
    boolean onCloseFailure(String errorMessage);
}