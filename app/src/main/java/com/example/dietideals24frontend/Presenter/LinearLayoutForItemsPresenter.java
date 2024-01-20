package com.example.dietideals24frontend.Presenter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.annotation.SuppressLint;
import android.widget.RelativeLayout;

import java.util.List;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;
import com.example.dietideals24frontend.Retrofit.Service.Requester;
import com.example.dietideals24frontend.utility.HomeConstantValues;
import com.example.dietideals24frontend.Retrofit.Callback.ImageCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveUserItemsCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveFeaturedItemsCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveItemsWantedByUserService;

import com.example.dietideals24frontend.utility.ItemUtils;
import com.example.dietideals24frontend.utility.ImageUtils;

// TODO: PENSARE A COME PORTARE NELL VARIE SCHERMATE
// TODO: LIMITO LA STAMPA DELLE ASTE IN EVIDENZA, STABILIRE UN CRITERIO SECONDO IL QUALE RECUPERARE GLI OGGETTI

public class LinearLayoutForItemsPresenter {
    private final Context context;
    private final Requester requester;

    public LinearLayoutForItemsPresenter(Context context, Requester requester) {
        this.context = context;
        this.requester = requester;
    }

    /* METHODS */

    public void createFeaturedItemsLinearLayout(LinearLayout layout, User loggedInUser) {
        requester.sendFeaturedItemsUpForAuctionRequest(loggedInUser.getUserId(), loggedInUser.getEmail(), new RetrieveFeaturedItemsCallback() {
            @Override
            public boolean onSearchItemsUpForAuctionSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> featuredItems = ItemUtils.createListOfItems(itemsRetrieved);

                if (featuredItems == null) { // It is impossible that something like this occurs, but we handle it anyway
                    Log.d("Home Fragment", "List<Item> featuredItems size: 0");
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
                                // TODO: COSA CAZZO FACCIO ORA, SICURO IMAGE_CONTENT DEVE ESSERE NULL E VA GESTITO
                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onSearchItemsUpForAuctionFailure(String errorMessage) {
                // TODO: COSA CAZZO FACCIO ORA, SICURO IMAGE_CONTENT DEVE ESSERE NULL E VA GESTITO
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
                    // TODO: IMPOSSIBILE ESCA NULL VERO??
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
                                // TODO: COSA CAZZO FACCIO ORA, SICURO IMAGE_CONTENT DEVE ESSERE NULL E VA GESTITO
                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onSearchItemsUpForAuctionFailure(String errorMessage) {
                // TODO: COSA CAZZO FACCIO ORA, SICURO IMAGE_CONTENT DEVE ESSERE NULL E VA GESTITO
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
                    addImageButton(layout, context, loggedInUser, HomeConstantValues.AUCTIONED);
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
                                // TODO: COSA CAZZO FACCIO ORA, SICURO IMAGE_CONTENT DEVE ESSERE NULL E VA GESTITO
                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onSearchCreatedByUserItemsFailure(String errorMessage) {
                // TODO: COSA CAZZO FACCIO ORA, SICURO IMAGE_CONTENT DEVE ESSERE NULL E VA GESTITO
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
                    addImageButton(layout, context, loggedInUser, HomeConstantValues.WANTED);
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
                                // TODO: COSA CAZZO FACCIO ORA, SICURO IMAGE_CONTENT DEVE ESSERE NULL E VA GESTITO
                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onItemsNotFoundFailure(String errorMessage) {
                // TODO: COSA CAZZO FACCIO ORA, SICURO IMAGE_CONTENT DEVE ESSERE NULL E VA GESTITO
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
        button.setText(item.getDescription()); // TODO: COSA DEVO SCRIVERE?
        button.setBackgroundResource(android.R.color.transparent); // background_light
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        );
        btnParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
        btnParams.addRule(RelativeLayout.CENTER_VERTICAL);

        // TODO: AGGIUNGERE LISTENER AL BOTTONE
        layout.addView(button, btnParams);
    }

    private void addImageButton(LinearLayout layout, Context context, User loggedInUser, final String auctionType) {
        ImageButton createAuctionButton = new ImageButton(context);

        if (auctionType.equals(HomeConstantValues.AUCTIONED))
            createAuctionButton.setBackgroundResource(R.drawable.add_auction);
        else
            createAuctionButton.setBackgroundResource(R.drawable.search_auction);

        createAuctionButton.setLayoutParams(new LinearLayout.LayoutParams(
                400, // Width in pixel
                400,       // Height in pixel
                0f         // Weight
        ));
        createAuctionButton.setOnClickListener(v -> {
            Intent intent = handleClickOnImageButton(auctionType, loggedInUser);
            context.startActivity(intent);
        });
        layout.addView(createAuctionButton);
    }

    private void handleClickOnItem(Item item, String auctionType) {
        // TODO: RECUPERARE I DETTAGLI DELL'ASTA: AUCTION, ITEM (UTENTE DALL'ITEM)
        switch (auctionType) {
            case HomeConstantValues.FEATURED:
                // TODO: PORTARE A SCHERMATA DI PARTECIPAZIONE ALL' ASTA (RELATIVAMENTE AL TIPO)
                break;
            case HomeConstantValues.AUCTIONED:
                // TODO: PORTARE A VISUALIZZAZIONE DELLO STATO DELL'ASTA
                break;
            case HomeConstantValues.WANTED:
                // TODO: PORTARE A SCHERMATA DI PARTECIPAZIONE ALL' ASTA
                break;
            default:
                // TODO:
        }
    }

    private Intent handleClickOnImageButton(final String auctionType, User loggedInUser) {
        ActivityPresenter activityFactory = new ActivityPresenter();
        if (auctionType.equals(HomeConstantValues.WANTED)) {
            return activityFactory.createIntentForSearchAuction(this.context, loggedInUser);
        }
        return activityFactory.createIntentForCreateAuction(this.context, loggedInUser);
    }
}