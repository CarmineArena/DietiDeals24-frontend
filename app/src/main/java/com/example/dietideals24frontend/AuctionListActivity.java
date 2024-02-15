package com.example.dietideals24frontend;

import android.os.Bundle;
import android.content.Intent;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Presenter.LinearLayoutForItemsPresenter;
import com.example.dietideals24frontend.R;

public class AuctionListActivity extends AppCompatActivity {

    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_list);

        Intent intent = getIntent();
        loggedInUser = (User) intent.getSerializableExtra("loggedInUser");

        LinearLayoutForItemsPresenter presenter = new LinearLayoutForItemsPresenter(getApplicationContext(), MainActivity.retrofitService, getSupportFragmentManager());

        RelativeLayout layout1 = findViewById(R.id.yourAuction);
        presenter.createAuctionedByUserItemsLayout(layout1, loggedInUser);

        RelativeLayout layout2 = findViewById(R.id.joinedAuction);
        presenter.createItemsWantedByUserLayout(layout2, loggedInUser);
    }
}