package com.example.dietideals24frontend.Controller.UserController.Callback;

import com.example.dietideals24frontend.Model.DTO.UserDTO;

public interface LoginUserCallback {
    boolean onLoginSuccess(UserDTO registeredUser);
    boolean onLoginFailure(String errorMessage);
}