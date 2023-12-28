package com.example.dietideals24frontend.View;

import static android.app.Activity.RESULT_OK;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;
import com.example.dietideals24frontend.Presenter.ActivityFactory;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.*;
import com.example.dietideals24frontend.Retrofit.Callback.*;
import com.example.dietideals24frontend.utility.ImageUtils;
import com.example.dietideals24frontend.View.Dialog.Dialog;
import com.example.dietideals24frontend.Retrofit.Service.Requester;

import android.net.Uri;
import android.widget.Spinner;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.io.IOException;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.MainActivity;

public class SilentAuctionCreationFragment extends Fragment {
    private Requester sender;
    private UserDTO user;
    private static final int PICK_IMAGE_REQUEST = 1;
    private View view;
    private byte[] imageContent;
    private String categoryChoice = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_silent_auction_creation, container, false);

        Dialog dialog = new Dialog(getContext());

        // Retrieve Retrofit instance
        Retrofit retrofitService = MainActivity.retrofitService;
        sender = new Requester(retrofitService);

        // Retrieve LoggedIn User
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (UserDTO) bundle.getSerializable("loggedInUser");
        } else {
            user = null;
        }

        Spinner categorySpinner = view.findViewById(R.id.spinner);

        // Retrieve from strings.xml (list of options)
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

        Button dataButton = view.findViewById(R.id.data_button);
        dataButton.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear        = c.get(Calendar.YEAR);
            int mMonth       = c.get(Calendar.MONTH);
            int mDay         = c.get(Calendar.DAY_OF_MONTH);

            // ExpirationDate error checking: It cannot be a day <= current day.
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        LocalDate currentDate = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String currentDateString = currentDate.format(formatter);

                        // TODO: A questo vogliamo aggiungere un tetto massimo relativo all'anno?
                        if (currentDateString.compareTo(selectedDate) < 0) {
                            dataButton.setText(selectedDate);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        Button createAuctionButton  = view.findViewById(R.id.next_button);
        createAuctionButton.setOnClickListener(v -> {
            EditText nameTextField  = view.findViewById(R.id.editTextTextPersonName);
            EditText basePrizeField = view.findViewById(R.id.editTextTextPersonName2);
            EditText descriptionField = view.findViewById(R.id.description_field);
            String expirationDate   = (String) dataButton.getText();

            String itemName        = String.valueOf(nameTextField.getText());
            String itemCategory    = getCategoryChoice();
            String itemBasePrize   = String.valueOf(basePrizeField.getText());
            String itemDescription = String.valueOf(descriptionField.getText());

            // TODO: Bisogna aggiungere il campo "Description" per l'Item. Per il momento è messo "Unknown"

            if (itemName.isEmpty() || itemCategory.isEmpty() || itemCategory.equals("Scegli una categoria") || itemBasePrize.isEmpty()
                    || expirationDate.isEmpty() || getImageContent() == null || itemDescription.isEmpty()) {
                String title, message;
                title = "FORM ERROR";
                message = "Assicurarsi di aver eseguito correttamente la procedura di creazione dell'asta!";

                if (getImageContent() == null) message += " Non è presente immagine del prodotto.";

                if (itemCategory.equals("Scegli una categoria") || itemCategory.isEmpty()) message += " Devi definire la categoria dell'oggetto!";

                dialog.showAlertDialog(title, message);
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
                requestedItem.setDescription(itemDescription);
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

            ImageView imageView = view.findViewById(R.id.itemImage);
            Glide.with(this).load(imageBytes).into(imageView);
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

                // TODO: Devo mostrare il Dialog di successo nella registrazione dell'asta?

                ActivityFactory factory = new ActivityFactory();
                Intent intent = factory.createIntentForHome(getContext(), user); // Return Home with loggedIn user's informations
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onAuctionRegistrationFailure(String errorMessage) {
                Log.i("AUCTION REGISTRATION REQUEST", "FAILED TO SEND");
                // TODO: Devo mostrare il Dialog di fallimento nella registrazione dell'asta?
                return false;
            }
        });
    }

    private void setCategoryChoice(String categoryChoice) {
        this.categoryChoice = categoryChoice;
    }

    private String getCategoryChoice() {
        return categoryChoice;
    }
}