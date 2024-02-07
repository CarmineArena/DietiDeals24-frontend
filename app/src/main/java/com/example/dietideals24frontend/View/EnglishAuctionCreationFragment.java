package com.example.dietideals24frontend.View;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.app.TimePickerDialog;
import static android.app.Activity.RESULT_OK;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.widget.ArrayAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.view.LayoutInflater;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.ImageView;

import retrofit2.Retrofit;
import com.bumptech.glide.Glide;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Utility.ImageUtils;
import com.example.dietideals24frontend.Controller.AuctionController.AuctionController;

import java.util.Locale;
import java.util.Calendar;
import java.io.IOException;

public class EnglishAuctionCreationFragment extends Fragment {
    private User user;
    private View view;
    private byte[] imageContent;
    private String categoryChoice = null;
    private static final int PICK_IMAGE_REQUEST = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_english_auction_creation, container, false);

        // Retrieve LoggedIn User
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (User) bundle.getSerializable("loggedInUser");
        }

        // Retrieve Retrofit instance
        Retrofit retrofitService = MainActivity.retrofitService;
        AuctionController controller = new AuctionController(retrofitService);

        Button timeButton = view.findViewById(R.id.time_button);
        final TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            timeButton.setText(selectedTime);
        };

        timeButton.setOnClickListener(v -> {
            final Calendar c  = Calendar.getInstance();
            int currentHour   = c.get(Calendar.HOUR_OF_DAY);
            int currentMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    timeSetListener,
                    currentHour,
                    currentMinute,
                    true  // Imposta true se desideri visualizzare il picker in modalità 24 ore
            );

            timePickerDialog.show();
        });

        // Retrieve from strings.xml (list of options)
        Spinner categorySpinner = view.findViewById(R.id.spinner);

        String[] arrayItems = getResources().getStringArray(R.array.categories_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, arrayItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedChoice;
                selectedChoice = parentView.getItemAtPosition(position).toString();
                setCategoryChoice(selectedChoice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                setCategoryChoice(null);
            }
        });

        Button imageButton = view.findViewById(R.id.button);
        imageButton.setOnClickListener(view -> openGallery());

        // TODO: CREARE EFFETTIVAMENTE L'ASTA

        return view;
    }

    /* [DESCRIPTION]
        - This function allows us to Open the Android's phone gallery (to upload images inside the form)
    **/
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /* [DESCRIPTION]
        - It is needed to ppen the Android's phone gallery
    **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            byte[] imageBytes;
            try {
                imageBytes = ImageUtils.convertUriToByteArray(getContext(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.setImageContent(imageBytes);

            ImageView imageView = view.findViewById(R.id.itemImage);
            Glide.with(this).load(imageBytes).into(imageView);
        }
    }

    private void setCategoryChoice(String categoryChoice) {
        this.categoryChoice = categoryChoice;
    }

    private String getCategoryChoice() {
        return categoryChoice;
    }

    private void setImageContent(byte[] content) {
        this.imageContent = content;
    }

    private byte[] getImageContent() {
        return imageContent;
    }
}