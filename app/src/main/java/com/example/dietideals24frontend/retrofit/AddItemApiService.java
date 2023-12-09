package com.example.dietideals24frontend.retrofit;

import com.example.dietideals24frontend.utility.RequestedItem;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddItemApiService {
    @POST("/item/saveItemImage")
    Call<Void> sendItemImageContent(@Body byte[] itemImageContent);

    @POST("/item/addItem")
    Call<Void> registerItem(@Body RequestedItem requestedItem);
}