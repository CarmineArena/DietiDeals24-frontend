package com.example.dietideals24frontend.utility;

import android.net.Uri;
import android.content.Context;
import android.content.ContentResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

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
}