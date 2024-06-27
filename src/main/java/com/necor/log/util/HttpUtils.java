package com.necor.log.util;

import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtils {

    public static String postJosn(String url, Object params) {
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            String postData = null;
            if (params instanceof String) {
                postData = (String)params;
            }else {
                postData =  JSONObject.toJSONString(params);
            }

            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Length", Integer.toString(postDataBytes.length));

            connection.setDoOutput(true);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(postDataBytes);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}