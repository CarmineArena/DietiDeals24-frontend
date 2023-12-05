package com.example.dietideals24frontend.view;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import com.example.dietideals24frontend.R;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dietideals24frontend.modelDTO.User;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: Quali sono i tasti per aggiornare Bio e/o link al sito web?

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("loggedInUser");

        TextView welcomeView = findViewById(R.id.WelcomeField);
        welcomeView.setText(String.format("Benvenuto, %s %s", user.getName(), user.getSurname()));

        Button btnCreate = findViewById(R.id.bntCreate);
        btnCreate.setOnClickListener(v -> {
            Intent intent1 = new Intent(Home.this, CreateAuction.class);
            intent1.putExtra("loggedInUser", user);
            startActivity(intent1);
        });
    }
}