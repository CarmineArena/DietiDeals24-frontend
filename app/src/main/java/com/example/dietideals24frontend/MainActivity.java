package com.example.dietideals24frontend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.dietideals24frontend.view.LogInFragment;
import com.example.dietideals24frontend.view.SignUpFragment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// TODO: RISOLVERE IL PROBLEMA DELL'INVIO DELL'IMMAGINE AL DB (LO STAI FACENDO IN DUE PROCESSI DIFFERENTI (VEDI FRAGMENT DI CREAZIONE ASTA SILENZIOSA))

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = ""; // TODO: REMEMBER NOT TO PUSH THIS
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

        replaceFragment((new LogInFragment()));


        btnFragment = findViewById(R.id.btnSwitchLogInFragment);
        TextFragment = findViewById(R.id.TextFragment);

        btnFragment.setOnClickListener(v -> {
            if (btnFragment.getText().equals("Registrati")) {
                TextFragment.setText("Hai già un account?");
                btnFragment.setText("Accedi");
                replaceFragment(new SignUpFragment());
            } else {
                TextFragment.setText("Sei un nuovo utente?");
                btnFragment.setText("Registrati");
                replaceFragment(new LogInFragment());
            }
        });
    }
}