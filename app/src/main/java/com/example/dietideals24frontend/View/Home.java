package com.example.dietideals24frontend.View;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import com.example.dietideals24frontend.R;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dietideals24frontend.Model.UserDTO;
import com.example.dietideals24frontend.Presenter.ActivityFactory;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: Mancano i bottoni per aggiornare Bio e/o link al sito web dell'utente.

        Intent intent = getIntent();
        UserDTO user = (UserDTO) intent.getSerializableExtra("loggedInUser");

        TextView welcomeView = findViewById(R.id.WelcomeField);
        welcomeView.setText(String.format("Benvenuto, %s %s", user.getName(), user.getSurname()));

        Button btnCreate = findViewById(R.id.bntCreate);
        btnCreate.setOnClickListener(v -> {
            ActivityFactory activityFactory = new ActivityFactory();
            Intent intent1 = activityFactory.createIntentForCreateAuction(Home.this, user);
            startActivity(intent1);
        });
    }
}