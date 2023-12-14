package com.example.dietideals24frontend.View;

import static android.app.Activity.RESULT_OK;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.provider.MediaStore;

import android.widget.MultiAutoCompleteTextView;

import com.bumptech.glide.Glide;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.*;
import com.example.dietideals24frontend.Retrofit.Callback.*;
import com.example.dietideals24frontend.utility.ImageUtils;
import com.example.dietideals24frontend.Retrofit.Service.PostRequester;

import android.net.Uri;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.io.IOException;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.MainActivity;

public class SilentAuctionCreationFragment extends Fragment {
    private PostRequester sender;
    private static final int PICK_IMAGE_REQUEST = 1;
    private View view;
    private byte[] imageContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_silent_auction_creation, container, false);

        // Retrieve Retrofit instance
        Retrofit retrofitService = MainActivity.retrofitService;
        sender = new PostRequester(retrofitService);

        // Retrieve LoggedIn User
        Bundle bundle = getArguments();
        UserDTO user;
        if (bundle != null) {
            user = (UserDTO) bundle.getSerializable("loggedInUser");
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

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");

                Date sqlDate = null;
                try {
                    java.util.Date parsed = inputDate.parse(expirationDate);
                    sqlDate = new Date(parsed.getTime()); // This is java.sql.Date now
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Send to Server the Item's related image (its a byte[])
                sender.sendItemImageContent(getImageContent(), new ImageContentRegistrationCallback() {
                    @Override
                    public boolean onReceptionSuccess(byte[] itemImageContent) { return true; }
                    @Override
                    public boolean onReceptionFailure(String errorMessage) { return false; }
                });

                RequestedItemDTO requestedItem = new RequestedItemDTO();
                requestedItem.setUser(user);
                requestedItem.setName(itemName);
                requestedItem.setCategory(itemCategory);
                requestedItem.setDescription("Unknown");
                requestedItem.setBasePrize(itemStartPrize);

                final Date finalSqlDate = sqlDate;
                float finalItemStartPrize = itemStartPrize;
                final Integer[] savedItemId = new Integer[1];

                // Send to Server the Item's registration Request
                sender.sendRegisterItemRequest(requestedItem, new ItemRegistrationCallback() {
                    @Override
                    public boolean onItemRegistrationSuccess(Integer itemId) {
                        savedItemId[0] = itemId;

                        // Item and Auction registration must happen one after the other (that is why we do it like that)
                        registerAuction(savedItemId[0], user.getUserId(), finalSqlDate, finalItemStartPrize);
                        return true;
                    }
                    @Override
                    public boolean onItemRegistrationFailure(String errorMessage) { return false; }
                });

                // TODO: L'asta deve avere un nome per la ricerca?
                // TODO: Afther all of this, we should inform the user of the success of the operation and go back home
            }
        });

        Button imageButton = view.findViewById(R.id.button);
        imageButton.setOnClickListener(view -> openGallery());

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

    private void registerAuction(Integer itemId, Integer userId, Date sqlDate, float itemStartPrize) {
        RequestedAuctionDTO requestedAuctionDTO = new RequestedAuctionDTO();
        requestedAuctionDTO.setOwnerId(userId);
        requestedAuctionDTO.setAuctionType(Type.SILENT);

        String date = sqlDate.toString();
        requestedAuctionDTO.setActive(true);
        requestedAuctionDTO.setExpirationDate(date);
        requestedAuctionDTO.setExpirationTime(null); // NULL because its a Silent Auction
        requestedAuctionDTO.setRequestedItemId(itemId);
        requestedAuctionDTO.setCurrentOfferValue(itemStartPrize);

        // Send to Server the Auction's registration Request
        sender.sendRegisterAuctionRequest(requestedAuctionDTO, new AuctionRegistrationCallback() {
            @Override
            public boolean onAuctionRegistrationSuccess(RequestedAuctionDTO requestedAuctionDTO) {
                Log.i("AUCTION REGISTRATION REQUEST", "SENT");
                // TODO: Riportare l'utente alla HOME (Ovviamente mantenendo salvata l'istanza dell'utente loggato)?
                return true;
            }

            @Override
            public boolean onAuctionRegistrationFailure(String errorMessage) {
                Log.i("AUCTION REGISTRATION REQUEST", "FAILED TO SEND");
                // TODO: Cosa facciamo in questo caso?
                return false;
            }
        });
    }
}