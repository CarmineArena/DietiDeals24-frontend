package com.example.dietideals24frontend.Retrofit.Callback;

public interface ByteArrayCallback {
    boolean onImageContentRetrievedSuccess(byte[] imageContent);
    boolean onImageContentRetrievedFailure(String errorMessage);
}