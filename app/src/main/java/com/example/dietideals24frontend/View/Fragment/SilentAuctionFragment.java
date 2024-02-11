package com.example.dietideals24frontend.View.Fragment;

import android.os.Bundle;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.annotation.SuppressLint;

import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.LinearLayout;

import android.util.Log;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import java.util.List;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.example.dietideals24frontend.Model.Type;
import com.example.dietideals24frontend.View.ToastManager;
import com.example.dietideals24frontend.Presenter.ActivityPresenter;
import com.example.dietideals24frontend.Controller.OfferController.Callback.*;
import com.example.dietideals24frontend.Controller.OfferController.OfferController;
import com.example.dietideals24frontend.Controller.AuctionController.AuctionController;
import com.example.dietideals24frontend.Controller.AuctionController.Callback.CloseAuctionCallback;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.Auction;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;
import com.example.dietideals24frontend.Utility.ImageUtils;
import com.example.dietideals24frontend.Utility.DateAndTimeRetriever;

public class SilentAuctionFragment extends Fragment {
    private Auction auction;
    private User loggedInUser;
    private View view;
    private ToastManager mToastManager;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_silent_auction, container, false);

        mToastManager = new ToastManager(getContext());

        ScrollView scrollView = view.findViewById(R.id.scrollView2);
        LinearLayout scrollViewLayout = view.findViewById(R.id.scrollViewLayout);

        Bundle bundle = getArguments();
        if (bundle != null) {
            auction      = (Auction) bundle.getSerializable("selectedAuction");
            loggedInUser = (User) bundle.getSerializable("loggedInUser");
        } else {
            auction      = null;
            loggedInUser = null;
        }

        TextView nameView = view.findViewById(R.id.ItemNameField);
        nameView.setText(auction.getItem().getName());

        // TODO: AGGIUNGERE
        // TextView categoryView = view.findViewById(R.id.categoryView);
        // categoryView.setText("Categoria: " + auction.getItem().getCategory());

        Button btnUser = view.findViewById(R.id.NameBtn);
        btnUser.setText(auction.getItem().getUser().getName() + " " + auction.getItem().getUser().getSurname());
        btnUser.setOnClickListener(v -> {
            Intent intent = new ActivityPresenter().createProfileIntent(getContext(), auction.getItem().getUser());
            startActivity(intent);
        });

        TextView descriptionView = view.findViewById(R.id.DescriptionView);
        descriptionView.setText(auction.getItem().getDescription());

        ImageView itemImageView = view.findViewById(R.id.itemImage);
        ImageUtils.fillImageView(itemImageView, auction.getItem().getImage());

        TextView dateView = view.findViewById(R.id.textView12);
        dateView.setText("Asta aperta fino al giorno: " + auction.getExpirationDate());

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ITALIAN);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", symbols);

        TextView priceView = view.findViewById(R.id.textView11);
        priceView.setText("Prezzo iniziale: € " + df.format(auction.getCurrentOfferValue()));

        EditText offerText = view.findViewById(R.id.editTextTextPersonName5);

        if (loggedInUser.getUserId().equals(auction.getItem().getUser().getUserId())) {
            // THE USER CLICKED TO SEE THE STATUS OF HIS/HER AUCTIONS
            view.findViewById(R.id.textView13).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.textView13).setEnabled(false);

            view.findViewById(R.id.editTextTextPersonName5).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.editTextTextPersonName5).setEnabled(false);

            Button offerBtn = view.findViewById(R.id.OfferButton);
            offerBtn.setEnabled(false);
            offerBtn.setVisibility(View.INVISIBLE);

            OfferController controller = new OfferController(MainActivity.retrofitService);
            controller.sendGetOffersRequest(auction.getItem().getItemId(), auction.getAuctionId(), new RetrieveOffersCallback() {
                @Override
                public boolean onRetrieveOffersSuccess(List<OfferDTO> offerDTOs) {
                    if (offerDTOs == null || offerDTOs.isEmpty()) {
                        // TODO: SEGNALARE NELLA SCHERMATA
                        Log.i("OFFERS", "NULL");
                    } else {
                        for (OfferDTO offer : offerDTOs) {
                            LinearLayout linearLayoutHorizontal = new LinearLayout(getContext());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            linearLayoutHorizontal.setLayoutParams(layoutParams);
                            linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

                            TextView userName = new TextView(getContext());
                            userName.setText(offer.getUser().getName());
                            linearLayoutHorizontal.addView(userName);

                            TextView userSurname = new TextView(getContext());
                            userSurname.setText(offer.getUser().getSurname());
                            linearLayoutHorizontal.addView(userSurname);

                            TextView userOffer = new TextView(getContext());
                            userOffer.setText(String.valueOf(offer.getOffer()));
                            linearLayoutHorizontal.addView(userOffer);

                            Button button = new Button(getContext());
                            button.setText("Accetta");

                            // TODO: QUESTO VA FATTO SOLO DOPO AVER CLICCATO "OK" IN UN DIALOG
                            button.setOnClickListener(v -> {
                                AuctionController controller = new AuctionController(MainActivity.retrofitService);
                                controller.sendCloseAuctionRequest(auction.getAuctionId(), offer.getUser().getUserId(), new CloseAuctionCallback() {
                                    @Override
                                    public boolean onCloseSuccess() {
                                        mToastManager.showToast("Hai accettato l'offerta!");
                                        // TODO: ASPETTARE QUALCHE SECONDO E PORTARE L'UTENTE ALLA HOME
                                        return true;
                                    }

                                    @Override
                                    public boolean onCloseFailure(String errorMessage) {
                                        // TODO: NOTIFICARE L'ERRORE
                                        return false;
                                    }
                                });
                            });
                            linearLayoutHorizontal.addView(button);

                            scrollViewLayout.addView(linearLayoutHorizontal);
                        }

                        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                    }
                    return true;
                }

                @Override
                public boolean onRetrieveOffersFailure(String errorMessage) {
                    // TODO: SEGNALARE ERRORE
                    return false;
                }
            });
        } else {
            manageOffer();
        }

        return view;
    }

    /* PRIVATE */
    private void manageOffer() {
        OfferController controller = new OfferController(MainActivity.retrofitService);

        Button offerBtn = view.findViewById(R.id.OfferButton);
        float currentOfferValue = auction.getCurrentOfferValue();

        offerBtn.setOnClickListener(v -> {
            EditText offerText = view.findViewById(R.id.editTextTextPersonName5);
            String offerString = offerText.getText().toString();

            if (offerString.isEmpty() || offerString.isBlank()) {
                mToastManager.showToast("Non hai fatto un'offerta!");
            } else {
                OfferDTO offerDTO = new OfferDTO();
                offerDTO.setUser(loggedInUser);
                offerDTO.setAuctionId(auction.getAuctionId());
                offerDTO.setAuctionType(Type.SILENT);
                offerDTO.setOfferDate(DateAndTimeRetriever.getCurrentDate());
                offerDTO.setOfferTime(DateAndTimeRetriever.getCurrentTime());

                float offerta = Float.parseFloat(offerString);
                if (offerta > currentOfferValue) {
                    offerDTO.setOffer(offerta);

                    controller.sendRegisterOfferRequest(offerDTO, new RegisterOfferCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public boolean onOfferRegistrationSuccess() {
                            mToastManager.showToast("Offerta fatta con successo!");
                            return true;
                        }

                        @Override
                        public boolean onOfferRegistrationFailure(String errorMessage) {
                            mToastManager.showToast("Operazione fallita. Riprovare!");
                            return false;
                        }
                    });
                } else {
                    mToastManager.showToast("Inserire un'offerta maggiore a quella iniziale!");
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}