package com.example.dietideals24frontend.Retrofit.Service;

import java.util.List;
import android.util.Log;
import java.util.Objects;
import androidx.annotation.NonNull;

import com.example.dietideals24frontend.Model.*;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;
import com.example.dietideals24frontend.Retrofit.*;
import com.example.dietideals24frontend.Retrofit.Callback.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Requester implements CommunicationInterface {
    private final Retrofit retrofitService;

    public Requester(Retrofit retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public void sendUserRegistrationRequest(User user, final UserRegistrationCallback callback) {
        RegisterUserApiService api = retrofitService.create(RegisterUserApiService.class);
        api.save(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                boolean returnValue;
                if (response.isSuccessful()) {
                    User loggedInUser = response.body();
                    returnValue = callback.onRegistrationSuccess(loggedInUser);
                } else {
                    returnValue = callback.onRegistrationFailure(response.message());
                }

                if (returnValue)
                    Log.i("User Register", "User registered correctly!");
                else
                    Log.e("User Register Error", "Could not register user!");
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                boolean returnValue = callback.onRegistrationFailure(t.getMessage());

                if (!returnValue) {
                    Log.e("User Register Error", "Could not register user!");
                    Log.e("User Register Error", Objects.requireNonNull(t.getMessage()));
                }
            }
        });
    }

    @Override
    public void sendUserLoginRequest(User user, final UserLoginCallback callback) {
        LoginUserApiService api = retrofitService.create(LoginUserApiService.class);

        String email = user.getEmail();
        String passw = user.getPassword();
        api.login(email, passw).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                boolean returnValue;
                if (response.isSuccessful()) {
                    User loggedInUser = response.body();
                    returnValue = callback.onLoginSuccess(loggedInUser);
                } else {
                    returnValue = callback.onLoginFailure(response.message());
                }

                if (returnValue)
                    Log.i("User Login", "User logged in correctly!");
                else
                    Log.e("User Login Error", "Could not login user!");
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                boolean returnValue = callback.onLoginFailure(t.getMessage());

                if (!returnValue) {
                    Log.e("User Login Error", "Could not login user!");
                    Log.e("User Login Error", Objects.requireNonNull(t.getMessage()));
                }
            }
        });
    }

    @Override
    public void sendItemImageContent(byte[] itemImageContent, final ImageContentRegistrationCallback callback) {
        AddItemApiService api = retrofitService.create(AddItemApiService.class);
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
    public void sendRegisterItemRequest(ItemDTO requestedItem, final ItemRegistrationCallback callback) {
        AddItemApiService api = retrofitService.create(AddItemApiService.class);

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
    public void sendRegisterAuctionRequest(AuctionDTO auctionDTO, final AuctionRegistrationCallback callback) {
        AddAuctionApiService api = retrofitService.create(AddAuctionApiService.class);

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
    public void sendFeaturedItemsUpForAuctionRequest(String searchTerm, List<String> categories, User user, final RetrieveFeaturedItemsCallback callback) {
        SearchItemUpForFeaturedAuctionService api = retrofitService.create(SearchItemUpForFeaturedAuctionService.class);

        Call<List<ItemDTO>> call = api.searchFeaturedItems(searchTerm, categories, user.getUserId());
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
    public void sendCreatedByUserItemsRequest(User user, final RetrieveUserItemsCallback callback) {
        SearchItemsCreatedByUserService api = retrofitService.create(SearchItemsCreatedByUserService.class);

        Call<List<ItemDTO>> call = api.searchCreatedByUserItems(user);
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
    public void sendFindItemsForWhichTheUserPartecipateAuction(Integer userId, String email, String password, final RetrieveItemsWantedByUserService callback) {
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
}