package com.example.dietideals24frontend.Controller.ItemController.Callback;

import java.util.List;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

public interface RetrieveItemsWantedByUserCallback {
    boolean onItemsFoundWithSuccess(List<ItemDTO> items);
    boolean onItemsNotFoundFailure(String errorMessage);
}