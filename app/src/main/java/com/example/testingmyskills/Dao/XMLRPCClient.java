package com.example.testingmyskills.Dao;

import android.os.AsyncTask;

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

    private static final String BASE_URL = "https://dev-api.wepayafrica.com/api/v1/";
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
                URL url = new URL(BASE_URL + "register");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = String.format("{\"name\": \"%s\", \"phone_number\": \"%s\", \"email\": \"%s\", \"password\": \"%s\"}", name, phoneNumber, email, password);

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

    public interface ResponseCallback {
        void onSuccess(String response);
        void onError(Exception e);
    }
}

