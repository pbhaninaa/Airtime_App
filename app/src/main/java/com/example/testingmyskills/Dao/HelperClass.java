package com.example.testingmyskills.Dao;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class HelperClass {
    public static JSONObject sendPostRequest(String urlString, String jsonInputString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonInputString);
        String transactionType = jsonObject.getString("TransactionType");
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        int responseCode = conn.getResponseCode();
        System.out.println("Parameters : "+jsonInputString);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("responseCode", responseCode);
                jsonResponse.put("response", response.length() > 0 ? response.toString() : "Empty response from server.");
                System.out.println("Transaction Type: " + transactionType);
                System.out.println("Request Response: " + jsonResponse);
                return jsonResponse;
            }
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = br.readLine()) != null) {
                    errorResponse.append(errorLine.trim());
                }
                System.out.println("Error Response: " + errorResponse.toString());
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("responseCode", responseCode);
                jsonResponse.put("error", errorResponse.toString());
                return jsonResponse;
            } catch (Exception e) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("responseCode", responseCode);
                jsonResponse.put("error", "Unable to read error response.");
                return jsonResponse;
            }
        }
    }

}
 