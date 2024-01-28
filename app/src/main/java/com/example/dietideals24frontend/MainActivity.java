package com.example.dietideals24frontend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.annotation.SuppressLint;

import androidx.fragment.app.*;
import androidx.appcompat.app.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.example.dietideals24frontend.Retrofit.AuctionNotificationService;
import com.example.dietideals24frontend.Utility.Task.AuctionNotificationTask;
import com.example.dietideals24frontend.Retrofit.Callback.AuctionNotificationCallback;

import com.example.dietideals24frontend.View.LogInFragment;
import com.example.dietideals24frontend.View.SignUpFragment;
import com.example.dietideals24frontend.Presenter.FragmentPresenter;
import com.example.dietideals24frontend.View.Notification.AuctionNotificationManager;

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
            AuctionNotificationService service = retrofitService.create(AuctionNotificationService.class);
            AuctionNotificationTask task = new AuctionNotificationTask(service, new AuctionNotificationCallback() {
                @Override
                public void onNotificationsReceived(List<String> notifications) {
                    if (notifications != null) {
                        AuctionNotificationManager manager = new AuctionNotificationManager(getApplicationContext());

                        for (String notification : notifications) {
                            manager.showNotification(1, "Auction Expired", notification);
                        }
                    }
                }

                @Override
                public void onApiError() {
                    // TODO: HANDLE ERROR CASE (IT SHOULD NOT HAPPEN)
                }
            });
            task.execute();
        }, 0, interval, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduler != null) scheduler.shutdown();
    }
}