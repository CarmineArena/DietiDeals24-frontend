package com.example.dietideals24frontend.Retrofit.Callback;

import java.util.List;

public interface AuctionNotificationCallback {
    void onNotificationsReceived(List<String> notifications);
    void onApiError();
}