package com.example.dietideals24frontend.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import com.example.dietideals24frontend.modelDTO.User;

public interface RegisterUserApiService {
    @POST("/userSignUp")
    Call<User> save(@Body User newUser);
}