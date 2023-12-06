package com.example.dietideals24frontend.retrofit;

import com.example.dietideals24frontend.modelDTO.Item;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddItemApiService {
    @POST("/addItem")
    Call<Item> addItem(@Body Item item);
}