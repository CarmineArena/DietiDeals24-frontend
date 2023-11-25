package com.example.dietideals24frontend;

import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.dietideals24frontend.view.LogInFragment;
import com.example.dietideals24frontend.view.SignUpFragment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static String BASE_URL = ""; // TODO: REMEMBER TO NOT PUSH THIS
    public static Retrofit retrofitService;
    private Button btnFragment;

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

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
        btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnFragment.getText().equals("Sei un nuovo utente? Registrati")) {
                    btnFragment.setText("Hai già un account? Accedi");
                    replaceFragment(new SignUpFragment());
                } else {
                    btnFragment.setText("Sei un nuovo utente? Registrati");
                    replaceFragment(new LogInFragment());
                }
            }
        });
    }
}