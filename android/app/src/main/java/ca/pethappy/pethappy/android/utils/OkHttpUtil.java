package ca.pethappy.pethappy.android.utils;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpUtil {
    public static String handleResponse(Response response) throws IOException {
        if (response.isSuccessful()) {
            try (ResponseBody body = response.body()) {
                if (body != null) {
                    return body.string();
                } else {
                    throw new IOException(response.message());
                }
            }
        } else {
            throw new IOException(response.message());
        }
    }
}
