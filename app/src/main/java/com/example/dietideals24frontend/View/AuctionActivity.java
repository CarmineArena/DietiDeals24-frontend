package com.example.dietideals24frontend.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.content.Intent;

import retrofit2.Retrofit;

import com.example.dietideals24frontend.Presenter.FragmentPresenter;
import com.example.dietideals24frontend.Utility.Exception.UnhandledOptionException;

import com.example.dietideals24frontend.R;
import com.example.dietideals24frontend.Model.Item;
import com.example.dietideals24frontend.MainActivity;
import com.example.dietideals24frontend.Model.Auction;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;
import com.example.dietideals24frontend.Retrofit.Service.Requester;
import com.example.dietideals24frontend.Retrofit.Callback.RetrieveAuctionCallback;

public class AuctionActivity extends AppCompatActivity {
    private Item item;
    private Retrofit retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("selectedItem");

        retrofitService = MainActivity.retrofitService;
        Requester requester = new Requester(retrofitService);
        try {
            requester.sendFindAuctionRequest(item.getItemId(), item.getName(), item.getDescription(), new RetrieveAuctionCallback() {
                @Override
                public boolean onRetrieveAuctionSuccess(AuctionDTO retrievedAuction) throws UnhandledOptionException {
                    Auction auction = new Auction(retrievedAuction, item);

                    String auctionType = String.valueOf(retrievedAuction.getAuctionType());
                    FragmentPresenter presenter = new FragmentPresenter();

                    switch (auctionType) {
                        case "SILENT":
                            SilentAuctionFragment fragment = presenter.createSilenAuctionFragment(auction);
                            replaceFragment(fragment);
                            break;
                        case "ENGLISH":
                            // TODO: FALLA CREARE AL FRAGMENT_PRESENTER
                            break;
                        default:
                            throw new UnhandledOptionException("onRetrieveAuctionSucces: auction type not found, value: " + auctionType);
                    }

                    return true;
                }

                @Override
                public boolean onRetrieveAuctionFailure(String errorMessage) {
                    // TODO: COSA FACCIO IN QUESTO CASO? MOSTRO UN DIALOG? (NON DOVREMMO AVERE ERRORI CLASSE 400 o 500)
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
        fragmentTransaction.replace(R.id.fragmentAuction,fragment);
        fragmentTransaction.commit();
    }
}