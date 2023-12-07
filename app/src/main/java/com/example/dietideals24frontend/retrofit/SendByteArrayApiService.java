package com.example.dietideals24frontend.retrofit;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface SendByteArrayApiService {
    @POST("/uploadImageContent")
    Call<Void> uploadImageContent(@Body byte[] imageData);
}