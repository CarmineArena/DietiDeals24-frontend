package com.example.dietideals24frontend.Controller.ItemController.Callback;

public interface ImageContentRequestCallback {
    boolean onFetchSuccess(byte[] itemImageContent);
    boolean onFetchFailure(String errorMessage);
}