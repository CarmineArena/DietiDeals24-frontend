package com.example.dietideals24frontend.Controller.UserController.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.dietideals24frontend.Model.User;

public interface LoginUserService {
    @GET("/userLogin")
    Call<User> login(@Query("email") String email, @Query("password") String password);
}