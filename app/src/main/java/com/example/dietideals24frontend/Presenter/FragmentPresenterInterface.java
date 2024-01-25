package com.example.dietideals24frontend.Presenter;

import com.example.dietideals24frontend.View.*;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;
import com.example.dietideals24frontend.View.SearchAuctionFragment;

public interface FragmentPresenterInterface {
    LogInFragment createLoginFragment();
    SignUpFragment createSignUpFragment();
    SilentAuctionCreationFragment createSilentAuctionFragment(User user);
    EnglishAuctionCreationFragment createEnglishAuctionFragment(User user);
    SearchAuctionFragment createSearchAuctionFragment(User user);
    HomeFragment createHomeFragment(User user);
    SilentAuctionFragment createSilenAuctionFragment(AuctionDTO auctionDTO, Item item);
}