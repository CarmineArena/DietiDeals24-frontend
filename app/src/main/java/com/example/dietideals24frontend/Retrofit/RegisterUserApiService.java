package com.example.dietideals24frontend.Retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import com.example.dietideals24frontend.Model.UserDTO;

public interface RegisterUserApiService {
    @POST("/userSignUp")
    Call<UserDTO> save(@Body UserDTO newUser);
}