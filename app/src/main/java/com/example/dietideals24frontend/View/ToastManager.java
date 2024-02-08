package com.example.dietideals24frontend.View;

import android.content.Context;
import android.widget.Toast;

public class ToastManager {

    private Context mContext;

    public ToastManager(Context context) {
        this.mContext = context;
    }

    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}

