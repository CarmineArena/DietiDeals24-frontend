package com.example.dietideals24frontend.View.Fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;

import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;

import com.example.dietideals24frontend.R;
import com.google.android.material.snackbar.Snackbar;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Controller.UserController.Callback.UpdateUserCallback;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.DTO.UserDTO;
import com.example.dietideals24frontend.Controller.UserController.UserController;
import com.example.dietideals24frontend.Controller.UserController.Callback.RetrieveUserCallback;

public class UserProfileFragment extends Fragment {
    private View view;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Retrieve LoggedIn User
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("loggedInUser");
        } else {
            user = null;
        }

        UserController controller = new UserController(MainActivity.retrofitService);

        EditText bioText     = view.findViewById(R.id.editTextTextPersonName3);
        EditText WebSiteText = view.findViewById(R.id.editTextTextPersonName4);
        controller.sendRetrieveUserDataRequest(user.getUserId(), user.getEmail(), new RetrieveUserCallback() {
            @Override
            public boolean onUserRetrievalSuccess(UserDTO userDTO) {
                String bio = userDTO.getBio();
                if (bio != null && !bio.isEmpty()) bioText.setText(bio);

                String webSiteUrl = userDTO.getWebSiteUrl();
                if (webSiteUrl != null && !webSiteUrl.isEmpty()) WebSiteText.setText(webSiteUrl);

                Snackbar.make(view, "Dati utente recuperati!", Snackbar.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onUserRetrievalFailure(String errorMessage) {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                return false;
            }
        });

        Button updateUserBtn = view.findViewById(R.id.ConfirmButton);
        updateUserBtn.setOnClickListener(v -> {
            String bio        = bioText.getText().toString();
            String webSiteUrl = WebSiteText.getText().toString();

            if (bio.isEmpty() && webSiteUrl.isEmpty()) {
                Snackbar.make(view, "Almeno un campo deve essere riempito!", Snackbar.LENGTH_SHORT).show();
            } else {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(user.getUserId());
                userDTO.setName(user.getName());
                userDTO.setSurname(user.getSurname());
                userDTO.setEmail(user.getEmail());
                userDTO.setPassword(user.getPassword());
                userDTO.setBio(bio);
                userDTO.setWebSiteUrl(webSiteUrl);

                controller.sendUpdateBioAndWebsiteRequest(userDTO, new UpdateUserCallback() {
                    @Override
                    public boolean onUserUpdateSuccess() {
                        bioText.setText(userDTO.getBio());
                        WebSiteText.setText(userDTO.getWebSiteUrl());
                        Snackbar.make(view, "Dati utente aggiornati!", Snackbar.LENGTH_SHORT).show();
                        return true;
                    }

                    @Override
                    public boolean onUserUpdateFailure(String errorMessage) {
                        // TODO: COSA FACCIAMO IN CASO DI ERRORE
                        return false;
                    }
                });
            }
        });

        return view;
    }
}