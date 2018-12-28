package com.gdou.android.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request reuqest = new Request.Builder().url(address).build();
        client.newCall((reuqest)).enqueue(callback);
    }

}
