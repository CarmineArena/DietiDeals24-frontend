package com.example.dietideals24frontend.View;

import android.os.Bundle;
import android.content.Context;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;

import android.view.*;
import android.widget.*;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Retrofit.Service.Requester;
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

        Context context = getContext();
        retrofitService = MainActivity.retrofitService;
        Requester requester = new Requester(retrofitService);

        /*LinearLayout verticalLayout = new LinearLayout(context);
        verticalLayout.setOrientation(LinearLayout.VERTICAL);

        /* [START] SCROLL VIEW CREATION
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        verticalLayout.removeAllViews();
        /* [END] SCROLL VIEW

        LinearLayoutForItemsPresenter presenter = new LinearLayoutForItemsPresenter(context, requester, getFragmentManager());
        for (int i = 1; i <= 3; i++) {
            TextView textView = new TextView(context);
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            if (i == 1) {
                textView.setText("Aste in evidenza");
                presenter.createFeaturedItemsLinearLayout(layout, loggedInUser);
            } else if (i == 2) {
                textView.setText("Le tue aste");
                presenter.createAuctionedByUserItemsLinearLayout(layout, loggedInUser);
            } else {
                textView.setText("Aste a cui partecipi");
                presenter.createItemsWantedByUserLinearLayout(layout, loggedInUser);
            }

            textView.setGravity(Gravity.START);
            horizontalScrollView.addView(layout);
            verticalLayout.addView(textView);
            verticalLayout.addView(horizontalScrollView);
        }
        scrollView.addView(verticalLayout);*/
        LinearLayoutForItemsPresenter presenter = new LinearLayoutForItemsPresenter(context, requester, getFragmentManager());
        LinearLayout evidenceLayout = view.findViewById(R.id.evidenceAuction);
        presenter.createFeaturedItemsLinearLayout(evidenceLayout, loggedInUser);
        LinearLayout createdLayout = view.findViewById(R.id.createdAuction);
        presenter.createAuctionedByUserItemsLinearLayout(createdLayout, loggedInUser);
        LinearLayout joinedLayout = view.findViewById(R.id.joinedAuction);
        presenter.createItemsWantedByUserLinearLayout(joinedLayout, loggedInUser);

        return view;
    }
}