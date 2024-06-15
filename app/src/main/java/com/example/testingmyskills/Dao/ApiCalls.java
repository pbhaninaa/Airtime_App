package com.example.testingmyskills.Dao;

import static com.example.testingmyskills.MainActivity.PROVIDER_CODE;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ApiCalls {

    private String username;
    private String password;
    private XmlRpcClient client;

    public ApiCalls(String serverUrl, String username, String password) throws MalformedURLException {
        this.username = username;
        this.password = password;

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(serverUrl));
        config.setBasicUserName(username);
        config.setBasicPassword(password);
        this.client = new XmlRpcClient();
        this.client.setConfig(config);
    }

    public Map<String, Object> accountBalanceEnquiry(String msisdn) throws XmlRpcException {
        Map<String, Object> params = new HashMap<>();
        params.put("MSISDN", msisdn);

        Object[] response = (Object[]) client.execute("account_balance_enquiry", new Object[]{username, password, params});


        if (response.length > 0 && response[0] instanceof Map) {
            Map<String, Object> responseMap = (Map<String, Object>) response[0];
            return responseMap;
        } else {
            throw new XmlRpcException("Invalid response from server");
        }
    }
    public Map<String, Object> loadValue(String msisdn, int amount, String reference, int currency) throws XmlRpcException {
        Map<String, Object> params = new HashMap<>();
        params.put("MSISDN", msisdn);
        params.put("Amount", amount);
        params.put("Reference", reference);
        params.put("Currency", currency);
        return (Map<String, Object>) client.execute("load_value", new Object[]{username, password, params});


    }

    public Map<String, Object> loadBundle(String msisdn, int amount, int currency, int accountType, int quantity, String reference) throws XmlRpcException {
        Map<String, Object> params = new HashMap<>();
        params.put("MSISDN", msisdn);
        params.put("ProviderCode", PROVIDER_CODE);
        params.put("Amount", amount);
        params.put("Currency", currency);
        params.put("AccountType", accountType);
        params.put("Quantity", quantity);
        params.put("Reference", reference);
        return (Map<String, Object>) client.execute("load_bundle", new Object[]{username, password, params});
    }

    public Map<String, Object> deductValue(String msisdn, int amount, String reference) throws XmlRpcException {
        Map<String, Object> params = new HashMap<>();
        params.put("MSISDN", msisdn);
        params.put("ProviderCode", PROVIDER_CODE);
        params.put("Amount", amount);
        params.put("Reference", reference);
        return (Map<String, Object>) client.execute("deduct_value", new Object[]{username, password, params});
    }

    public Map<String, Object> getTransactionStatus(String reference) throws XmlRpcException {
        Map<String, Object> params = new HashMap<>();
        params.put("Reference", reference);
        return (Map<String, Object>) client.execute("get_transaction_status", new Object[]{username, password, params});
    }

    public Map<String, Object> accountTransfer(int fromCompanyID, int toCompanyID, int amount, int currency, String reference) throws XmlRpcException {
        Map<String, Object> params = new HashMap<>();
        params.put("FromCompanyID", fromCompanyID);
        params.put("ToCompanyID", toCompanyID);
        params.put("Amount", amount);
        params.put("Currency", currency);
        params.put("Reference", reference);
        return (Map<String, Object>) client.execute("account_transfer", new Object[]{username, password, params});
    }

    public Map<String, Object> validateMsisdn(String msisdn, int providerCode) throws XmlRpcException {
        Map<String, Object> params = new HashMap<>();
        params.put("MSISDN", msisdn);
        params.put("ProviderCode", providerCode);
        Object[] response = (Object[]) client.execute("validate_msisdn", new Object[]{username, password, params});

        if (response.length > 0 && response[0] instanceof Map) {
            Map<String, Object> responseMap = (Map<String, Object>) response[0];
            return responseMap;
        } else {
            throw new XmlRpcException("Invalid response from server");
        }
    }
}

