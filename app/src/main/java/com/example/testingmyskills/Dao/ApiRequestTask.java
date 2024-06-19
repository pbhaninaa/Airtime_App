package com.example.testingmyskills.Dao;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ApiRequestTask extends AsyncTask<String, Void, String> {
    public static String SERVER_URL = "http://102.219.85.66:7022/xmlrpc";
    public static String USERNAME = "QuposUSDTest";
    public static String PASSWORD = "pass123";
    public static String MSISDN = "263781801175";
    @Override
    public  String doInBackground(String... params) {
        String sUsername = USERNAME;
        String sPassword = PASSWORD;
        String sMSISDN = MSISDN;
        String sURI = SERVER_URL;
        String sTransactionType = "Balance Enquiry";
        String sAmount = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        String strDate = sdf.format(new Date());

        try {
            // Create a new XML document with the given values
            Document xmlRequest = xmlBalanceEnquiry(sUsername, sPassword, sMSISDN, sTransactionType, sAmount);

            // Convert the Xml to a byte array for the HTTPS
            byte[] requestData = xmlRequestToString(xmlRequest).getBytes("UTF-8");
            Log.d("ApiRequestTask", strDate + xmlRequestToString(xmlRequest));

            // Define the request
            URL url = new URL(sURI);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestData);
                os.flush();
            }

            StringBuilder result = new StringBuilder();
            try (InputStream is = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }

            String responseStr = result.toString();
            apiResponse(responseStr);

            String strDate2 = sdf.format(new Date());
            Log.d("ApiRequestTask", strDate2 + responseStr);

            //---
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(responseStr.getBytes("UTF-8")));

            NodeList structNodes = doc.getElementsByTagName("struct");

            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < structNodes.getLength(); i++) {
                Element structNode = (Element) structNodes.item(i);

                String accountType = structNode.getElementsByTagName("AccountType").item(0).getTextContent();
                String currency = structNode.getElementsByTagName("Currency").item(0).getTextContent();
                String amount = structNode.getElementsByTagName("Amount").item(0).getTextContent();

                list.add("AccountType: " + accountType + ", Currency: " + currency + ", Amount: " + amount);
                Log.d("ApiRequestTask", "AccountType: " + accountType + ", Currency: " + currency + ", Amount: " + amount);
            }
        } catch (Exception ex) {
            Log.e("ApiRequestTask", "*** Exception : " + strDate + "   " + ex.getMessage(), ex);
        }

        return null;
    }
//    public static Document xmlBalanceEnquiry(String sUsername, String sPassword, String sMSISDN, String sTransactionType, String sAmount) throws Exception {
//        // Create an XML Document
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.newDocument();
//
//        // Construct the XML
//        Element rootElement = doc.createElement("BalanceEnquiry");
//        doc.appendChild(rootElement);
//
//        Element usernameElement = doc.createElement("Username");
//        usernameElement.appendChild(doc.createTextNode(sUsername));
//        rootElement.appendChild(usernameElement);
//
//        Element passwordElement = doc.createElement("Password");
//        passwordElement.appendChild(doc.createTextNode(sPassword));
//        rootElement.appendChild(passwordElement);
//
//        Element msisdnElement = doc.createElement("MSISDN");
//        msisdnElement.appendChild(doc.createTextNode(sMSISDN));
//        rootElement.appendChild(msisdnElement);
//
//        Element transactionTypeElement = doc.createElement("TransactionType");
//        transactionTypeElement.appendChild(doc.createTextNode(sTransactionType));
//        rootElement.appendChild(transactionTypeElement);
//
//        Element amountElement = doc.createElement("Amount");
//        amountElement.appendChild(doc.createTextNode(sAmount));
//        rootElement.appendChild(amountElement);
//        printDocument(doc);
//        return doc;
//    }

    public static Document xmlBalanceEnquiry(String sUsername, String sPassword, String sMSISDN, String sTransactionType, String sAmount) throws Exception {
        // Create and return an XML Document similar to xmlBalanceEnquiry in C#
        // This is a placeholder function and should be implemented with actual XML creation logic
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        // Construct the XML here
        // ...
printDocument(doc);
        return doc;
    }
public static void printDocument(Document doc) throws Exception {
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

    StreamResult result = new StreamResult(new StringWriter());
    DOMSource source = new DOMSource(doc);
    transformer.transform(source, result);

    String xmlString = result.getWriter().toString();
    System.out.println(xmlString);
}
    private static String xmlRequestToString(Document xmlRequest) throws Exception {
        // Convert Document to String
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(xmlRequest), new StreamResult(writer));
        return writer.getBuffer().toString();
    }

    private static void apiResponse(String response) {
        // Handle API response
        // This is a placeholder function and should be implemented
    }
}
