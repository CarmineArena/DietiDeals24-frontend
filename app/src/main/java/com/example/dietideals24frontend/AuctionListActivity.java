package com.example.dietideals24frontend;

import android.os.Bundle;
import android.content.Intent;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Presenter.LinearLayoutForItemsPresenter;

public class AuctionListActivity extends AppCompatActivity {

    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_list);

        Intent intent = getIntent();
        loggedInUser = (User) intent.getSerializableExtra("loggedInUser");

        LinearLayoutForItemsPresenter presenter = new LinearLayoutForItemsPresenter(getApplicationContext(), MainActivity.retrofitService, getSupportFragmentManager());

        LinearLayout layout1 = findViewById(R.id.yourAuctionLayout);
        presenter.createAuctionedByUserItemsLayout(layout1, loggedInUser);

        LinearLayout layout2 = findViewById(R.id.joinedAuctionLayout);
        presenter.createItemsWantedByUserLayout(layout2, loggedInUser);

        // LinearLayout for SilentAuction for which the User needs to assign a winner
        LinearLayout layout3 = findViewById(R.id.terminatedAuctionLayout);
        presenter.createItemsWithNoWinnerLayout(layout3, loggedInUser);

        // TODO: DEFINIRE SEZIONE VISIBILE PER ASTE VINTE NOTIFICATE
    }
}