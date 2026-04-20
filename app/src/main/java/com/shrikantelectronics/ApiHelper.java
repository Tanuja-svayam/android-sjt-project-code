package com.shrikantelectronics; // your package name

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiHelper {  // ✅ public class

    private static final OkHttpClient client = new OkHttpClient();

    // Callback interface
    public interface ApiCallback {  // ✅ public interface
        void onSuccess(JSONObject response);
        void onFailure(String errorMessage);
    }

    // Public static method to make POST requests
    public static void post(String url, Map<String, String> params, ApiCallback callback) {
        FormBody.Builder formBuilder = new FormBody.Builder();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(() -> callback.onFailure(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respString = response.body().string();
                mainHandler.post(() -> {
                    try {
                        if (!response.isSuccessful()) {
                            callback.onFailure("HTTP error code: " + response.code());
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(respString);
                        callback.onSuccess(jsonObject);
                    } catch (Exception e) {
                        callback.onFailure(e.getMessage());
                    }
                });
            }
        });
    }
}
