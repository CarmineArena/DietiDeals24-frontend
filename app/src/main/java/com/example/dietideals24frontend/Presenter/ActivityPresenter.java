package com.example.dietideals24frontend.Presenter;

import android.content.Intent;
import android.content.Context;

import com.example.dietideals24frontend.View.Activity.AuctionListActivity;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.View.Activity.AuctionActivity;
import com.example.dietideals24frontend.View.Activity.CreateAuctionActivity;
import com.example.dietideals24frontend.View.Activity.HomeActivity;
import com.example.dietideals24frontend.View.Activity.OtherProfileActivity;

public class ActivityPresenter implements ActivityPresenterInterface {
    @Override
    public Intent createIntentForHome(Context context, User loggedInUser) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("loggedInUser", loggedInUser);
        return intent;
    }

    @Override
    public Intent createIntentForCreateAuction(Context context, User loggedInUser) {
        Intent intent = new Intent(context, CreateAuctionActivity.class);
        intent.putExtra("loggedInUser", loggedInUser);
        return intent;
    }

    @Override
    public Intent createAuctionIntent(Context context, User loggedInUser, Item item, boolean hasAuctionEnded) {
        Intent intent = new Intent(context, AuctionActivity.class);
        intent.putExtra("selectedItem", item);
        intent.putExtra("loggedInUser", loggedInUser);
        intent.putExtra("hasAuctionEnded", hasAuctionEnded);
        return intent;
    }

    @Override
    public Intent createProfileIntent(Context context, User user) {
        Intent intent = new Intent(context, OtherProfileActivity.class);
        intent.putExtra("user", user);
        return intent;
    }

    @Override
    public Intent createAuctionListIntent(Context context, User loggedInUser) {
        Intent intent = new Intent(context, AuctionListActivity.class);
        intent.putExtra("loggedInUser", loggedInUser);
        return intent;
    }
}