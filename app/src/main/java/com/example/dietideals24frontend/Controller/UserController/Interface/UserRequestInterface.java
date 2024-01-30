package com.example.dietideals24frontend.Controller.UserController.Interface;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Controller.UserController.Callback.*;

public interface UserRequestInterface {
    void sendUserLoginRequest(User user, final LoginUserCallback callback);
    void sendUserRegistrationRequest(User user, final RegisterUserCallback callback);
}