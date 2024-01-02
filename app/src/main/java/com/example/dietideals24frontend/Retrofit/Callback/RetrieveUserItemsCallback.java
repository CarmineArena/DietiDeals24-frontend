package com.example.dietideals24frontend.Retrofit.Callback;

import java.util.List;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

public interface RetrieveUserItemsCallback {
    boolean onSearchCreatedByUserItemsSuccess(List<ItemDTO> items);
    boolean onSearchCreatedByUserItemsFailure(String errorMessage);
}