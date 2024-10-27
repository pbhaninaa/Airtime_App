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
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Validate MSISDN\", \"CustomerID\":\"" + customerID.replace("+", "") + "\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "validate-msisdn", jsonInputString);
    }

    // Balance Enquiry Endpoint
    public static JSONObject balanceEnquiry(String network, String agentID) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Balance Enquiry\", \"AgentID\":\"" + agentID + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "balance-enquiry", jsonInputString);
    }

    // Deposit Funds Endpoint
    public static JSONObject depositFunds(String agentID, String depositAmount, String currency) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Deposit Funds\", \"AgentID\":\"" + agentID + "\", \"DepositAmount\":\"" + depositAmount.replace(".", "") + "\", \"Currency\":\"" + currency + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "deposit-funds", jsonInputString);
    }

    // Load Value Endpoint
    public static JSONObject loadValue(String network, String agentID, String customerID, String rechargeAmount, String productID, String productDescription) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Load Value\", \"AgentID\":\"" + agentID + "\", \"CustomerID\":\"" + customerID.replace("+", "") + "\", \"RechargeAmount\":\"" + rechargeAmount.replace(".", "") + "\", \"ProductID\":\"" + productID + "\", \"ProductDescription\":\"" + productDescription + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "load-value", jsonInputString);
    }

    // Load Bundle Endpoint
    public static JSONObject loadBundle(String network, String agentID, String agentName, String agentPassword, String customerID, String rechargeAmount, String productID, String productDescription) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Load Bundle\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"CustomerID\":\"" + customerID.replace("+", "") + "\", \"RechargeAmount\":\"" + rechargeAmount.replace(".", "") + "\", \"ProductID\":\"" + productID + "\", \"ProductDescription\":\"" + productDescription + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "load-bundle", jsonInputString);
    }

    // Transaction Status Enquiry Endpoint
    public static JSONObject transactionStatusEnquiry(String network, String agentID, String customerID, String referenceID) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Transaction Status Enquiry\", \"AgentID\":\"" + agentID + "\", \"CustomerID\":\"" + customerID.replace("+", "") + "\", \"ReferenceID\":\"" + referenceID + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "transaction-status-enquiry", jsonInputString);
    }

    // Statement Endpoint
    public static JSONObject statement(String agentID, String agentName, String agentPassword, String agentEmail) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Statement\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"AgentEmail\":\"" + agentEmail + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL, jsonInputString);
    }
}
