package com.example.dietideals24frontend.View.Fragment;

import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.DTO.UserDTO;
import com.example.dietideals24frontend.Presenter.ActivityPresenter;
import com.example.dietideals24frontend.Controller.UserController.UserController;
import com.example.dietideals24frontend.Controller.UserController.Callback.RegisterUserCallback;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.View.Dialog.Dialog;
import com.example.dietideals24frontend.Utility.EmailValidator;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SignUpFragment extends Fragment {
    private View view;
    private Retrofit retrofitService;
    private FirebaseAnalytics analytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Dialog dialog = new Dialog(getContext());
        retrofitService = MainActivity.retrofitService;

        analytics = FirebaseAnalytics.getInstance(getContext());

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
                dialog.showAlertDialog("FORM ERROR", "Controllare la correttezza delle credenziali e non lasciare campi vuoti.");
            } else {
                UserDTO userDTO = new UserDTO();
                userDTO.setName(name);
                userDTO.setSurname(surname);
                userDTO.setEmail(email);
                userDTO.setPassword(password);

                UserController controller = new UserController(retrofitService);
                controller.sendUserRegistrationRequest(userDTO, new RegisterUserCallback() {
                    @Override
                    public boolean onRegistrationSuccess(User loggedInUser) {
                        // SignUp Analytics
                        Bundle bundle = new Bundle();
                        analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
                        analytics.setAnalyticsCollectionEnabled(true);

                        ActivityPresenter activityFactory = new ActivityPresenter();
                        Intent intent = activityFactory.createIntentForHome(getActivity(), loggedInUser);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onRegistrationFailure(String errorMessage) {
                        Log.d("onRegistrationFailure", errorMessage);
                        dialog.showAlertDialog("REGISTER ERROR", "Non è stato possibile effettuare la registrazione.");
                        return false;
                    }
                });
            }
        });
        return view;
    }
}