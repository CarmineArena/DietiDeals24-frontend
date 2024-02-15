package com.example.dietideals24frontend.Controller.ItemController.Interface;

import java.util.List;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Controller.ItemController.Callback.*;

public interface ItemRequestInterface {
    void sendFindItemImageRequest(Integer itemId, String name, final ImageContentRequestCallback callback);
    void sendFeaturedItemsUpForAuctionRequest(Integer userId, String email, final RetrieveFeaturedItemsCallback callback);
    void sendFeaturedItemsUpForAuctionBySearchTermAndCategoryRequest(String searchTerm, List<String> categories, User loggedInUser, final RetrieveFeaturedItemsCallback callback);
    void sendCreatedByUserItemsRequest(User loggedInUser, final RetrieveUserItemsCallback callback);
    void sendFindItemsWantedByUserRequest(Integer userId, String email, String password, final RetrieveItemsWantedByUserCallback callback);
    void sendFindItemsWithNoWinnerRequest(Integer userId, final RetrieveItemsWithNoWinnerCallback callback);
}