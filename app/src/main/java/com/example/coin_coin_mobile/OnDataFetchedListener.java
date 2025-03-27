package com.example.coin_coin_mobile;

import org.json.JSONException;

public interface OnDataFetchedListener {
    void onSuccess(String data) throws JSONException;
    void onError(String error);}

