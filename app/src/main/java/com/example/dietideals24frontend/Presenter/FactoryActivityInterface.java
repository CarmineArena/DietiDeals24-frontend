package com.example.dietideals24frontend.Presenter;

import android.content.Context;
import android.content.Intent;

import com.example.dietideals24frontend.Model.UserDTO;

public interface FactoryActivityInterface {
    Intent createIntentForHome(Context context, UserDTO loggedInUser);
    Intent createIntentForCreateAuction(Context context, UserDTO loggedInUser);
    Intent createIntentForSearchAuction(Context context, UserDTO loggedInUser);
}