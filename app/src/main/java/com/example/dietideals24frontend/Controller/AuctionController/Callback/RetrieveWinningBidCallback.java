package com.example.dietideals24frontend.Controller.AuctionController.Callback;

public interface RetrieveWinningBidCallback {
    boolean onWinningBidRetrievalSuccess(Float winningBid);
    boolean onWinningBidRetrievalFailure(String errorMessage);
}