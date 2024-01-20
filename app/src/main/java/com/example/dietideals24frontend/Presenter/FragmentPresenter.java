package com.example.dietideals24frontend.Presenter;

import android.os.Bundle;
import com.example.dietideals24frontend.View.*;
import com.example.dietideals24frontend.Model.User;

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
    public SilentAuctionCreationFragment createSilentAuctionFragment(User user) {
        Bundle bundle = createBundleWithLoggedInUser(user);
        SilentAuctionCreationFragment fragment = new SilentAuctionCreationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public EnglishAuctionCreationFragment createEnglishAuctionFragment(User user) {
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

    private Bundle createBundleWithLoggedInUser(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("loggedInUser", user);
        return bundle;
    }
}