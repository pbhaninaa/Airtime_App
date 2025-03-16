package com.example.testingmyskills.JavaClasses;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import zw.co.paynow.core.Paynow;
import zw.co.paynow.core.Payment;
import zw.co.paynow.responses.WebInitResponse;

public class PayNowPaymentProcessor {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void createPayNowOrder(Context context, String item, double price, PayNowCallback callback) {
        executorService.execute(() -> {
            try {
                String paynowApiKey = context.getString(com.example.testingmyskills.R.string.paynow_api_key);
                String paynowCode = context.getString(com.example.testingmyskills.R.string.paynow_code);
                String userEmail = context.getString(com.example.testingmyskills.R.string.paynow_user_email);

                Paynow paynow = new Paynow(paynowCode, paynowApiKey);

                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String invoiceReference = "Invoice_" + timeStamp;

                Payment payment = paynow.createPayment(invoiceReference, userEmail);
                payment.add(item, price);

                WebInitResponse response = paynow.send(payment);


                if (response.isRequestSuccess()) {
                    callback.onSuccess(response.redirectURL());
                } else {
                    callback.onFailure("Error: " + response.errors());
                }
            } catch (Exception e) {
                Log.e("PayNowPaymentProcessor", "Payment Error: " + e.getMessage());
                callback.onFailure("Payment Error: " + e.getMessage());
            }
        });
    }

    public interface PayNowCallback {
        void onSuccess(String redirectUrl);
        void onFailure(String error);
    }
}
