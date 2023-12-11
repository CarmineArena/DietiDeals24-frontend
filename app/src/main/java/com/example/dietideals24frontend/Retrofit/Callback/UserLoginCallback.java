package com.example.dietideals24frontend.Retrofit.Callback;

import com.example.dietideals24frontend.Model.UserDTO;

public interface UserLoginCallback {
    boolean onLoginSuccess(UserDTO registeredUser); // This parameter could be useful
    boolean onLoginFailure(String errorMessage);
}