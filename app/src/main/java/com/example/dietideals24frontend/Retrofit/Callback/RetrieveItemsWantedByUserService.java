package com.example.dietideals24frontend.Retrofit.Callback;

import java.util.List;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

public interface RetrieveItemsWantedByUserService {
    boolean onItemsFoundWithSuccess(List<ItemDTO> items);
    boolean onItemsNotFoundFailure(String errorMessage);
}