package com.example.dietideals24frontend.Retrofit.Callback;

import com.example.dietideals24frontend.Model.User;

public interface UserLoginCallback {
    boolean onLoginSuccess(User registeredUser); // This parameter could be useful
    boolean onLoginFailure(String errorMessage);
}