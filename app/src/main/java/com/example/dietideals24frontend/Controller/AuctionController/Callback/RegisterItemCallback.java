package com.example.dietideals24frontend.Controller.AuctionController.Callback;

public interface RegisterItemCallback {
    boolean onItemRegistrationSuccess(Integer itemId);
    boolean onItemRegistrationFailure(String errorMessage);
}