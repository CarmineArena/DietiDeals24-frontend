package com.example.dietideals24frontend.Utility;

import android.util.Log;

import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

import com.example.dietideals24frontend.Retrofit.Service.Requester;
import com.example.dietideals24frontend.Retrofit.Callback.ImageCallback;
import com.example.dietideals24frontend.Retrofit.Callback.ImageContentRequestCallback;

import java.util.List;
import java.util.ArrayList;

public class ItemUtils {
    public static List<Item> createListOfItems(List<ItemDTO> itemsRetrieved) {
        if (itemsRetrieved.isEmpty()) return null;

        List<Item> items = new ArrayList<>();
        for (ItemDTO itemDTO : itemsRetrieved) {
            Item item = new Item();
            item.setItemId(itemDTO.getItemId());
            item.setName(itemDTO.getName());
            item.setDescription(itemDTO.getDescription());
            item.setCategory(itemDTO.getCategory());
            item.setBasePrize(itemDTO.getBasePrize());
            item.setUser(itemDTO.getUser()); // This is the User who bind to Auction the Item
            items.add(item);
        }

        return items;
    }

    public static void assignImageToItem(Item item, Requester requester, ImageCallback callback) {
        int itemId = item.getItemId();
        String name = item.getName();
        requester.sendFindItemImageRequest(itemId, name, new ImageContentRequestCallback() {
            @Override
            public boolean onFetchSuccess(byte[] itemImageContent) {
                Log.d("Fetch Item Image", "Success");
                callback.onImageAvailable(itemImageContent);
                return true;
            }

            @Override
            public boolean onFetchFailure(String errorMessage) {
                Log.e("Fetch Item Image Failure", errorMessage);
                callback.onImageNotAvailable(errorMessage);
                return false;
            }
        });
    }
}