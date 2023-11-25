package com.example.dietideals24frontend.view;

import android.os.Bundle;

import android.util.Log;
import android.widget.Button;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.modelDTO.User;
import com.example.dietideals24frontend.retrofit.LoginUserApiService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LogInFragment extends Fragment {
    private Retrofit retrofitService;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log_in, container, false);

        retrofitService = MainActivity.retrofitService;

        Button button = view.findViewById(R.id.btnLogIn);
        button.setOnClickListener(v -> {
            EditText mailField     = view.findViewById(R.id.editTextTextEmailAddress);
            EditText passwordField = view.findViewById(R.id.editTextTextPassword);

            String email    = mailField.getText().toString();
            String password = passwordField.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                // TODO: come gestire errore
                // TODO: controllare la correttezza della mail
            } else {
                // 1. Create User
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);

                // 2. Send User to the server to LogIn
                LoginUserApiService api = retrofitService.create(LoginUserApiService.class);
                api.login(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.isSuccessful()) {
                            Log.d("User Login", "User is logged in!");
                            // TODO: riportare ad una nuova schermata!
                            // TODO: devo ottenere indietro lo User?
                                // User loggedInUser = response.body();
                        } else {
                            Log.d("User Login Error", "Could not log in the user. Server error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Log.d("User Login", "Could not log in the user!");
                        Log.d("User Login Error", Objects.requireNonNull(t.getMessage()));

                        // TODO: cosa fare in questo caso ?

                        if (t instanceof HttpException) {
                            int errorCode = ((HttpException) t).code();
                            if (errorCode == 404) {
                                Log.d("User Login", "User not found for Login!");
                                // TODO: Aggiungi la logica per gestire il caso in cui l'utente non viene trovato
                            }
                        }
                    }
                });
            }
        });

        return view;
    }
}