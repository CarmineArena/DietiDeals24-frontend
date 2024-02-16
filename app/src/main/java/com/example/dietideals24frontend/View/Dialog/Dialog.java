package com.example.dietideals24frontend.View.Dialog;

import android.app.AlertDialog;
import android.content.Context;

import android.os.Looper;
import android.os.Handler;

public class Dialog {
    private final Context context;
    private final Handler handler;

    public Dialog(Context context) {
        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        this.handler.post(() -> {
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}