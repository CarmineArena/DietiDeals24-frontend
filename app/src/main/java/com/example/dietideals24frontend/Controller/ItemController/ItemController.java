package com.example.dietideals24frontend.Controller.ItemController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.dietideals24frontend.Model.User;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

import com.example.dietideals24frontend.Controller.ItemController.Callback.*;
import com.example.dietideals24frontend.Controller.ItemController.Retrofit.*;
import com.example.dietideals24frontend.Controller.ItemController.Interface.ItemRequestInterface;

import java.util.List;
import java.util.Objects;

public class ItemController implements ItemRequestInterface {
    private final Retrofit retrofitService;

    public ItemController(Retrofit retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public void sendFeaturedItemsUpForAuctionRequest(Integer userId, String email, final RetrieveFeaturedItemsCallback callback) {
        SearchItemUpForFeaturedAuctionService api = retrofitService.create(SearchItemUpForFeaturedAuctionService.class);

        Call<List<ItemDTO>> call = api.searchFeaturedItems(userId, email);
        call.enqueue(new Callback<List<ItemDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemDTO>> call, @NonNull Response<List<ItemDTO>> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onSearchItemsUpForAuctionSuccess(response.body());
                } else {
                    returnValue = callback.onSearchItemsUpForAuctionFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Search Featured Items", "List<Items> retrieved correctly!");
                } else {
                    Log.e("Search Featured Items Error", "Could not find the items requested. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemDTO>> call, @NonNull Throwable t) {
                boolean returnValue = callback.onSearchItemsUpForAuctionFailure(t.getMessage());
                Log.e("Search Featured Items Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendFeaturedItemsUpForAuctionBySearchTermAndCategoryRequest(String searchTerm, List<String> categories, User user, final RetrieveFeaturedItemsCallback callback) {
        SearchItemUpForFeaturedAuctionService api = retrofitService.create(SearchItemUpForFeaturedAuctionService.class);

        Call<List<ItemDTO>> call = api.searchFeaturedItemsBySearchTermAndCategory(searchTerm, categories, user.getUserId());
        call.enqueue(new Callback<List<ItemDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemDTO>> call, @NonNull Response<List<ItemDTO>> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onSearchItemsUpForAuctionSuccess(response.body());
                } else {
                    returnValue = callback.onSearchItemsUpForAuctionFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Search Featured Items", "List<Items> retrieved correctly!");
                } else {
                    Log.e("Search Featured Items Error", "Could not find the items requested. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemDTO>> call, @NonNull Throwable t) {
                boolean returnValue = callback.onSearchItemsUpForAuctionFailure(t.getMessage());
                Log.e("Search Featured Items Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendCreatedByUserItemsRequest(User loggedInUser, final RetrieveUserItemsCallback callback) {
        SearchItemsCreatedByUserService api = retrofitService.create(SearchItemsCreatedByUserService.class);

        Call<List<ItemDTO>> call = api.searchCreatedByUserItems(loggedInUser.getUserId(), loggedInUser.getEmail());
        call.enqueue(new Callback<List<ItemDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemDTO>> call, @NonNull Response<List<ItemDTO>> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onSearchCreatedByUserItemsSuccess(response.body());
                } else {
                    returnValue = callback.onSearchCreatedByUserItemsFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Search Items", "List<Items> retrieved correctly!");
                } else {
                    Log.e("Search Items Error", "Could not find the items requested. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemDTO>> call, @NonNull Throwable t) {
                boolean returnValue = callback.onSearchCreatedByUserItemsFailure(t.getMessage());
                Log.e("Search Items Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendFindItemsWantedByUserRequest(Integer userId, String email, String password, final RetrieveItemsWantedByUserCallback callback) {
        SearchItemsWantedByUserService api = retrofitService.create(SearchItemsWantedByUserService.class);

        Call<List<ItemDTO>> call = api.findItemsForUser(userId, email, password);
        call.enqueue(new Callback<List<ItemDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemDTO>> call, @NonNull Response<List<ItemDTO>> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onItemsFoundWithSuccess(response.body());
                } else {
                    returnValue = callback.onItemsNotFoundFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Search Partecipate Items", "List<Items> retrieved correctly!");
                } else {
                    Log.e("Search Partecipate Items Error", "Could not find the items requested. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemDTO>> call, @NonNull Throwable t) {
                boolean returnValue = callback.onItemsNotFoundFailure(t.getMessage());
                Log.e("Search Parecipate Items Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendFindItemImageRequest(Integer itemId, String name, final ImageContentRequestCallback callback) {
        RequestItemImageService api = retrofitService.create(RequestItemImageService.class);

        Call<byte[]> call = api.fetchItemImage(itemId, name);
        call.enqueue(new Callback<byte[]>() {
            @Override
            public void onResponse(@NonNull Call<byte[]> call, @NonNull Response<byte[]> response) {
                boolean returnValue;
                if(response.isSuccessful()) {
                    returnValue = callback.onFetchSuccess(response.body());
                } else {
                    returnValue = callback.onFetchFailure(response.message());
                }

                if (returnValue) {
                    Log.i("Search Item Image", "Image retrieved correctly!");
                } else {
                    Log.e("Search Item Image Error", "Could not find the image requested. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<byte[]> call, @NonNull Throwable t) {
                boolean returnValue = callback.onFetchFailure(t.getMessage());
                Log.e("Search Item Image Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendFindItemsWithNoWinnerRequest(Integer userId, final RetrieveItemsWithNoWinnerCallback callback) {
        SearchItemsWithNoWinnerService service = retrofitService.create(SearchItemsWithNoWinnerService.class);

        Call<List<ItemDTO>> call = service.searchItemsWithNoWinner(userId);
        call.enqueue(new Callback<List<ItemDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemDTO>> call, @NonNull Response<List<ItemDTO>> response) {
                if(response.isSuccessful()) {
                    Log.i("Search Items With No Winner", "Items retrieved correctly!");
                    callback.onItemsWithNoWinnerRetrievalSuccess(response.body());
                } else {
                    Log.e("Search Items With No Winner Error", "Could not the list of items requested. Error code: " + response.code());
                    callback.onItemsWithNoWinnerRetrievalFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemDTO>> call, @NonNull Throwable t) {
                boolean returnValue = callback.onItemsWithNoWinnerRetrievalFailure(t.getMessage());
                Log.e("Search Items With No Winner Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void sendFindItemsWonByUserRequest(Integer userId, final RetrieveItemsWonByUserCallback callback) {
        SearchItemsWonByUserService service = retrofitService.create(SearchItemsWonByUserService.class);

        Call<List<ItemDTO>> call = service.findItemsWonByUser(userId);
        call.enqueue(new Callback<List<ItemDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemDTO>> call, @NonNull Response<List<ItemDTO>> response) {
                if(response.isSuccessful()) {
                    Log.i("Search Items Won By User", "Items retrieved correctly!");
                    callback.onItemsWonByUserRetrievalSuccess(response.body());
                } else {
                    Log.e("Search Items Won By User Error", "Could not the list of items requested. Error code: " + response.code());
                    callback.onItemsWonByUserRetrievalFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemDTO>> call, @NonNull Throwable t) {
                boolean returnValue = callback.onItemsWonByUserRetrievalFailure(t.getMessage());
                Log.e("Search Items Won By User Error", Objects.requireNonNull(t.getMessage()) + ", Cause: " + t.getCause());
                t.printStackTrace();
            }
        });
    }
}