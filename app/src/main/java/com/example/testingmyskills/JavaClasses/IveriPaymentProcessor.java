package com.example.testingmyskills.JavaClasses;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class IveriPaymentProcessor {
    private static final String BASE_URL = "https://portal.host.iveri.com/Lite/Authorise.aspx";

    // Hardcoded parameters
    private static final String APPLICATION_ID = "YOUR_APPLICATION_ID"; // Replace with your iVeri Application ID
    private static final String SUCCESS_URL = "testingmyskills://payment-success"; // Redirect back to the app
    private static final String FAIL_URL = "https://portal.host.iveri.com/Lite/Fail";
    private static final String TRY_LATER_URL = "https://portal.host.iveri.com/Lite/TryLater";
    private static final String ERROR_URL = "https://portal.host.iveri.com/Lite/Error";

    public String createOrder(String amount) {
        String result = "";

        try {
            // Setup URL connection
            URL url = new URL(BASE_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoOutput(true);

            // Prepare POST data
            Map<String, String> postData = new HashMap<>();
            postData.put("Lite_Merchant_ApplicationId", APPLICATION_ID);
            postData.put("Lite_Order_Amount", amount.replace(".", "")); // Amount in cents
            postData.put("Lite_Currency_AlphaCode", "ZAR");
            postData.put("Lite_Website_Successful_Url", SUCCESS_URL); // Redirect to app on success
            postData.put("Lite_Website_Fail_Url", FAIL_URL); // iVeri fail page
            postData.put("Lite_Website_TryLater_Url", TRY_LATER_URL); // iVeri try later page
            postData.put("Lite_Website_Error_Url", ERROR_URL); // iVeri error page
            postData.put("Lite_Authorisation", "False"); // Indicates a purchase/sale

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

            // Get response
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
            result = "Error: " + e.getMessage();
        }

        return result;
    }
}
