package com.example.dietideals24frontend.Retrofit.Service;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import androidx.annotation.NonNull;

import com.example.dietideals24frontend.Model.*;
import com.example.dietideals24frontend.Retrofit.*;
import com.example.dietideals24frontend.Retrofit.Callback.*;
import com.example.dietideals24frontend.Model.RequestedItemDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Requester implements Sender {
    private final Retrofit retrofitService;

    public Requester(Retrofit retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public void sendUserRegistrationRequest(UserDTO user, final UserRegistrationCallback callback) {
        RegisterUserApiService api = retrofitService.create(RegisterUserApiService.class);
        api.save(user).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                boolean returnValue;
                if (response.isSuccessful()) {
                    UserDTO loggedInUser = response.body();
                    returnValue = callback.onRegistrationSuccess(loggedInUser);
                } else {
                    returnValue = callback.onRegistrationFailure(response.message());
                }

                if (returnValue)
                    Log.i("User Register", "User registered correctly!");
                else
                    Log.e("User Register Error", "Could not register user!");
            }

            @Override
            public void onFailure(@NonNull Call<UserDTO> call, @NonNull Throwable t) {
                boolean returnValue = callback.onRegistrationFailure(t.getMessage());

                if (!returnValue) {
                    Log.e("User Register Error", "Could not register user!");
                    Log.e("User Register Error", Objects.requireNonNull(t.getMessage()));
                }
            }
        });
    }

    @Override
    public void sendUserLoginRequest(UserDTO user, final UserLoginCallback callback) {
        LoginUserApiService api = retrofitService.create(LoginUserApiService.class);

        String email = user.getEmail();
        String passw = user.getPassword();
        api.login(email, passw).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                boolean returnValue;
                if (response.isSuccessful()) {
                    UserDTO loggedInUser = response.body();
                    returnValue = callback.onLoginSuccess(loggedInUser);
                } else {
                    returnValue = callback.onLoginFailure(response.message());
                }

                if (returnValue)
                    Log.i("User Login", "User logged in correctly!");
                else
                    Log.e("User Login Error", "Could not login user!");
            }

            @Override
            public void onFailure(@NonNull Call<UserDTO> call, @NonNull Throwable t) {
                boolean returnValue = callback.onLoginFailure(t.getMessage());

                if (!returnValue) {
                    Log.e("User Login Error", "Could not login user!");
                    Log.e("User Login Error", Objects.requireNonNull(t.getMessage()));
                }
            }
        });
    }

    @Override
    public void sendItemImageContent(byte[] itemImageContent, final ImageContentRegistrationCallback callback) {
        AddItemApiService api = retrofitService.create(AddItemApiService.class);
        Call<Void> call = api.sendItemImageContent(itemImageContent);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onReceptionSuccess(itemImageContent);
                } else {
                    returnValue = callback.onReceptionFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Send Item Image Content", "Item Image content sent correctly!");
                } else {
                    Log.e("Send Item Image Content Error", "Could not send byte[] content. Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                boolean returnValue = callback.onReceptionFailure(t.getMessage());
                Log.e("Send Item Image Content Error", "Could not send byte[] content. Server error: " + Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public void sendRegisterItemRequest(RequestedItemDTO requestedItem, final ItemRegistrationCallback callback) {
        AddItemApiService api = retrofitService.create(AddItemApiService.class);

        Call<Integer> call = api.registerItem(requestedItem);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    Integer itemId = response.body();
                    returnValue = callback.onItemRegistrationSuccess(itemId);
                } else {
                    returnValue = callback.onItemRegistrationFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Add RequestedItem Procedure", "Item registered correctly!");
                } else {
                    Log.e("Add RequestedItem Procedure Error", "Could not register the Item provided. Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                boolean returnValue = callback.onItemRegistrationFailure(t.getMessage());
                Log.e("Add RequestedItem Procedure Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendRegisterAuctionRequest(RequestedAuctionDTO requestedAuctionDTO, final AuctionRegistrationCallback callback) {
        AddAuctionApiService api = retrofitService.create(AddAuctionApiService.class);

        Call<Void> call = api.registerAuction(requestedAuctionDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onAuctionRegistrationSuccess(requestedAuctionDTO);
                } else {
                    returnValue = callback.onAuctionRegistrationFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Add RequestedAuction Procedure", "Auction registered correctly!");
                } else {
                    Log.e("Add RequestedAuction Procedure Error", "Could not register the Auction provided. Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                boolean returnValue = callback.onAuctionRegistrationFailure(t.getMessage());
                Log.e("Add RequestedAuction Procedure Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendItemsUpForAuctionRequest(String searchTerm, List<String> categories, final SearchItemsCallback callback) {
        SearchItemUpForAuctionService api = retrofitService.create(SearchItemUpForAuctionService.class);

        Call<List<ItemDTO>> call = api.searchItemsUpForAuction(searchTerm, categories);
        call.enqueue(new Callback<List<ItemDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemDTO>> call, @NonNull Response<List<ItemDTO>> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onSearchItemsUpForAuctionSuccess(response.body());
                } else {
                    returnValue = callback.onSearchItemsUpForAuctionFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Search ItemsUpForAuction Procedure", "Auction registered correctly!");
                } else {
                    Log.e("Search ItemsUpForAuction Procedure Error", "Could not find the items requested. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemDTO>> call, @NonNull Throwable t) {
                boolean returnValue = callback.onSearchItemsUpForAuctionFailure(t.getMessage());
                Log.e("Search ItemsUpForAuction Procedure Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }
}