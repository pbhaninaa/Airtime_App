package com.example.testingmyskills.JavaClasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.testingmyskills.R;

import java.text.DecimalFormat;

public class Utils {
    public static final String PREF_NAME = "UserPrefs";
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    public static void hideSoftNavBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
    public static void hideAlphaKeyboard(AlphaKeyboard myKeyboard) {
        if (myKeyboard.getParent() != null) {
            ((ViewGroup) myKeyboard.getParent()).removeView(myKeyboard);
        }
    }
    public static void showAlphaKeyboard(AlphaKeyboard myKeyboard, Activity activity, int gravity) {
        if (myKeyboard.getParent() == null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = gravity;
            ((ViewGroup) activity.findViewById(android.R.id.content)).addView(myKeyboard, layoutParams);
        }
    }
    public static void showToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = layout.findViewById(R.id.textViewToast);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
    public static void success(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = layout.findViewById(R.id.textViewToast);
        layout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.dark_green));
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    // Function to save email and password to SharedPreferences
    public static void saveCredentials(Context context, String email, String password) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(EMAIL_KEY, email);
        editor.putString(PASSWORD_KEY, password);
        editor.apply();
    }
    // Function to get email from SharedPreferences
    public static String getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(EMAIL_KEY, "");
    }
    public static boolean isUserLogged(Context context){
        SharedPreferences prefs = context.getSharedPreferences("StayLogged", Context.MODE_PRIVATE);
        return prefs.getBoolean("isUserLogged", false);
    }
    // Function to get password from SharedPreferences
    public static String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PASSWORD_KEY, "");
    }
    public static void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        SharedPreferences prefs = context.getSharedPreferences("StayLogged", Context.MODE_PRIVATE);
        SharedPreferences.Editor StayLogged= prefs.edit();
        StayLogged.clear();
        StayLogged.apply();

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor user= preferences.edit();
        user.clear();
        user.apply();
    }
    public static String FormatAmount(String a) {
        String amountString = a.replace(",", "");
        try {
            double parsedAmount = Double.parseDouble(amountString);
            double amountConverted = Math.round(parsedAmount * 100.00) / 100.00;
            // Format the double value with commas separating every three digits before the decimal point and two decimal places
            DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00");
            return decimalFormat.format(amountConverted);
        } catch (NumberFormatException e) {
            System.out.println(amountString);
            return "NaN"; // Indicate that the input could not be parsed
        }
    }

    public static boolean isValidEmail(String email) {
        // Email regex pattern to validate email format
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

        // Check if the provided email matches the regex pattern
        return email.matches(emailPattern);
    }
}