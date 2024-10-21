package com.example.testingmyskills.Dao;

import android.os.AsyncTask;
import android.os.Build;

import com.example.testingmyskills.JavaClasses.Utils;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class XMLRPCClient {

    private static String SERVER_URL = "http://102.219.85.66:7022/xmlrpc";
    private static String USERNAME = "QuposUSDTest";
    private static String PASSWORD = "pass123";

    public static Map<String, Object> accountBalanceEnquiry(String msisdn, String transactionType) throws Exception {
        // Create an XML-RPC client configuration
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(SERVER_URL));
        config.setBasicUserName(USERNAME);
        config.setBasicPassword(PASSWORD);
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);

        // Create the XML-RPC client
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        // Prepare the parameters "263781801174"
        Map<String, Object> struct = new HashMap<>();
        struct.put("MSISDN", msisdn);
        struct.put("ProviderCode", 100);
        struct.put("Currency", 840);
        struct.put("Amount", 10);
        struct.put("Reference", Utils.ref());

        Object[] params = new Object[]{USERNAME, PASSWORD, struct};
        System.out.println("params: " + transactionType + Arrays.toString(params));
        Object response = client.execute(transactionType, params);
        System.out.println("res:" + response);
        if (response instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> responseMap = (Map<String, Object>) response;

            return responseMap;
        } else if (response instanceof Object[]) {
            Object[] responseArray = (Object[]) response;

            // Assuming the response array contains a map at the first position
            if (responseArray.length > 0 && responseArray[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseMap = (Map<String, Object>) responseArray[0];

                return responseMap;
            } else {
                throw new XmlRpcException("Invalid response structure from server");
            }
        } else {
            throw new XmlRpcException("Unexpected response type from server: " + response.getClass().getName());
        }
    }

    public static Map<String, Object> LoadBalanceEnquiry(int id, String ref, String note, String amount) throws Exception {
//        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
//        config.setServerURL(new URL(SERVER_URL));
//        config.setBasicUserName(USERNAME);
//        config.setBasicPassword(PASSWORD);
//        config.setEnabledForExtensions(true);
//        config.setConnectionTimeout(60 * 1000);
//        config.setReplyTimeout(60 * 1000);
//
//        XmlRpcClient client = new XmlRpcClient();
//        client.setConfig(config);
//
//        Map<String, Object> struct = new HashMap<>();
//        struct.put("USERID", id);
//        struct.put("REFERENCE", ref);
//        struct.put("AMOUNT", Double.parseDouble(amount));
//        struct.put("NOTES", note);
//        struct.put("PAYMENTDATE", Utils.ref());
//
//        Object[] params = new Object[]{USERNAME, PASSWORD, struct};
//        System.out.println("params: " + transactionType + Arrays.toString(params));
//        Object response = client.execute(transactionType, params);
//
//        System.out.println("res:" + response);
//        if (response instanceof Map) {
//            @SuppressWarnings("unchecked")
//            Map<String, Object> responseMap = (Map<String, Object>) response;
//
//            return responseMap;
//        } else if (response instanceof Object[]) {
//            Object[] responseArray = (Object[]) response;
//
//            if (responseArray.length > 0 && responseArray[0] instanceof Map) {
//                @SuppressWarnings("unchecked")
//                Map<String, Object> responseMap = (Map<String, Object>) responseArray[0];
//
//                return responseMap;
//            } else {
//                throw new XmlRpcException("Invalid response structure from server");
//            }
//        } else {
//            throw new XmlRpcException("Unexpected response type from server: " + response.getClass().getName());
//        }
        return null;
    }

    public static Map<String, Object> loadValue(String msisdn, String transactionType, int currency, int amount) throws Exception {
        // Create an XML-RPC client configuration
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(SERVER_URL));
        config.setBasicUserName(USERNAME);
        config.setBasicPassword(PASSWORD);
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);

        // Create the XML-RPC client
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        // Prepare the parameters
        Map<String, Object> struct = new HashMap<>();
        struct.put("MSISDN", msisdn);
        struct.put("ProviderCode", 100);
        struct.put("Currency", currency);
        struct.put("Amount", amount);
        struct.put("Reference", Utils.ref());

        Object[] params = new Object[]{USERNAME, PASSWORD, struct};
        Object response = client.execute(transactionType, params);

        if (response instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> responseMap = (Map<String, Object>) response;

            return responseMap;
        } else if (response instanceof Object[]) {
            Object[] responseArray = (Object[]) response;

            // Assuming the response array contains a map at the first position
            if (responseArray.length > 0 && responseArray[0] instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseMap = (Map<String, Object>) responseArray[0];

                return responseMap;
            } else {
                throw new XmlRpcException("Invalid response structure from server");
            }
        } else {
            throw new XmlRpcException("Unexpected response type from server: " + response.getClass().getName());
        }
    }
//====================================================================

    private static final String BASE_URL = "http://102.219.85.66:8080/api/";
//    private static final String BASE_URL = "https://dev-api.wepayafrica.com/api/v1/";
//    https://dev-api.wepayafrica.com/api/v1/

    // User Registration
    public static void registerUserAsync(String name, String phoneNumber, String email, String password, ResponseCallback callback) {
        new RegisterUserTask(name, phoneNumber, email, password, callback).execute();
    }

    private static class RegisterUserTask extends AsyncTask<Void, Void, String> {
        private String name;
        private String phoneNumber;
        private String email;
        private String password;
        private ResponseCallback callback;
        private Exception exception;

        public RegisterUserTask(String name, String phoneNumber, String email, String password, ResponseCallback callback) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.password = password;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Setup URL connection
                URL url = new URL(BASE_URL + "register");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");  // Set content type to application/json
                conn.setRequestProperty("Accept", "application/json");        // Ensure the server returns JSON
                conn.setDoOutput(true);                                       // Allow output for POST request

                // Prepare JSON input
                String jsonInputString = String.format(
                        "{\"name\": \"%s\", \"phone_number\": \"%s\", \"email\": \"%s\", \"password\": \"%s\"}",
                        name, phoneNumber, email, password
                );
                System.out.println("Request JSON: " + jsonInputString);

                // Write JSON to output stream
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get response code
                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                // Read response
                InputStream inputStream = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = reader.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response: " + response.toString());

                    // Return success or error response
                    return (responseCode >= 200 && responseCode < 300) ? "1" : response.toString();
                } else {
                    System.out.println("No response received from the server.");
                    return "No response received from the server.";
                }

            } catch (Exception e) {
                e.printStackTrace();
                this.exception = e;
                return null;
            }
        }

     /*   protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(BASE_URL + "register");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = String.format("{\"name\": \"%s\", \"phone_number\": \"%s\", \"email\": \"%s\", \"password\": \"%s\"}", name, phoneNumber, email, password);
                System.out.println("Request JSON: " + jsonInputString);

                // Printing connection details
                System.out.println("Request Method: " + conn.getRequestMethod());
                System.out.println("Request URL: " + conn.getURL().toString());
                System.out.println("Request Properties: ");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    conn.getRequestProperties().forEach((key, value) -> System.out.println(key + ": " + value));
//                }

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                InputStream inputStream = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();

                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = reader.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response: " + response.toString());
                    return (responseCode >= 200 && responseCode < 300) ? "1" : response.toString();
                } else {
                    return "No response received from the server.";
                }

            } catch (Exception e) {
                e.printStackTrace();
                this.exception = e;
                return null;
            }
        }*/


        @Override
        protected void onPostExecute(String response) {
            if (callback != null) {
                if (exception == null) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(exception);
                }
            }
        }
    }

    // User Login
    public static void userLoginAsync(String email, String password, ResponseCallback callback) {
        new LoginUserTask(email, password, callback).execute();
    }

    private static class LoginUserTask extends AsyncTask<Void, Void, String> {
        private String email;
        private String password;
        private ResponseCallback callback;
        private Exception exception;

        public LoginUserTask(String email, String password, ResponseCallback callback) {
            this.email = email;
            this.password = password;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(BASE_URL + "login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                InputStream inputStream;
                int code = conn.getResponseCode();
                if (code == 200) { // success
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = reader.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                return response.toString();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (callback != null) {
                if (exception == null) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(exception);
                }
            }
        }
    }

    // Refresh User Token
    public static void refreshTokenAsync(String token, ResponseCallback callback) {
        new RefreshTokenTask(token, callback).execute();
    }

    private static class RefreshTokenTask extends AsyncTask<Void, Void, String> {
        private String token;
        private ResponseCallback callback;
        private Exception exception;

        public RefreshTokenTask(String token, ResponseCallback callback) {
            this.token = token;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(BASE_URL + "refresh");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                InputStream inputStream;
                int code = conn.getResponseCode();
                if (code == 200) { // success
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = reader.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                return response.toString();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (callback != null) {
                if (exception == null) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(exception);
                }
            }
        }
    }

    // Invalidate Token (Logout)
    public static void logoutAsync(String token, ResponseCallback callback) {
        new LogoutTask(token, callback).execute();
    }

    private static class LogoutTask extends AsyncTask<Void, Void, String> {
        private String token;
        private ResponseCallback callback;
        private Exception exception;

        public LogoutTask(String token, ResponseCallback callback) {
            this.token = token;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(BASE_URL + "logout");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                InputStream inputStream;
                int code = conn.getResponseCode();
                if (code == 200) { // success
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = reader.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                return response.toString();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (callback != null) {
                if (exception == null) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(exception);
                }
            }
        }
    }

    // Add Manual Payment
    public static void addManualPaymentAsync(int userId, String reference, double amount, String notes, String paymentDate, ResponseCallback callback) {
        new AddManualPaymentTask(userId, reference, amount, notes, paymentDate, callback).execute();
    }

    private static class AddManualPaymentTask extends AsyncTask<Void, Void, String> {
        private int userId;
        private String reference;
        private double amount;
        private String notes;
        private String paymentDate;
        private ResponseCallback callback;
        private Exception exception;

        public AddManualPaymentTask(int userId, String reference, double amount, String notes, String paymentDate, ResponseCallback callback) {
            this.userId = userId;
            this.reference = reference;
            this.amount = amount;
            this.notes = notes;
            this.paymentDate = paymentDate;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(BASE_URL + "manual-payments/test-options/add");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = String.format("{\"user_id\": %d, \"reference\": \"%s\", \"amount\": %.2f, \"notes\": \"%s\", \"payment_date\": \"%s\"}",
                        userId, reference, amount, notes, paymentDate);
                System.out.println("Request JSON: " + jsonInputString);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                InputStream inputStream = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();

                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = reader.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response: " + response.toString());
                    return  response.toString();
                } else {
                    return "No response received from the server.";
                }

            } catch (Exception e) {
                e.printStackTrace();
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (callback != null) {
                if (exception == null) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(exception);
                }
            }
        }
    }
    public interface ResponseCallback {
        void onSuccess(String response);

        void onError(Exception e);
    }
}

