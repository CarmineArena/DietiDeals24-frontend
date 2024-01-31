package com.example.dietideals24frontend.Controller.UserController.Callback;

public interface UpdateUserCallback {
    boolean onUserUpdateSuccess();
    boolean onUserUpdateFailure(String errorMessage);
}