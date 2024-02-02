package com.example.dietideals24frontend.Presenter;

import android.content.Context;
import android.content.Intent;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.Model.User;

public interface ActivityPresenterInterface {
    Intent createIntentForHome(Context context, User loggedInUser);
    Intent createIntentForCreateAuction(Context context, User loggedInUser);
    Intent createAuctionIntent(Context context, User loggedInUser, Item item);
    Intent createProfileIntent(Context context, User user);
}