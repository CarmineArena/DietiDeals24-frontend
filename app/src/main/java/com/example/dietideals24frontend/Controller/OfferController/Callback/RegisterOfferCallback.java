package com.example.dietideals24frontend.Controller.OfferController.Callback;

public interface RegisterOfferCallback {
    boolean onOfferRegistrationSuccess();
    boolean onOfferRegistrationFailure(String errorMessage);
}