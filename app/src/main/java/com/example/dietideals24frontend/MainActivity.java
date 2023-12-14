package com.example.dietideals24frontend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.annotation.SuppressLint;

import androidx.fragment.app.*;
import androidx.appcompat.app.*;

import com.example.dietideals24frontend.View.LogInFragment;
import com.example.dietideals24frontend.View.SignUpFragment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.dietideals24frontend.Presenter.FragmentFactory;

// TODO: 1. LOGIN SHOULD BE A GET REQUEST (CHANGE REQUEST TYPE)
// TODO: 2. DIVIDE "VIEW" PACKAGE IN SUB-PACKAGES (FOR ACTIVITIES, FRAGMENTS AND SO ON)

public class MainActivity extends AppCompatActivity {
    public static final String BASE_URL = ""; // TODO: REMEMBER NOT TO PUSH THIS
    public static Retrofit retrofitService;
    private Button btnFragment;
    private TextView TextFragment;

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CREATE RETROFIT INSTANCE
        retrofitService = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FragmentFactory fragmentFactory = new FragmentFactory();

        LogInFragment logInFragment   = fragmentFactory.createLoginFragment();
        SignUpFragment signUpFragment = fragmentFactory.createSignUpFragment();
        replaceFragment(logInFragment);

        btnFragment  = findViewById(R.id.btnSwitchLogInFragment);
        TextFragment = findViewById(R.id.TextFragment);

        btnFragment.setOnClickListener(v -> {
            if (btnFragment.getText().equals("Registrati")) {
                TextFragment.setText("Hai già un account?");
                btnFragment.setText("Accedi");
                replaceFragment(signUpFragment);
            } else {
                TextFragment.setText("Sei un nuovo utente?");
                btnFragment.setText("Registrati");
                replaceFragment(logInFragment);
            }
        });
    }
}