package com.example.testingmyskills.JavaClasses;

import android.content.Context;
import android.os.AsyncTask;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailSender {
    static MailjetClient client;
    private static String API_KEY ="6c3afe2c49b9577c76173a5b89cdf8a3";
    private static String SECRET_KEY ="d02384dbbc4ec9787de3235aaa9c2736";
    static MailjetRequest request;
    static MailjetResponse response;

    public static void sendEmail(Context context, String email, String name, String password) throws JSONException {

        // Initialize the Mailjet client with API key and secret
        client = new MailjetClient(API_KEY, SECRET_KEY);
        Utils.success(context, "Email");
        // Create the email request
        request = new MailjetRequest(Emailv31.resource)
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
                                .put(Emailv31.Message.HTMLPART, "<h3>Dear App user, your app password is as follows!</h3><br />Password: " + password + "\n Try not to forget it next time")));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                // Send the email

                response = client.post(request);

                System.out.println("============================================================================");
                System.out.println(response.getStatus());
                System.out.println(response.getData());
                System.out.println("============================================================================");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
