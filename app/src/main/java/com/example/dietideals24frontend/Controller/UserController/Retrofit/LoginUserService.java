package com.example.dietideals24frontend.Controller.UserController.Retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.dietideals24frontend.Model.DTO.UserDTO;

public interface LoginUserService {
    @POST("/userLogin")
    Call<UserDTO> loginUser(@Body UserDTO newUser);
}