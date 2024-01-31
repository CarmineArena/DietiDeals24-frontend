package com.example.dietideals24frontend.Controller.UserController.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.dietideals24frontend.Model.DTO.UserDTO;

public interface RetrieveUserService {
    @GET("/user/findUser")
    Call<UserDTO> retrieveUser(@Query("userId") Integer userId, @Query("email") String email);
}