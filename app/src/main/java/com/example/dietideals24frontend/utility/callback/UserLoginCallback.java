package com.example.dietideals24frontend.utility.callback;

import com.example.dietideals24frontend.modelDTO.User;

public interface UserLoginCallback {
    boolean onLoginSuccess(User registeredUser);
    boolean onLoginFailure(String errorMessage);
}