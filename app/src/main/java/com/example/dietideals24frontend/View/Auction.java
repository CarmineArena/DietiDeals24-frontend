package com.example.dietideals24frontend.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.Item;

public class Auction extends AppCompatActivity {
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        // TODO: CREARE SWITCH-CASE PER FAR PARTIRE IL FRAGMENT CORRETTO
            // TODO: --> RECUPERARE I DETTAGLI DELL'ASTA DATO L'UTENTE IN INPUT ALL'ACTIVITY E MOSTRARE IL CORRETTO FRAGMENT

        // TODO: MOSTRARE TUTTO NELLA PAGINA
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("selectedItem");

        replaceFragment(new SilentAuctionFragment()); // TODO: FALLO DARE AL FRAGMENT_PRESENTER
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentAuction,fragment);
        fragmentTransaction.commit();
    }
}