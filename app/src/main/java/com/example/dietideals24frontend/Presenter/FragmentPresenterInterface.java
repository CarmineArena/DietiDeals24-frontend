package com.example.dietideals24frontend.Presenter;

import com.example.dietideals24frontend.View.*;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.View.Dialog.SearchAuctionFragment;

public interface FragmentPresenterInterface {
    LogInFragment createLoginFragment();
    SignUpFragment createSignUpFragment();
    SilentAuctionCreationFragment createSilentAuctionFragment(User user);
    EnglishAuctionCreationFragment createEnglishAuctionFragment(User user);
    SearchAuctionFragment
    HomeFragment createHomeFragment(User user);
}