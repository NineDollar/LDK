package com.sys.ldk.http;

import android.content.Context;

import com.google.gson.JsonObject;
import com.sys.ldk.MainActivity;
import com.sys.ldk.accessibility.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Http {

    public static String get_http(String url) {
        final StringBuilder sb = new StringBuilder();
        FutureTask<String> task = new FutureTask<>(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL requestUrl = new URL(url);
                connection = (HttpURLConnection) requestUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                if (connection.getResponseCode() == 200) {
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    System.out.println(sb);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return sb.toString();
        });
        new Thread(task).start();
        String s = null;
        try {
            s = task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.V("get: " + s);
        return s;
    }
}
