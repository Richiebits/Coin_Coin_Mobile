package com.example.coin_coin_mobile;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FetchApi {

    public static void fetchData(String path, String method, String jsonBody, Map<String, String> headers, OnDataFetchedListener listener) {
        String ipAddressP = "10.0.0.182"; // téléphone - louis
        String ipAddressE = "10.0.2.2"; // émulateur

        new Thread(() -> {
            try {
                URL url = new URL("http://" + ipAddressE + ":8000/api/" + path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(method);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        if (entry.getValue() == null) {
                            Log.e("FETCH_API", "Header " + entry.getKey() + " has null value!");
                        } else {
                            conn.setRequestProperty(entry.getKey(), entry.getValue());
                        }
                    }
                } else {
                    Log.w("FETCH_API", "Headers map is null");
                }

                if ((method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) && jsonBody != null) {
                    conn.setDoOutput(true);
                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    if (inputStream != null) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
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
                                if (listener != null) {
                                    listener.onSuccess(data);
                                } else {
                                    Log.e("FETCH_API", "Listener is null in onSuccess!");
                                }
                            } catch (JSONException e) {
                                Log.e("FETCH_API", "JSONException in onSuccess", e);
                            }
                        });
                    }

                } else {
                    InputStream errorStream = conn.getErrorStream();
                    if(errorStream != null) {


                        BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        Log.e("POST_ERROR", "Server Error Response: " + responseBuilder.toString());
                        Log.e("API_ERROR", "Response Code: " + responseCode);

                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (listener != null) {
                                listener.onError("Error: " + responseCode);
                            } else {
                                Log.e("FETCH_API", "Listener is null in onError (response code case)");
                            }
                        });
                    }
                }

            } catch (Exception e) {
                Log.e("EXCEPTION", "Exception caught", e);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (listener != null) {
                        listener.onError(e.toString());
                    } else {
                        Log.e("FETCH_API", "Listener is null in onError (exception case)");
                    }
                });
            }
        }).start();
    }
}


