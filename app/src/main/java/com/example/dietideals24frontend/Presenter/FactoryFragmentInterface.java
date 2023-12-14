package com.example.dietideals24frontend.Presenter;

import com.example.dietideals24frontend.View.*;
import com.example.dietideals24frontend.Model.UserDTO;

public interface FactoryFragmentInterface {
    LogInFragment createLoginFragment();
    SignUpFragment createSignUpFragment();
    SilentAuctionCreationFragment createSilentAuctionFragment(UserDTO user);
    EnglishAuctionCreationFragment createEnglishAuctionFragment(UserDTO user);
}