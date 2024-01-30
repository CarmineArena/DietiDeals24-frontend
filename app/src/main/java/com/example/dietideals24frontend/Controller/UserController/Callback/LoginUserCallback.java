package com.example.dietideals24frontend.Controller.UserController.Callback;

import com.example.dietideals24frontend.Model.User;

public interface LoginUserCallback {
    boolean onLoginSuccess(User registeredUser);
    boolean onLoginFailure(String errorMessage);
}