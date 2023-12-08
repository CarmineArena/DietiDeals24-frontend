package com.example.dietideals24frontend.utility.callback;

import com.example.dietideals24frontend.modelDTO.User;

public interface UserRegistrationCallback {
    boolean onRegistrationSuccess(User registeredUser);
    boolean onRegistrationFailure(String errorMessage);
}