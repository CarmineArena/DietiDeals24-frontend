
package com.example.dietideals24frontend;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.dietideals24frontend.retrofit.Message;
import com.example.dietideals24frontend.graphics.LogInFragment;
import com.example.dietideals24frontend.graphics.SignUpFragment;
import com.example.dietideals24frontend.retrofit.GitHubApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static String BASE_URL = ""; // TODO: REMEMBER TO NOT PUSH THIS
    private static Retrofit retrofitService;
    private Button btnFragment;

    /* METHODS */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void initiateGitHubProcess(Retrofit retrofitService) {
        GitHubApiService service = retrofitService.create(GitHubApiService.class);
        Call<Message> call = service.startGitHubProcess();

        Log.d("GitHubProcess", "Request URL: " + call.request().url());
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    String result = response.body().getMessage();
                    Log.d("GitHubProcess", result);
                } else {
                    Log.e("GitHubProcess", "GitHub login initiation failed!");
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e("GitHubProcess", t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CREATE RETROFIT INSTANCE
        retrofitService = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Manage GitHub requests
        Button gitHubButton = findViewById(R.id.gitHubButton);
        gitHubButton.setOnClickListener(v -> {
            initiateGitHubProcess(retrofitService);
            // TODO: After that must redirect to other pages
        });

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
}