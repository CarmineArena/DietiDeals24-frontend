package com.example.dietideals24frontend.Presenter;

import android.content.Context;
import android.content.Intent;

import com.example.dietideals24frontend.Model.User;

public interface FactoryActivityInterface {
    Intent createIntentForHome(Context context, User loggedInUser);
    Intent createIntentForCreateAuction(Context context, User loggedInUser);
    Intent createIntentForSearchAuction(Context context, User loggedInUser);
}