package com.example.dietideals24frontend.View;

import android.os.Bundle;
import android.content.Intent;

import android.util.Log;
import android.widget.Button;

import android.view.*;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.UserDTO;
import com.example.dietideals24frontend.utility.EmailValidator;
import com.example.dietideals24frontend.Retrofit.Service.PostRequester;
import com.example.dietideals24frontend.Retrofit.Callback.UserLoginCallback;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.View.Dialog.Dialog;
import com.example.dietideals24frontend.Presenter.ActivityFactory;

public class LogInFragment extends Fragment {
    private Retrofit retrofitService;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log_in, container, false);

        Dialog dialog = new Dialog(getContext());
        retrofitService = MainActivity.retrofitService;

        Button button = view.findViewById(R.id.btnLogIn);
        button.setOnClickListener(v -> {
            EditText mailField     = view.findViewById(R.id.editTextTextEmailAddress);
            EditText passwordField = view.findViewById(R.id.editTextTextPassword);

            String email    = mailField.getText().toString();
            String password = passwordField.getText().toString();

            EmailValidator validator = new EmailValidator();
            if (email.isEmpty() || password.isEmpty() || !validator.validate(email)) {
                dialog.showAlertDialog("FORM ERROR", "Controllare la correttezza delle credenziali e non lasciare campi vuoti.");
            } else {
                UserDTO user = new UserDTO();
                user.setEmail(email);
                user.setPassword(password);

                PostRequester sender = new PostRequester(retrofitService);
                sender.sendUserLoginRequest(user, new UserLoginCallback() {
                    @Override
                    public boolean onLoginSuccess(UserDTO loggedInUser) {
                        ActivityFactory activityFactory = new ActivityFactory();
                        Intent intent = activityFactory.createIntentForHome(getActivity(), loggedInUser);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onLoginFailure(String errorMessage) {
                        Log.d("onLoginFailure", errorMessage);
                        dialog.showAlertDialog("LOGIN ERROR", "Non hai ancora creato un Account.");
                        return false;
                    }
                });
            }
        });
        return view;
    }
}