package com.example.dietideals24frontend.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.UserDTO;
import com.example.dietideals24frontend.Presenter.FragmentFactory;

public class CreateAuction extends AppCompatActivity {
    String[] type = { "Scegli il tuo tipo di asta", "Asta Silenziosa", "Asta all'inglese" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_auction);

        Intent intent = getIntent();
        UserDTO user = (UserDTO) intent.getSerializableExtra("loggedInUser");

        Button btnActiveFragment  = findViewById(R.id.fragment_button);
        Button englishbtnDaLevare = findViewById(R.id.button3);
        Spinner spinnerType       = findViewById(R.id.spinnerTypeAuction);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Rimuovi l'opzione "Scegli il tuo tipo di asta" se è stata selezionata un'altra opzione
                String selectedType = type[position];
                if (selectedType.equals("Asta Silenziosa")) {
                    replaceFragment(new SilentAuctionCreationFragment());
                } else if (selectedType.equals("Asta all'Inglese")) {
                    replaceFragment(new EnglishAuctionCreationFragment());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        btnActiveFragment.setOnClickListener(v -> {
            FragmentFactory fragmentFactory = new FragmentFactory();
            SilentAuctionCreationFragment fragment = fragmentFactory.createSilentAuctionFragment(user);
            replaceFragment(fragment);
        });

        englishbtnDaLevare.setOnClickListener(v -> {
            FragmentFactory fragmentFactory = new FragmentFactory();
            EnglishAuctionCreationFragment fragment = fragmentFactory.createEnglishAuctionFragment(user);
            replaceFragment(fragment);
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameAuction,fragment);
        fragmentTransaction.commit();
    }
}