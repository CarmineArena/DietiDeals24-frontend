package com.example.dietideals24frontend.Presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Looper;
import android.widget.*;
import android.util.Log;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.content.Context;
import android.annotation.SuppressLint;

import java.util.List;
import java.util.logging.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dietideals24frontend.Controller.AuctionController.AuctionController;
import com.example.dietideals24frontend.Controller.AuctionController.Callback.RetrieveWinningBidCallback;
import com.example.dietideals24frontend.Controller.ItemController.Callback.RetrieveItemsWonByUserCallback;
import com.example.dietideals24frontend.Controller.ItemController.ItemController;
import com.example.dietideals24frontend.Controller.ItemController.Callback.RetrieveFeaturedItemsCallback;
import com.example.dietideals24frontend.Controller.ItemController.Callback.RetrieveItemsWithNoWinnerCallback;

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
    private Activity dialogActivity;

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
                    addImageButtonToRelativeLayout(layout, context, loggedInUser, HomeConstantValues.WANTED);
                } else {
                    Log.d("Home Fragment", "List<Item> Search Auction Activity size: " + items.size());
                    int size = items.size();
                    for (int j = 0; j < size; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(items.get(pos), controller, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                items.get(pos).setImage(imageContent);
                                createRelativeLayout(layout, loggedInUser, items.get(pos), HomeConstantValues.WANTED);
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

    public void createAuctionedByUserItemsLayout(ViewGroup layout, User loggedInUser) {
        ItemController controller = new ItemController(this.retrofitService);
        controller.sendCreatedByUserItemsRequest(loggedInUser, new RetrieveUserItemsCallback() {
            @Override
            public boolean onSearchCreatedByUserItemsSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> auctionedByUserItems = ItemUtils.createListOfItems(itemsRetrieved);

                if (auctionedByUserItems == null) {
                    Log.d("Home Fragment", "List<Item> createdByUserItems size: 0");

                    if (layout instanceof LinearLayout)
                        addImageButtonToLinearLayout((LinearLayout) layout, context, loggedInUser, HomeConstantValues.AUCTIONED);
                    else if (layout instanceof RelativeLayout)
                        addImageButtonToRelativeLayout((RelativeLayout) layout, context, loggedInUser, HomeConstantValues.AUCTIONED);
                } else {
                    Log.d("Home Fragment", "List<Item> createdByUserItems size: " + auctionedByUserItems.size());

                    final int size = auctionedByUserItems.size();
                    for (int j = 0; j < size; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(auctionedByUserItems.get(pos), controller, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                auctionedByUserItems.get(pos).setImage(imageContent);

                                if (layout instanceof LinearLayout)
                                    layout.addView(createInternalLayout(auctionedByUserItems.get(pos), loggedInUser, HomeConstantValues.AUCTIONED));
                                else if (layout instanceof RelativeLayout)
                                    createRelativeLayout((RelativeLayout) layout, loggedInUser, auctionedByUserItems.get(pos), HomeConstantValues.AUCTIONED);
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

    public void createItemsWantedByUserLayout(ViewGroup layout, User loggedInUser) {
        ItemController controller = new ItemController(this.retrofitService);
        controller.sendFindItemsWantedByUserRequest(loggedInUser.getUserId(), loggedInUser.getEmail(), loggedInUser.getPassword(), new RetrieveItemsWantedByUserCallback() {
            @Override
            public boolean onItemsFoundWithSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> wantedByUserItems = ItemUtils.createListOfItems(itemsRetrieved);

                if (wantedByUserItems == null) {
                    Log.d("Home Fragment", "List<Item> wantedByUserItems size: 0");

                    if (layout instanceof LinearLayout)
                        addImageButtonToLinearLayout((LinearLayout) layout, context, loggedInUser, HomeConstantValues.WANTED);
                    else if (layout instanceof RelativeLayout)
                        addImageButtonToRelativeLayout((RelativeLayout) layout, context, loggedInUser, HomeConstantValues.WANTED);
                } else {
                    Log.d("Home Fragment", "List<Item> wantedByUserItems size: " + wantedByUserItems.size());

                    final int size = wantedByUserItems.size();
                    for (int j = 0; j < size; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(wantedByUserItems.get(pos), controller, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                wantedByUserItems.get(pos).setImage(imageContent);

                                if (layout instanceof LinearLayout)
                                    layout.addView(createInternalLayout(wantedByUserItems.get(pos), loggedInUser, HomeConstantValues.WANTED));
                                else if (layout instanceof RelativeLayout) {
                                    createRelativeLayout((RelativeLayout) layout, loggedInUser, wantedByUserItems.get(pos), HomeConstantValues.WANTED);
                                }
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

    public void createItemsWithNoWinnerLayout(ViewGroup layout, User loggedInUser) {
        ItemController controller = new ItemController(this.retrofitService);
        controller.sendFindItemsWithNoWinnerRequest(loggedInUser.getUserId(), new RetrieveItemsWithNoWinnerCallback() {
            @Override
            public boolean onItemsWithNoWinnerRetrievalSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> itemsWithNoWinner = ItemUtils.createListOfItems(itemsRetrieved);
                if (itemsWithNoWinner == null) {
                    Log.d("Home Fragment", "List<Item> itemsWithNoWinner size: 0");

                    if (layout instanceof LinearLayout)
                        addImageButtonToLinearLayout((LinearLayout) layout, context, loggedInUser, HomeConstantValues.AUCTIONED);
                    else if (layout instanceof RelativeLayout)
                        addImageButtonToRelativeLayout((RelativeLayout) layout, context, loggedInUser, HomeConstantValues.AUCTIONED);
                } else {
                    Log.d("Home Fragment", "List<Item> itemsWithNoWinner size: " + itemsWithNoWinner.size());

                    final int size = itemsWithNoWinner.size();
                    for (int j = 0; j < size; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(itemsWithNoWinner.get(pos), controller, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                itemsWithNoWinner.get(pos).setImage(imageContent);

                                if (layout instanceof LinearLayout)
                                    layout.addView(createInternalLayout(itemsWithNoWinner.get(pos), loggedInUser, HomeConstantValues.ITEM_WITH_NO_WINNER));
                                else if (layout instanceof RelativeLayout) {
                                    createRelativeLayout((RelativeLayout) layout, loggedInUser, itemsWithNoWinner.get(pos), HomeConstantValues.ITEM_WITH_NO_WINNER);
                                }
                            }

                            @Override
                            public void onImageNotAvailable(String errorMessage) {
                                Log.e("ERROR", "createItemsWithNoWinnerLayout, List<Items> with no winners is not available: " + errorMessage);
                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onItemsWithNoWinnerRetrievalFailure(String errorMessage) {
                /* [MOTIVATION: BAD REQUEST] */
                Log.d("onItemsWithNoWinnerRetrievalFailure", errorMessage);
                showPopUpError("Could not retrieve the list of items auctioned by you (with no winner). " + errorMessage);
                return false;
            }
        });
    }

    public void createItemsWonByUserLayout(ViewGroup layout, User loggedInUser, Activity dialogActivity) {
        this.dialogActivity = dialogActivity;

        ItemController controller = new ItemController(retrofitService);
        controller.sendFindItemsWonByUserRequest(loggedInUser.getUserId(), new RetrieveItemsWonByUserCallback() {
            @Override
            public boolean onItemsWonByUserRetrievalSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> itemsWon = ItemUtils.createListOfItems(itemsRetrieved);
                if (itemsWon == null) {
                    Log.d("Home Fragment", "List<Item> itemsWon size: 0");

                    if (layout instanceof LinearLayout)
                        addImageButtonToLinearLayout((LinearLayout) layout, context, loggedInUser, HomeConstantValues.WANTED);
                    else if (layout instanceof RelativeLayout)
                        addImageButtonToRelativeLayout((RelativeLayout) layout, context, loggedInUser, HomeConstantValues.WANTED);
                } else {
                    Log.d("Home Fragment", "List<Item> itemsWon size: " + itemsWon.size());

                    final int size = itemsWon.size();
                    for (int j = 0; j < size; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(itemsWon.get(pos), controller, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                itemsWon.get(pos).setImage(imageContent);

                                if (layout instanceof LinearLayout)
                                    layout.addView(createInternalLayout(itemsWon.get(pos), loggedInUser, HomeConstantValues.ITEM_WON_BY_USER));
                                else if (layout instanceof RelativeLayout) {
                                    createRelativeLayout((RelativeLayout) layout, loggedInUser, itemsWon.get(pos), HomeConstantValues.ITEM_WON_BY_USER);
                                }
                            }

                            @Override
                            public void onImageNotAvailable(String errorMessage) {
                                Log.e("ERROR", "createItemsWithNoWinnerLayout, List<Items> with no winners is not available: " + errorMessage);
                            }
                        });
                    }
                }
                return true;
            }

            @Override
            public boolean onItemsWonByUserRetrievalFailure(String errorMessage) {
                /* [MOTIVATION: BAD REQUEST] */
                Log.d("onItemsWonByUserRetrievalFailure", errorMessage);
                showPopUpError("Could not retrieve the list of items won by the User. " + errorMessage);
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
        button.setBackgroundResource(R.drawable.button2bg);
        switch (auctionType) {
            case HomeConstantValues.FEATURED:
                button.setText("ACQUISTA");
                break;
            case HomeConstantValues.FEATURED_SEARCH_AUCTION:
                button.setText(item.getDescription());
                break;
            case HomeConstantValues.ITEM_WITH_NO_WINNER:
                button.setText("ACCETTA");
                break;
            default:
                button.setText("VISUALIZZA");
                break;
        }

        if (auctionType.equals(HomeConstantValues.ITEM_WON_BY_USER)) {
            button.setOnClickListener(v -> showInformationDialog(item.getItemId()));
        } else {
            button.setOnClickListener(v -> handleClickOnItem(item, loggedInUser, auctionType));
        }

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Imposta i margini per il Button
        buttonLayoutParams.setMargins(0, 20, 0, 0); // Margine superiore di 20 pixel

        // Applica i parametri di layout al Button
        button.setLayoutParams(buttonLayoutParams);

        LinearLayout internal = new LinearLayout(context);
        internal.setOrientation(LinearLayout.VERTICAL);
        internal.addView(imageView);
        internal.addView(button);
        return internal;
    }

    private void createRelativeLayout(RelativeLayout layout, User loggedInUser, Item item, String auctionType) {
        ImageView imageView = new ImageView(context);
        imageView.setId(View.generateViewId());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                400, // Width in pixel
                400        // Height in pixel
        ));
        ImageUtils.fillImageView(imageView, item.getImage());
        layout.addView(imageView);

        Button button = new Button(context);
        button.setText(Html.fromHtml(item.getName() + "<br>" + item.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        button.setBackgroundResource(android.R.color.transparent); // background_light
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        );
        btnParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
        btnParams.addRule(RelativeLayout.CENTER_VERTICAL);

        button.setOnClickListener(v -> handleClickOnItem(item, loggedInUser, auctionType));
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

    private void addImageButtonToRelativeLayout(RelativeLayout layout, Context context, User loggedInUser, String auctionType) {
        ImageButton button = new ImageButton(context);

        if (auctionType.equals(HomeConstantValues.WANTED))
            button.setBackgroundResource(R.drawable.search_auction);
        else
            button.setBackgroundResource(R.drawable.add_auction);

        button.setLayoutParams(new LinearLayout.LayoutParams(
                400, // Width in pixel
                400        // Height in pixel
        ));
        // It takes you only to SearchAuctionFragment
        button.setOnClickListener(v -> createSearchAuctionFragment(loggedInUser));
        layout.addView(button);
    }

    private void handleClickOnItem(Item item, User loggedInUser, String flag) {
        Intent intent;
        if (flag.equals(HomeConstantValues.ITEM_WITH_NO_WINNER)) {
            intent = new ActivityPresenter().createAuctionIntent(context, loggedInUser, item, true);
        } else {
            intent = new ActivityPresenter().createAuctionIntent(context, loggedInUser, item, false);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
    }

    private Intent handleClickForCreateAuction(User loggedInUser) {
        ActivityPresenter activityPresenter = new ActivityPresenter();
        return activityPresenter.createIntentForCreateAuction(this.context, loggedInUser);
    }

    private void showPopUpError(String message) {
        Dialog dialog = new Dialog(context);
        dialog.showAlertDialog("ITEM RETRIEVAL ERROR", message);
    }

    private void showInformationDialog(Integer itemId) {
        AuctionController controller = new AuctionController(retrofitService);
        controller.sendGetWinningBidRequest(itemId, new RetrieveWinningBidCallback() {
            @Override
            public boolean onWinningBidRetrievalSuccess(Float winningBid) {
                if (dialogActivity instanceof Activity) {
                    ((Activity) dialogActivity).runOnUiThread(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(dialogActivity);
                        builder.setTitle("AUCTION INFORMATION");
                        builder.setMessage("Congratulazioni! Ti sei aggiudicato l'Item per " + winningBid + "€ !");
                        builder.setIcon(android.R.drawable.ic_dialog_info);
                        builder.setPositiveButton("Ok", (dialog, which) -> {});
                        builder.show();
                    });
                }
                return true;
            }

            @Override
            public boolean onWinningBidRetrievalFailure(String errorMessage) {
                /* BAD REQUEST [SHOULD NOT HAPPEN] */
                Log.e("Dialog for Winning Bid", "An Error occurred: " + errorMessage);
                return false;
            }
        });
    }

    private void createSearchAuctionFragment(User loggedInUser) {
        FragmentTransaction fr = this.manager.beginTransaction();
        FragmentPresenter presenter = new FragmentPresenter();
        fr.replace(R.id.frameGeneral, presenter.createSearchAuctionFragment(loggedInUser));
        fr.commit();
    }
}