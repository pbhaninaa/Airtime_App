package com.example.testingmyskills.JavaClasses;

import com.example.testingmyskills.Dao.HelperClass;

import org.json.JSONObject;


public class ApiService {
    private static final String BASE_URL = "http://102.219.85.66:8080/api/";

    // Login Endpoint
    public static JSONObject login(String agentPassword, String agentID) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Login\", \"AgentPassword\":\"" + agentPassword + "\", \"AgentID\":\"" + agentID + "\"}";
        return HelperClass.sendPostRequest(BASE_URL + "login", jsonInputString);
    }

    // Registration Endpoint
    public static JSONObject register(String agentName, String agentPassword, String agentID, String email) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Registration\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"AgentID\":\"" + agentID + "\", \"Email\":\"" + email + "\"}";
        return HelperClass.sendPostRequest(BASE_URL + "register", jsonInputString);
    }

    // Catalog Request Endpoint
    public static JSONObject catalogRequest() throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Catalog Request\"}";
        return HelperClass.sendPostRequest(BASE_URL + "catalog", jsonInputString);
    }

    // Validate MSISDN Endpoint
    public static JSONObject validateMsisdn(String network, String customerID, String agentID, String agentName, String agentPassword) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Validate MSISDN\", \"CustomerID\":\"" + customerID + "\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\"}";
        return HelperClass.sendPostRequest(BASE_URL + "validate-msisdn", jsonInputString);
    }

    // Balance Enquiry Endpoint
    public static JSONObject balanceEnquiry(String network, String agentID, String agentName, String agentPassword) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Balance Enquiry\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\"}";
        return HelperClass.sendPostRequest(BASE_URL + "balance-enquiry", jsonInputString);
    }

    // Deposit Funds Endpoint
    public static JSONObject depositFunds(String agentID, String agentName, String agentPassword, String agentEmail, String depositAmount, String currency) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Deposit Funds\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"AgentEmail\":\"" + agentEmail + "\", \"DepositAmount\":\"" + depositAmount + "\", \"Currency\":\"" + currency + "\"}";
        return HelperClass.sendPostRequest(BASE_URL + "deposit-funds", jsonInputString);
    }

    // Load Value Endpoint
    public static JSONObject loadValue(String network, String agentID, String agentName, String agentPassword, String customerID, String rechargeAmount, String productID, String productDescription) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Load Value\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"CustomerID\":\"" + customerID + "\", \"RechargeAmount\":\"" + rechargeAmount + "\", \"ProductID\":\"" + productID + "\", \"ProductDescription\":\"" + productDescription + "\"}";
        return HelperClass.sendPostRequest(BASE_URL + "load-value", jsonInputString);
    }

    // Load Bundle Endpoint
    public static JSONObject loadBundle(String network, String agentID, String agentName, String agentPassword, String customerID, String rechargeAmount, String productID, String productDescription) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Load Bundle\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"CustomerID\":\"" + customerID + "\", \"RechargeAmount\":\"" + rechargeAmount + "\", \"ProductID\":\"" + productID + "\", \"ProductDescription\":\"" + productDescription + "\"}";
        return HelperClass.sendPostRequest(BASE_URL + "load-bundle", jsonInputString);
    }


    // Statement Endpoint (matching the Postman request)
    public static JSONObject statement(String agentID, String agentName, String agentPassword, String agentEmail) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Statement\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"AgentEmail\":\"" + agentEmail + "\"}";
        return HelperClass.sendPostRequest(BASE_URL, jsonInputString); // Using BASE_URL without appending further paths
    }


}
