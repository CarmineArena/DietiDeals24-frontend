package com.example.dietideals24frontend.Retrofit.Callback;

import com.example.dietideals24frontend.Model.UserDTO;

public interface UserRegistrationCallback {
    boolean onRegistrationSuccess(UserDTO registeredUser); // This parameter could be useful
    boolean onRegistrationFailure(String errorMessage);
}