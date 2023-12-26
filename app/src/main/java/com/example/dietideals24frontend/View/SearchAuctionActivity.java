package com.example.dietideals24frontend.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dietideals24frontend.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAuctionActivity extends AppCompatActivity {

    private TextView selectedCategoryTextView;
    private boolean[] checkedCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_auction);

        selectedCategoryTextView = findViewById(R.id.selected_category_view);

        Button selectCategoryButton = findViewById(R.id.filter_button);
        selectCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });

    }

    private void showCategoryDialog() {
        final String[] categories = {"Categoria 1", "Categoria 2", "Categoria 3"};

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleziona una categoria")
                .setItems(categories, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Azione da eseguire quando l'utente seleziona una categoria
                        String selectedCategory = categories[which];
                        // Puoi fare qualcosa con la categoria selezionata, ad esempio, visualizzarla in un Toast
                        // Toast.makeText(MainActivity.this, "Hai selezionato: " + selectedCategory, Toast.LENGTH_SHORT).show();
                        // Oppure puoi fare altro a seconda della categoria selezionata
                        selectedCategoryTextView.setText("Categoria selezionata: " + selectedCategory);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();*/
        checkedCategories = new boolean[categories.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleziona categorie")
                .setMultiChoiceItems(categories, checkedCategories, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // Aggiorna lo stato della categoria quando l'utente seleziona/deseleziona una casella di controllo
                        checkedCategories[which] = isChecked;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Azione da eseguire quando l'utente preme il pulsante OK
                        updateSelectedCategoriesTextView(categories);
                    }
                })
                .setNegativeButton("Annulla", null); // Azione da eseguire quando l'utente preme il pulsante Annulla

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateSelectedCategoriesTextView(String[] categories) {
        List<String> selectedCategoriesList = new ArrayList<>();

        for (int i = 0; i < checkedCategories.length; i++) {
            if (checkedCategories[i]) {
                selectedCategoriesList.add(categories[i]);
            }
        }

        String selectedCategories = TextUtils.join(", ", selectedCategoriesList);

        // Aggiorna il testo del TextView con le categorie selezionate
        selectedCategoryTextView.setText("Categorie selezionate: " + selectedCategories);
    }
}