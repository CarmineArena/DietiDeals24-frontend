package com.example.dietideals24frontend.Presenter;

import android.content.Intent;
import android.content.Context;
import com.example.dietideals24frontend.View.*;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.Model.User;

public class ActivityPresenter implements ActivityPresenterInterface {
    @Override
    public Intent createIntentForHome(Context context, User loggedInUser) {
        Intent intent = new Intent(context, Home.class);
        intent.putExtra("loggedInUser", loggedInUser);
        return intent;
    }

    @Override
    public Intent createIntentForCreateAuction(Context context, User loggedInUser) {
        Intent intent = new Intent(context, CreateAuction.class);
        intent.putExtra("loggedInUser", loggedInUser);
        return intent;
    }

    @Override
    public Intent createAuctionIntent(Context context, Item item) {
        Intent intent = new Intent(context, AuctionActivity.class);
        intent.putExtra("selectedItem", item);
        return intent;
    }
}