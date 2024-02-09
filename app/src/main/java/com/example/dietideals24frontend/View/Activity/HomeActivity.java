package com.example.dietideals24frontend.View.Activity;

import android.Manifest;
import android.os.Build;
import android.util.Log;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.example.dietideals24frontend.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Controller.AuctionNotificationController.AuctionNotificationController;

import androidx.fragment.app.Fragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.View.Fragment.HomeFragment;
import com.example.dietideals24frontend.Presenter.ActivityPresenter;
import com.example.dietideals24frontend.Presenter.FragmentPresenter;
import com.example.dietideals24frontend.View.Fragment.UserProfileFragment;
import com.example.dietideals24frontend.View.Fragment.SearchAuctionFragment;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;

public class HomeActivity extends AppCompatActivity {
    private User loggedInUser;
    private ScheduledExecutorService scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        loggedInUser  = (User) intent.getSerializableExtra("loggedInUser");

        FragmentPresenter fragmentPresenter = new FragmentPresenter();
        ActivityPresenter activityPresenter = new ActivityPresenter();

        HomeFragment fragment = fragmentPresenter.createHomeFragment(loggedInUser);
        replaceFragment(fragment);

        Button btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(v -> {
            Intent intent1 = activityPresenter.createIntentForCreateAuction(HomeActivity.this, loggedInUser);
            startActivity(intent1);
        });

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            SearchAuctionFragment searchAuctionFragment = fragmentPresenter.createSearchAuctionFragment(loggedInUser);
            replaceFragment(searchAuctionFragment);
        });

        Button btnHome = findViewById(R.id.button2);
        btnHome.setOnClickListener(v -> {
            Intent intent1 = activityPresenter.createIntentForHome(HomeActivity.this, loggedInUser);
            startActivity(intent1);
        });

        Button btnProfile = findViewById(R.id.button3);
        btnProfile.setOnClickListener(v -> {
            UserProfileFragment userProfileFragment = fragmentPresenter.createUserProfileFragment(loggedInUser);
            replaceFragment(userProfileFragment);
        });

        checkAndRequestNotificationPermission();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameGeneral,fragment);
        fragmentTransaction.commit();
    }

    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            } else {
                startNotificationTask();
            }
        } else {
            startNotificationTask();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startNotificationTask();
            } else {
                // TODO: GESTIRE IL PERMISSION DENIED
                Log.e("PERMISSION DENIED", "NOTIFICATION PERMISSION DENIED");
            }
        }
    }

    private void startNotificationTask() {
        long interval = 10 * 1000; // 10 secondi

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            AuctionNotificationController controller = new AuctionNotificationController(loggedInUser.getUserId(), MainActivity.retrofitService, HomeActivity.this);
            controller.notifyUser();
        }, 0, interval, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduler != null) scheduler.shutdown();
    }
}