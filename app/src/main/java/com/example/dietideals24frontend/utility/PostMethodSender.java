package com.example.dietideals24frontend.utility;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.dietideals24frontend.modelDTO.Item;
import com.example.dietideals24frontend.modelDTO.Auction;
import com.example.dietideals24frontend.retrofit.AddItemApiService;
import com.example.dietideals24frontend.retrofit.AddAuctionApiService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostMethodSender {
    private final Retrofit retrofitService;

    public PostMethodSender(Retrofit retrofitService) {
        this.retrofitService = retrofitService;
    }

    public boolean sendItemToServer(Item item) {
        try {
            AddItemApiService api = retrofitService.create(AddItemApiService.class);
            api.registerItem(item).enqueue(new Callback<Item>() {
                @Override
                public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
                    if (response.isSuccessful()) {
                        Log.d("Add Item Procedure", "Item registered correctly!");
                        // TODO: Cosa facciamo in questo caso?
                    } else {
                        Log.d("Add Item Procedure Error", "Could not register the Item provided. Server error: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
                    Log.d("Add Item Procedure Error", Objects.requireNonNull(t.getMessage()));
                    // TODO: Cosa facciamo in questo caso?
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Add Item Procedure Error", "Exception during API call: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean sendAuctionToServer(Auction auction) {
        try {
            AddAuctionApiService api = retrofitService.create(AddAuctionApiService.class);
            api.registerAuction(auction).enqueue(new Callback<Auction>() {
                @Override
                public void onResponse(@NonNull Call<Auction> call, @NonNull Response<Auction> response) {
                    if (response.isSuccessful()) {
                        Log.d("Add Auction Procedure", "Auction registered correctly!");
                        // TODO: Cosa facciamo in questo caso?
                    } else {
                        Log.d("Add Auction Procedure Error", "Could not register the Auction provided. Server error: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Auction> call, @NonNull Throwable t) {
                    Log.d("Add Auction Procedure Error", Objects.requireNonNull(t.getMessage()));
                    // TODO: Cosa facciamo in questo caso?
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Add Auction Procedure Error", "Exception during API call: " + e.getMessage());
            return false;
        }
        return true;
    }
}