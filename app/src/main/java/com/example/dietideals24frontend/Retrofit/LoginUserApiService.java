package com.example.dietideals24frontend.Retrofit;

import com.example.dietideals24frontend.Model.UserDTO;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface LoginUserApiService {
    @POST("/userLogin")
    Call<UserDTO> login(@Body UserDTO user);
}