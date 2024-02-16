package com.example.dietideals24frontend.Presenter;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.Auction;
import com.example.dietideals24frontend.View.Fragment.*;

public interface FragmentPresenterInterface {
    LogInFragment createLoginFragment();
    SignUpFragment createSignUpFragment();
    SilentAuctionCreationFragment createSilentAuctionCreationFragment(User user);
    EnglishAuctionCreationFragment createEnglishAuctionCreationFragment(User user);
    SearchAuctionFragment createSearchAuctionFragment(User user);
    HomeFragment createHomeFragment(User user);
    SilentAuctionFragment createSilenAuctionFragment(User loggedInUser, Auction auction, boolean hasAuctionEnded);
    EnglishAuctionFragment createEnglishAuctionFragment(User loggedInUser, Auction auction);
    UserProfileFragment createUserProfileFragment(User loggedInUser);
}