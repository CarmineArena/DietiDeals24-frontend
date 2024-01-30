package com.example.dietideals24frontend.Controller.UserController.Retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.dietideals24frontend.Model.User;

public interface RegisterUserService {
    @POST("/userSignUp")
    Call<User> save(@Body User newUser);
}