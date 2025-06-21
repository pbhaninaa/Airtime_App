package com.example.qupos.JavaClasses;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class IveriPaymentProcessor {

    // Method to create an iVeri payment order
    public String createOrder(String baseUrl, String amount, String appId,
                              String successUrl, String errorUrl,
                              String failureUrl, String tryLaterUrl) {
        String result = "";

        try {
            // Setup the URL connection
            URL url = new URL(baseUrl); // Use full URL
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoOutput(true);

            // Prepare POST data
            Map<String, String> postData = new HashMap<>();
            postData.put("Lite_Merchant_ApplicationId", appId); // Application ID
            postData.put("Lite_Order_Amount", amount.replace(".", "")); // Amount in cents
            postData.put("Lite_Currency_AlphaCode", "ZAR"); // Currency
            postData.put("Lite_Website_Successful_Url", successUrl); // Success URL
            postData.put("Lite_Website_Fail_Url", failureUrl); // Failure URL
            postData.put("Lite_Website_TryLater_Url", tryLaterUrl); // Try Later URL
            postData.put("Lite_Website_Error_Url", errorUrl); // Error URL
            postData.put("Lite_Authorisation", "False"); // False for purchase/sale

            // Convert POST data to URL-encoded format
            StringBuilder postDataString = new StringBuilder();
            for (Map.Entry<String, String> entry : postData.entrySet()) {
                if (postDataString.length() > 0) {
                    postDataString.append("&");
                }
                postDataString.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                postDataString.append("=");
                postDataString.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            // Write data to the output stream
            OutputStream os = urlConnection.getOutputStream();
            os.write(postDataString.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            // Get the response
            int responseCode = urlConnection.getResponseCode();
            BufferedReader br;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            }

            // Read and build the response string
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            result = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            result = "Error: " + e.getMessage();
        }

        return result; // Return the server response
    }
}
