package com.example.dietideals24frontend.Controller.ItemController.Retrofit;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

public interface SearchItemsWantedByUserService {
    @GET("/item/findItemsWantedByUser")
    Call<List<ItemDTO>> findItemsForUser(@Query("userId") Integer userId, @Query("email") String email, @Query("password") String password);
}