package com.example.dietideals24frontend.Retrofit;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchItemsCreatedByUserService {
    @GET("/item/findAuctionedByUser")
    Call<List<ItemDTO>> searchCreatedByUserItems(@Query("user") User user);
}