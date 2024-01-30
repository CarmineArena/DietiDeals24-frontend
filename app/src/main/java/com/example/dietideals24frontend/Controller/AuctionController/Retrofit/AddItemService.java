package com.example.dietideals24frontend.Controller.AuctionController.Retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

public interface AddItemService {
    @POST("/item/saveItemImage")
    Call<Void> sendItemImageContent(@Body byte[] itemImageContent);

    @POST("/item/addItem")
    Call<Integer> registerItem(@Body ItemDTO requestedItem);
}