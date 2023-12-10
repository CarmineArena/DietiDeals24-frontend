package com.example.dietideals24frontend.Retrofit.Callback;

// import com.example.dietideals24frontend.Model.RequestedItemDTO;

public interface ItemRegistrationCallback {
    // boolean onItemRegistrationSuccess(RequestedItemDTO requestedItem);
    boolean onItemRegistrationSuccess(Integer itemId);
    boolean onItemRegistrationFailure(String errorMessage);
}