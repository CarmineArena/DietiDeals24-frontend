package com.example.dietideals24frontend.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.view.EnglishAuctionCreationFragment;
import com.example.dietideals24frontend.view.SilentAuctionCreationFragment;

public class CreateAuction extends AppCompatActivity {

    String[] type = {"Scegli il tuo tipo di asta", "Asta Silenziosa", "Asta all'inglese"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_auction);
        Button btnActiveFragment = findViewById(R.id.fragment_button);
        Button englishbtnDaLevare = findViewById(R.id.button3);
        Spinner spinnerType = findViewById(R.id.spinnerTypeAuction);


        // Crea un ArrayAdapter per le opzioni e collega l'array all'adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, type);

        // Specifica il layout da utilizzare quando l'elenco delle opzioni viene visualizzato
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Collega l'adapter al tuo Spinner
        spinnerType.setAdapter(adapter);

        // Imposta un listener per gestire gli eventi di selezione
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = type[position];
                if (!selectedType.equals("Scegli il tuo tipo di asta")) {
                    // Fai qualcosa con il tipo selezionato
                    adapter.notifyDataSetChanged();
                    if(selectedType.equals("Asta silenziosa")) {
                        replaceFragment(new SilentAuctionCreationFragment());
                    } else if(selectedType.equals("Asta all'inglese")) {
                        //replaceFragment();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Chiamato quando non è stata selezionata alcuna opzione
            }
        });

        btnActiveFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SilentAuctionCreationFragment());
            }
        });
        englishbtnDaLevare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new EnglishAuctionCreationFragment());
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameAuction,fragment);
        fragmentTransaction.commit();
    }
}