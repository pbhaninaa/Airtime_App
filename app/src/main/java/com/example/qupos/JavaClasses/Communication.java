package com.example.qupos.JavaClasses;

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

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Communication {
    private static final String API_KEY = "6c3afe2c49b9577c76173a5b89cdf8a3"; // Be cautious with hardcoding this
    private static final String SECRET_KEY = "d02384dbbc4ec9787de3235aaa9c2736";

    public static void sendEmailInEmailV31(Context context, String email, String password, String agentName) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");

        MailjetClient client = new MailjetClient(API_KEY, SECRET_KEY);

        // Create the email request
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
//                                        .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "quposadmin@qupos.com")  // Replace this with your new email address
                                        .put("Name", "Qupos Admin"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", name)))
                                .put(Emailv31.Message.SUBJECT, "Requested App Password.")
                                .put(Emailv31.Message.TEXTPART, "My first Mailjet email")
                                .put(Emailv31.Message.HTMLPART, "<h4>Dear " + agentName + ",<br/> your app password is below!</h4>" + password)));

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

    public static boolean sendEmailsInSMTP(String recipientEmail, String agentName, String agentPassword) {
        String host = "smtp.gmail.com";
        final String fromEmail = "quposreports@gmail.com";
        final String password = "nporpwlqawpzslgk";  // Replace with secure storage
        int port = 587;

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            // Use Callable to return a boolean value
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    try {
                        Session session = Session.getInstance(properties, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(fromEmail, password);
                            }
                        });

                        MimeMessage message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(fromEmail));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
                        message.setSubject("Requested App Password.");
                        message.setText("Dear " + agentName + ",\nYour app password is below!\n" + agentPassword);

                        Transport.send(message);

                        new Handler(Looper.getMainLooper()).post(() -> {
                            System.out.println("Email sent successfully to " + recipientEmail);
                        });

                        return true;  // Email sent successfully

                    } catch (MessagingException e) {
                        e.printStackTrace();
                        new Handler(Looper.getMainLooper()).post(() -> {
                            System.out.println("Failed to send email to " + recipientEmail);
                        });
                        return false;  // Email failed
                    }
                }
            });

            boolean result = future.get();  // Wait for execution and get result
            executorService.shutdown();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            executorService.shutdown();
            return false;
        }
    }public static void sendSMS(Context context, String phoneNumber, String message) {
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
