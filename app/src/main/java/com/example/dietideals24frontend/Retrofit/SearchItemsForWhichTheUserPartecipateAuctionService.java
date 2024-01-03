package com.example.dietideals24frontend.Retrofit;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

public interface SearchItemsForWhichTheUserPartecipateAuctionService {
    @GET("/item/findItemsForWhichTheUserPartecipateAuction")
    Call<List<ItemDTO>> findItemsForUser( // I don't want to make this function name too long (the endpoint is already enough)
            @Query("userId") Integer userId,
            @Query("email") String email,
            @Query("password") String password);
}