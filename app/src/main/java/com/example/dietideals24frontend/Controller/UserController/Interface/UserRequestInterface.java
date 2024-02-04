package com.example.dietideals24frontend.Controller.UserController.Interface;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.DTO.UserDTO;
import com.example.dietideals24frontend.Controller.UserController.Callback.*;

public interface UserRequestInterface {
    void sendUserLoginRequest(UserDTO user, final LoginUserCallback callback);
    void sendUserRegistrationRequest(User user, final RegisterUserCallback callback);
    void sendRetrieveUserDataRequest(Integer userdId, String email, final RetrieveUserCallback callback);
    void sendUpdateBioAndWebsiteRequest(UserDTO userDTO, final UpdateUserCallback callback);
}