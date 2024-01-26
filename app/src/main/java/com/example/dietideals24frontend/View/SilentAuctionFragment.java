package com.example.dietideals24frontend.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;

import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;

import java.util.List;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import java.util.Arrays;
import java.util.ArrayList;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.Auction;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;
import com.example.dietideals24frontend.Utility.ImageUtils;
import com.example.dietideals24frontend.Retrofit.Service.Requester;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveBestOfferCallback;

public class SilentAuctionFragment extends Fragment {
    private Auction auction;
    private View view;
    private final String[] offer = { "Rialza di 10€", "Rialza di 20€", "Rialzo personalizzato" };
    private List<String> offerList;
    private float lastOfferValue; // The highest value among the current offers
    private Retrofit retrofitService;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_silent_auction, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            auction = (Auction) bundle.getSerializable("selectedAuction");
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
        priceView.setText(df.format(auction.getCurrentOfferValue()));

        /* Retrieval of the Best offer for that Item (with Details) */

        TextView bidderView    = view.findViewById(R.id.textView7);
        TextView LastOfferView = view.findViewById(R.id.LastOfferView);

        Requester requester = new Requester(retrofitService);
        requester.sendFindBestOfferRequest(auction.getItem().getItemId(), auction.getAuctionId(), new RetrieveBestOfferCallback() {
            @Override
            public boolean onBestOfferRetrievalSuccess(OfferDTO offerDTO) {

                if (offerDTO != null) {
                    bidderView.setText("Fatta da: " + offerDTO.getUser().getName() + " " + offerDTO.getUser().getSurname());
                    LastOfferView.setText("Ultima offerta: € " + offerDTO.getOffer());
                } else {
                    // TODO: MIGLIORARE E RIPULIRE TUTTO QUANTO
                    // TODO: COSA FACCIAMO SE NULL?
                }

                return true;
            }

            @Override
            public boolean onBestOfferRetrievalFailure(String errorMessage) {
                // TODO: COSA FACCIO QUI?
                return false;
            }
        });





        // TODO: GESTIRE LE PROPOSTE DI OFFERTE [ CAPIRE L'ORDINE IN CUI SCRIVERE QUESTO CODICE ]
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
                if (selectedType.equals("Rialzo personalizzato")) {
                    offerField.setVisibility(View.VISIBLE);
                } else {
                    offerField.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        return view;
    }
}