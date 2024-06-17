package com.example.testingmyskills.JavaClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends AsyncTask<Void, Void, Boolean> {
    private final String smtpHost = "smtp.gmail.com";
    private final String smtpPort = "587";
    private final String senderEmail = "your_email@gmail.com"; // replace with your email
    private final String senderPassword = "your_email_password"; // replace with your email password
    private String recipientEmail;
    private String subject;
    private String body;
    private Context context;

    public EmailSender(Context context, String recipientEmail, String subject, String body) {
        this.context = context;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", smtpHost);
            properties.put("mail.smtp.port", smtpPort);

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(context, "Email sent successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Not yet done.", Toast.LENGTH_SHORT).show();
        }
    }
}
