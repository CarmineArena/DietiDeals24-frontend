package com.example.dietideals24frontend.Retrofit.Callback;

public interface ImageCallback {
    void onImageAvailable(byte[] imageContent);
    void onImageNotAvailable(String errorMessage);
}