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
import android.widget.EditText;
import android.widget.ImageView;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import android.widget.MultiAutoCompleteTextView;

import com.bumptech.glide.Glide;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.modelDTO.Auction;
import com.example.dietideals24frontend.modelDTO.Item;
import com.example.dietideals24frontend.modelDTO.Type;
import com.example.dietideals24frontend.modelDTO.User;
import com.example.dietideals24frontend.utility.ImageUtils;

import android.net.Uri;

import java.util.Locale;
import java.util.Calendar;
import java.io.IOException;

import retrofit2.Retrofit;

public class SilentAuctionCreationFragment extends Fragment {
    private Retrofit retrofitService;
    private static final int PICK_IMAGE_REQUEST = 1;
    private View view;
    private byte[] imageContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_silent_auction_creation, container, false);

        retrofitService = MainActivity.retrofitService;

        // Retrieve LoggedIn User
        Bundle bundle = getArguments();
        User user;
        if (bundle != null) {
            user = (User) bundle.getSerializable("loggedInUser");
        } else {
            user = null;
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

        Button createAuctionButton  = view.findViewById(R.id.next_button);
        createAuctionButton.setOnClickListener(v -> {
            EditText nameTextField  = view.findViewById(R.id.editTextTextPersonName);
            EditText basePrizeField = view.findViewById(R.id.editTextTextPersonName2);
            MultiAutoCompleteTextView categoryField = view.findViewById(R.id.categoriesMultiView);
            String date = (String) timeButton.getText();

            String itemName     = String.valueOf(nameTextField.getText());
            String itemCategory = String.valueOf(categoryField.getText());
            String itemBasePrize = String.valueOf(basePrizeField.getText());

            // TODO: Still need to add item Description (there is no EditText for it). For now I invented It.
            // TODO: Define proper categories for items.
            if (itemName.isEmpty() || itemCategory.isEmpty() || itemBasePrize.isEmpty() || date.isEmpty()) {
                // TODO: Cosa facciamo in questo caso?
            } else {
                float itemStartPrize = 0.0f;
                try {
                    itemStartPrize = Float.parseFloat(itemBasePrize);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                // 1. Create Item and associate the User to It.
                Item item = new Item();
                item.setUser(user);
                item.setImage(getImageContent());
                item.setName(itemName);
                item.setCategory(itemCategory);
                item.setBasePrize(itemStartPrize);
                item.setDescription("Unkown");

                // 2. Create Auction and associate it to the Item
                Auction auction = new Auction();
                auction.setAuctionType(Type.SILENT);
                auction.setCurrentOfferValue(itemStartPrize);
                auction.setDuration();

                item.setAuction(auction);
                auction.setItem(item);

                // 3. Send data to the Server and save it into DB (correct order)


                // TODO: 4. Inform the user of the success of the operation and go back home
            }
        });

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
            setImageContent(imageBytes);

            ImageView imageView = view.findViewById(R.id.secondopera);
            Glide.with(this) .load(imageBytes).into(imageView);
        }
    }

    private void setImageContent(byte[] content) {
        this.imageContent = content;
    }

    private byte[] getImageContent() {
        return imageContent;
    }

    private void goBackToHome() {
        Intent intent = new Intent(getActivity(), Home.class);
        startActivity(intent);
        requireActivity().finish();
    }
}