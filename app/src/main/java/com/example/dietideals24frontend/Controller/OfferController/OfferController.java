package com.example.dietideals24frontend.Controller.OfferController;

import java.util.List;
import android.util.Log;
import java.util.Objects;
import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.dietideals24frontend.Model.DTO.OfferDTO;
import com.example.dietideals24frontend.Controller.OfferController.Callback.*;
import com.example.dietideals24frontend.Controller.OfferController.Retrofit.*;
import com.example.dietideals24frontend.Controller.OfferController.Interface.OfferRequestInterface;

public class OfferController implements OfferRequestInterface {
    private final Retrofit retrofitService;

    public OfferController(Retrofit retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public void sendFindBestOfferRequest(Integer itemId, Integer auctionId, final RetrieveBestOfferCallback callback) {
        RequestBestOfferService api = retrofitService.create(RequestBestOfferService.class);

        Call<OfferDTO> call = api.getBestOffer(itemId, auctionId);
        call.enqueue(new Callback<OfferDTO>() {
            @Override
            public void onResponse(@NonNull Call<OfferDTO> call, @NonNull Response<OfferDTO> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onBestOfferRetrievalSuccess(response.body());
                } else {
                    returnValue = callback.onBestOfferRetrievalFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Search Best Offer", "Best Offer retrieved correctly!");
                } else {
                    Log.e("Search Best Offer Error", "Could not find the offer requested. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<OfferDTO> call, @NonNull Throwable t) {
                boolean returnValue = callback.onBestOfferRetrievalFailure(t.getMessage());
                Log.e("Search Best Offer Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendRegisterOfferRequest(OfferDTO offerDTO, final RegisterOfferCallback callback) {
        OfferRegistrationService api = retrofitService.create(OfferRegistrationService.class);

        Call<Void> call = api.saveOffer(offerDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onOfferRegistrationSuccess();
                } else {
                    returnValue = callback.onOfferRegistrationFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Register Offer", "Offer registered correctly!");
                } else {
                    Log.e("Register Offer Error", "Could not find terminate offer validation process. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                boolean returnValue = callback.onOfferRegistrationFailure(t.getMessage());
                Log.e("Register Offer Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendGetOffersRequest(Integer itemId, Integer auctionId, final RetrieveOffersCallback callback) {
        RequestOffersService api = retrofitService.create(RequestOffersService.class);

        Call<List<OfferDTO>> call = api.getOffers(itemId, auctionId);
        call.enqueue(new Callback<List<OfferDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<OfferDTO>> call, @NonNull Response<List<OfferDTO>> response) {
                if (response.isSuccessful()) {
                    Log.i("Retrieve Offers", "List<Offers> retrieved correctly!");
                    callback.onRetrieveOffersSuccess(response.body());
                } else {
                    Log.e("Retrieve Offers Error", "Could not find List<Offers>. Error code: " + response.code());
                    callback.onRetrieveOffersFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OfferDTO>> call, @NonNull Throwable t) {
                boolean returnValue = callback.onRetrieveOffersFailure(t.getMessage());
                Log.e("Retrieve Offers Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendGetOffersEndedAuctionRequest(Integer itemId, final RetrieveOffersCallback callback) {
        RequestOffersAuctionEndedService service = retrofitService.create(RequestOffersAuctionEndedService.class);

        Call<List<OfferDTO>> call = service.getOfferList(itemId);
        call.enqueue(new Callback<List<OfferDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<OfferDTO>> call, @NonNull Response<List<OfferDTO>> response) {
                if (response.isSuccessful()) {
                    Log.i("Retrieve Offers Auction Ended", "List<Offers> retrieved correctly!");
                    callback.onRetrieveOffersSuccess(response.body());
                } else {
                    Log.e("Retrieve Offers Auction Ended Error", "Could not find List<Offers>. Error code: " + response.code());
                    callback.onRetrieveOffersFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OfferDTO>> call, @NonNull Throwable t) {
                boolean returnValue = callback.onRetrieveOffersFailure(t.getMessage());
                Log.e("Retrieve Offers Auction Ended Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }
}