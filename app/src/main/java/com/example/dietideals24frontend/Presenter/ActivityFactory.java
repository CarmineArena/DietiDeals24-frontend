package com.example.dietideals24frontend.Presenter;

import android.content.Intent;
import android.content.Context;
import com.example.dietideals24frontend.View.*;
import com.example.dietideals24frontend.Model.UserDTO;

public class ActivityFactory implements FactoryActivityInterface {
    @Override
    public Intent createIntentForHome(Context context, UserDTO loggedInUser) {
        Intent intent = new Intent(context, Home.class);
        intent.putExtra("loggedInUser", loggedInUser);
        return intent;
    }

    @Override
    public Intent createIntentForCreateAuction(Context context, UserDTO loggedInUser) {
        Intent intent = new Intent(context, CreateAuction.class);
        intent.putExtra("loggedInUser", loggedInUser);
        return intent;
    }

    @Override
    public Intent createIntentForSearchAuction(Context context, UserDTO loggedInUser) {
        Intent intent = new Intent(context, SearchAuctionActivity.class);
        intent.putExtra("loggedInUser", loggedInUser);
        return intent;
    }
}