package com.example.dietideals24frontend.Utility;

import android.net.Uri;
import android.content.Context;
import android.content.ContentResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.graphics.BitmapFactory;

public class ImageUtils {
    public static byte[] convertUriToByteArray(Context context, Uri uri) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(uri);

        if (inputStream != null) {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int bytesRead;

            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, bytesRead);
                }
                return byteBuffer.toByteArray();
            } finally {
                inputStream.close();
            }
        }
        return null;
    }

    public static Bitmap getImageBitMap(byte[] imageContent) {
        return BitmapFactory.decodeByteArray(imageContent, 0, imageContent.length);
    }

    public static void fillImageView(ImageView imageView, byte[] imageContent) {
        imageView.setImageBitmap(ImageUtils.getImageBitMap(imageContent));
    }
}