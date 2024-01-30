package com.example.dietideals24frontend.View.Fragment;

import android.os.Bundle;
import android.app.TimePickerDialog;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;

import java.util.Locale;
import java.util.Calendar;

// TODO: LAVORARE A QUESTO FRAGMENT
public class EnglishAuctionCreationFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_english_auction_creation, container, false);

        Bundle bundle = getArguments();
        User user = null;
        if (bundle != null) {
            user = (User) bundle.getSerializable("loggedInUser");
        }


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

        return view;
    }
}