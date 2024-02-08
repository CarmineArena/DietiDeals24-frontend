package com.example.dietideals24frontend.View.Activity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ScrollView;
import android.widget.TextView;
import android.annotation.SuppressLint;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.DTO.UserDTO;
import com.example.dietideals24frontend.Controller.UserController.UserController;
import com.example.dietideals24frontend.Controller.UserController.Callback.RetrieveUserCallback;

import androidx.appcompat.app.AppCompatActivity;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.View.ToastManager;

public class OtherProfileActivity extends AppCompatActivity {
    private User user;
    private ToastManager mToastManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        mToastManager = new ToastManager(this);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        // TODO: FACCIO COSI PERCHE NON MI PIACE LA SCRITTA "BENVENUTO NEL TUO PROFILO"
        TextView WelcomeView = findViewById(R.id.WelcomeView);
        WelcomeView.setText(user.getName() + " " + user.getSurname());

        TextView bioView = findViewById(R.id.editTextTextPersonName3);
        TextView urlView = findViewById(R.id.editTextTextPersonName4);
        ScrollView scrollView = findViewById(R.id.scrollView);

        UserController controller = new UserController(MainActivity.retrofitService);
        controller.sendRetrieveUserDataRequest(user.getUserId(), user.getEmail(), new RetrieveUserCallback() {
            @Override
            public boolean onUserRetrievalSuccess(UserDTO userDTO) {
                String bio = userDTO.getBio();
                if (bio != null && !bio.isEmpty()) bioView.setText(bio);
                else bioView.setText("");


                String webSiteUrl = userDTO.getWebSiteUrl();
                if (webSiteUrl != null && !webSiteUrl.isEmpty()) urlView.setText(webSiteUrl);
                else urlView.setText("");

                //Snackbar.make(scrollView, "Dati utente recuperati!", Snackbar.LENGTH_SHORT).show();
                mToastManager.showToast("Dati utente recuperati");

                return true;
            }

            @Override
            public boolean onUserRetrievalFailure(String errorMessage) {
                //Snackbar.make(scrollView, errorMessage, Snackbar.LENGTH_SHORT).show();
                mToastManager.showToastLong(errorMessage);
                return false;
            }
        });
    }
}