package com.example.dietideals24frontend.Retrofit.Callback;

import java.util.List;
import com.example.dietideals24frontend.Model.ItemDTO;

public interface SearchItemsCallback {
    boolean onSearchItemsUpForAuctionSuccess(List<ItemDTO> items);
    boolean onSearchItemsUpForAuctionFailure(String errorMessage);
}