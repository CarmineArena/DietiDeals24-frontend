package com.example.dietideals24frontend.Retrofit.Service;

import java.util.List;
import com.example.dietideals24frontend.Model.*;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;
import com.example.dietideals24frontend.Retrofit.Callback.*;

public interface CommunicationInterface {
    /* POST REQUESTS */
    void sendUserRegistrationRequest(User user, final UserRegistrationCallback callback);
    void sendItemImageContent(byte[] itemImageContent, final ImageContentRegistrationCallback callback);
    void sendRegisterItemRequest(ItemDTO requestedItem, final ItemRegistrationCallback callback);
    void sendRegisterAuctionRequest(AuctionDTO auctionDTO, final AuctionRegistrationCallback callback);

    /* GET REQUESTS */
    void sendUserLoginRequest(User user, final UserLoginCallback callback);
    void sendFindItemImageRequest(Integer itemId, String name, final ImageContentRequestCallback callback);
    void sendFeaturedItemsUpForAuctionRequest(String searchTerm, List<String> selectedCategories, User user, final RetrieveFeaturedItemsCallback callback);
    void sendCreatedByUserItemsRequest(User user, final RetrieveUserItemsCallback callback);
    void sendFindItemsForWhichTheUserPartecipateAuction(Integer userId, String email, String password, final RetrieveItemsWantedByUserService callback);
}