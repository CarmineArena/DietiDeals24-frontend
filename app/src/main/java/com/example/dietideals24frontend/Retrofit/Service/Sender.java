package com.example.dietideals24frontend.Retrofit.Service;

import com.example.dietideals24frontend.Model.*;
import com.example.dietideals24frontend.Retrofit.Callback.*;
import com.example.dietideals24frontend.Model.RequestedItemDTO;

public interface Sender {
    /* POST REQUESTS */
    void sendUserRegistrationRequest(UserDTO user, final UserRegistrationCallback callback);
    void sendUserLoginRequest(UserDTO user, final UserLoginCallback callback);
    void sendItemImageContent(byte[] itemImageContent, final ImageContentRegistrationCallback callback);
    void sendRegisterItemRequest(RequestedItemDTO requestedItem, final ItemRegistrationCallback callback);
    void sendRegisterAuctionRequest(RequestedAuctionDTO requestedAuctionDTO, final AuctionRegistrationCallback callback);

    /* GET REQUESTS */
}