package com.example.dietideals24frontend.View.Dialog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dietideals24frontend.R;

public class SearchAuctionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_search_auction, container, false);
        return view;
    }
}