package com.example.dietideals24frontend.View.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.annotation.SuppressLint;

import android.os.Looper;
import android.os.Handler;

import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ArrayAdapter;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.view.LayoutInflater;

import java.util.List;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.Arrays;
import java.util.ArrayList;

import com.example.dietideals24frontend.Model.Type;
import com.example.dietideals24frontend.Model.DTO.AuctionStatusDTO;
import com.example.dietideals24frontend.Controller.AuctionController.AuctionController;
import com.example.dietideals24frontend.Controller.AuctionController.Callback.TimeRemainingCallback;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.View.Activity.HomeActivity;
import com.example.dietideals24frontend.View.ToastManager;
import com.example.dietideals24frontend.View.Dialog.Dialog;
import com.example.dietideals24frontend.Presenter.ActivityPresenter;
import com.example.dietideals24frontend.Controller.OfferController.OfferController;
import com.example.dietideals24frontend.Controller.OfferController.Callback.RegisterOfferCallback;
import com.example.dietideals24frontend.Controller.OfferController.Callback.RetrieveBestOfferCallback;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.Auction;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;
import com.example.dietideals24frontend.Utility.ImageUtils;
import com.example.dietideals24frontend.Utility.DateAndTimeRetriever;

public class EnglishAuctionFragment extends Fragment {
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = null;
    private Auction auction;
    private User loggedInUser;
    private View view;
    private TextView textViewCountDown;
    private final String[] offer = { "Rialza di 10€", "Rialza di 20€", "Rialzo personalizzato" };
    private List<String> offerList;
    private float lastOfferValue; // The highest value among the current offers
    private Retrofit retrofitService;
    private String choice;
    private ToastManager mToastManager;
    private Integer userId, auctionId;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_english_auction, container, false);

        retrofitService = MainActivity.retrofitService;
        mToastManager = new ToastManager(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            auction      = (Auction) bundle.getSerializable("selectedAuction");
            loggedInUser = (User) bundle.getSerializable("loggedInUser");

            auctionId = auction.getAuctionId();
            userId    = loggedInUser.getUserId();
        } else {
            auction      = null;
            loggedInUser = null;
        }

        textViewCountDown = view.findViewById(R.id.textView10);
        TextView nameView = view.findViewById(R.id.ItemNameField);
        nameView.setText(auction.getItem().getName());

        TextView categoryView = view.findViewById(R.id.categoryView);
        categoryView.setText("Categoria: " + auction.getItem().getCategory());

        Button btnUser = view.findViewById(R.id.NameBtn);
        btnUser.setText(auction.getItem().getUser().getName() + " " + auction.getItem().getUser().getSurname());
        btnUser.setOnClickListener(v -> {
            Intent intent = new ActivityPresenter().createProfileIntent(getContext(), auction.getItem().getUser());
            startActivity(intent);
        });

        TextView descriptionView = view.findViewById(R.id.DescriptionView);
        descriptionView.setText(auction.getItem().getDescription());

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        ImageView itemImageView = view.findViewById(R.id.itemImage);
        ImageUtils.fillImageView(itemImageView, auction.getItem().getImage());

        TextView priceView = view.findViewById(R.id.textView11);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ITALIAN);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        priceView.setText("Prezzo iniziale: € " + df.format(auction.getCurrentOfferValue()));

        /* Retrieval of the Best offer for that Item (with Details) */

        TextView bidderView    = view.findViewById(R.id.textView7);
        TextView LastOfferView = view.findViewById(R.id.LastOfferView);

        Button bidderBtn = view.findViewById(R.id.Name2Btn);

        OfferController controller = new OfferController(retrofitService);
        controller.sendFindBestOfferRequest(auction.getItem().getItemId(), auction.getAuctionId(), new RetrieveBestOfferCallback() {
            @Override
            public boolean onBestOfferRetrievalSuccess(OfferDTO offerDTO) {
                bidderView.setText("Fatta da: ");

                bidderBtn.setText(offerDTO.getUser().getName() + " " + offerDTO.getUser().getSurname());
                bidderBtn.setOnClickListener(v -> {
                    Intent intent = new ActivityPresenter().createProfileIntent(getContext(), offerDTO.getUser());
                    startActivity(intent);
                });

                LastOfferView.setText("Ultima offerta: € " + offerDTO.getOffer());
                return true;
            }

            @Override
            public boolean onBestOfferRetrievalFailure(String errorMessage) {
                bidderView.setText(" ");

                bidderBtn.setEnabled(false);
                bidderBtn.setVisibility(View.INVISIBLE);

                LastOfferView.setText("Non ci sono ancora offerte.");
                return false;
            }
        });

        TextView offerField = view.findViewById(R.id.offertField);
        offerField.setVisibility(View.INVISIBLE);

        Spinner spinnerType = view.findViewById(R.id.offerSpinner);

        offerList = new ArrayList<>(Arrays.asList(offer));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.custom_spinner_item, offerList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = offer[position];
                setChoice(selectedType);
                if (selectedType.equals("Rialzo personalizzato")) {
                    offerField.setVisibility(View.VISIBLE);
                } else {
                    offerField.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                setChoice("Rialza di 10€");
            }
        });

        if (loggedInUser.getUserId().equals(auction.getItem().getUser().getUserId())) {
            // THE USER CLICKED TO SEE THE STATUS OF HIS/HER AUCTIONS
            spinnerType.setEnabled(false);
            spinnerType.setVisibility(View.INVISIBLE);

            Button offerBtn = view.findViewById(R.id.OfferButton);
            offerBtn.setEnabled(false);
            offerBtn.setVisibility(View.INVISIBLE);
        } else {
            manageOffer();
        }

        return view;
    }

    private void manageOffer() {
        Button offerBtn    = view.findViewById(R.id.OfferButton);
        EditText offerText = view.findViewById(R.id.offertField);
        float currentOfferValue = auction.getCurrentOfferValue();

        offerBtn.setOnClickListener(v -> {
            float offerta = 0.0f;

            OfferDTO offerDTO = new OfferDTO();
            offerDTO.setUser(loggedInUser);
            offerDTO.setAuctionId(auction.getAuctionId());
            offerDTO.setAuctionType(Type.ENGLISH);
            offerDTO.setOfferDate(DateAndTimeRetriever.getCurrentDate());
            offerDTO.setOfferTime(DateAndTimeRetriever.getCurrentTime());

            switch(getChoice()) {
                case "Rialza di 10€":
                    offerta = currentOfferValue + 10.0f;
                    break;
                case "Rialza di 20€":
                    offerta = currentOfferValue + 20.0f;
                    break;
                default:
                    String offer = offerText.getText().toString();

                    if (offer.isEmpty()) {
                        Dialog dialog = new Dialog(getContext());
                        dialog.showAlertDialog("OFFER NOT VALID", "Specificare di quanto si vuole rialzare l'offerta.");
                        Log.e("OFFER VALUE", "NOT VALID");
                    }
            }

            if (offerta > 0.0f) {
                offerDTO.setOffer(offerta);

                OfferController controller = new OfferController(retrofitService);
                controller.sendRegisterOfferRequest(offerDTO, new RegisterOfferCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public boolean onOfferRegistrationSuccess() {
                        mToastManager.showToast("Offerta fatta con successo!");

                        offerText.setText("Ultima offerta: € " + offerDTO.getOffer());
                        Button bidderBtn = view.findViewById(R.id.Name2Btn);
                        bidderBtn.setText(loggedInUser.getName() + " " + loggedInUser.getSurname());
                        return true;
                    }

                    @Override
                    public boolean onOfferRegistrationFailure(String errorMessage) {
                        mToastManager.showToast("Operazione fallita. Riprovare!");
                        return false;
                    }
                });
            } else {
                Dialog dialog = new Dialog(getContext());
                dialog.showAlertDialog("FORM ERROR", "Il valore della tua offerta deve essere strettamente maggiore di zero.");
                Log.e("OFFER VALUE", "NOT VALID <= 0");
            }
        });
    }

    private void setChoice(String choice) {
        this.choice = choice;
    }

    private String getChoice() {
        return choice;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void startPollingTimeRemaining(Integer auctionId, Integer userId) {
        if (runnable != null)
            handler.removeCallbacks(runnable);

        runnable = new Runnable() {
            @Override
            public void run() {
                AuctionController controller = new AuctionController(MainActivity.retrofitService);
                controller.sendGetTimeRemainingRequest(auctionId, userId, new TimeRemainingCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public boolean onTimeRetrievedSuccessfull(AuctionStatusDTO auctionStatusDTO) {
                        if (auctionStatusDTO.isActive()) {
                            textViewCountDown.post(() -> textViewCountDown.setText("Tempo rimanente: " + DateAndTimeRetriever.formatTime(auctionStatusDTO.getTimeRemaining())));
                        } else {
                            textViewCountDown.post(() -> textViewCountDown.setText("Asta terminata!"));
                            Context context = getContext();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("TERMINATED AUCTION");
                            builder.setMessage("L'asta è terminata, verrai reindirizzato alla tua home.");
                            builder.setIcon(android.R.drawable.ic_dialog_info);
                            builder.setPositiveButton("Ok", ((dialog, which) -> {
                                ActivityPresenter presenter = new ActivityPresenter();
                                Intent intent = presenter.createIntentForHome(getContext(), loggedInUser); // Return Home with loggedIn user's informations
                                startActivity(intent);
                            }));
                            new Handler(Looper.getMainLooper()).post(() -> {
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            });
                        }
                        return true;
                    }

                    @Override
                    public boolean onTimeRetrievedFailure(String errorMessage) {
                        textViewCountDown.post(() -> textViewCountDown.setText(errorMessage));
                        return false;
                    }
                });

                handler.postDelayed(this, 60000); // Every 60 seconds
            }
        };

        handler.post(runnable);
    }

    public void stopPollingTimeRemaining() {
        if (runnable != null)
            handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        startPollingTimeRemaining(auctionId, userId);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPollingTimeRemaining();
    }
}