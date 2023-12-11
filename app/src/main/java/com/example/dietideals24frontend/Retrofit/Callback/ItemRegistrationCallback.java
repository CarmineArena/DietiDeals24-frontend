package com.example.dietideals24frontend.Retrofit.Callback;

public interface ItemRegistrationCallback {
    boolean onItemRegistrationSuccess(Integer itemId);
    boolean onItemRegistrationFailure(String errorMessage);
}