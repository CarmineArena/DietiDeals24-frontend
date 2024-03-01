package com.example.dietideals24frontend.View.Fragment;

import android.os.Bundle;
import android.content.Intent;

import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.MainActivity;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.DTO.UserDTO;
import com.example.dietideals24frontend.Utility.EmailValidator;
import com.example.dietideals24frontend.Controller.UserController.UserController;
import com.example.dietideals24frontend.Controller.UserController.Callback.LoginUserCallback;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.View.Dialog.Dialog;
import com.example.dietideals24frontend.Presenter.ActivityPresenter;
import com.google.firebase.analytics.FirebaseAnalytics;

public class LogInFragment extends Fragment {
    private View view;
    private Retrofit retrofitService;
    private FirebaseAnalytics analytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log_in, container, false);

        Dialog dialog = new Dialog(getContext());
        retrofitService = MainActivity.retrofitService;

        analytics = FirebaseAnalytics.getInstance(getContext());

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

                UserController controller = new UserController(retrofitService);
                controller.sendUserLoginRequest(user, new LoginUserCallback() {
                    @Override
                    public boolean onLoginSuccess(UserDTO userDTO) {
                        User loggedInUser = User.createUser(userDTO);

                        // Login Analytics
                        Bundle bundle = new Bundle();
                        analytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
                        analytics.setAnalyticsCollectionEnabled(true);

                        ActivityPresenter activityFactory = new ActivityPresenter();
                        Intent intent = activityFactory.createIntentForHome(getActivity(), loggedInUser);
                        startActivity(intent);
                        getActivity().finish();
                        return true;
                    }

                    @Override
                    public boolean onLoginFailure(String errorMessage) {
                        dialog.showAlertDialog("LOGIN ERROR", "Non hai ancora creato un Account.");
                        return false;
                    }
                });
            }
        });
        return view;
    }
}