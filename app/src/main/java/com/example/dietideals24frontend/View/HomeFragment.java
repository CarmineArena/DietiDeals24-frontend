package com.example.dietideals24frontend.View;

import android.os.Bundle;
import android.content.Context;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;

import java.util.List;
import android.view.*;
import android.widget.*;
import android.util.Log;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.utility.ItemUtils;
import com.example.dietideals24frontend.utility.ImageUtils;
import com.example.dietideals24frontend.utility.HomeConstantValues;
import com.example.dietideals24frontend.Retrofit.Service.Requester;
import com.example.dietideals24frontend.Retrofit.Callback.ImageCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveUserItemsCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveFeaturedItemsCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveItemsWantedByUserService;

// TODO: LIMITO LA STAMPA DELLE ASTE IN EVIDENZA, STABILIRE UN CRITERIO SECONDO IL QUALE RECUPERARE GLI OGGETTI

public class HomeFragment extends Fragment {
    private View view;
    private User loggedInUser;
    private Retrofit retrofitService;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            loggedInUser = (User) bundle.getSerializable("loggedInUser");
        } else {
            loggedInUser = null;
        }

        retrofitService = MainActivity.retrofitService;
        Requester requester = new Requester(retrofitService);

        Context context = getContext();

        LinearLayout verticalLayout = new LinearLayout(context);
        verticalLayout.setOrientation(LinearLayout.VERTICAL);

        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        verticalLayout.removeAllViews();

        for (int i = 1; i <= 3; i++) {
            TextView textView = new TextView(context);
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            if (i == 1) {
                textView.setText("Aste in evidenza");
                createFeaturedItemsLinearLayout(requester, layout, context);
            } else if (i == 2) {
                textView.setText("Le tue aste");
                createAuctionedByUserItemsLinearLayout(requester, layout, context);
            } else {
                textView.setText("Aste a cui partecipi");
                createItemsWantedByUserLinearLayout(requester, layout, context);
            }

            textView.setGravity(Gravity.START);
            horizontalScrollView.addView(layout);
            verticalLayout.addView(textView);
            verticalLayout.addView(horizontalScrollView);
        }

        scrollView.addView(verticalLayout);
        return scrollView;
    }

    private void fillImageView(ImageView imageView, byte[] imageContent) {
        imageView.setImageBitmap(ImageUtils.getImageBitMap(imageContent));
    }

    private void createFeaturedItemsLinearLayout(Requester requester, LinearLayout layout, Context context) {
        requester.sendFeaturedItemsUpForAuctionRequest(loggedInUser.getUserId(), loggedInUser.getEmail(), new RetrieveFeaturedItemsCallback() {
            @Override
            public boolean onSearchItemsUpForAuctionSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> featuredItems = ItemUtils.createListOfItems(itemsRetrieved);

                if (featuredItems == null) { // Impossible that something like this occurs, but we handle it anyway
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
                                layout.addView(createInternalLayout(featuredItems.get(pos), pos, HomeConstantValues.FEATURED));
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

    private void createAuctionedByUserItemsLinearLayout(Requester requester, LinearLayout layout, Context context) {
        requester.sendCreatedByUserItemsRequest(loggedInUser, new RetrieveUserItemsCallback() {
            @Override
            public boolean onSearchCreatedByUserItemsSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> auctionedByUserItems = ItemUtils.createListOfItems(itemsRetrieved);

                if (auctionedByUserItems == null) {
                    Log.d("Home Fragment", "List<Item> createdByUserItems size: 0");
                    addImageButton(layout, context, HomeConstantValues.AUCTIONED);
                } else {
                    Log.d("Home Fragment", "List<Item> createdByUserItems size: " + auctionedByUserItems.size());

                    final int size = auctionedByUserItems.size();
                    for (int j = 0; j < size; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(auctionedByUserItems.get(pos), requester, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                auctionedByUserItems.get(pos).setImage(imageContent);
                                layout.addView(createInternalLayout(auctionedByUserItems.get(pos), pos, HomeConstantValues.AUCTIONED));

                                if (pos == size - 1) addImageButton(layout, context, HomeConstantValues.AUCTIONED);
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

    private void createItemsWantedByUserLinearLayout(Requester requester, LinearLayout layout, Context context) {
        requester.sendFindItemsWantedByUserRequest(loggedInUser.getUserId(), loggedInUser.getEmail(), loggedInUser.getPassword(), new RetrieveItemsWantedByUserService() {
            @Override
            public boolean onItemsFoundWithSuccess(List<ItemDTO> itemsRetrieved) {
                List<Item> wantedByUserItems = ItemUtils.createListOfItems(itemsRetrieved);

                if (wantedByUserItems == null) {
                    Log.d("Home Fragment", "List<Item> wantedByUserItems size: 0");
                    addImageButton(layout, context, HomeConstantValues.WANTED);
                } else {
                    Log.d("Home Fragment", "List<Item> wantedByUserItems size: " + wantedByUserItems.size());

                    final int size = wantedByUserItems.size();
                    for (int j = 0; j < size; j++) {
                        final int pos = j;
                        ItemUtils.assignImageToItem(wantedByUserItems.get(pos), requester, new ImageCallback() {
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                wantedByUserItems.get(pos).setImage(imageContent);
                                layout.addView(createInternalLayout(wantedByUserItems.get(pos), pos, HomeConstantValues.WANTED));

                                if (pos == size - 1) addImageButton(layout, context, HomeConstantValues.WANTED);
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

    @SuppressLint("SetTextI18n")
    private LinearLayout createInternalLayout(Item item, final int position, final String auctionType) {
        Context context = getContext();

        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                400, // Width in pixel
                400,       // Height in pixel
                2f         // Weight
        ));
        fillImageView(imageView, item.getImage());

        Button button = new Button(context);

        if (auctionType.equals(HomeConstantValues.FEATURED))
            button.setText("ACQUISTA");
        else
            button.setText("VISUALIZZA");

        LinearLayout internal = new LinearLayout(context);
        internal.setOrientation(LinearLayout.VERTICAL);
        internal.addView(imageView);
        internal.addView(button);

        internal.setOnClickListener(v -> handleClickOnItem(item, auctionType));
        return internal;
    }

    private void addImageButton(LinearLayout layout, Context context, final String auctionType) {
        ImageButton createAuctionButton = new ImageButton(context);
        createAuctionButton.setBackgroundResource(R.drawable.add_auction);
        createAuctionButton.setLayoutParams(new LinearLayout.LayoutParams(
                400, // Width in pixel
                400        // Height in pixel
        ));
        createAuctionButton.setOnClickListener(v -> handleClickOnImageButton(auctionType));
        layout.addView(createAuctionButton);
    }

    private void handleClickOnItem(Item item, String auctionType) {
        // TODO: RECUPERARE I DETTAGLI DELL'ASTA: AUCTION, USER, ITEM
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

    private void handleClickOnImageButton(final String auctionType) {
        switch (auctionType) {
            case HomeConstantValues.AUCTIONED:
                // TODO: PORTARE A SCHERMATA DI CREAZIONE DELL'ASTA (CREATE AUCTION)
                break;
            case HomeConstantValues.WANTED:
                // TODO: PORTARE A SCHERMATA DI RICERCA (SEARCH AUCTION)
                break;
            default:
                // TODO:
        }
    }
}