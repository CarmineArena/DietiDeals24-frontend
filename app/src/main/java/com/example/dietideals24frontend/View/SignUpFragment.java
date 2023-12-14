package com.example.dietideals24frontend.View;

import android.content.Intent;

import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.fragment.app.Fragment;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.UserDTO;
import com.example.dietideals24frontend.Presenter.ActivityFactory;

import retrofit2.Retrofit;

import com.example.dietideals24frontend.utility.EmailValidator;
import com.example.dietideals24frontend.Retrofit.Service.PostRequester;
import com.example.dietideals24frontend.Retrofit.Callback.UserRegistrationCallback;

public class SignUpFragment extends Fragment {
    private Retrofit retrofitService;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        retrofitService = MainActivity.retrofitService;

        Button button = view.findViewById(R.id.btnSignUp);
        button.setOnClickListener(v -> {
            EditText nameField     = view.findViewById(R.id.nameField);
            EditText surnameField  = view.findViewById(R.id.surnameField);
            EditText mailField     = view.findViewById(R.id.mailField);
            EditText passwordField = view.findViewById(R.id.editTextTextPassword2);

            String name     = nameField.getText().toString();
            String surname  = surnameField.getText().toString();
            String email    = mailField.getText().toString();
            String password = passwordField.getText().toString();
            
            EmailValidator validator = new EmailValidator();
            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || !validator.validate(email)) {
                // TODO: Come gestiamo l'errore in questo caso?
            } else {
                UserDTO user = new UserDTO();
                user.setName(name);
                user.setSurname(surname);
                user.setEmail(email);
                user.setPassword(password);

                PostRequester sender = new PostRequester(retrofitService);
                sender.sendUserRegistrationRequest(user, new UserRegistrationCallback() {
                    @Override
                    public boolean onRegistrationSuccess(UserDTO loggedInUser) {
                        ActivityFactory activityFactory = new ActivityFactory();
                        Intent intent = activityFactory.createIntentForHome(getActivity(), loggedInUser);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onRegistrationFailure(String errorMessage) {
                        Log.d("onRegistrationFailure", errorMessage);
                        return false;
                    }
                });
            }
        });
        return view;
    }
}