package com.example.dietideals24frontend.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import com.example.dietideals24frontend.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Presenter.ActivityFactory;
import com.example.dietideals24frontend.View.Dialog.HomeFragment;

/*public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: Mancano i bottoni per aggiornare Bio e/o link al sito web dell'utente.

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("loggedInUser");

        TextView welcomeView = findViewById(R.id.WelcomeField);
        welcomeView.setText(String.format("Benvenuto, %s %s", user.getName(), user.getSurname()));

        ActivityFactory activityFactory = new ActivityFactory();

        Button btnCreate = findViewById(R.id.bntCreate);
        btnCreate.setOnClickListener(v -> {
            Intent intent1 = activityFactory.createIntentForCreateAuction(Home.this, user);
            startActivity(intent1);
        });

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            Intent intent1 = activityFactory.createIntentForSearchAuction(Home.this, user);
            startActivity(intent1);
        });
    }
}*/

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button btnCreate = findViewById(R.id.btnCreate);
        replaceFragment(new HomeFragment());
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, CreateAuction.class);
                startActivity(intent);
            }
        });
        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SearchAuctionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameGeneral,fragment);
        fragmentTransaction.commit();
    }
}