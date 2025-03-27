package com.example.coin_coin_mobile;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FetchApi {

    public static void fetchData( String path, String method, String jsonBody, OnDataFetchedListener listener) {
        //path: api path ex:client/id
        //method: fetch method ex: GET, POST
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8000/api/" + path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(method);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                if (method.equalsIgnoreCase("POST") && jsonBody != null){
                    conn.setDoOutput(true);
                    try (OutputStream os = conn.getOutputStream()){
                        byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();
                    conn.disconnect();

                    String data = response.toString();
                    Log.d("API_RESPONSE", data);


                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            listener.onSuccess(data);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });

                } else {
                    Log.e("API_ERROR", "Response Code: " + responseCode);
                    new Handler(Looper.getMainLooper()).post(() -> listener.onError("Error: " + responseCode));
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> listener.onError(e.getMessage()));
            }
        }).start();
    }
}

