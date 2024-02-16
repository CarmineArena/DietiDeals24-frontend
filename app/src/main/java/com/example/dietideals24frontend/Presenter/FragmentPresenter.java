package com.example.dietideals24frontend.Presenter;

import android.os.Bundle;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.Auction;
import com.example.dietideals24frontend.View.Fragment.EnglishAuctionFragment;
import com.example.dietideals24frontend.View.Fragment.UserProfileFragment;
import com.example.dietideals24frontend.View.Fragment.SearchAuctionFragment;
import com.example.dietideals24frontend.View.Fragment.HomeFragment;
import com.example.dietideals24frontend.View.Fragment.LogInFragment;
import com.example.dietideals24frontend.View.Fragment.SignUpFragment;
import com.example.dietideals24frontend.View.Fragment.SilentAuctionFragment;
import com.example.dietideals24frontend.View.Fragment.SilentAuctionCreationFragment;
import com.example.dietideals24frontend.View.Fragment.EnglishAuctionCreationFragment;

public class FragmentPresenter implements FragmentPresenterInterface {
    @Override
    public LogInFragment createLoginFragment() {
        return new LogInFragment();
    }

    @Override
    public SignUpFragment createSignUpFragment() {
        return new SignUpFragment();
    }

    @Override
    public SilentAuctionCreationFragment createSilentAuctionCreationFragment(User user) {
        Bundle bundle = createBundleWithLoggedInUser(user);
        SilentAuctionCreationFragment fragment = new SilentAuctionCreationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public EnglishAuctionCreationFragment createEnglishAuctionCreationFragment(User user) {
        Bundle bundle = createBundleWithLoggedInUser(user);
        EnglishAuctionCreationFragment fragment = new EnglishAuctionCreationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public HomeFragment createHomeFragment(User user) {
        Bundle bundle = createBundleWithLoggedInUser(user);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public SearchAuctionFragment createSearchAuctionFragment(User user) {
        Bundle bundle = createBundleWithLoggedInUser(user);
        SearchAuctionFragment fragment = new SearchAuctionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public SilentAuctionFragment createSilenAuctionFragment(User loggedInUser, Auction auction, boolean hasAuctionEnded) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedAuction", auction);
        bundle.putSerializable("loggedInUser", loggedInUser);
        bundle.putSerializable("hasAuctionEnded", hasAuctionEnded);

        SilentAuctionFragment fragment = new SilentAuctionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public EnglishAuctionFragment createEnglishAuctionFragment(User loggedInUser, Auction auction) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedAuction", auction);
        bundle.putSerializable("loggedInUser", loggedInUser);

        EnglishAuctionFragment fragment = new EnglishAuctionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public UserProfileFragment createUserProfileFragment(User user) {
        Bundle bundle = createBundleWithLoggedInUser(user);
        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private Bundle createBundleWithLoggedInUser(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("loggedInUser", user);
        return bundle;
    }
}