package com.example.dietideals24frontend.Controller.ItemController.Callback;

import java.util.List;
import com.example.dietideals24frontend.Model.DTO.ItemDTO;

public interface RetrieveItemsWonByUserCallback {
    boolean onItemsWonByUserRetrievalSuccess(List<ItemDTO> items);
    boolean onItemsWonByUserRetrievalFailure(String errorMessage);
}