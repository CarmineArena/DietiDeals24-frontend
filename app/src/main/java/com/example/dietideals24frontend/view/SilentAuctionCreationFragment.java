package com.example.dietideals24frontend.view;

import static android.app.Activity.RESULT_OK;

import android.os.Bundle;
import android.content.Intent;
import android.app.TimePickerDialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import android.widget.MultiAutoCompleteTextView;

import com.bumptech.glide.Glide;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.modelDTO.User;
import com.example.dietideals24frontend.utility.ImageUtils;

import android.net.Uri;

import java.util.Locale;
import java.util.Calendar;
import java.io.IOException;

public class SilentAuctionCreationFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_silent_auction_creation, container, false);

        // Retrieve LoggedIn User
        Bundle bundle = getArguments();
        User user = null;
        if (bundle != null) {
            user = (User) bundle.getSerializable("loggedInUser");
        }

        MultiAutoCompleteTextView multiAutoCompleteTextView = view.findViewById(R.id.categoriesMultiView);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categories_array,
                android.R.layout.simple_dropdown_item_1line
        );

        multiAutoCompleteTextView.setAdapter(adapter);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        Button timeButton = view.findViewById(R.id.time_button);
        final TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            // Format the time as you wish (for example, in an HH:mm format)
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            timeButton.setText(selectedTime);
        };

        timeButton.setOnClickListener(v -> {
            final Calendar c  = Calendar.getInstance();
            int currentHour   = c.get(Calendar.HOUR_OF_DAY);
            int currentMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), timeSetListener, currentHour, currentMinute,
                    true  // Set to true if you want to visualize the picker 24 hours mode
            );
            timePickerDialog.show();
        });

        Button yourButton  = view.findViewById(R.id.next_button);

        Button imageButton = view.findViewById(R.id.button);
        imageButton.setOnClickListener(view -> openGallery());

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            // Converte l'URI in un byte array o un altro formato necessario
            byte[] imageBytes = null;
            try {
                imageBytes = ImageUtils.convertUriToByteArray(getContext(), uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // TODO: 1. CREA L'ITEM E ASSOCIA L'ITEM ALL'UTENTE
            // item.setImage(imageBytes);

            // TODO: 2. Inserire nel DB il record relativo all'item creato (mandare tramite rest Api l'oggetto Item)
            // uploadImageToServer(imageBytes);

            // TODO: 3. Creare Asta, associargli l'oggetto e l'id del proprietario, e inserire nel DB l'istanza dell'asta (mandare tramite rest Api l'oggetto Auction)

            ImageView imageView = view.findViewById(R.id.secondopera);
            Glide.with(this) .load(imageBytes).into(imageView);
        }
    }

    private void goBackToHome() {
        Intent intent = new Intent(getActivity(), Home.class);
        startActivity(intent);
        requireActivity().finish();
    }
}