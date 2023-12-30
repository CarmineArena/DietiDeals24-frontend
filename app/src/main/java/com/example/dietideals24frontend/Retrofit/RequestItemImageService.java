package com.example.dietideals24frontend.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestItemImageService {
    @GET("/item/findItemImage")
    Call<byte[]> fetchItemImage(@Query("itemId") Integer itemId,
                                @Query("name") String name);
}