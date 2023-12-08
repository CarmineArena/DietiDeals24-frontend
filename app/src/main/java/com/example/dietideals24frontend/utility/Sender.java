package com.example.dietideals24frontend.utility;

import com.example.dietideals24frontend.modelDTO.User;
import com.example.dietideals24frontend.utility.callback.UserLoginCallback;
import com.example.dietideals24frontend.utility.callback.UserRegistrationCallback;

public interface Sender {
    void sendUserRegistrationRequest(User user, final UserRegistrationCallback callback);
    void sendUserLoginRequest(User user, final UserLoginCallback callback);
}