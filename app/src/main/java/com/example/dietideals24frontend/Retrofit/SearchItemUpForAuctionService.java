package com.example.dietideals24frontend.Retrofit;

import com.example.dietideals24frontend.Model.RequestedItemDTO;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchItemUpForAuctionService {
    @GET("/item/findItemsUpForAuction")
    Call<List<RequestedItemDTO>> searchItemsUpForAuction(
            @Query("searchTerm") String searchTerm,
            @Query("categories") List<String> categories);
}