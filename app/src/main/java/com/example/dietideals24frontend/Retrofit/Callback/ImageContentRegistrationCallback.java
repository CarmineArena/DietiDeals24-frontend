package com.example.dietideals24frontend.Retrofit.Callback;

public interface ImageContentRegistrationCallback {
    boolean onReceptionSuccess(byte[] itemImageContent);
    boolean onReceptionFailure(String errorMessage);
}