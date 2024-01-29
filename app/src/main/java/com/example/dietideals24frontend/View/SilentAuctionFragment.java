package com.example.dietideals24frontend.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.Arrays;
import java.util.ArrayList;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.Auction;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;
import com.example.dietideals24frontend.Utility.ImageUtils;
import com.example.dietideals24frontend.Retrofit.Service.Requester;
import com.example.dietideals24frontend.Utility.DateAndTimeRetriever;
import com.example.dietideals24frontend.Retrofit.Callback.OfferRegistrationCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveBestOfferCallback;

public class SilentAuctionFragment extends Fragment {
    private Auction auction;
    private User loggedInUser;
    private View view;
    private final String[] offer = { "Rialza di 10€", "Rialza di 20€", "Rialzo personalizzato" };
    private List<String> offerList;
    private float lastOfferValue; // The highest value among the current offers
    private Retrofit retrofitService;
    private String choice;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_silent_auction, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            auction = (Auction) bundle.getSerializable("selectedAuction");
            loggedInUser = (User) bundle.getSerializable("loggedInUser");
        } else {
            auction = null;
        }

        retrofitService = MainActivity.retrofitService;

        TextView nameView = view.findViewById(R.id.ItemNameField);
        nameView.setText(auction.getItem().getName());

        TextView categoryView = view.findViewById(R.id.categoryView);
        categoryView.setText("Categoria: " + auction.getItem().getCategory());

        Button btnUser = view.findViewById(R.id.NameBtn);
        btnUser.setText(auction.getItem().getUser().getName() + " " + auction.getItem().getUser().getSurname());
        btnUser.setOnClickListener(v -> {
            // TODO: PORTARE AL PROFILO DELL'UTENTE
        });

        TextView descriptionView = view.findViewById(R.id.DescriptionView);
        descriptionView.setText(auction.getItem().getDescription());

        ImageView itemImageView = view.findViewById(R.id.itemImage);
        ImageUtils.fillImageView(itemImageView, auction.getItem().getImage());

        TextView dateView = view.findViewById(R.id.textView10);
        dateView.setText("Asta aperta fino al giorno: " + auction.getExpirationDate());

        TextView priceView = view.findViewById(R.id.textView11);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ITALIAN);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        priceView.setText("Prezzo iniziale: € " + df.format(auction.getCurrentOfferValue()));

        /* Retrieval of the Best offer for that Item (with Details) */

        TextView bidderView    = view.findViewById(R.id.textView7);
        TextView LastOfferView = view.findViewById(R.id.LastOfferView);

        Button bidderBtn = view.findViewById(R.id.Name2Btn);

        Requester requester = new Requester(retrofitService);
        requester.sendFindBestOfferRequest(auction.getItem().getItemId(), auction.getAuctionId(), new RetrieveBestOfferCallback() {
            @Override
            public boolean onBestOfferRetrievalSuccess(OfferDTO offerDTO) {
                bidderView.setText("Fatta da: ");

                bidderBtn.setText(offerDTO.getUser().getName() + " " + offerDTO.getUser().getSurname());
                bidderBtn.setOnClickListener(v -> {
                    // TODO: PORTARE AL PROFILO DELL'UTENTE
                });

                LastOfferView.setText("Ultima offerta: € " + offerDTO.getOffer());
                return true;
            }

            @Override
            public boolean onBestOfferRetrievalFailure(String errorMessage) {
                bidderView.setText("Fatta da: ");

                bidderBtn.setEnabled(false);
                bidderBtn.setVisibility(View.INVISIBLE);

                LastOfferView.setText("Ultima offerta: non ci sono ancora offerte.");
                return false;
            }
        });

        TextView offerField = view.findViewById(R.id.offertField);
        offerField.setVisibility(View.INVISIBLE);

        Spinner spinnerType = view.findViewById(R.id.offerSpinner);

        offerList = new ArrayList<>(Arrays.asList(offer));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, offerList);

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
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        manageOffer();
        return view;
    }

    /* PRIVATE */

    private void manageOffer() {
        Button offerBtn = view.findViewById(R.id.OfferButton);
        EditText offerText = view.findViewById(R.id.offertField);
        float currentOfferValue = auction.getCurrentOfferValue();

        offerBtn.setOnClickListener(v -> {
            float offerta = 0.0f;

            OfferDTO offerDTO = new OfferDTO();
            offerDTO.setUser(loggedInUser);
            offerDTO.setAuctionId(auction.getAuctionId());
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
                        // TODO: DARE ERRORE O ECCEZIONE
                        Log.e("OFFER VALUE", "NOT VALID");
                    }
            }

            // TODO: IL VALORE DI OFFERTA VIENE LETTO NELLA MANIERA CORRETTA. CAPIRE PERCHE' IL SERVER REGISTRA VALORE DI OFFERTA ZERO.
            // Log.i("OFFER", "VALUE: " + offerta);
            if (offerta > 0.0f) {
                offerDTO.setOffer(offerta);
                // Log.i("OFFER 2", "VALUE: " + offerDTO.getOffer());
                Requester requester = new Requester(retrofitService);
                requester.sendRegisterOfferRequest(offerDTO, new OfferRegistrationCallback() {
                    @Override
                    public boolean onOfferRegistrationSuccess() {
                        // TODO: NOTIFICARE ALL'UTENTE IL SUCCESSO DELL'OFFERTA
                        // TODO: AGGIORNARE LA PAGINA
                        return true;
                    }

                    @Override
                    public boolean onOfferRegistrationFailure(String errorMessage) {
                        // TODO: COSA FACCIO IN QUESTO CASO?
                        return false;
                    }
                });
            } else {
                // TODO: DARE ERRORE
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
}