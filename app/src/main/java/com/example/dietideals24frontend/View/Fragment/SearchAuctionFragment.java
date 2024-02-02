package com.example.dietideals24frontend.View.Fragment;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.KeyEvent;
import android.text.TextUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.RelativeLayout;
import android.view.inputmethod.EditorInfo;

import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;
import com.example.dietideals24frontend.View.Dialog.Dialog;
import com.example.dietideals24frontend.Controller.ItemController.ItemController;
import com.example.dietideals24frontend.Presenter.LinearLayoutForItemsPresenter;
import com.example.dietideals24frontend.Controller.ItemController.Callback.RetrieveFeaturedItemsCallback;

import java.util.List;
import retrofit2.Retrofit;
import java.util.ArrayList;

public class SearchAuctionFragment extends Fragment {
    private View view;
    private TextView selectedCategoryTextView;
    private boolean[] checkedCategories;
    private List<String> selectedCategories;
    private User loggedInUser;
    private Dialog dialog;
    private Retrofit retrofitService;
    private EditText searchTextField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_auction, container, false);

        retrofitService = MainActivity.retrofitService;

        Bundle bundle = getArguments();
        if (bundle != null) {
            loggedInUser = (User) bundle.getSerializable("loggedInUser");
        } else {
            loggedInUser = null;
        }

        selectedCategoryTextView = view.findViewById(R.id.selected_category_view);

        Button selectCategoryButton = view.findViewById(R.id.filter_button);
        selectCategoryButton.setOnClickListener(v -> showCategoryDialog());

        searchTextField = view.findViewById(R.id.search_button);
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

        return view;
    }

    private void showCategoryDialog() {
        Resources res = getResources();
        String[] listOfCategories = res.getStringArray(R.array.categories_array);

        // We need to exclude the first element
        String[] categories = new String[listOfCategories.length - 1];
        System.arraycopy(listOfCategories, 1, categories, 0, categories.length);

        checkedCategories = new boolean[categories.length];
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
            Dialog dialog = new Dialog(getContext());
            dialog.showAlertDialog("FORM ERROR", "Devi digitare ciò che vuoi cercare!");
        } else {
            ItemController controller = new ItemController(retrofitService);
            controller.sendFeaturedItemsUpForAuctionBySearchTermAndCategoryRequest(searchTerm, selectedCategories, loggedInUser, new RetrieveFeaturedItemsCallback() {
                @Override
                public boolean onSearchItemsUpForAuctionSuccess(List<ItemDTO> itemsRetrieved) {
                    Context context = getContext();
                    if (itemsRetrieved.isEmpty()) {
                        Log.i("SEARCH SUCCESS BUT FOUND NONE", "LIST SIZE: " + itemsRetrieved.size());
                        Dialog dialog = new Dialog(context);
                        dialog.showAlertDialog("ELEMENTS NOT FOUND", "Non ci sono oggetti all'asta che soddisfano la tua richiesta!");
                        searchTextField.setText("");
                    } else {
                        LinearLayoutForItemsPresenter presenter = new LinearLayoutForItemsPresenter(context, retrofitService, getFragmentManager());

                        ScrollView scrollView = view.findViewById(R.id.scrollView);
                        RelativeLayout layout = new RelativeLayout(context);
                        presenter.createInternalLayoutWithFeaturedAuctions(layout, loggedInUser, searchTerm, selectedCategories);

                        scrollView.removeAllViews();
                        scrollView.addView(layout);
                    }
                    return true;
                }

                @Override
                public boolean onSearchItemsUpForAuctionFailure(String errorMessage) {
                    Log.e("Search Featured List<Items> Failure", errorMessage);
                    Dialog dialog = new Dialog(getContext());
                    dialog.showAlertDialog("ITEM RETRIEVAL ERROR", "Could not retrieve the items requested. " + errorMessage);
                    return false;
                }
            });

            // Once clicked "ENTER" to search, filter categories are resetted (empty list)
            selectedCategoryTextView.setText("Categorie selezionate: ");
        }
    }
}