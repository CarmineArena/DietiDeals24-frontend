package com.example.dietideals24frontend.view;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.modelDTO.User;

import java.util.Calendar;
import java.util.Locale;


public class SilentAuctionCreationFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_silent_auction_creation, container, false);

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
        final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Formatta l'orario come desideri (ad esempio, in un formato HH:mm)
                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

                // Puoi utilizzare selectedTime come necessario
                timeButton.setText(selectedTime);
            }
        };
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int currentHour = c.get(Calendar.HOUR_OF_DAY);
                int currentMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        requireContext(),
                        timeSetListener,
                        currentHour,
                        currentMinute,
                        true  // Imposta true se desideri visualizzare il picker in modalità 24 ore
                );

                timePickerDialog.show();
            }
        });

        Button yourButton = view.findViewById(R.id.next_button);
        /*yourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen();
            }
        });*/


        return view;
    }

    private void changeScreen() {
        // Crea un'Intent per avviare la tua nuova attività
        Intent intent = new Intent(getActivity(), Home.class); //cambia home.class con la classe successiva

        // Avvia la nuova attività
        startActivity(intent);

        // Chiamando finish() puoi chiudere l'attuale attività se necessario
        requireActivity().finish();
    }
}