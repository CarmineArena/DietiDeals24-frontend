package com.example.dietideals24frontend.utility;

import android.util.Log;
import java.util.Objects;
import androidx.annotation.NonNull;
import com.example.dietideals24frontend.modelDTO.Item;
import com.example.dietideals24frontend.modelDTO.Auction;
import com.example.dietideals24frontend.modelDTO.User;
import com.example.dietideals24frontend.retrofit.AddItemApiService;
import com.example.dietideals24frontend.retrofit.AddAuctionApiService;
import com.example.dietideals24frontend.retrofit.LoginUserApiService;
import com.example.dietideals24frontend.retrofit.RegisterUserApiService;
import com.example.dietideals24frontend.retrofit.SendByteArrayApiService;
import com.example.dietideals24frontend.utility.callback.UserLoginCallback;
import com.example.dietideals24frontend.utility.callback.UserRegistrationCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostMethodSender implements Sender {
    private final Retrofit retrofitService;

    public PostMethodSender(Retrofit retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public void sendUserRegistrationRequest(User user, final UserRegistrationCallback callback) {
        RegisterUserApiService api = retrofitService.create(RegisterUserApiService.class);
        api.save(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                boolean returnValue;
                if (response.isSuccessful()) {
                    User loggedInUser = response.body();
                    returnValue = callback.onRegistrationSuccess(loggedInUser);
                } else {
                    returnValue = callback.onRegistrationFailure(response.message());
                }

                if (returnValue)
                    Log.d("User Register", "User registered correctly!");
                else
                    Log.d("User Register Error", "Could not register user!");
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                boolean returnValue = callback.onRegistrationFailure(t.getMessage());

                if (!returnValue) {
                    Log.d("User Register Error", "Could not register user!");
                    Log.d("User Register Error", Objects.requireNonNull(t.getMessage()));
                }
            }
        });
    }

    @Override
    public void sendUserLoginRequest(User user, final UserLoginCallback callback) {
        LoginUserApiService api = retrofitService.create(LoginUserApiService.class);
        api.login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                boolean returnValue;
                if (response.isSuccessful()) {
                    User loggedInUser = response.body();
                    returnValue = callback.onLoginSuccess(loggedInUser);
                } else {
                    returnValue = callback.onLoginFailure(response.message());
                }

                if (returnValue)
                    Log.d("User Login", "User logged in correctly!");
                else
                    Log.d("User Login Error", "Could not login user!");
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                boolean returnValue = callback.onLoginFailure(t.getMessage());

                if (!returnValue) {
                    Log.d("User Login Error", "Could not login user!");
                    Log.d("User Login Error", Objects.requireNonNull(t.getMessage()));
                }
            }
        });
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

    public boolean sendByteArrayImageContentToServer(byte[] imageContent) {
        // MANDIAMO PRIMA byte[] imageContent e poi Item e Auction nella speranza che la registrazione in DB vada a buon fine
        try {
            SendByteArrayApiService api = retrofitService.create(SendByteArrayApiService.class);
            Call<Void> call = api.uploadImageContent(imageContent);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Log.d("IMMAGINE", "MANDATA CON SUCCESSO");
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Log.d("IMMAGINE", "INVIO FALLITO");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}