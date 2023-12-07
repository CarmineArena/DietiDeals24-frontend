package com.example.dietideals24frontend.retrofit;

import com.example.dietideals24frontend.modelDTO.Item;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface AddItemApiService {
    @POST("/addItem")
    Call<Item> registerItem(@Body Item item);
}