package com.example.dietideals24frontend.View.Fragment;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import androidx.fragment.app.Fragment;
import androidx.core.content.res.ResourcesCompat;

import android.os.Looper;
import android.view.View;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.annotation.SuppressLint;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.LinearLayout;

import android.util.Log;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Toast;

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
    private boolean hasAuctionEnded;
    private View view;
    private ToastManager mToastManager;
    private Typeface typeface;
    private LinearLayout scrollViewLayout;
    private ScrollView scrollView;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_silent_auction, container, false);

        mToastManager = new ToastManager(getContext());

        scrollView       = view.findViewById(R.id.scrollView2);
        scrollViewLayout = view.findViewById(R.id.scrollViewLayout);

        Bundle bundle = getArguments();
        if (bundle != null) {
            auction         = (Auction) bundle.getSerializable("selectedAuction");
            loggedInUser    = (User)    bundle.getSerializable("loggedInUser");
            hasAuctionEnded = (boolean) bundle.getSerializable("hasAuctionEnded");
        } else {
            auction      = null;
            loggedInUser = null;
        }

        TextView nameView = view.findViewById(R.id.ItemNameField);
        nameView.setText(auction.getItem().getName());

        TextView categoryView = view.findViewById(R.id.categoryView);
        categoryView.setText("Categoria: " + auction.getItem().getCategory());

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

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

        EditText offerText = view.findViewById(R.id.editTextTextPersonName5);

        if (loggedInUser.getUserId().equals(auction.getItem().getUser().getUserId())) {
            // THE USER CLICKED TO SEE THE STATUS OF HIS/HER AUCTIONS (BUT CAN'T ACCEPT OFFERS UNLESS AUCTION HAS ENDED)
            view.findViewById(R.id.textView13).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.textView13).setEnabled(false);

            view.findViewById(R.id.editTextTextPersonName5).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.editTextTextPersonName5).setEnabled(false);

            Button offerBtn = view.findViewById(R.id.OfferButton);
            offerBtn.setEnabled(false);
            offerBtn.setVisibility(View.INVISIBLE);

            typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins_medium);
            manageGetOffersRequest(hasAuctionEnded);
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

    private void manageGetOffersRequest(boolean hasAuctionEnded) {
        OfferController controller = new OfferController(MainActivity.retrofitService);
        Integer itemId = auction.getItem().getItemId();

        if (hasAuctionEnded) {
            controller.sendGetOffersEndedAuctionRequest(itemId, new RetrieveOffersCallback() {
                @SuppressLint("SetTextI18n")
                @Override
                public boolean onRetrieveOffersSuccess(List<OfferDTO> offerDTOs) {
                    if (offerDTOs == null || offerDTOs.isEmpty()) {
                        Log.i("LIST OF OFFERS", "NULL");
                        mToastManager.showToast("Non ci sono offerte per l'Item!");
                    } else {
                        for (OfferDTO offer : offerDTOs) {
                            LinearLayout linearLayoutHorizontal = createLinearLayoutHorizontal(offer);

                            Button button = new Button(getContext());
                            button.setText("Accetta");
                            button.setTypeface(typeface);
                            button.setBackgroundResource(R.drawable.buttonbgaccept);
                            button.setTextSize(17);
                            button.setOnClickListener(v -> showDialogToAcceptOffer(auction.getAuctionId(), offer.getUser().getUserId(), offer.getOffer()));
                            linearLayoutHorizontal.addView(button);

                            scrollViewLayout.addView(linearLayoutHorizontal);
                        }
                        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                    }
                    return true;
                }

                @Override
                public boolean onRetrieveOffersFailure(String errorMessage) {
                    ToastManager mToastManager = new ToastManager(getContext());
                    mToastManager.showToastLong("Errore durante la ricerca delle offerte, riprovare");
                    return false;
                }
            });
        } else {
            controller.sendGetOffersRequest(itemId, auction.getAuctionId(), new RetrieveOffersCallback() {
                @Override
                public boolean onRetrieveOffersSuccess(List<OfferDTO> offerDTOs) {
                    if (offerDTOs == null || offerDTOs.isEmpty()) {
                        Log.i("LIST OF OFFERS", "NULL");
                        mToastManager.showToast("Non ci sono offerte per l'Item!");
                    } else {
                        for (OfferDTO offer : offerDTOs) {
                            LinearLayout linearLayoutHorizontal = createLinearLayoutHorizontal(offer);
                            scrollViewLayout.addView(linearLayoutHorizontal);
                        }
                        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                    }
                    return true;
                }

                @Override
                public boolean onRetrieveOffersFailure(String errorMessage) {
                    ToastManager mToastManager = new ToastManager(getContext());
                    mToastManager.showToastLong("Errore durante la ricerca delle offerte, riprovare");
                    return false;
                }
            });
        }
    }

    private void showDialogToAcceptOffer(Integer auctionId, Integer userId, Float winningBid) {
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("CONFIRM OFFER");
        builder.setMessage("Sei sicuro di voler accettare l'offerta?");
        builder.setIcon(android.R.drawable.ic_dialog_info);

        builder.setPositiveButton("Si", (dialog, which) -> new AuctionController(MainActivity.retrofitService).sendCloseAuctionRequest(auctionId, userId, winningBid, new CloseAuctionCallback() {
            @Override
            public boolean onCloseSuccess() {
                ToastManager mToastManager = new ToastManager(context);
                mToastManager.showToast("Hai accettato l'offerta!");

                // Redirect User to HomePage
                ActivityPresenter activityFactory = new ActivityPresenter();
                Intent intent = activityFactory.createIntentForHome(getActivity(), loggedInUser);
                startActivity(intent);
                getActivity().finish();
                return true;
            }

            @Override
            public boolean onCloseFailure(String errorMessage) {
                ToastManager mToastManager = new ToastManager(context);
                mToastManager.showToastLong("Errore durante l'accettazione dell'offerta, riprovare");
                return false;
            }
        }));

        builder.setNegativeButton("No", null);

        new Handler(Looper.getMainLooper()).post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void addTextViewToLayout(LinearLayout layout, String text, Typeface typeface) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTypeface(typeface);
        layout.addView(textView);
    }

    private LinearLayout createLinearLayoutHorizontal(OfferDTO offer) {
        LinearLayout linearLayoutHorizontal = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayoutHorizontal.setLayoutParams(layoutParams);
        linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

        TextView userName = new TextView(getContext());
        userName.setText("Offerta: " + offer.getUser().getName() + " ");
        userName.setTextSize(17);
        userName.setTypeface(typeface);
        linearLayoutHorizontal.addView(userName);

        TextView userSurname = new TextView(getContext());
        userSurname.setText(offer.getUser().getSurname() + " ");
        userSurname.setTextSize(17);
        userSurname.setTypeface(typeface);
        linearLayoutHorizontal.addView(userSurname);

        TextView userOffer = new TextView(getContext());
        userOffer.setText("€" + String.valueOf(offer.getOffer()));
        userOffer.setTextSize(17);
        userOffer.setTypeface(typeface);
        linearLayoutHorizontal.addView(userOffer);

        return linearLayoutHorizontal;
    }
}