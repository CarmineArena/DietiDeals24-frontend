package com.example.dietideals24frontend.Controller.UserController;

import retrofit2.Call;
import android.util.Log;
import java.util.Objects;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import androidx.annotation.NonNull;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Controller.UserController.Retrofit.*;
import com.example.dietideals24frontend.Controller.UserController.Callback.*;
import com.example.dietideals24frontend.Controller.UserController.Interface.UserRequestInterface;

public class UserController implements UserRequestInterface {
    private final Retrofit retrofitService;

    public UserController(Retrofit retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public void sendUserLoginRequest(User user, final LoginUserCallback callback) {
        LoginUserService api = retrofitService.create(LoginUserService.class);

        String email = user.getEmail();
        String passw = user.getPassword();
        api.login(email, passw).enqueue(new Callback<User>() {
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
                    Log.i("User Login", "User logged in correctly!");
                else
                    Log.e("User Login Error", "Could not login user!");
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                boolean returnValue = callback.onLoginFailure(t.getMessage());

                if (!returnValue) {
                    Log.e("User Login Error", "Could not login user!");
                    Log.e("User Login Error", Objects.requireNonNull(t.getMessage()));
                }
            }
        });
    }

    @Override
    public void sendUserRegistrationRequest(User user, final RegisterUserCallback callback) {
        RegisterUserService api = retrofitService.create(RegisterUserService.class);
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
                    Log.i("User Register", "User registered correctly!");
                else
                    Log.e("User Register Error", "Could not register user!");
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                boolean returnValue = callback.onRegistrationFailure(t.getMessage());

                if (!returnValue) {
                    Log.e("User Register Error", "Could not register user!");
                    Log.e("User Register Error", Objects.requireNonNull(t.getMessage()));
                }
            }
        });
    }
}