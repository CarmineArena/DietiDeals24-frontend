package com.example.dietideals24frontend.Presenter;

import android.content.Intent;
import android.content.Context;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.Model.User;

public interface ActivityPresenterInterface {
    Intent createIntentForHome(Context context, User loggedInUser);
    Intent createIntentForCreateAuction(Context context, User loggedInUser);
    Intent createAuctionIntent(Context context, User loggedInUser, Item item, boolean hasAuctionEnded);
    Intent createProfileIntent(Context context, User user);
    Intent createAuctionListIntent(Context context, User user);
}