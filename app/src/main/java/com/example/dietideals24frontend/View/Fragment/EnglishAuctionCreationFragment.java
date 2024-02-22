package com.example.dietideals24frontend.View.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.app.TimePickerDialog;
import static android.app.Activity.RESULT_OK;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.view.LayoutInflater;

import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.dietideals24frontend.View.Dialog.Dialog;

import com.example.dietideals24frontend.Model.Type;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;
import com.example.dietideals24frontend.Presenter.ActivityPresenter;
import com.example.dietideals24frontend.Controller.AuctionController.Callback.*;

import retrofit2.Retrofit;
import com.bumptech.glide.Glide;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.View.ToastManager;
import com.example.dietideals24frontend.Utility.ImageUtils;
import com.example.dietideals24frontend.Controller.AuctionController.AuctionController;

import java.util.Locale;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnglishAuctionCreationFragment extends Fragment {
    private User user;
    private View view;
    private byte[] imageContent;
    private String categoryChoice = null;
    private static final int PICK_IMAGE_REQUEST = 2;
    private ToastManager mToastManager;
    private NumberPicker numberPicker;

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

        mToastManager = new ToastManager(getContext());
        Dialog dialog = new Dialog(getContext());

        numberPicker = view.findViewById(R.id.numberPicker);

        // Impostazione dei limiti del NumberPicker
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(24);

        /*Button timeButton = view.findViewById(R.id.time_button);
        timeButton.setText("01:00");
        timeButton.setOnClickListener(v -> {
            TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
                int hour = Math.max(hourOfDay, 1);
                String selectedTime = String.format(Locale.getDefault(), "%02d:00:00", hour);
                timeButton.setText(selectedTime);
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    timeSetListener,
                    1,
                    0,
                    true  // Imposta true se desideri visualizzare il picker in modalità 24 ore
            );
            timePickerDialog.show();
        }); */

        // Retrieve from strings.xml (list of options)
        Spinner categorySpinner = view.findViewById(R.id.spinner);

        String[] arrayItems = getResources().getStringArray(R.array.categories_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.custom_spinner_item, arrayItems);
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

        Button createAuctionBtn = view.findViewById(R.id.next_button);
        createAuctionBtn.setOnClickListener(v -> {
            EditText nameTextField    = view.findViewById(R.id.editTextTextPersonName);
            EditText descriptionField = view.findViewById(R.id.description_field);
            EditText basePrizeField   = view.findViewById(R.id.editTextTextPersonName2);

            String itemName           = String.valueOf(nameTextField.getText());
            String itemBasePrize      = String.valueOf(basePrizeField.getText());
            String itemDescription    = String.valueOf(descriptionField.getText());
            String expirationTime     = String.valueOf(numberPicker.getValue());
            String itemCategory       = getCategoryChoice();

            if (itemName.isEmpty() || itemCategory.isEmpty() || itemCategory.equals("Scegli una categoria") || itemBasePrize.isEmpty()
                    || expirationTime.isEmpty() || getImageContent() == null || itemDescription.isEmpty()) {
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

                // Send to Server the Item's related image (its a byte[])
                float finalItemStartPrize = itemStartPrize;
                controller.sendItemImageContent(getImageContent(), new ImageContentRegisterCallback() {
                    @Override
                    public boolean onReceptionSuccess(byte[] itemImageContent) {
                        ItemDTO requestedItem = new ItemDTO();
                        requestedItem.setUser(user);
                        requestedItem.setName(itemName);
                        requestedItem.setCategory(itemCategory);
                        requestedItem.setDescription(itemDescription);
                        requestedItem.setBasePrize(finalItemStartPrize);

                        final Integer[] savedItemId = new Integer[1];

                        // Send to Server the Item's registration Request
                        controller.sendRegisterItemRequest(requestedItem, new RegisterItemCallback() {
                            @Override
                            public boolean onItemRegistrationSuccess(Integer itemId) {
                                savedItemId[0] = itemId;

                                // Item and Auction registration must happen one after the other (that is why we do it like that)
                                registerAuction(savedItemId[0], user.getUserId(), expirationTime, finalItemStartPrize, retrofitService);
                                return true;
                            }
                            @Override
                            public boolean onItemRegistrationFailure(String errorMessage) { return false; }
                        });

                        return true;
                    }
                    @Override
                    public boolean onReceptionFailure(String errorMessage) { return false; }
                });
            }
        });

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

    private void registerAuction(Integer itemId, Integer userId, String expirationTime, float itemStartPrize, Retrofit retrofitService) {
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setOwnerId(userId);
        auctionDTO.setAuctionType(Type.ENGLISH);
        auctionDTO.setActive(true);
        auctionDTO.setExpirationDate(null); // NULL because its an English Auction

        /*String[] parts = expirationTime.split(":");
        int hours   = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);*/
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusHours(Integer.parseInt(expirationTime));
        auctionDTO.setExpirationTime(endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        auctionDTO.setAmountOfTimeToReset(Integer.parseInt(expirationTime));
        auctionDTO.setRequestedItemId(itemId);
        auctionDTO.setCurrentOfferValue(itemStartPrize);

        // Send to Server the Auction's registration Request
        new AuctionController(retrofitService).sendRegisterAuctionRequest(auctionDTO, new RegisterAuctionCallback() {
            @Override
            public boolean onAuctionRegistrationSuccess(AuctionDTO auctionDTO) {
                Log.i("AUCTION REGISTRATION REQUEST", "SENT");
                mToastManager.showToast("Asta creata con successo!");

                ActivityPresenter factory = new ActivityPresenter();
                Intent intent = factory.createIntentForHome(getContext(), user); // Return Home with loggedIn user's informations
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onAuctionRegistrationFailure(String errorMessage) {
                Log.i("AUCTION REGISTRATION REQUEST", "FAILED TO SEND");

                Dialog dialog = new Dialog(getContext());
                dialog.showAlertDialog("AUCTION ERROR", "Non è stato possibile registrare la creazione dell'asta.");
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

    private void setImageContent(byte[] content) {
        this.imageContent = content;
    }

    private byte[] getImageContent() {
        return imageContent;
    }
}