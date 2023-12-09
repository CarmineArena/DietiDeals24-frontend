package com.example.dietideals24frontend.utility;

import android.util.Log;
import java.util.Objects;
import androidx.annotation.NonNull;
import com.example.dietideals24frontend.modelDTO.Item;
import com.example.dietideals24frontend.modelDTO.User;
import com.example.dietideals24frontend.retrofit.AddItemApiService;
import com.example.dietideals24frontend.retrofit.LoginUserApiService;
import com.example.dietideals24frontend.retrofit.RegisterUserApiService;
import com.example.dietideals24frontend.utility.callback.UserLoginCallback;
import com.example.dietideals24frontend.utility.callback.UserRegistrationCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.util.Base64;

public class PostRequestSender implements Sender {
    private final Retrofit retrofitService;

    public PostRequestSender(Retrofit retrofitService) {
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

    /**********************************************************************************************************************************/

    // TODO: FARE REFACTOR COME PER LOGIN E REGISTRAZIONE
    public void sendItemImageContent(byte[] itemImageContent) {
        AddItemApiService api = retrofitService.create(AddItemApiService.class);
        Call<Void> call = api.sendItemImageContent(itemImageContent);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Send Item Image Content", "Item Image content sent correctly!");
                } else {
                    Log.e("Send Item Image Content Error", "Could not send byte[] content. Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Send Item Image Content Error", "Could not send byte[] content. Server error: " + Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    // TODO: FARE REFACTOR COME PER LOGIN E REGISTRAZIONE
    public void sendRegisterItemRequest(RequestedItem requestedItem) {
        AddItemApiService api = retrofitService.create(AddItemApiService.class);

        Call<Void> call = api.registerItem(requestedItem);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Add RequestedItem Procedure", "Item registered correctly!");
                } else {
                    Log.d("Add RequestedItem Procedure Error", "Could not register the Item provided. Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Add RequestedItem Procedure Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }
}