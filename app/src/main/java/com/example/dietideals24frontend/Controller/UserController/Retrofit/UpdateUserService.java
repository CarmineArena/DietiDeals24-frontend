package com.example.dietideals24frontend.Controller.UserController.Retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.dietideals24frontend.Model.DTO.UserDTO;

public interface UpdateUserService {
    @POST("/user/updateUser")
    Call<Void> update(@Body UserDTO userDTO);
}