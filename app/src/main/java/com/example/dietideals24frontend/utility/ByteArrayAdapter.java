package com.example.dietideals24frontend.utility;

import android.util.Base64;
import java.io.IOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ByteArrayAdapter extends TypeAdapter<byte[]> {
    @Override
    public void write(JsonWriter out, byte[] value) throws IOException {
        // Serialize byte[] as Base64-encoded string
        if (value != null) {
            out.value(Base64.encodeToString(value, Base64.DEFAULT));
        } else {
            out.nullValue();
        }
    }

    @Override
    public byte[] read(JsonReader in) throws IOException {
        // Deserialize Base64-encoded string to byte[]
        JsonToken token = in.peek();
        if (token == JsonToken.STRING) {
            String base64 = in.nextString();
            return Base64.decode(base64, Base64.DEFAULT);
        } else {
            in.skipValue();
            return null;
        }
    }
}