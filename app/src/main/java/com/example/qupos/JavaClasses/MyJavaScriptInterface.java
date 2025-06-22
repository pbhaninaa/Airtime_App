package com.example.qupos.JavaClasses;

import android.app.Activity;
import android.webkit.JavascriptInterface;

public class MyJavaScriptInterface {
    private Activity activity;

    // Constructor accepts an Activity reference
    public MyJavaScriptInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void processPaymentResult(String result) {
        // Use the activity reference to run code on the UI thread
        activity.runOnUiThread(() -> {
            System.out.println("Payment Result: " + result);
            // Perform actions based on the result
            handlePaymentResult(result);  // You need to implement this in your activity or elsewhere
        });
    }

    // This method should handle the result in your activity
    private void handlePaymentResult(String result) {
        // Logic to handle payment result (success/failure)
        System.out.println("Handling payment result: " + result);
    }
}

