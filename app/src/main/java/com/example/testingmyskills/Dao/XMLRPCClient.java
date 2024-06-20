package com.example.testingmyskills.Dao;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
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

        // Prepare the parameters
        Map<String, Object> struct = new HashMap<>();
        struct.put("MSISDN", msisdn);
        struct.put("ProviderCode",100);

        Object[] params = new Object[]{USERNAME, PASSWORD, struct};
        System.out.println("Testing ================================================"+ Arrays.toString(params));
        Object response = client.execute(transactionType, params);
        System.out.println("Testing1 ================================================"+response);

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

}

