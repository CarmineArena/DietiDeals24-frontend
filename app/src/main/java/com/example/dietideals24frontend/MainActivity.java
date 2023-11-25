package com.example.dietideals24frontend;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.dietideals24frontend.graphics.LogInFragment;
import com.example.dietideals24frontend.graphics.SignUpFragment;

public class MainActivity extends AppCompatActivity {

    Button btnFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment((new LogInFragment()));
        btnFragment = findViewById(R.id.btnSwitchLogInFragment);
        btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnFragment.getText().equals("Sei un nuovo utente? Registrati")) {
                    btnFragment.setText("Hai già un account? Accedi");
                    replaceFragment(new SignUpFragment());
                } else {
                    btnFragment.setText("Sei un nuovo utente? Registrati");
                    replaceFragment(new LogInFragment());
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}