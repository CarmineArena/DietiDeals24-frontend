package com.example.dietideals24frontend.Presenter;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.Auction;
import com.example.dietideals24frontend.View.Fragment.UserProfileFragment;
import com.example.dietideals24frontend.View.SearchAuctionFragment;
import com.example.dietideals24frontend.View.Fragment.HomeFragment;
import com.example.dietideals24frontend.View.Fragment.LogInFragment;
import com.example.dietideals24frontend.View.Fragment.SignUpFragment;
import com.example.dietideals24frontend.View.EnglishAuctionCreationFragment;
import com.example.dietideals24frontend.View.Fragment.SilentAuctionFragment;
import com.example.dietideals24frontend.View.Fragment.SilentAuctionCreationFragment;

public interface FragmentPresenterInterface {
    LogInFragment createLoginFragment();
    SignUpFragment createSignUpFragment();
    SilentAuctionCreationFragment createSilentAuctionFragment(User user);
    EnglishAuctionCreationFragment createEnglishAuctionFragment(User user);
    SearchAuctionFragment createSearchAuctionFragment(User user);
    HomeFragment createHomeFragment(User user);
    SilentAuctionFragment createSilenAuctionFragment(User loggedInUser, Auction auction);
    UserProfileFragment createUserProfileFragment(User loggedInUser);
}