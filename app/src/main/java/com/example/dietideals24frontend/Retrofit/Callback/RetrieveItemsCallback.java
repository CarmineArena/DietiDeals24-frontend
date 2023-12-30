package com.example.dietideals24frontend.Retrofit.Callback;

import java.util.List;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

public interface RetrieveItemsCallback {
    boolean onSearchItemsUpForAuctionSuccess(List<ItemDTO> items);
    boolean onSearchItemsUpForAuctionFailure(String errorMessage);
}