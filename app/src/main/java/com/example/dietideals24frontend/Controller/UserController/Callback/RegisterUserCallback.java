package com.example.dietideals24frontend.Controller.UserController.Callback;

import com.example.dietideals24frontend.Model.User;

public interface RegisterUserCallback {
    boolean onRegistrationSuccess(User registeredUser);
    boolean onRegistrationFailure(String errorMessage);
}