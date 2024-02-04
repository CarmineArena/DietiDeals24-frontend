package com.example.dietideals24frontend.Controller.UserController;

import android.util.Log;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import androidx.annotation.NonNull;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.DTO.UserDTO;
import com.example.dietideals24frontend.Controller.UserController.Retrofit.*;
import com.example.dietideals24frontend.Controller.UserController.Callback.*;
import com.example.dietideals24frontend.Controller.UserController.Interface.UserRequestInterface;

public class UserController implements UserRequestInterface {
    private final Retrofit retrofitService;

    public UserController(Retrofit retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public void sendUserLoginRequest(UserDTO user, final LoginUserCallback callback) {
        LoginUserService api = retrofitService.create(LoginUserService.class);

        api.loginUser(user).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("User Login", "User logged in correctly!");
                    callback.onLoginSuccess(response.body());
                } else {
                    String message = response.errorBody() != null ? response.errorBody().toString() : "Received empty body as response!";
                    Log.e("User Login Error", "Could not login user!");
                    callback.onLoginFailure(message);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDTO> call, @NonNull Throwable t) {
                boolean returnValue = callback.onLoginFailure(t.getMessage());
                Log.e("User Login Error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public void sendUserRegistrationRequest(UserDTO userDTO, final RegisterUserCallback callback) {
        RegisterUserService api = retrofitService.create(RegisterUserService.class);
        api.registerUser(userDTO).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    User loggedInUser = new User();
                    loggedInUser.setUserId(response.body().getUserId());
                    loggedInUser.setName(response.body().getName());
                    loggedInUser.setSurname(response.body().getSurname());
                    loggedInUser.setEmail(response.body().getEmail());
                    loggedInUser.setPassword(response.body().getPassword());

                    Log.i("User Register", "User registered correctly!");
                    callback.onRegistrationSuccess(loggedInUser);
                } else {
                    Log.e("User Register Error", "Could not register user!");
                    callback.onRegistrationFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDTO> call, @NonNull Throwable t) {
                Log.e("User Register Error", Objects.requireNonNull(t.getMessage()));
                callback.onRegistrationFailure(t.getMessage());
            }
        });
    }

    @Override
    public void sendRetrieveUserDataRequest(Integer userId, String email, final RetrieveUserCallback callback) {
        RetrieveUserService api = retrofitService.create(RetrieveUserService.class);

        Call<UserDTO> call = api.retrieveUser(userId, email);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    Log.i("User Retrieval", "User data retrieved correctly!");
                    callback.onUserRetrievalSuccess(response.body());
                } else {
                    Log.e("User Retrieval Error", "Could not retrieve user data!");
                    callback.onUserRetrievalFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDTO> call, @NonNull Throwable t) {
                callback.onUserRetrievalFailure(t.getMessage());
                Log.e("User Retrieval Error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public void sendUpdateBioAndWebsiteRequest(UserDTO userDTO, final UpdateUserCallback callback) {
        UpdateUserService api = retrofitService.create(UpdateUserService.class);

        Call<Void> call = api.update(userDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("User Update", "User data updated correctly!");
                    callback.onUserUpdateSuccess();
                } else {
                    Log.e("User Update Error", "Could not update user data!");
                    callback.onUserUpdateFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("User Update Error", Objects.requireNonNull(t.getMessage()));
                callback.onUserUpdateFailure(t.getMessage());
            }
        });
    }
}