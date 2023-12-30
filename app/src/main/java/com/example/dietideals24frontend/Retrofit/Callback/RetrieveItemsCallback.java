package com.example.dietideals24frontend.Retrofit.Callback;

import java.util.List;
import com.example.dietideals24frontend.Model.RequestedItemDTO;

public interface RetrieveItemsCallback {
    boolean onSearchItemsUpForAuctionSuccess(List<RequestedItemDTO> items);
    boolean onSearchItemsUpForAuctionFailure(String errorMessage);
}