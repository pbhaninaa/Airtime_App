package com.example.qupos.JavaClasses;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class PaymentProcessor {
    public String createOrder(String amount, String apiKey,String successUrl,String checkoutUrl) {

        HttpURLConnection urlConnection = null;
        String result = "";
        try {
            URL url = new URL(checkoutUrl);

            JSONObject postData = new JSONObject();
            postData.put("amount", amount.replace(".",""));
            postData.put("currency", "ZAR");

            // Use custom scheme to bounce back to the app
            postData.put("successUrl", successUrl);
            postData.put("failureUrl", checkoutUrl);
            postData.put("cancelUrl", checkoutUrl);

            // Open connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer " + apiKey);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);

            // Write data to the output stream
            OutputStream os = urlConnection.getOutputStream();
            os.write(postData.toString().getBytes());
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

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            result = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            result = e.toString();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }
}

