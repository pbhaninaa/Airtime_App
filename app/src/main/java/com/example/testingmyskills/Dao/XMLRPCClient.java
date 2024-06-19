package com.example.testingmyskills.Dao;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class XMLRPCClient {

    private static final String SERVER_URL = "http://102.219.85.66:7022/xmlrpc";
    private static final String USERNAME = "QuposUSDTest";
    private static final String PASSWORD = "pass123";

    public static Map<String, Object> accountBalanceEnquiry(String msisdn,String TransactionType) throws Exception {
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
        struct.put("ProviderCode", 100); // Use the appropriate provider code

        Object[] params = new Object[]{USERNAME, PASSWORD, struct};

        // Execute the XML-RPC method
        Object response = client.execute(TransactionType, params);

        // Check if response is an array
        if (response instanceof Object[]) {
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
            throw new XmlRpcException("Unexpected response type from server");
        }
    }
//    public Map<String, Object> loadValue(String msisdn, int amount, String reference, int currency) throws XmlRpcException {
//        Map<String, Object> params = new HashMap<>();
//        params.put("MSISDN", msisdn);
//        params.put("Amount", amount);
//        params.put("Reference", reference);
//        params.put("Currency", currency);
//        return (Map<String, Object>) client.execute("load_value", new Object[]{username, password, params});
//
//
//    }
//
//    public Map<String, Object> loadBundle(String msisdn, int amount, int currency, int accountType, int quantity, String reference) throws XmlRpcException {
//        Map<String, Object> params = new HashMap<>();
//        params.put("MSISDN", msisdn);
//        params.put("ProviderCode", MainActivity.PROVIDER_CODE);
//        params.put("Amount", amount);
//        params.put("Currency", currency);
//        params.put("AccountType", accountType);
//        params.put("Quantity", quantity);
//        params.put("Reference", reference);
//        return (Map<String, Object>) client.execute("load_bundle", new Object[]{username, password, params});
//    }
//
//    public Map<String, Object> deductValue(String msisdn, int amount, String reference) throws XmlRpcException {
//        Map<String, Object> params = new HashMap<>();
//        params.put("MSISDN", msisdn);
//        params.put("ProviderCode", MainActivity.PROVIDER_CODE);
//        params.put("Amount", amount);
//        params.put("Reference", reference);
//        return (Map<String, Object>) client.execute("deduct_value", new Object[]{username, password, params});
//    }
//
//    public Map<String, Object> getTransactionStatus(String reference) throws XmlRpcException {
//        Map<String, Object> params = new HashMap<>();
//        params.put("Reference", reference);
//        return (Map<String, Object>) client.execute("get_transaction_status", new Object[]{username, password, params});
//    }
//
//    public Map<String, Object> accountTransfer(int fromCompanyID, int toCompanyID, int amount, int currency, String reference) throws XmlRpcException {
//        Map<String, Object> params = new HashMap<>();
//        params.put("FromCompanyID", fromCompanyID);
//        params.put("ToCompanyID", toCompanyID);
//        params.put("Amount", amount);
//        params.put("Currency", currency);
//        params.put("Reference", reference);
//        return (Map<String, Object>) client.execute("account_transfer", new Object[]{username, password, params});
//    }
//
//    public Map<String, Object> validateMsisdn(String msisdn, int providerCode) throws XmlRpcException {
//        Map<String, Object> params = new HashMap<>();
//        params.put("MSISDN", msisdn);
//        params.put("ProviderCode", providerCode);
//        Object[] response = (Object[]) client.execute("validate_msisdn", new Object[]{username, password, params});
//
//        if (response.length > 0 && response[0] instanceof Map) {
//            Map<String, Object> responseMap = (Map<String, Object>) response[0];
//            return responseMap;
//        } else {
//            throw new XmlRpcException("Invalid response from server");
//        }
//    }
}

