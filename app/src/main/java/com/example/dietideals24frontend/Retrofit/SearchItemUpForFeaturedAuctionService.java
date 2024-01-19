package com.example.dietideals24frontend.Retrofit;

import com.example.dietideals24frontend.Model.DTO.ItemDTO;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchItemUpForFeaturedAuctionService {
    @GET("/item/findItemsUpForFeaturedAuctionBySearchTermAndCategory")
    Call<List<ItemDTO>> searchFeaturedItemsBySearchTermAndCategory(
            @Query("searchTerm") String searchTerm,
            @Query("categories") List<String> categories,
            @Query("userId") Integer userId);

    @GET("/item/findItemsUpForFeaturedAuction")
    Call<List<ItemDTO>> searchFeaturedItems(@Query("userId") Integer userId, @Query("email") String email);
}