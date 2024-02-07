package com.example.dietideals24frontend.View.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.os.Bundle;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Presenter.FragmentPresenter;
import com.example.dietideals24frontend.View.EnglishAuctionCreationFragment;
import com.example.dietideals24frontend.View.Fragment.SilentAuctionCreationFragment;

public class CreateAuctionActivity extends AppCompatActivity {
    String[] type = { "Asta Silenziosa", "Asta all'Inglese" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_auction);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("loggedInUser");

        FragmentPresenter presenter = new FragmentPresenter();
        SilentAuctionCreationFragment silentFragment   = presenter.createSilentAuctionFragment(user);
        EnglishAuctionCreationFragment englishFragment = presenter.createEnglishAuctionFragment(user);

        Spinner spinnerType = findViewById(R.id.spinnerTypeAuction);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = type[position];
                if (selectedType.equals("Asta Silenziosa")) {
                    replaceFragment(silentFragment);
                } else if (selectedType.equals("Asta all'Inglese")) {
                    replaceFragment(englishFragment);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameAuction,fragment);
        fragmentTransaction.commit();
    }
}