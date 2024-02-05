package com.example.dietideals24frontend.View.Fragment;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Paint;
import android.content.Context;
import android.annotation.SuppressLint;

import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Presenter.ActivityPresenter;
import com.example.dietideals24frontend.Presenter.FragmentPresenter;
import com.example.dietideals24frontend.Presenter.LinearLayoutForItemsPresenter;

public class HomeFragment extends Fragment {
    private View view;
    private User loggedInUser;
    private Retrofit retrofitService;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            loggedInUser = (User) bundle.getSerializable("loggedInUser");
        } else {
            loggedInUser = null;
        }

        TextView welcomeView = view.findViewById(R.id.welcomeView);
        welcomeView.setText("Benvenuto " + loggedInUser.getName() + " " + loggedInUser.getSurname());

        Context context = getContext();
        retrofitService = MainActivity.retrofitService;

        Button btnAuction = view.findViewById(R.id.allAuctionButton);
        btnAuction.setPaintFlags(btnAuction.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnAuction.setOnClickListener(v -> {
            Intent intent = new ActivityPresenter().createAuctionListIntent(getContext(), loggedInUser);
            startActivity(intent);
            getActivity().finish();
        });

        Button btnSearch = view.findViewById(R.id.searchButton);
        btnSearch.setOnClickListener(v -> {
            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.frameGeneral, new FragmentPresenter().createSearchAuctionFragment(loggedInUser));
            fr.commit();
        });

        LinearLayoutForItemsPresenter presenter = new LinearLayoutForItemsPresenter(context, retrofitService, getFragmentManager());

        LinearLayout evidenceLayout = view.findViewById(R.id.evidenceAuction);
        presenter.createFeaturedItemsLinearLayout(evidenceLayout, loggedInUser);

        LinearLayout createdLayout = view.findViewById(R.id.createdAuction);
        presenter.createAuctionedByUserItemsLayout(createdLayout, loggedInUser);

        LinearLayout joinedLayout = view.findViewById(R.id.joinedAuction);
        presenter.createItemsWantedByUserLayout(joinedLayout, loggedInUser);

        return view;
    }
}