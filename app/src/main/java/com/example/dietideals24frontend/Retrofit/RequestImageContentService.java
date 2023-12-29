package com.example.dietideals24frontend.Retrofit;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestImageContentService {
    @GET("/item/imageContent")
    Call<byte[]> getItemImageConent(@Query("searchTerm") String searchTerm, @Query("categories") List<String> categories);
}