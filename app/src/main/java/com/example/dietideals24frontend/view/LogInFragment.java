package com.example.dietideals24frontend.view;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.modelDTO.User;
import com.example.dietideals24frontend.utility.EmailValidator;
import com.example.dietideals24frontend.utility.PostRequestSender;
import com.example.dietideals24frontend.utility.callback.UserLoginCallback;

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

            EmailValidator validator = new EmailValidator();
            if (email.isEmpty() || password.isEmpty() || !validator.validate(email)) {
                // TODO: Come gestiamo l'errore in questo caso?
            } else {
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);

                PostRequestSender sender = new PostRequestSender(retrofitService);
                sender.sendUserLoginRequest(user, new UserLoginCallback() {
                    @Override
                    public boolean onLoginSuccess(User loggedInUser) {
                        Intent intent = new Intent(getActivity(), Home.class);
                        intent.putExtra("loggedInUser", loggedInUser);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onLoginFailure(String errorMessage) {
                        Log.d("onLoginFailure", errorMessage);
                        return false;
                    }
                });
            }
        });
        return view;
    }
}