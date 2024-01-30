package com.example.dietideals24frontend.Controller.ItemController.Callback;

public interface ImageCallback {
    void onImageAvailable(byte[] imageContent);
    void onImageNotAvailable(String errorMessage);
}