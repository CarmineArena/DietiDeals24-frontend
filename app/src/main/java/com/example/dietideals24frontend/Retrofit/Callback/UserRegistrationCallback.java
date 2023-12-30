package com.example.dietideals24frontend.Retrofit.Callback;

import com.example.dietideals24frontend.Model.User;

public interface UserRegistrationCallback {
    boolean onRegistrationSuccess(User registeredUser); // This parameter could be useful
    boolean onRegistrationFailure(String errorMessage);
}