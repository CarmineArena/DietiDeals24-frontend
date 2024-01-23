package com.example.dietideals24frontend.Presenter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;
import com.example.dietideals24frontend.View.Dialog.Dialog;
import com.example.dietideals24frontend.Retrofit.Service.Requester;
import com.example.dietideals24frontend.utility.HomeConstantValues;
import com.example.dietideals24frontend.Retrofit.Callback.ImageCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveUserItemsCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveFeaturedItemsCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveItemsWantedByUserService;

import com.example.dietideals24frontend.utility.ItemUtils;
import com.example.dietideals24frontend.utility.ImageUtils;

// TODO: SICCOME LIMITO LA STAMPA DELLE ASTE IN EVIDENZA A 10, STABILIRE UN CRITERIO SECONDO IL QUALE RECUPERARE GLI OGGETTI

public class LinearLayoutForItemsPresenter {
    private static final String TAG = "LinearLayoutForItemsPresenter";
    private final Context context;
    private final Requester requester;
    private final FragmentManager manager;

    public LinearLayoutForItemsPresenter(Context context, Requester requester, FragmentManager manager) {
        this.context = context;
        this.requester = requester;
        this.manager = manager;
    }

    /* METHODS */

    public void createFeaturedItemsLinearLayout(LinearLayout layout, User loggedInUser) {
        requester.sendFeaturedItemsUpForAuctionRequest(loggedInUser.getUserId(), loggedInUser.getEmail(), new RetrieveFeaturedItemsCallback() {
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
                        ItemUtils.assignImageToItem(featuredItems.get(pos), requester, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                featuredItems.get(pos).setImage(imageContent);
                                layout.addView(createInternalLayout(featuredItems.get(pos), HomeConstantValues.FEATURED));
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
        requester.sendFeaturedItemsUpForAuctionBySearchTermAndCategoryRequest(searchTerm, categories, loggedInUser, new RetrieveFeaturedItemsCallback() {
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
                        ItemUtils.assignImageToItem(items.get(pos), requester, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                items.get(pos).setImage(imageContent);
                                createRelativeLayout(layout, items.get(pos));
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
        requester.sendCreatedByUserItemsRequest(loggedInUser, new RetrieveUserItemsCallback() {
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
                        ItemUtils.assignImageToItem(auctionedByUserItems.get(pos), requester, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                auctionedByUserItems.get(pos).setImage(imageContent);
                                layout.addView(createInternalLayout(auctionedByUserItems.get(pos), HomeConstantValues.AUCTIONED));
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
        requester.sendFindItemsWantedByUserRequest(loggedInUser.getUserId(), loggedInUser.getEmail(), loggedInUser.getPassword(), new RetrieveItemsWantedByUserService() {
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
                        ItemUtils.assignImageToItem(wantedByUserItems.get(pos), requester, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                wantedByUserItems.get(pos).setImage(imageContent);
                                layout.addView(createInternalLayout(wantedByUserItems.get(pos), HomeConstantValues.WANTED));
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
    private LinearLayout createInternalLayout(Item item, final String auctionType) {
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

        button.setOnClickListener(v -> handleClickOnItem(item, auctionType));

        LinearLayout internal = new LinearLayout(context);
        internal.setOrientation(LinearLayout.VERTICAL);
        internal.addView(imageView);
        internal.addView(button);

        // internal.setOnClickListener(v -> handleClickOnItem(item, auctionType));
        return internal;
    }

    private void createRelativeLayout(RelativeLayout layout, Item item) {
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

        button.setOnClickListener(v -> handleClickOnItem(item, HomeConstantValues.WANTED));
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
                400,       // Height in pixel
                0f         // Weight
        ));
        // It takes you only to SearchAuctionFragment
        button.setOnClickListener(v -> createSearchAuctionFragment(loggedInUser));
        layout.addView(button);
    }

    private void handleClickOnItem(Item item, String auctionType) {
        // TODO: RECUPERARE I DETTAGLI DELL'ASTA: AUCTION, ITEM (L'UTENTE LO HAI NELL'ITEM)
        switch (auctionType) {
            case HomeConstantValues.FEATURED:
                // TODO: PORTARE L'UTENTE ALLA SCHERMATA DI PARTECIPAZIONE ALL' ASTA (RELATIVAMENTE AL TIPO)
                break;
            case HomeConstantValues.AUCTIONED:
                // TODO: PORTARE L'UTENTE ALLA VISUALIZZAZIONE DELLO STATO DELL'ASTA
                break;
            case HomeConstantValues.WANTED:
                // TODO: PORTARE L'UTENTE ALLA SCHERMATA DI PARTECIPAZIONE ALL' ASTA (RELATIVAMENTE AL TIPO) [QUI L'UTENTE FARA' L'OFFERTA PER LA PRIMA VOLTA]
                break;
            default:
                Log.e(TAG, "handleClickOnItem() --> Unexpected auctionType: " + auctionType);
        }
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