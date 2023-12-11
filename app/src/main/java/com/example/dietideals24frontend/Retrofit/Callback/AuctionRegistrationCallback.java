package com.example.dietideals24frontend.Retrofit.Callback;

import com.example.dietideals24frontend.Model.RequestedAuctionDTO;

public interface AuctionRegistrationCallback {
    boolean onAuctionRegistrationSuccess(RequestedAuctionDTO requestedAuctionDTO); // This parameter could be useful
    boolean onAuctionRegistrationFailure(String errorMessage);
}