package com.example.testingmyskills.JavaClasses;

import com.example.testingmyskills.BuildConfig;
import com.example.testingmyskills.Dao.HelperClass;

import org.json.JSONObject;


public class ApiService {

    // Login Endpoint
    public static JSONObject login(String agentPassword, String agentID) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Login\", \"AgentPassword\":\"" + agentPassword + "\", \"AgentID\":\"" + agentID + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "login", jsonInputString);
    }

    // Registration Endpoint
    public static JSONObject register(String agentName, String agentPassword, String agentID, String email) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Registration\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"AgentID\":\"" + agentID + "\", \"AgentEmail\":\"" + email + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "register", jsonInputString);
    }

    // Catalog Request Endpoint
    public static JSONObject catalogRequest() throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Catalog Request\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "catalog", jsonInputString);
    }

    // Validate MSISDN Endpoint
    public static JSONObject validateMsisdn(String network, String customerID, String agentID, String agentName, String agentPassword) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Validate MSISDN\", \"CustomerID\":\"" + customerID + "\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "validate-msisdn", jsonInputString);
    }

    // Balance Enquiry Endpoint
    public static JSONObject balanceEnquiry(String network, String agentID) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Balance Enquiry\", \"AgentID\":\"" + agentID + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "balance-enquiry", jsonInputString);
    }

    // Deposit Funds Endpoint
    public static JSONObject depositFunds(String agentID, String agentName, String agentPassword, String agentEmail, String depositAmount, String currency) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Deposit Funds\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"AgentEmail\":\"" + agentEmail + "\", \"DepositAmount\":\"" + depositAmount + "\", \"Currency\":\"" + currency + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "deposit-funds", jsonInputString);
    }

    // Load Value Endpoint
//    {
//        "Network":"Econet",
//            "TransactionType":"Load Value",
//            "AgentID":"27649045091",
//            "CustomerID":"263781801175",
//            "RechargeAmount":"10",
//            "ProductID":"Airtime",
//            "ProductDescription":"Airtime"
//    }
    public static JSONObject loadValue(String network, String agentID,  String customerID, String rechargeAmount, String productID, String productDescription) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Load Value\", \"AgentID\":\"" + agentID + "\", \"CustomerID\":\"" + customerID + "\", \"RechargeAmount\":\"" + rechargeAmount + "\", \"CustomerID\":\"" + customerID + "\", \"RechargeAmount\":\"" + rechargeAmount + "\", \"ProductID\":\"" + productID + "\", \"ProductDescription\":\"" + productDescription + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "load-value", jsonInputString);
    }

    // Load Bundle Endpoint
    public static JSONObject loadBundle(String network, String agentID, String agentName, String agentPassword, String customerID, String rechargeAmount, String productID, String productDescription) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Load Bundle\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"CustomerID\":\"" + customerID + "\", \"RechargeAmount\":\"" + rechargeAmount + "\", \"ProductID\":\"" + productID + "\", \"ProductDescription\":\"" + productDescription + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "load-bundle", jsonInputString);
    }
    public static JSONObject transactionStatusEnquiry(String network, String agentID, String customerID, String referenceID) throws Exception {
        // Construct the JSON input string with the parameters
        String jsonInputString = "{\"Network\":\"" + network + "\", " +
                "\"TransactionType\":\"Transaction Status Enquiry\", " +
                "\"AgentID\":\"" + agentID + "\", " +
                "\"CustomerID\":\"" + customerID + "\", " +
                "\"ReferenceID\":\"" + referenceID + "\"}";

        // Send a POST request and return the response
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "transaction-status-enquiry", jsonInputString);
    }


    // Statement Endpoint (matching the Postman request)
    public static JSONObject statement(String AgentID,String AgentName,String AgentPassword,String AgentEmail) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Statement\", \"AgentID\":\"" + AgentID + "\", \"AgentName\":\"" + AgentName + "\", \"AgentPassword\":\"" + AgentPassword + "\", \"AgentEmail\":\"" + AgentEmail + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL, jsonInputString); // Using BASE_URL without appending further paths
    }


}
