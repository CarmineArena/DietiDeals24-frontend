package com.example.dietideals24frontend.View.Fragment;

import android.os.Bundle;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import com.example.dietideals24frontend.View.Dialog.Dialog;
import com.example.dietideals24frontend.Presenter.ActivityPresenter;
import com.example.dietideals24frontend.Controller.OfferController.OfferController;
import com.example.dietideals24frontend.Controller.OfferController.Callback.RegisterOfferCallback;
import com.example.dietideals24frontend.ToastManager;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.example.dietideals24frontend.Model.Auction;
import com.example.dietideals24frontend.Model.DTO.OfferDTO;
import com.example.dietideals24frontend.Utility.ImageUtils;
import com.example.dietideals24frontend.Utility.DateAndTimeRetriever;
import com.example.dietideals24frontend.Controller.OfferController.Callback.RetrieveBestOfferCallback;

public class SilentAuctionFragment extends Fragment {
    private Auction auction;
    private User loggedInUser;
    private View view;
    private final String[] offer = { "Rialza di 10€", "Rialza di 20€", "Rialzo personalizzato" };
    private List<String> offerList;
    private float lastOfferValue; // The highest value among the current offers
    private Retrofit retrofitService;
    private String choice;
    private ToastManager mToastManager;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_silent_auction, container, false);

        mToastManager = new ToastManager(getContext());

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
            Intent intent = new ActivityPresenter().createProfileIntent(getContext(), auction.getItem().getUser());
            startActivity(intent);
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



        return view;
    }

    /* PRIVATE */


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}