package com.example.dietideals24frontend.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.modelDTO.User;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("loggedInUser");

        Button btnCreate = findViewById(R.id.bntCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, CreateAuction.class);
                startActivity(intent);
            }
        });
    }
}