package com.example.dietideals24frontend.Retrofit.Callback;

import com.example.dietideals24frontend.Model.DTO.AuctionDTO;

public interface AuctionRegistrationCallback {
    boolean onAuctionRegistrationSuccess(AuctionDTO auctionDTO); // This parameter could be useful
    boolean onAuctionRegistrationFailure(String errorMessage);
}