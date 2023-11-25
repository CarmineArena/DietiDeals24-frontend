package com.example.dietideals24frontend.retrofit;

import com.example.dietideals24frontend.modelDTO.User;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface LoginUserApiService {
    @POST("/userLogin")
    Call<User> login(@Body User user);
}