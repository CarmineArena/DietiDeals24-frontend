package com.example.dietideals24frontend.Presenter;

import android.widget.*;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.annotation.SuppressLint;

import java.util.List;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.dietideals24frontend.Controller.ItemController.ItemController;
import com.example.dietideals24frontend.Controller.ItemController.Callback.RetrieveFeaturedItemsCallback;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;
import com.example.dietideals24frontend.View.Dialog.Dialog;
import com.example.dietideals24frontend.Utility.HomeConstantValues;
import com.example.dietideals24frontend.Controller.ItemController.Callback.ImageCallback;
import com.example.dietideals24frontend.Controller.ItemController.Callback.RetrieveUserItemsCallback;
import com.example.dietideals24frontend.Controller.ItemController.Callback.RetrieveItemsWantedByUserCallback;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.Utility.ItemUtils;
import com.example.dietideals24frontend.Utility.ImageUtils;

public class LinearLayoutForItemsPresenter {
    private static final String TAG = "LinearLayoutForItemsPresenter";
    private final Context context;
    private final Retrofit retrofitService;
    private final FragmentManager manager;

    public LinearLayoutForItemsPresenter(Context context, Retrofit retrofitService, FragmentManager manager) {
        this.context         = context;
        this.retrofitService = retrofitService;
        this.manager         = manager;
    }

    /* METHODS */

    public void createFeaturedItemsLinearLayout(LinearLayout layout, User loggedInUser) {
        ItemController controller = new ItemController(this.retrofitService);
        controller.sendFeaturedItemsUpForAuctionRequest(loggedInUser.getUserId(), loggedInUser.getEmail(), new RetrieveFeaturedItemsCallback() {
            @Override
            public boolean onSearchItemsUpForAuctionSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> featuredItems = ItemUtils.createListOfItems(itemsRetrieved);

                if (featuredItems == null) { // It is impossible that something like this occurs, but we handle it anyway
                    Log.d("Home Fragment", "List<Item> featuredItems size: 0");
                    addImageButtonToLinearLayout(layout, context, loggedInUser, HomeConstantValues.WANTED);
                } else {
                    Log.d("Home Fragment", "List<Item> featuredItems size: " + featuredItems.size());
                    int size = featuredItems.size();
                    int steps;

                    if (size < 10) {
                        steps = size;
                    } else {
                        steps = 9; // [0 - 9] 10 elements in total
                    }
                    for (int j = 0; j < steps; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(featuredItems.get(pos), controller, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                featuredItems.get(pos).setImage(imageContent);
                                layout.addView(createInternalLayout(featuredItems.get(pos), loggedInUser, HomeConstantValues.FEATURED));
                            }

                            @Override
                            public void onImageNotAvailable(String errorMessage) {
                                /* [MOTIVATION]
                                    - If the request is done correctly there is no way that the image is not available.
                                    - That is because of how we crate auction: the item image must exits otherwise you can't create the auction
                                **/
                                Log.e("ERROR", "createFeaturedItemsLinearLayout, Item's image is not available: " + errorMessage);
                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onSearchItemsUpForAuctionFailure(String errorMessage) {
                /* [MOTIVATION: BAD REQUEST] */
                Log.d("onSearchItemsUpForAuctionFailure", errorMessage);
                showPopUpError("Could not retrieve the items requested. " + errorMessage);
                return false;
            }
        });
    }

    public void createInternalLayoutWithFeaturedAuctions(RelativeLayout layout, User loggedInUser,
                                                         String searchTerm, List<String> categories) {
        ItemController controller = new ItemController(this.retrofitService);
        controller.sendFeaturedItemsUpForAuctionBySearchTermAndCategoryRequest(searchTerm, categories, loggedInUser, new RetrieveFeaturedItemsCallback() {
            @Override
            public boolean onSearchItemsUpForAuctionSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> items = ItemUtils.createListOfItems(itemsRetrieved);

                if (items == null) { // It is impossible that something like this occurs, but we handle it anyway
                    Log.d("Home Fragment", "List<Item> Search Auction Activity size: 0");
                    addImageButtonToRelativeLayout(layout, context, loggedInUser);
                } else {
                    Log.d("Home Fragment", "List<Item> Search Auction Activity size: " + items.size());
                    int size = items.size();
                    for (int j = 0; j < size; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(items.get(pos), controller, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                items.get(pos).setImage(imageContent);
                                createRelativeLayout(layout, loggedInUser, items.get(pos));
                            }

                            @Override
                            public void onImageNotAvailable(String errorMessage) {
                               /* [MOTIVATION]
                                    - If the request is done correctly there is no way that the image is not available.
                                    - That is because of how we crate auction: the item image must exits otherwise you can't create the auction
                                **/
                                Log.e("ERROR", "createInternalLayoutWithFeaturedAuctions, Item's image is not available: " + errorMessage);
                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onSearchItemsUpForAuctionFailure(String errorMessage) {
                /* [MOTIVATION: BAD REQUEST] */
                Log.d("onSearchItemsUpForAuctionFailure", errorMessage);
                showPopUpError("Could not retrieve the items requested. " + errorMessage);
                return false;
            }
        });
    }

    public void createAuctionedByUserItemsLinearLayout(LinearLayout layout, User loggedInUser) {
        ItemController controller = new ItemController(this.retrofitService);
        controller.sendCreatedByUserItemsRequest(loggedInUser, new RetrieveUserItemsCallback() {
            @Override
            public boolean onSearchCreatedByUserItemsSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> auctionedByUserItems = ItemUtils.createListOfItems(itemsRetrieved);

                if (auctionedByUserItems == null) {
                    Log.d("Home Fragment", "List<Item> createdByUserItems size: 0");
                    addImageButtonToLinearLayout(layout, context, loggedInUser, HomeConstantValues.AUCTIONED);
                } else {
                    Log.d("Home Fragment", "List<Item> createdByUserItems size: " + auctionedByUserItems.size());

                    final int size = auctionedByUserItems.size();
                    for (int j = 0; j < size; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(auctionedByUserItems.get(pos), controller, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                auctionedByUserItems.get(pos).setImage(imageContent);
                                layout.addView(createInternalLayout(auctionedByUserItems.get(pos), loggedInUser, HomeConstantValues.AUCTIONED));
                            }

                            @Override
                            public void onImageNotAvailable(String errorMessage) {
                               /* [MOTIVATION]
                                    - If the request is done correctly there is no way that the image is not available.
                                    - That is because of how we crate auction: the item image must exits otherwise you can't create the auction
                                **/
                                Log.e("ERROR", "createAuctionedByUserItemsLinearLayout, Item's image is not available: " + errorMessage);                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onSearchCreatedByUserItemsFailure(String errorMessage) {
                /* [MOTIVATION: BAD REQUEST] */
                Log.d("onSearchCreatedByUserItemsFailure", errorMessage);
                showPopUpError("Could not retrieve the items auctioned by you. " + errorMessage);
                return false;
            }
        });
    }

    public void createItemsWantedByUserLinearLayout(LinearLayout layout, User loggedInUser) {
        ItemController controller = new ItemController(this.retrofitService);
        controller.sendFindItemsWantedByUserRequest(loggedInUser.getUserId(), loggedInUser.getEmail(), loggedInUser.getPassword(), new RetrieveItemsWantedByUserCallback() {
            @Override
            public boolean onItemsFoundWithSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> wantedByUserItems = ItemUtils.createListOfItems(itemsRetrieved);

                if (wantedByUserItems == null) {
                    Log.d("Home Fragment", "List<Item> wantedByUserItems size: 0");
                    addImageButtonToLinearLayout(layout, context, loggedInUser, HomeConstantValues.WANTED);
                } else {
                    Log.d("Home Fragment", "List<Item> wantedByUserItems size: " + wantedByUserItems.size());

                    final int size = wantedByUserItems.size();
                    for (int j = 0; j < size; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(wantedByUserItems.get(pos), controller, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                wantedByUserItems.get(pos).setImage(imageContent);
                                layout.addView(createInternalLayout(wantedByUserItems.get(pos), loggedInUser, HomeConstantValues.WANTED));
                            }

                            @Override
                            public void onImageNotAvailable(String errorMessage) {
                               /* [MOTIVATION]
                                    - If the request is done correctly there is no way that the image is not available.
                                    - That is because of how we crate auction: the item image must exits otherwise you can't create the auction
                                **/
                                Log.e("ERROR", "createAuctionedByUserItemsLinearLayout, Item's image is not available: " + errorMessage);
                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onItemsNotFoundFailure(String errorMessage) {
                /* [MOTIVATION: BAD REQUEST] */
                Log.d("onItemsNotFoundFailure", errorMessage);
                showPopUpError("Could not retrieve the items you wanted to search. " + errorMessage);
                return false;
            }
        });
    }

    /* PRIVATE METHODS */

    @SuppressLint("SetTextI18n")
    private LinearLayout createInternalLayout(Item item, User loggedInUser, final String auctionType) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                400, // Width in pixel
                400,       // Height in pixel
                2f         // Weight
        ));
        ImageUtils.fillImageView(imageView, item.getImage());

        Button button = new Button(context);
        if (auctionType.equals(HomeConstantValues.FEATURED))
            button.setText("ACQUISTA");
        else if (auctionType.equals(HomeConstantValues.FEATURED_SEARCH_AUCTION))
            button.setText(item.getDescription());
        else
            button.setText("VISUALIZZA");

        button.setOnClickListener(v -> handleClickOnItem(item, loggedInUser, auctionType));

        LinearLayout internal = new LinearLayout(context);
        internal.setOrientation(LinearLayout.VERTICAL);
        internal.addView(imageView);
        internal.addView(button);
        return internal;
    }

    private void createRelativeLayout(RelativeLayout layout, User loggedInUser, Item item) {
        ImageView imageView = new ImageView(context);
        imageView.setId(View.generateViewId());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                400, // Width in pixel
                400        // Height in pixel
        ));
        ImageUtils.fillImageView(imageView, item.getImage());
        layout.addView(imageView);

        Button button = new Button(context);
        button.setText(item.getName()); // We show the name of the Item
        button.setBackgroundResource(android.R.color.transparent); // background_light
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        );
        btnParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
        btnParams.addRule(RelativeLayout.CENTER_VERTICAL);

        button.setOnClickListener(v -> handleClickOnItem(item, loggedInUser, HomeConstantValues.WANTED));
        layout.addView(button, btnParams);
    }

    private void addImageButtonToLinearLayout(LinearLayout layout, Context context, User loggedInUser, final String auctionType) {
        ImageButton button = new ImageButton(context);

        if (auctionType.equals(HomeConstantValues.AUCTIONED))
            button.setBackgroundResource(R.drawable.add_auction);
        else
            button.setBackgroundResource(R.drawable.search_auction);

        button.setLayoutParams(new LinearLayout.LayoutParams(
                400, // Width in pixel
                400,       // Height in pixel
                0f         // Weight
        ));
        button.setOnClickListener(v -> {
            if (auctionType.equals(HomeConstantValues.AUCTIONED)) {
                Intent intent = handleClickForCreateAuction(loggedInUser);
                context.startActivity(intent);
            } else {
                createSearchAuctionFragment(loggedInUser);
            }
        });
        layout.addView(button);
    }

    private void addImageButtonToRelativeLayout(RelativeLayout layout, Context context, User loggedInUser) {
        ImageButton button = new ImageButton(context);

        button.setBackgroundResource(R.drawable.search_auction);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                400, // Width in pixel
                400        // Height in pixel
        ));
        // It takes you only to SearchAuctionFragment
        button.setOnClickListener(v -> createSearchAuctionFragment(loggedInUser));
        layout.addView(button);
    }

    private void handleClickOnItem(Item item, User loggedInUser, String auctionType) {
        Intent intent1 = new ActivityPresenter().createAuctionIntent(context, loggedInUser, item);
        this.context.startActivity(intent1);
    }

    private Intent handleClickForCreateAuction(User loggedInUser) {
        ActivityPresenter activityPresenter = new ActivityPresenter();
        return activityPresenter.createIntentForCreateAuction(this.context, loggedInUser);
    }

    private void showPopUpError(String message) {
        Dialog dialog = new Dialog(context);
        dialog.showAlertDialog("ITEM RETRIEVAL ERROR", message);
    }

    private void createSearchAuctionFragment(User loggedInUser) {
        FragmentTransaction fr = this.manager.beginTransaction();

        FragmentPresenter presenter = new FragmentPresenter();
        fr.replace(R.id.frameGeneral, presenter.createSearchAuctionFragment(loggedInUser));
        fr.commit();
    }
}