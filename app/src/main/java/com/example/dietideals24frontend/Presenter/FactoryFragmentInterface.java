package com.example.dietideals24frontend.Presenter;

import com.example.dietideals24frontend.View.*;
import com.example.dietideals24frontend.Model.User;

public interface FactoryFragmentInterface {
    LogInFragment createLoginFragment();
    SignUpFragment createSignUpFragment();
    SilentAuctionCreationFragment createSilentAuctionFragment(User user);
    EnglishAuctionCreationFragment createEnglishAuctionFragment(User user);
}