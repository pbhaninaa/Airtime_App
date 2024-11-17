package com.example.testingmyskills.JavaClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Communication {
    private static final String API_KEY = "6c3afe2c49b9577c76173a5b89cdf8a3"; // Be cautious with hardcoding this
    private static final String SECRET_KEY = "d02384dbbc4ec9787de3235aaa9c2736"; // Same as above

    public static void sendEmail(Context context, String email, String password) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");

        MailjetClient client = new MailjetClient(API_KEY, SECRET_KEY);

        // Create the email request
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "kwazibhani@gmail.com")
                                        .put("Name", "Philasande Bhani"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", name)))
                                .put(Emailv31.Message.SUBJECT, "Requested App Password.")
                                .put(Emailv31.Message.TEXTPART, "My first Mailjet email")
                                .put(Emailv31.Message.HTMLPART, "<h3>Dear " + name + ",<br/> your app password is as follows!</h3><br />Password: " + password + "\n Try not to forget it next time")));

        // Use ExecutorService for asynchronous task
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                MailjetResponse response = client.post(request);
                if (response.getStatus() == 200) {
                    // Successfully sent email
                    System.out.println("Email sent successfully.");
                } else {
                    // Failed to send email
                    System.out.println("Failed to send email. Status: " + response.getStatus());
                    System.out.println("Error: " + response.getData().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public static void sendSMS(Context context, String phoneNumber,  String message) {
        // Ensure the phone number is valid
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Get SmsManager instance
            SmsManager smsManager = SmsManager.getDefault();

            // Send SMS in a background thread to avoid blocking the UI
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    System.out.println("SMS sent successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed to send SMS.");
                }
            });
        } else {
            System.out.println("Invalid phone number.");
        }
    }
}
