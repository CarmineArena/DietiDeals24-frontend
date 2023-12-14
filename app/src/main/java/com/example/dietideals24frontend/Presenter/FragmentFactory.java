package com.example.dietideals24frontend.Presenter;

import android.os.Bundle;
import com.example.dietideals24frontend.Model.UserDTO;
import com.example.dietideals24frontend.View.EnglishAuctionCreationFragment;
import com.example.dietideals24frontend.View.LogInFragment;
import com.example.dietideals24frontend.View.SignUpFragment;
import com.example.dietideals24frontend.View.SilentAuctionCreationFragment;

public class FragmentFactory implements FactoryFragmentInterface {
    @Override
    public LogInFragment createLoginFragment() {
        return new LogInFragment();
    }

    @Override
    public SignUpFragment createSignUpFragment() {
        return new SignUpFragment();
    }

    @Override
    public SilentAuctionCreationFragment createSilentAuctionFragment(UserDTO user) {
        Bundle bundle = createBundleWithLoggedInUser(user);
        SilentAuctionCreationFragment fragment = new SilentAuctionCreationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public EnglishAuctionCreationFragment createEnglishAuctionFragment(UserDTO user) {
        Bundle bundle = createBundleWithLoggedInUser(user);
        EnglishAuctionCreationFragment fragment = new EnglishAuctionCreationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private Bundle createBundleWithLoggedInUser(UserDTO user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("loggedInUser", user);
        return bundle;
    }
}