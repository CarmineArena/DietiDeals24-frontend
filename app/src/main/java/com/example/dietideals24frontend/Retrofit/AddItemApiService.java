package com.example.dietideals24frontend.Retrofit;

import com.example.dietideals24frontend.Model.RequestedItemDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddItemApiService {
    @POST("/item/saveItemImage")
    Call<Void> sendItemImageContent(@Body byte[] itemImageContent);

    @POST("/item/addItem")
    Call<Void> registerItem(@Body RequestedItemDTO requestedItem);
}