package com.example.dietideals24frontend.Retrofit.Callback;

public interface ImageContentRequestCallback {
    boolean onFetchSuccess(byte[] itemImageContent);
    boolean onFetchFailure(String errorMessage);
}