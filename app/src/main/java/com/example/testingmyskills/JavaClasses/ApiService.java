package com.example.testingmyskills.JavaClasses;

import android.content.Context;

import com.example.testingmyskills.BuildConfig;
import com.example.testingmyskills.Dao.HelperClass;

import org.json.JSONObject;

public class ApiService {

    // Login Endpoint
    public static JSONObject login(String agentPassword, String agentID, Context context) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Collector Login\", \"AgentPassword\":\"" + agentPassword + "\", \"AgentID\":\"" + agentID + "\",\"DeviceID\":\"" + Utils.getDeviceEMEI(context) + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "login", jsonInputString);
    }

    // Registration Endpoint
    public static JSONObject register(String agentName, String agentPassword, String agentID, String email, Context context) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Registration\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"AgentID\":\"" + agentID + "\", \"AgentEmail\":\"" + email + "\",\"DeviceID\":\"" + Utils.getDeviceEMEI(context) + "\"}";
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
    public static JSONObject balanceEnquiry(String network, String agentID, Context context) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Balance Enquiry\", \"AgentID\":\"" + agentID + "\",\"DeviceID\":\"" + Utils.getDeviceEMEI(context) + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "balance-enquiry", jsonInputString);
    }

    // Deposit Funds Endpoint
    public static JSONObject depositFunds( String agentID,String collectorID, String depositAmount, String currency, Context context) throws Exception {
        String jsonInputString = "{\"TransactionType\":\"Deposit Funds\"," + "\"AgentID\":\"" + agentID + "\"," + "\"CollectorID\":\"" + collectorID + "\"," + "\"DepositAmount\":\"" + depositAmount.replace(".", "") + "\"," + "\"Currency\":\"" + currency + "\"," + "\"DeviceID\":\"" + Utils.getDeviceEMEI(context) + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "deposit-funds", jsonInputString);
    }

    public static JSONObject collectFunds(            String network, String agentID, String cashAmount, String commissionAmount, String currency, String collectorID, String collectorName, Context context) throws Exception {
        String jsonInputString = "{" + "\"Network\":\"" + network + "\"," + "\"TransactionType\":\"Collect Cash\"," + "\"AgentID\":\"" + agentID + "\"," + "\"CashAmount\":\"" + cashAmount.replace(".", "") + "\"," + "\"CommissionAmount\":\"" + commissionAmount.replace(".", "") + "\"," + "\"Currency\":\"" + currency + "\"," + "\"CollectorID\":\"" + collectorID + "\"," + "\"CollectorName\":\"" + collectorName + "\"" + "}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "collect-funds", jsonInputString);
    }


    //Get Agents
    public static JSONObject getAgents(String network, String agentID, String collectorID, Context context) throws Exception {
        String jsonInputString = "{" + "\"Network\":\"" + network + "\"," + "\"TransactionType\":\"Retrieve Agents\"," + "\"AgentID\":\"" + agentID + "\"," + "\"CollectorID\":\"" + collectorID + "\"" + "}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "get-agents", jsonInputString);
    }


    // Load Value Endpoint
    public static JSONObject loadValue(String network, String agentID, String customerID, String rechargeAmount, String productID, String productDescription, Context context) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Load Value\", \"AgentID\":\"" + agentID + "\", \"CustomerID\":\"" + customerID.replace("+", "") + "\", \"RechargeAmount\":\"" + rechargeAmount.replace(".", "") + "\", \"ProductID\":\"" + productID + "\", \"ProductDescription\":\"" + productDescription + "\",\"DeviceID\":\"" + Utils.getDeviceEMEI(context) + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "load-value", jsonInputString);
    }

    // Load Bundle Endpoint
    public static JSONObject loadBundle(String network, String agentID, String agentName, String agentPassword, String customerID, String rechargeAmount, String productID, String productDescription, Context context) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Load Bundle\", \"AgentID\":\"" + agentID + "\", \"AgentName\":\"" + agentName + "\", \"AgentPassword\":\"" + agentPassword + "\", \"CustomerID\":\"" + customerID.replace("+", "") + "\", \"RechargeAmount\":\"" + rechargeAmount.replace(".", "") + "\", \"ProductID\":\"" + productID + "\", \"ProductDescription\":\"" + productDescription + "\",\"DeviceID\":\"" + Utils.getDeviceEMEI(context) + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "load-bundle", jsonInputString);
    }

    public static JSONObject transactionStatusEnquiry(String network, String agentID, String customerID, String referenceID, Context context) throws Exception {
        String jsonInputString = "{\"Network\":\"" + network + "\", \"TransactionType\":\"Transaction Status Enquiry\", \"AgentID\":\"" + agentID + "\", \"CustomerID\":\"" + customerID.replace("+", "") + "\", \"ReferenceID\":\"" + referenceID + "\",\"DeviceID\":\"" + Utils.getDeviceEMEI(context) + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "transaction-status-enquiry", jsonInputString);
    }

    public static JSONObject getLastTransaction(String agentID, Context context) throws Exception {
        String jsonInputString = "{ \"TransactionType\":\"Last Transaction\", \"AgentID\":\"" + agentID + "\",\"DeviceID\":\"" + Utils.getDeviceEMEI(context) + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL, jsonInputString);
    }

    public static JSONObject resetPassword(String agentID, String email, Context context) throws Exception {
        String jsonInputString = "{ \"TransactionType\":\"Password Reset\", \"AgentID\":\"" + agentID + "\", \"AgentEmail\":\"" + email + "\",\"DeviceID\":\"" + Utils.getDeviceEMEI(context) + "\"}";
        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "reset-password", jsonInputString);
    }
    public static JSONObject getCollectorSummary(
            String network,
            String agentID,
            String collectorID,
            String startDate,
            String endDate
    ) throws Exception {
        String jsonInputString = "{"
                + "\"Network\":\"" + network + "\","
                + "\"TransactionType\":\"Collector Summary\","
                + "\"AgentID\":\"" + agentID + "\","
                + "\"CollectorID\":\"" + collectorID + "\","
                + "\"StartDate\":\"" + startDate + "\","
                + "\"EndDate\":\"" + endDate + "\""
                + "}";

        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL + "collector_summary", jsonInputString);
    }


    // Statement Endpoint
    public static JSONObject statement(String agentID, String agentName, String agentPassword, String agentEmail, Context context, String startDate, String endDate) throws Exception {
        // Start building the JSON string
        StringBuilder jsonInputString = new StringBuilder("{\"TransactionType\":\"Collector Statement\", \"AgentID\":\"" + agentID +
                "\", \"AgentName\":\"" + agentName +                "\", \"AgentPassword\":\"" + agentPassword +                "\", \"AgentEmail\":\"" + agentEmail +                "\", \"DeviceID\":\"" + Utils.getDeviceEMEI(context) + "\"");

        // Add StartDate if it's not null or empty
        if (startDate != null && !startDate.trim().isEmpty()) {
            jsonInputString.append(", \"StartDate\":\"").append(startDate).append("\"");
        }

        // Add EndDate if it's not null or empty
        if (endDate != null && !endDate.trim().isEmpty()) {
            jsonInputString.append(", \"EndDate\":\"").append(endDate).append("\"");
        }

        // Close the JSON string
        jsonInputString.append("}");

        return HelperClass.sendPostRequest(BuildConfig.API_BASE_URL, jsonInputString.toString());
    }

}