package com.example.dietideals24frontend.Controller.AuctionController.Callback;

public interface ImageContentRegisterCallback {
    boolean onReceptionSuccess(byte[] itemImageContent);
    boolean onReceptionFailure(String errorMessage);
}