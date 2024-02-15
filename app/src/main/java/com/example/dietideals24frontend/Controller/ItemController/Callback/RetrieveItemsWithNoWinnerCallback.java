package com.example.dietideals24frontend.Controller.ItemController.Callback;

import java.util.List;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

public interface RetrieveItemsWithNoWinnerCallback {
    boolean onItemsWithNoWinnerRetrievalSuccess(List<ItemDTO> items);
    boolean onItemsWithNoWinnerRetrievalFailure(String errorMessage);
}