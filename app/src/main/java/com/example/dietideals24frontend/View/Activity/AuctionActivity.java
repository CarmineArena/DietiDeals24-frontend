package com.example.dietideals24frontend.View.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;

import retrofit2.Retrofit;
import com.example.dietideals24frontend.Presenter.FragmentPresenter;
import com.example.dietideals24frontend.Utility.Exception.UnhandledOptionException;
import com.example.dietideals24frontend.Controller.AuctionController.AuctionController;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.Auction;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;
import com.example.dietideals24frontend.View.Fragment.SilentAuctionFragment;
import com.example.dietideals24frontend.View.Fragment.EnglishAuctionFragment;
import com.example.dietideals24frontend.Controller.AuctionController.Callback.RetrieveAuctionCallback;

public class AuctionActivity extends AppCompatActivity {
    private Item item;
    private User loggedInUser;
    private Retrofit retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        Intent intent = getIntent();
        item         = (Item) intent.getSerializableExtra("selectedItem");
        loggedInUser = (User) intent.getSerializableExtra("loggedInUser");
        boolean hasAuctionEnded = (boolean) intent.getSerializableExtra("hasAuctionEnded");

        retrofitService = MainActivity.retrofitService;
        try {
            new AuctionController(retrofitService).sendFindAuctionRequest(item.getItemId(), item.getName(), item.getDescription(), new RetrieveAuctionCallback() {
                @Override
                public boolean onRetrieveAuctionSuccess(AuctionDTO retrievedAuction) throws UnhandledOptionException {
                    Auction auction = new Auction(retrievedAuction, item);

                    String auctionType = String.valueOf(retrievedAuction.getAuctionType());
                    FragmentPresenter presenter = new FragmentPresenter();

                    switch (auctionType) {
                        case "SILENT":
                            SilentAuctionFragment fragment = presenter.createSilenAuctionFragment(loggedInUser, auction, hasAuctionEnded);
                            replaceFragment(fragment);
                            break;
                        case "ENGLISH":
                            EnglishAuctionFragment fragment2 = presenter.createEnglishAuctionFragment(loggedInUser, auction);
                            replaceFragment(fragment2);
                            break;
                        default:
                            throw new UnhandledOptionException("onRetrieveAuctionSucces: auction type not found, value: " + auctionType);
                    }

                    return true;
                }

                // NO ERRORS SHOULD HAPPEN HERE (THE ITEM AND THE AUCTION BOTH EXIST)
                @Override
                public boolean onRetrieveAuctionFailure(String errorMessage) {
                    Log.e("Auction Activity", "Could not retrieve auction informations. Error: " + errorMessage);
                    return false;
                }
            });
        } catch (UnhandledOptionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentAuction, fragment);
        fragmentTransaction.commit();
    }
}