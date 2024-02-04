package com.example.dietideals24frontend;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dietideals24frontend.Model.User;

public class AuctionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_list);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("loggedInUser");
    }
}