package com.example.dietideals24frontend.View;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Spinner;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import com.example.dietideals24frontend.R;

// TODO: RICORDATI DI GESTIRE CORRETTAMENTE QUESTO FRAGMENT

public class SilentAuctionFragment extends Fragment {

    View view;
    String[] offer = {"Rialza di 10€", "Rialza di 20€", "Rialzo personalizzato"};
    private List<String> offerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_silent_auction, container, false);

        TextView offerField = view.findViewById(R.id.offertField);
        offerField.setVisibility(View.INVISIBLE);

        Spinner spinnerType = view.findViewById(R.id.offerSpinner);

        offerList = new ArrayList<>(Arrays.asList(offer));
        // Crea un ArrayAdapter per le opzioni e collega l'array all'adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, offerList);

        // Specifica il layout da utilizzare quando l'elenco delle opzioni viene visualizzato
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Collega l'adapter al tuo Spinner
        spinnerType.setAdapter(adapter);

        // Imposta un listener per gestire gli eventi di selezione
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Rimuovi l'opzione "Scegli il tuo tipo di asta" se è stata selezionata un'altra opzione
                String selectedType = offer[position];
                if (selectedType.equals("Rialzo personalizzato")) {
                    offerField.setVisibility(View.VISIBLE);
                } else {
                    offerField.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        return view;
    }
}