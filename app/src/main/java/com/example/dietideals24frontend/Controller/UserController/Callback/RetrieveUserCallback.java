package com.example.dietideals24frontend.Controller.UserController.Callback;

import com.example.dietideals24frontend.Model.DTO.UserDTO;

public interface RetrieveUserCallback {
    boolean onUserRetrievalSuccess(UserDTO userDTO);
    boolean onUserRetrievalFailure(String errorMessage);
}