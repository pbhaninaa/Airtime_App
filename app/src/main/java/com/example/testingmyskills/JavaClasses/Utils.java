package com.example.testingmyskills.JavaClasses;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.testingmyskills.R;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static final String PREF_NAME = "UserPrefs";
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    private static final String REMEMBER_ME = "rememberMe";

    public static void hideSoftNavBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }


    public static void shakeView(View view, Context context) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        view.startAnimation(shake);
    }

    public static void triggerHapticFeedback(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("HapticFeedback", "Vibrating with VibrationEffect");
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)); // Increase time for testing
            } else {
                Log.d("HapticFeedback", "Vibrating with pre-O vibration");
                vibrator.vibrate(100); // Increase time for testing
            }
        } else {
            Log.d("HapticFeedback", "Vibrator is null");
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
        text.setTextColor(ContextCompat.getColor(context, R.color.white));
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

    public static void saveAutoFillPermission(Context context, boolean rememberMe) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(REMEMBER_ME, rememberMe);
        editor.apply();
    }

    // Function to get email from SharedPreferences
    public static String getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(EMAIL_KEY, "");
    }

    public static boolean RememberMe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(REMEMBER_ME, false);
    }

    public static boolean isUserLogged(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("StayLogged", Context.MODE_PRIVATE);
        return prefs.getBoolean("isUserLogged", false);
    }

    public static boolean isUserRegistered(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("StayLogged", Context.MODE_PRIVATE);
        return prefs.getBoolean("isUserRegistered", false);
    }

    // Function to get password from SharedPreferences
    public static String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PASSWORD_KEY, "");
    }

    // Function to get password from SharedPreferences
    public static String getString(Context context, String Pref_name, String Pref_Key) {
        SharedPreferences prefs = context.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);
        return prefs.getString(Pref_Key, "");
    }

    public static void saveString(Context context, String Pref_name, String Pref_Key, String Value) {
        SharedPreferences prefs = context.getSharedPreferences(Pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Pref_Key, Value);
        editor.apply();
    }

    public static void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        SharedPreferences prefs = context.getSharedPreferences("StayLogged", Context.MODE_PRIVATE);
        SharedPreferences.Editor StayLogged = prefs.edit();
        StayLogged.clear();
        StayLogged.apply();

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor user = preferences.edit();
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

    public static void setStatusColor(Activity activity, String bal, ImageView statusLight) {

        // Check if bal is null or empty
        if (bal == null || bal.trim().isEmpty()) {
            statusLight.setColorFilter(ContextCompat.getColor(activity, R.color.red)); // Set to red for invalid balance
            return;
        }

        // Parse the balance amount to a double
        double balance;
        try {
            balance = Double.parseDouble(bal.trim()); // Trim to remove any leading or trailing spaces
        } catch (NumberFormatException e) {
            statusLight.setColorFilter(ContextCompat.getColor(activity, R.color.red)); // Set to red for invalid number format
            return;
        }

        // Set the default image resource
        statusLight.setImageResource(R.drawable.round_conners_background);

        // Set appropriate colors based on the remaining balance
        if (balance > 3000) {
            statusLight.setColorFilter(ContextCompat.getColor(activity, R.color.lime)); // Green color
        } else if (balance > 2000) {
            statusLight.setColorFilter(ContextCompat.getColor(activity, R.color.gold)); // Yellow color
        } else if (balance > 1000) {
            statusLight.setColorFilter(ContextCompat.getColor(activity, R.color.orange)); // Orange color
        } else {
            statusLight.setColorFilter(ContextCompat.getColor(activity, R.color.red)); // Red color
        }
    }

    public static String ref() {
        String strWorkstationNum = "1"; // Example workstation number

        String strBasketPrefix = "QU";
        Date date = new Date();
        String strBasket = "";
        String basketID = "";
        String format = "";
        String strTemp = "0";
        String strWID = "0";

        if (strWorkstationNum.length() == 2) {
            strWID = strWorkstationNum;
            format = "ddHHmmss";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            basketID = dateFormat.format(date);
            strBasket = strBasketPrefix + strWID + basketID;
        } else if (strWorkstationNum.length() == 1) {
            strWID = strTemp + strWorkstationNum;
            format = "ddHHmmss";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            basketID = dateFormat.format(date);
            strBasket = strBasketPrefix + strWID + basketID;
        } else {
            format = "MMddHHmmss";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            basketID = dateFormat.format(date);
            strBasket = strBasketPrefix + basketID;
        }

        System.out.println("Generated Basket ID: " + strBasket);
        return strBasket;
    }


}
