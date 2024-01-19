package com.example.dietideals24frontend.View;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.utility.ItemUtils;
import com.example.dietideals24frontend.View.Dialog.Dialog;
import com.example.dietideals24frontend.Retrofit.Service.Requester;
import com.example.dietideals24frontend.Retrofit.Callback.ImageCallback;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveFeaturedItemsCallback;

import java.util.List;
import java.util.ArrayList;

import android.content.res.Resources;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

public class SearchAuctionActivity extends AppCompatActivity {
    private TextView selectedCategoryTextView;
    private boolean[] checkedCategories;
    private List<String> selectedCategories;
    private User loggedInUser;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_auction);

        Retrofit retrofitService = MainActivity.retrofitService;

        Intent intent = getIntent();
        loggedInUser = (User) intent.getSerializableExtra("loggedInUser");

        selectedCategoryTextView = findViewById(R.id.selected_category_view);

        Button selectCategoryButton = findViewById(R.id.filter_button);
        selectCategoryButton.setOnClickListener(v -> showCategoryDialog());

        EditText searchTextField = findViewById(R.id.search_field);
        searchTextField.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                            keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                // User pressed "Enter" or clicked on "Done" (keyboard action)
                handleEnterKey(retrofitService, String.valueOf(searchTextField.getText()));
                return true;
            }
            return false;
        });
    }

    private void showCategoryDialog() {
        Resources res = getResources();
        String[] listOfCategories = res.getStringArray(R.array.categories_array);

        // We need to exclude the first element
        String[] categories = new String[listOfCategories.length - 1];
        System.arraycopy(listOfCategories, 1, categories, 0, categories.length);

        checkedCategories = new boolean[categories.length];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleziona categorie")
                .setMultiChoiceItems(categories, checkedCategories, (dialog, which, isChecked) -> checkedCategories[which] = isChecked)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Execute when button "OK" is pressed
                    updateSelectedCategoriesTextView(categories);
                })
                .setNegativeButton("Annulla", null); // Execute when button "ANNULLA" is pressed

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void updateSelectedCategoriesTextView(String[] categories) {
        List<String> selectedCategoriesList = new ArrayList<>();

        for (int i = 0; i < checkedCategories.length; i++) {
            if (checkedCategories[i]) {
                selectedCategoriesList.add(categories[i]);
            }
        }

        String selectedCategories = TextUtils.join(", ", selectedCategoriesList);
        selectedCategoryTextView.setText("Categorie selezionate: " + selectedCategories);
        setSelectedCategories(selectedCategoriesList);
    }

    private void setSelectedCategories(List<String> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    private List<String> getSelectedCategories() {
        return selectedCategories;
    }

    @SuppressLint("SetTextI18n")
    private void handleEnterKey(Retrofit retrofitService, String searchTerm) {
        // Retrieve selected categories
        List<String> selectedCategories = getSelectedCategories();

        if (searchTerm.isEmpty() || searchTerm.isBlank()) {
            Dialog dialog = new Dialog(getApplicationContext());
            dialog.showAlertDialog("FORM ERROR", "Devi digitare ciò che vuoi cercare!");
        } else {
            Requester requester = new Requester(retrofitService);
            requester.sendFeaturedItemsUpForAuctionBySearchTermAndCategoryRequest(searchTerm, selectedCategories, loggedInUser, new RetrieveFeaturedItemsCallback() {
                @Override
                public boolean onSearchItemsUpForAuctionSuccess(List<ItemDTO> itemsRetrieved) {
                    if (itemsRetrieved.isEmpty()) {
                        Log.i("SEARCH SUCCESS BUT FOUND NONE", "LIST SIZE: " + itemsRetrieved.size());
                        // TODO: MOSTRARE NELLA PAGINA "NON CI SONO OGGETTI" O QUALCOSA DI SIMILE
                    } else {
                        // Retrieval of all the Items
                        /*
                        List<Item> items = ItemUtils.createListOfItems(itemsRetrieved);

                        // Now retrieve the images: # of requests = # of items inside the list
                        ItemUtils.assignImageToItem(items, requester, new ImageCallback() { // QUESTA FUNZIONE E' CAMBIATA
                            @Override
                            public void onImageAvailable(byte[] imageContent) {
                                // TODO: COSA CAZZO FACCIO
                            }

                            @Override
                            public void onImageNotAvailable(String errorMessage) {
                                // TODO: COSA CAZZO FACCIO
                            }
                        });
                        Log.i("Fetch Items", "DONE, LIST SIZE: " + items.size()); */

                        // TODO: MOSTRARE GLI ITEM NELLA PAGINA
                        // TODO: STABILIRE POI IN QUALE ALTRA INTERFACCIA PASSARE
                    }
                    return true;
                }

                @Override
                public boolean onSearchItemsUpForAuctionFailure(String errorMessage) {
                    Log.e("Search Featured List<Items> Failure", errorMessage);
                    // TODO: MOSTRARE NELLA PAGINA QUALCOSA CHE FACCIA CAPIRE CHE E' PRESENTE UN PROBLEMA
                    return false;
                }
            });

            // Once clicked "ENTER" to search, filter categories are resetted (empty list)
            selectedCategoryTextView.setText("Categorie selezionate: ");
        }
    }
}