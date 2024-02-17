package com.example.dietideals24frontend.Controller.AuctionController.Interface;

import com.example.dietideals24frontend.Model.DTO.ItemDTO;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;
import com.example.dietideals24frontend.Controller.AuctionController.Callback.*;
import com.example.dietideals24frontend.Utility.Exception.UnhandledOptionException;

public interface AuctionRequestInterface {
    void sendItemImageContent(byte[] itemImageContent, final ImageContentRegisterCallback callback);
    void sendRegisterItemRequest(ItemDTO requestedItem, final RegisterItemCallback callback);
    void sendRegisterAuctionRequest(AuctionDTO auctionDTO, final RegisterAuctionCallback callback);
    void sendFindAuctionRequest(Integer itemId, String name, String description, final RetrieveAuctionCallback callback) throws UnhandledOptionException;
    void sendCloseAuctionRequest(Integer auctionId, Integer userId, Float winningBid, final CloseAuctionCallback callback);
    void sendGetTimeRemainingRequest(Integer auctionId, Integer userId, final TimeRemainingCallback callback);
    void sendGetWinningBidRequest(Integer itemId, final RetrieveWinningBidCallback callback);
}