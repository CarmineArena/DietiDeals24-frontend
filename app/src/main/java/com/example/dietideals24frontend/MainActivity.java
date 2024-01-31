package com.example.dietideals24frontend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.annotation.SuppressLint;

import androidx.fragment.app.*;
import androidx.appcompat.app.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.example.dietideals24frontend.View.Fragment.LogInFragment;
import com.example.dietideals24frontend.Presenter.FragmentPresenter;
import com.example.dietideals24frontend.View.Fragment.SignUpFragment;
import com.example.dietideals24frontend.Controller.AuctionNotificationController.AuctionNotificationController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String BASE_URL = ""; // ALWAYS REMEMBER NOT TO PUSH THIS
    public static Retrofit retrofitService;
    private Button btnFragment;
    private TextView TextFragment;
    private ScheduledExecutorService scheduler;

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CREATE RETROFIT INSTANCE
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        retrofitService = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FragmentPresenter fragmentFactory = new FragmentPresenter();

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

        scheduler = Executors.newSingleThreadScheduledExecutor();
        startNotificationTask();
    }

    private void startNotificationTask() {
        long interval = 20 * 1000; // 20 seconds

        scheduler.scheduleAtFixedRate(() -> {
            AuctionNotificationController controller = new AuctionNotificationController(retrofitService, getApplicationContext());
            controller.notifyUser();
        }, 0, interval, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduler != null) scheduler.shutdown();
    }
}