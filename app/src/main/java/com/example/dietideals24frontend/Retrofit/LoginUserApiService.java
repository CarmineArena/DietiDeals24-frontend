package com.example.dietideals24frontend.Retrofit;

import com.example.dietideals24frontend.Model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
// import retrofit2.http.POST;

public interface LoginUserApiService {
    // @POST("/userLogin")
    // Call<UserDTO> login(@Body UserDTO user);

    @GET("/userLogin")
    Call<User> login(@Query("email") String email, @Query("password") String password);
}