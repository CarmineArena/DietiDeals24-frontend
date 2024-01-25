package com.example.dietideals24frontend.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;

public interface SearchAuctionService {
    @GET("/auction/findAuction")
    Call<AuctionDTO> searchAuction(
            @Query("itemId") Integer itemId,
            @Query("name") String name,
            @Query("description") String description);
}