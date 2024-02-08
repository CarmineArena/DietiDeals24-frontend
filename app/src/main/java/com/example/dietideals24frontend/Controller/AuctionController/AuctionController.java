package com.example.dietideals24frontend.Controller.AuctionController;

import android.util.Log;
import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.Objects;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;
import com.example.dietideals24frontend.Controller.AuctionController.Callback.*;
import com.example.dietideals24frontend.Controller.AuctionController.Retrofit.*;
import com.example.dietideals24frontend.Utility.Exception.UnhandledOptionException;
import com.example.dietideals24frontend.Controller.AuctionController.Interface.AuctionRequestInterface;

public class AuctionController implements AuctionRequestInterface {
    private final Retrofit retrofitService;

    public AuctionController(Retrofit retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public void sendItemImageContent(byte[] itemImageContent, final ImageContentRegisterCallback callback) {
        AddItemService api = retrofitService.create(AddItemService.class);
        Call<Void> call = api.sendItemImageContent(itemImageContent);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onReceptionSuccess(itemImageContent);
                } else {
                    returnValue = callback.onReceptionFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Send Item Image Content", "Item Image content sent correctly!");
                } else {
                    Log.e("Send Item Image Content Error", "Could not send byte[] content. Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                boolean returnValue = callback.onReceptionFailure(t.getMessage());
                Log.e("Send Item Image Content Error", "Could not send byte[] content. Server error: " + Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public void sendRegisterItemRequest(ItemDTO requestedItem, final RegisterItemCallback callback) {
        AddItemService api = retrofitService.create(AddItemService.class);

        Call<Integer> call = api.registerItem(requestedItem);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    Integer itemId = response.body();
                    returnValue = callback.onItemRegistrationSuccess(itemId);
                } else {
                    returnValue = callback.onItemRegistrationFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Add RequestedItem Procedure", "Item registered correctly!");
                } else {
                    Log.e("Add RequestedItem Procedure Error", "Could not register the Item provided. Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                boolean returnValue = callback.onItemRegistrationFailure(t.getMessage());
                Log.e("Add RequestedItem Procedure Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendRegisterAuctionRequest(AuctionDTO auctionDTO, final RegisterAuctionCallback callback) {
        AddAuctionService api = retrofitService.create(AddAuctionService.class);

        Call<Void> call = api.registerAuction(auctionDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onAuctionRegistrationSuccess(auctionDTO);
                } else {
                    returnValue = callback.onAuctionRegistrationFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Add RequestedAuction Procedure", "Auction registered correctly!");
                } else {
                    Log.e("Add RequestedAuction Procedure Error", "Could not register the Auction provided. Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                boolean returnValue = callback.onAuctionRegistrationFailure(t.getMessage());
                Log.e("Add RequestedAuction Procedure Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendFindAuctionRequest(Integer itemId, String name, String description, final RetrieveAuctionCallback callback) throws UnhandledOptionException {
        FindAuctionService api = retrofitService.create(FindAuctionService.class);

        Call<AuctionDTO> call = api.searchAuction(itemId, name, description);
        call.enqueue(new Callback<AuctionDTO>() {
            @Override
            public void onResponse(@NonNull Call<AuctionDTO> call, @NonNull Response<AuctionDTO> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    try {
                        returnValue = callback.onRetrieveAuctionSuccess(response.body());
                    } catch (UnhandledOptionException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    returnValue = callback.onRetrieveAuctionFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Search Auction", "Auction retrieved correctly!");
                } else {
                    Log.e("Search Auction Error", "Could not find the auction requested. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuctionDTO> call, @NonNull Throwable t) {
                boolean returnValue = callback.onRetrieveAuctionFailure(t.getMessage());
                Log.e("Search Auction Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendCloseAuctionRequest(Integer auctionId, final CloseAuctionCallback callback) {
        CloseAuctionService api = retrofitService.create(CloseAuctionService.class);

        Call<Void> call = api.closeAuction(auctionId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("Close Auction", "Auction closed correctly!");
                    callback.onCloseSuccess();
                } else {
                    Log.e("Close Auction Error", "Could not close auction!");
                    callback.onCloseFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                boolean returnValue = callback.onCloseFailure(t.getMessage());
                Log.e("Close Auction Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }
}