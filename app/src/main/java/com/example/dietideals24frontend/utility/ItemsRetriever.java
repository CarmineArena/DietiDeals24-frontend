package com.example.dietideals24frontend.utility;

import java.util.List;
import java.util.ArrayList;

import android.util.Log;
import retrofit2.Retrofit;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;
import com.example.dietideals24frontend.Retrofit.Service.*;
import com.example.dietideals24frontend.Retrofit.Callback.*;

public class ItemsRetriever {
    private List<Item> featuredItems; // List of Items up for featured Auction
    private List<Item> userItems; // List of Items for wich the user created an Auction
    private List<Item> itemsForWhichUserPartecipateAuction; // List of Items for wich you partecipate at the Auction (the user did not auctioned them)
    private final Retrofit retrofitService;

    public ItemsRetriever(Retrofit retrofitService) {
        this.retrofitService = retrofitService;
    }

    /* [DESCRIPTION]
        - This function retrieves the items up for featured auction.
    **/
    public void setRetrievedFeaturedItems(String searchTerm, List<String> selectedCategories, User loggedInUser) {
        Requester requester = new Requester(this.retrofitService);
        requester.sendFeaturedItemsUpForAuctionRequest(searchTerm, selectedCategories, loggedInUser, new RetrieveFeaturedItemsCallback() {
            @Override
            public boolean onSearchItemsUpForAuctionSuccess(List<ItemDTO> itemsRetrieved) {
                if (itemsRetrieved.isEmpty()) {
                    Log.i("SEARCH SUCCESS BUT FOUND NONE", "LIST SIZE: " + itemsRetrieved.size());
                    setFeaturedItems(null);
                } else {
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

                    // Now retrieve the images: # of requests = # of items inside the list
                    for (int i = 0; i < items.size(); i++) {
                        int itemId = items.get(i).getItemId();
                        String name = items.get(i).getName();

                        int finalI = i;
                        requester.sendFindItemImageRequest(itemId, name, new ImageContentRequestCallback() {
                            @Override
                            public boolean onFetchSuccess(byte[] itemImageContent) {
                                items.get(finalI).setImage(itemImageContent);
                                return true;
                            }

                            @Override
                            public boolean onFetchFailure(String errorMessage) {
                                Log.e("Fetch Item Image Failure", errorMessage);
                                items.get(finalI).setImage(null);
                                return false;
                            }
                        });
                    }
                    Log.i("Fetch Items", "DONE, LIST SIZE: " + items.size());
                    setFeaturedItems(items);
                }
                return true;
            }

            @Override
            public boolean onSearchItemsUpForAuctionFailure(String errorMessage) {
                Log.e("Search Featured List<Items> Failure", errorMessage);
                setFeaturedItems(null);
                return false;
            }
        });
    }

    /* [DESCRIPTION]
        - This function retrieves (only) the items aucitoned by the user (Auction must be active == true).
    **/
    public void setRetrieveItemsAuctionedByUser(User loggedInUser) {
        Requester requester = new Requester(retrofitService);
        requester.sendCreatedByUserItemsRequest(loggedInUser, new RetrieveUserItemsCallback() {
            @Override
            public boolean onSearchCreatedByUserItemsSuccess(List<ItemDTO> itemsRetrieved) {
                if (itemsRetrieved == null || itemsRetrieved.size() == 0) {
                    setUserItems(null);
                } else {
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

                    // Now retrieve the images: # of requests = # of items inside the list
                    for (int i = 0; i < items.size(); i++) {
                        int itemId = items.get(i).getItemId();
                        String name = items.get(i).getName();

                        int finalI = i;
                        requester.sendFindItemImageRequest(itemId, name, new ImageContentRequestCallback() {
                            @Override
                            public boolean onFetchSuccess(byte[] itemImageContent) {
                                items.get(finalI).setImage(itemImageContent);
                                return true;
                            }

                            @Override
                            public boolean onFetchFailure(String errorMessage) {
                                Log.e("Fetch Item Image Failure", errorMessage);
                                items.get(finalI).setImage(null);
                                return false;
                            }
                        });
                    }
                    Log.i("Fetch Items", "DONE, LIST SIZE: " + items.size());
                    setUserItems(items);
                }
                return true;
            }

            @Override
            public boolean onSearchCreatedByUserItemsFailure(String errorMessage) {
                Log.e("Search Auctioned By User List<Items> Failure", errorMessage);
                setUserItems(null);
                return false;
            }
        });
    }

    /* [DESCRIPTION]
        - This function retrieves (only) the items for wich the user is partecipating in an Auction (must be active).
    **/
    public void setItemsForWhichUserPartecipateAuction(Integer userId, String email, String password) {
        Requester requester = new Requester(this.retrofitService);
        requester.sendFindItemsForWhichTheUserPartecipateAuction(userId, email, password, new RetrieveItemsForWhichTheUserPartecipateAuctionCallback() {
            @Override
            public boolean onItemsFoundWithSuccess(List<ItemDTO> itemsRetrieved) {
                if (itemsRetrieved.isEmpty()) {
                    Log.i("SEARCH SUCCESS BUT FOUND NONE", "LIST SIZE: " + itemsRetrieved.size());
                    setItemsUserWants(null);
                } else {
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

                    // Now retrieve the images: # of requests = # of items inside the list
                    for (int i = 0; i < items.size(); i++) {
                        int itemId = items.get(i).getItemId();
                        String name = items.get(i).getName();

                        int finalI = i;
                        requester.sendFindItemImageRequest(itemId, name, new ImageContentRequestCallback() {
                            @Override
                            public boolean onFetchSuccess(byte[] itemImageContent) {
                                items.get(finalI).setImage(itemImageContent);
                                return true;
                            }

                            @Override
                            public boolean onFetchFailure(String errorMessage) {
                                Log.e("Fetch Item Image Failure", errorMessage);
                                items.get(finalI).setImage(null);
                                return false;
                            }
                        });
                    }
                    Log.i("Fetch Items", "DONE, LIST SIZE: " + items.size());
                    setItemsUserWants(items);
                }
                return true;
            }

            @Override
            public boolean onItemsNotFoundFailure(String errorMessage) {
                Log.e("Search List<Items> Failure", errorMessage);
                setItemsUserWants(null);
                return false;
            }
        });
    }

    private void setFeaturedItems(List<Item> featuredItems) {
        this.featuredItems = featuredItems;
    }

    public List<Item> getFeaturedItems() {
        return featuredItems;
    }

    private void setUserItems(List<Item> userItems) {
        this.userItems = userItems;
    }

    public List<Item> getUserItems() {
        return userItems;
    }

    private void setItemsUserWants(List<Item> list) {
        this.itemsForWhichUserPartecipateAuction = list;
    }

    public List<Item> getItemsUserWants() {
        return itemsForWhichUserPartecipateAuction;
    }
}