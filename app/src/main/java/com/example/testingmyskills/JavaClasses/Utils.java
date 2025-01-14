package com.example.testingmyskills.JavaClasses;


import static android.content.Context.MODE_PRIVATE;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.testingmyskills.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
public class Utils {
    public static final String PREF_NAME = "UserPrefs";
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    private static final String REMEMBER_ME = "rememberMe";

    public static void hideSoftNavBar(Activity activity) {
//        View decorView = activity.getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//        corVie      | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        dew.setSystemUiVisibility(uiOptions);
    }

    public static void rotateImageView(ImageView imageView) {
        // Create an ObjectAnimator to rotate the ImageView
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotateAnimator.setDuration(1000);  // Duration for one full rotation (in milliseconds)
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);  // Repeat infinitely
        rotateAnimator.setRepeatMode(ObjectAnimator.RESTART);  // Restart animation after one full rotation
        rotateAnimator.start();  // Start the animation
    }

    public static void showEmailDialog(Context context) {
        // Create a LinearLayout to hold the EditTexts
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create an EditText for the email input
        final EditText emailInput = new EditText(context);
        emailInput.setBackgroundResource(R.drawable.edit_text_background); // Set custom background
        emailInput.setHint("Enter email address");

        // Create an EditText for the User ID input
        final EditText userIdInput = new EditText(context);
        userIdInput.setBackgroundResource(R.drawable.edit_text_background);
        userIdInput.setInputType(InputType.TYPE_CLASS_PHONE); // Allow only numeric input

// Set the main hint
        userIdInput.setHint("Enter User ID");

// Create a smaller text example for the hint
        String exampleHint = " (e.g., 263783241537)";
        SpannableString spannableString = new SpannableString(userIdInput.getHint() + exampleHint);
        spannableString.setSpan(new RelativeSizeSpan(0.8f), userIdInput.getHint().length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Set the size of the example

        userIdInput.setHint(spannableString); // Set the combined hint with the example

        // Set margin for both EditTexts
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Use the Utils method to convert dp to px for margins
        int marginInDp = 20; // 50dp margin
        int marginInPx = Utils.convertDpToPx(context, marginInDp); // Convert dp to pixels
        params.setMargins(marginInPx, 5, marginInPx, 5);

        // Apply margins to both EditTexts
        emailInput.setLayoutParams(params);
        userIdInput.setLayoutParams(params);

        // Add both EditTexts to the LinearLayout
        layout.addView(emailInput);
        layout.addView(userIdInput);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Send Email")
                .setMessage("Please enter your email address and User ID:")
                .setView(layout) // Set the layout containing the EditTexts
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String recipientEmail = emailInput.getText().toString().trim();
                        String userId = userIdInput.getText().toString().trim(); // Get the User ID

                        String recipientPassword = "Philas@12345"; // Replace this with the actual password

                        if (recipientEmail.isEmpty() && userId.isEmpty()) {
                            showToast(context, "Email and User ID cannot be empty.");
                        } else if (recipientEmail.isEmpty()) {
                            showToast(context, "Email cannot be empty.");
                        } else if (userId.isEmpty()) {
                            showToast(context, "User ID cannot be empty.");
                        } else if (!isValidEmail(recipientEmail)) {
                            showToast(context, "Please enter a valid email address.");
                        } else if (!isValidUserId(userId)) {
                            showToast(context, "User ID must include a valid country code and cannot start with 0.");
                        } else {
                            // Send the email including the User ID in the body
                            try {
                                Communication.sendEmail(context, recipientEmail, recipientPassword);
                                showToast(context, "Email sent successfully!");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                })


                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    public static void showSMSDialog(Context context) {
        // Create a LinearLayout to hold the EditTexts
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create an EditText for the phone number input
        final EditText phoneNumberInput = new EditText(context);
        phoneNumberInput.setBackgroundResource(R.drawable.edit_text_background); // Set custom background
        phoneNumberInput.setHint("Enter phone number");

        // Create an EditText for the SMS message input
        final EditText messageInput = new EditText(context);
        messageInput.setBackgroundResource(R.drawable.edit_text_background);
        messageInput.setHint("Enter your message");

        // Set margin for both EditTexts
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Use the Utils method to convert dp to px for margins
        int marginInDp = 20; // 20dp margin
        int marginInPx = Utils.convertDpToPx(context, marginInDp); // Convert dp to pixels
        params.setMargins(marginInPx, 5, marginInPx, 5);

        // Apply margins to both EditTexts
        phoneNumberInput.setLayoutParams(params);
        messageInput.setLayoutParams(params);

        // Add both EditTexts to the LinearLayout
        layout.addView(phoneNumberInput);
        layout.addView(messageInput);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Send SMS")
                .setMessage("Please enter the phone number and message:")
                .setView(layout) // Set the layout containing the EditTexts
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phoneNumber = phoneNumberInput.getText().toString().trim();
                        String message = messageInput.getText().toString().trim(); // Get the message

                        if (phoneNumber.isEmpty() && message.isEmpty()) {
                            showToast(context, "Phone number and message cannot be empty.");
                        } else if (phoneNumber.isEmpty()) {
                            showToast(context, "Phone number cannot be empty.");
                        } else if (message.isEmpty()) {
                            showToast(context, "Message cannot be empty.");
//                        } else if (!isValidPhoneNumber(phoneNumber)) {
                        } else if (phoneNumber.length() != 10) {
                            showToast(context, "Please enter a valid phone number.");
                        } else {
                            // Send the SMS
                            Communication.sendSMS(context, phoneNumber, message);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        // Example validation for phone number (basic check for length and if it starts with a country code)
        return phoneNumber.matches("^\\+?[1-9][0-9]{7,14}$"); // Simple regex for international phone numbers
    }


    public static int convertDpToPx(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
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
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(EMAIL_KEY, email);
        editor.putString(PASSWORD_KEY, password);
        editor.apply();
    }

    public static void saveAutoFillPermission(Context context, boolean rememberMe) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(REMEMBER_ME, rememberMe);
        editor.apply();
    }

    // Function to get email from SharedPreferences
    public static String getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return prefs.getString(EMAIL_KEY, "");
    }

    public static boolean RememberMe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return prefs.getBoolean(REMEMBER_ME, false);
    }

    public static boolean isUserLogged(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("LoggedUserCredentials", MODE_PRIVATE);
        return prefs.getBoolean("isUserLogged", false);
    }


    // Function to get password from SharedPreferences
    public static String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return prefs.getString(PASSWORD_KEY, "");
    }

    // Function to get password from SharedPreferences
    public static String getString(Context context, String Pref_name, String Pref_Key) {
        SharedPreferences prefs = context.getSharedPreferences(Pref_name, MODE_PRIVATE);
        return prefs.getString(Pref_Key, "");
    }

    public static void saveString(Context context, String Pref_name, String Pref_Key, String Value) {
        SharedPreferences prefs = context.getSharedPreferences(Pref_name, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Pref_Key, Value);
        editor.apply();
    }

    public static void logout(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();

        SharedPreferences prefs = context.getSharedPreferences("StayLogged", MODE_PRIVATE);
        SharedPreferences.Editor StayLogged = prefs.edit();
        StayLogged.clear();
        StayLogged.apply();

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor user = preferences.edit();
        user.clear();
        user.apply();

        SharedPreferences preferences1 = context.getSharedPreferences("LoggedUserCredentials", MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences1.edit();
        ed.clear();  // Clear all data in this SharedPreferences
        ed.apply();  // Use apply() to asynchronously save changes


    }



    public static String FormatAmount(String a) {
        // Handle locale-specific decimal and grouping separators
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.getDefault());
        char decimalSeparator = symbols.getDecimalSeparator();
        char groupingSeparator = symbols.getGroupingSeparator();

        // Remove grouping separators (e.g., commas in US or periods in some European countries)
        String amountString = a.replace(String.valueOf(groupingSeparator), "");

        // Replace the decimal separator (e.g., comma for some locales) with a period for parsing
        amountString = amountString.replace(decimalSeparator, '.');

        if (a.contains(String.valueOf(decimalSeparator))) {
            // If there's a decimal separator in the original string, return the formatted input
            return a;
        }

        try {
            double parsedAmount = Double.parseDouble(amountString);

            // Round to two decimal places
            double amountConverted = Math.round(parsedAmount * 100.00) / 100.00;

            // Create a DecimalFormat instance for the output with the current locale
            DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00", symbols);

            // Format the double value and return it
            return decimalFormat.format(amountConverted);
        } catch (NumberFormatException e) {
            System.out.println(amountString);
            return "NaN"; // Indicate that the input could not be parsed
        }
    }

    public static void setFieldFocus(EditText field, Context context) {
        // Request focus for the field
        field.requestFocus();

        // Get the InputMethodManager system service
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Show the soft keyboard
        if (imm != null) {
            imm.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideSoftKeyboard(Context context) {
        // Get the InputMethodManager system service
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Get the current window token
        View view = ((Activity) context).getCurrentFocus();

        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void saveRefs(Context context, String network, String agentID, String customerID, String referenceID) {
        SharedPreferences sharedPref = context.getSharedPreferences("LastTransactionRefs", MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPref.edit();
        ed.putString("network", network);
        ed.putString("agentID", agentID);
        ed.putString("customerID", customerID);
        ed.putString("referenceID", referenceID);
        ed.apply();
    }

    public static boolean isValidEmail(String email) {
        // Email regex pattern to validate email format
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

        // Check if the provided email matches the regex pattern
        return email.matches(emailPattern);
    }

    private static boolean isValidUserId(String userId) {
        return userId.matches("^(\\d{1,3})([1-9][0-9]{7,9})$");
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
        if (balance > 50.00) {
            statusLight.setColorFilter(ContextCompat.getColor(activity, R.color.lime)); // Green color
        } else if (balance > 30.00) {
            statusLight.setColorFilter(ContextCompat.getColor(activity, R.color.gold)); // Yellow color
        } else if (balance > 20.00) {
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
