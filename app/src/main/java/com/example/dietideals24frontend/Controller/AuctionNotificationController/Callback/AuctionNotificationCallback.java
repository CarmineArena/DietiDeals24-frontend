package com.example.dietideals24frontend.Controller.AuctionNotificationController.Callback;

import java.util.List;

public interface AuctionNotificationCallback {
    void onNotificationsReceived(List<String> notifications);
    void onApiError(String errorMessage);
}