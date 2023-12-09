package com.example.dietideals24frontend.view;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.app.DatePickerDialog;

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
import com.example.dietideals24frontend.utility.PostRequestSender;
import com.example.dietideals24frontend.utility.RequestedItem;

import android.net.Uri;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

        // Retrieve Retrofit instance
        retrofitService = MainActivity.retrofitService;

        // Retrieve LoggedIn User
        Bundle bundle = getArguments();
        User user;
        if (bundle != null) {
            user = (User) bundle.getSerializable("loggedInUser");
        } else {
            user = null;
        }

        MultiAutoCompleteTextView itemCategoryField = view.findViewById(R.id.categoriesMultiView);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categories_array,
                android.R.layout.simple_dropdown_item_1line
        );

        itemCategoryField.setAdapter(adapter);
        itemCategoryField.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        Button dataButton = view.findViewById(R.id.data_button);
        dataButton.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear        = c.get(Calendar.YEAR);
            int mMonth       = c.get(Calendar.MONTH);
            int mDay         = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        dataButton.setText(selectedDate);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        Button createAuctionButton  = view.findViewById(R.id.next_button);
        createAuctionButton.setOnClickListener(v -> {
            EditText nameTextField  = view.findViewById(R.id.editTextTextPersonName);
            EditText basePrizeField = view.findViewById(R.id.editTextTextPersonName2);
            String expirationDate   = (String) dataButton.getText();

            String itemName      = String.valueOf(nameTextField.getText());
            String itemCategory  = String.valueOf(itemCategoryField.getText());
            String itemBasePrize = String.valueOf(basePrizeField.getText());

            // TODO: Still need to add item Description (there is no EditText for it). For now I invented It.
            // TODO: Define proper categories for items.
            if (itemName.isEmpty() || itemCategory.isEmpty() || itemBasePrize.isEmpty() || expirationDate.isEmpty() || getImageContent() == null) {
                // TODO: Cosa facciamo in questo caso?
                // TODO: Pensare poi al controllo degli errori
            } else {
                float itemStartPrize = 0.0f;
                try {
                    itemStartPrize = Float.parseFloat(itemBasePrize);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                @SuppressLint("SimpleDateFormat") SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");

                Date sqlDate = null;
                try {
                    java.util.Date parsed = inputDate.parse(expirationDate);
                    sqlDate = new Date(parsed.getTime()); // This is java.sql.Date now
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                PostRequestSender sender = new PostRequestSender(retrofitService);
                sender.sendItemImageContent(getImageContent());

                RequestedItem requestedItem = new RequestedItem();
                requestedItem.setName(itemName);
                requestedItem.setCategory(itemCategory);
                requestedItem.setDescription("Unknown");
                requestedItem.setBasePrize(itemStartPrize);
                requestedItem.setUser(user);

                sender.sendRegisterItemRequest(requestedItem);

                /*
                Item item = new Item();
                item.setUser(user);
                item.setImage(getImageContent());
                item.setName(itemName);
                item.setCategory(itemCategory);
                item.setBasePrize(itemStartPrize);
                item.setDescription("Unkown"); */

                /* Auction auction = new Auction();
                auction.setAuctionType(Type.SILENT);
                auction.setOwnerId(user.getUserId());
                auction.setCurrentOfferValue(itemStartPrize);
                auction.setExpirationDate(sqlDate);

                item.setAuction(auction);
                auction.setItem(item); */

                // TODO: 5. Mandare anche Auction al server. L'asta deve avere un nome per la ricerca?
                // TODO: 6. Inform the user of the success of the operation and go back home
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

            byte[] imageBytes;
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
}