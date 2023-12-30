package com.example.dietideals24frontend.Retrofit;

import com.example.dietideals24frontend.Model.DTO.ItemDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddItemApiService {
    @POST("/item/saveItemImage")
    Call<Void> sendItemImageContent(@Body byte[] itemImageContent);

    @POST("/item/addItem")
    Call<Integer> registerItem(@Body ItemDTO requestedItem);
}