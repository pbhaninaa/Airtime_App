package com.example.testingmyskills.JavaClasses;


import static android.content.Context.MODE_PRIVATE;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.testingmyskills.R;
import com.google.android.gms.maps.model.Dash;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.mail.MessagingException;

import android.telephony.TelephonyManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.Manifest;

import androidx.core.app.ActivityCompat;

import java.util.List;

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


    public static void rotateImageView(ImageView imageView) {
        // Create an ObjectAnimator to rotate the ImageView
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotateAnimator.setDuration(1000);  // Duration for one full rotation (in milliseconds)
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);  // Repeat infinitely
        rotateAnimator.setRepeatMode(ObjectAnimator.RESTART);  // Restart animation after one full rotation
        rotateAnimator.start();  // Start the animation
    }

    public static void LoadingLayout(Activity activity, Context context) {
        ConstraintLayout layout = activity.findViewById(R.id.load_layout);
        ImageView imageView = activity.findViewById(R.id.load_layout_image);

        if (layout == null || imageView == null) {
            showToast(context, "Error: Unable to find required views!");
            System.out.println("Error: Views are null!");
            return;
        }

        rotateImageView(imageView);
        layout.setVisibility(View.VISIBLE);


    }

    public static void CloseLoadingLayout(Activity activity, Context context) {
        ConstraintLayout layout = activity.findViewById(R.id.load_layout);
        ImageView imageView = activity.findViewById(R.id.load_layout_image);

        if (layout == null || imageView == null) {
            showToast(context, "Error: Unable to find required views!");
            System.out.println("Error: Views are null!");
            return;
        }

        rotateImageView(imageView);
        layout.setVisibility(View.GONE);
    }

    public static String getTodayDate() {
        // Get the current date
        Date date = new Date();

        // Format the date into "yyyy/MM/dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    // Static method for showing email dialog
    public static void showEmailDialog(final Context context, Activity activity) {
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

        String exampleHint = " (e.g., 263783241537)";
        SpannableString spannableString = new SpannableString(userIdInput.getHint() + exampleHint);
        spannableString.setSpan(new RelativeSizeSpan(0.8f), userIdInput.getHint().length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Set the size of the example

        userIdInput.setHint(spannableString);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        int marginInDp = 20;
        int marginInPx = convertDpToPx(context, marginInDp);
        params.setMargins(marginInPx, 5, marginInPx, 5);
        emailInput.setLayoutParams(params);
        userIdInput.setLayoutParams(params);
        layout.addView(emailInput);
        layout.addView(userIdInput);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Send Email")
                .setMessage("Enter Email Address and AgentID:")
                .setView(layout)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoadingLayout(activity, context);
                        final String recipientEmail = emailInput.getText().toString().trim();
                        final String userId = userIdInput.getText().toString().trim();

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
                            // Call getPassword to retrieve the agent password asynchronously
                            getPassword(context, userId, recipientEmail, new PasswordCallback() {
                                @Override
                                public void onPasswordRetrieved(String agentPassword, String agentName) {
                                    try {
                                        if (Communication.sendEmailsInSMTP(recipientEmail, agentName, agentPassword)) {
                                            Utils.showToast(context, "Email sent successfully.");
                                        } else {
                                            Utils.showToast(context, "Failed to send email.");
                                        }

                                        CloseLoadingLayout(activity, context);

                                    } catch (Exception e) {  // Change to general Exception
                                        e.printStackTrace();
                                        CloseLoadingLayout(activity, context);
                                        Utils.showToast(context, "Error sending email: " + e.getMessage());
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    // Handle error in retrieving password
                                    CloseLoadingLayout(activity, context);

                                    showToast(context, "User Not Found");
                                }
                            });
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

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // For Android 6.0 and above, we need to request the permission dynamically.
                if (context.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // From Android 10, the IMEI is accessible via getImei() for each SIM card
                        return telephonyManager.getImei();
                    } else {
                        // Before Android 10, getDeviceId() works
                        System.out.print("IMEI : " + telephonyManager.getDeviceId());
                        return telephonyManager.getDeviceId();
                    }
                } else {
                    // Handle case where permission is not granted
                    return "No Permission";
                }
            } else {
                // For Android versions below Marshmallow
                System.out.print("IMEI : " + telephonyManager.getDeviceId());
                return telephonyManager.getDeviceId();
            }
        }

        return null; // Return null if TelephonyManager is unavailable
    }

    public static void requestPermissions(Activity activity) {
        // List of permissions to request
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE, // Access phone state
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.VIBRATE,
                Manifest.permission.SEND_SMS
        };

        // Check and request permissions if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, 0);
                }
            }
        }
    }

    public static String getDeviceEMEI(Context context) {
        // Validate input
        String input = getIMEI(context);
        if (input == null || input.length() <= 7) {
            return input;  // If the string is too short or empty, return it as is
        }

        // Get the unmasked part and replace the rest with asterisks
        String unmaskedPart = input.substring(0, 7);
        String maskedPart = "*".repeat(input.length() - 7);

        return unmaskedPart + maskedPart;
    }

    public static String getDeviceDetails(Context context) {
        StringBuilder deviceDetails = new StringBuilder();

        // Add greeting
        deviceDetails.append("Dear ").append("Admin").append(",\n");
        deviceDetails.append("The following device has made transactions:\n");

        // Get Device Information
        deviceDetails.append("Device Model: ").append(Build.MODEL).append("\n");
        deviceDetails.append("Manufacturer: ").append(Build.MANUFACTURER).append("\n");

        // TelephonyManager to get IMEI and device details
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // Get IMEI (if permission is granted)
        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // For Android 10 and above, use getImei()
                        deviceDetails.append("IMEI NO: ").append(telephonyManager.getImei()).append("\n");
                    } else {
                        // For Android 9 and below, use getDeviceId()
                        deviceDetails.append("IMEI NO: ").append(telephonyManager.getDeviceId()).append("\n");
                    }
                } else {
                    deviceDetails.append("IMEI NO: Permission not granted\n");
                }
            } else {
                deviceDetails.append("IMEI NO: ").append(telephonyManager.getDeviceId()).append("\n");
            }
        } else {
            deviceDetails.append("IMEI NO: Device not available\n");
        }

        // Add additional device information
        deviceDetails.append("OS Version: ").append(Build.VERSION.RELEASE).append("\n");
        deviceDetails.append("API Level: ").append(Build.VERSION.SDK_INT).append("\n");

        // Get the device's location (actual location as an address)
        String locationInfo = getDeviceLocation(context);
        deviceDetails.append("Device Location: ").append(locationInfo).append("\n");

        return deviceDetails.toString();
    }

    private static String getDeviceLocation(Context context) {
        // Get the LocationManager service
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Check if we have location permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Get the last known location (assuming high accuracy location provider is available)
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                // Use Geocoder to convert coordinates into a human-readable address
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        // Return the address as a string (e.g., city, state, country)
                        return address.getAddressLine(0);  // First address line
                    } else {
                        return "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Unable to get location address";
                }
            } else {
                return "Location not available";
            }
        } else {
            return "Location permission not granted";
        }
    }

    // Static method for resetting password and using callback to return the result
    public static void getPassword(final Context context, String agentId, String agentEmail, final PasswordCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject res = ApiService.resetPassword(agentId, agentEmail, context);

                    if (res.getInt("responseCode") == 200) {
                        // To handle success and UI updates on the main thread
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String responseString = res.getString("response");
                                    System.out.println("Success: " + responseString);
                                    JSONObject responseJson = new JSONObject(responseString);
                                    JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                    JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                    JSONObject userObject = paramsList.getJSONObject(0);

                                    String agentPassword = userObject.getString("agentPassword");
                                    String agentName = userObject.getString("agentName");

                                    callback.onPasswordRetrieved(agentPassword, agentName);  // Return the result through callback
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    callback.onError("Error parsing response.");
                                }
                            }
                        });
                    } else {
                        // Handle error response on UI thread
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError(res.toString());  // Pass error message to callback
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle IOException on UI thread
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError("An unexpected error occurred.");
                }
            }
        }).start();
    }

    // Callback interface for retrieving the password
    public interface PasswordCallback {
        void onPasswordRetrieved(String agentPassword, String agentName);

        void onError(String errorMessage);
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

    public static void setCaps(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isUpdating) {
                    isUpdating = true;
                    String text = s.toString();
                    if (!text.isEmpty() && Character.isLowerCase(text.charAt(0))) {
                        // Capitalize the first letter
                        String updatedText = Character.toUpperCase(text.charAt(0)) + text.substring(1);
                        editText.setText(updatedText);
                        editText.setSelection(updatedText.length()); // Move cursor to the end
                    }
                    isUpdating = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });
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