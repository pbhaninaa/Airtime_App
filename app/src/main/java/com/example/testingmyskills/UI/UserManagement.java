package com.example.testingmyskills.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingmyskills.Dao.XMLRPCClient;
import com.example.testingmyskills.Interfaces.AccountValidationCallback;
import com.example.testingmyskills.JavaClasses.AlphaKeyboard;
import com.example.testingmyskills.JavaClasses.Country;
import com.example.testingmyskills.JavaClasses.EmailSender;
import com.example.testingmyskills.R;
import com.example.testingmyskills.JavaClasses.Utils;

import org.apache.xmlrpc.XmlRpcException;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import okhttp3.internal.Util;

//8QGGLHPVSQ3TFEX7QLTCRU2Y
public class UserManagement extends AppCompatActivity implements AccountValidationCallback {


    private ConstraintLayout SignInLayout;
    private ConstraintLayout SignUpLayout, RegScreen;
    private TextView RegisterBtn;
    private EditText getEmailTextInLogin;
    private EditText getPasswordTextInLogin;
    private ImageButton ShowPasswordInLogin;
    private Button SignInBtn;
    private CheckBox RememberMeCheckBox;
    private TextView ForgotPasswordBtn;
    private boolean showPassword;
    private TextView SignUpBtn;
    private EditText getEmailTextInRegister;
    private EditText getPasswordTextInRegister;
    private EditText getConfirmPasswordTextInRegister;
    private ImageButton ShowPasswordInRegister;
    private ImageButton ShowConformPasswordInRegister;
    private Button SignUp;
    private Spinner languagesSpinner, CountryCode;
    private ImageView CountryFlag;
    private EditText password,
            Firstname,
            Lastname,
            phoneNumber,
            email,
            emailConfirmation;
    private Button CreateAccBtn;
    String[] values = {"Select home language", "IsiXhosa", "IsiZulu", "Tswana", "IsiPedi", "Ndebele", "English"};
    boolean rememberMe;
    private FrameLayout backButton;
    private boolean gotData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils.hideSoftNavBar(UserManagement.this);
        showPassword = false;
        rememberMe = false;
        gotData = false;
        initialiseViews();
        setOnclickListeners();

        // Load screen based on the constraintLayoutId received from Intent
        int constraintLayoutId = getIntent().getIntExtra("constraintLayoutId", R.id.login_page);
        screenToLoad(constraintLayoutId);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, values);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        languagesSpinner.setAdapter(adapter);


        ArrayAdapter<Country> ada = new ArrayAdapter<>(this, R.layout.spinner_item, getCountryList());
        ada.setDropDownViewResource(R.layout.spinner_item);
        CountryCode.setAdapter(ada);

        CountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideBottomNav();
                Country selectedCountry = (Country) parent.getItemAtPosition(position);
                String flagName = selectedCountry.getCountryFlag();
                String countryName = selectedCountry.getCountryName();
                // Get the resource ID of the drawable dynamically
                int flagResourceId = getResources().getIdentifier(flagName, "drawable", getPackageName());
                // Set the ImageView with the corresponding flag
                if (flagResourceId != 0) {  // Check if the resource was found
                    CountryFlag.setImageResource(flagResourceId);
                } else {
                    Toast.makeText(getApplicationContext(), "Flag not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void hideBottomNav() {
        // Hide the bottom navigation bar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void validateUser(Map<String, Object> response) {
        int status = (int) response.get("Status");
        String des = Objects.requireNonNull(response.get("Description")).toString();
        if (status == 0) {
            gotData = true;
            Utils.showToast(this, des);
        } else if (status == 1) {
//            saveAccount();
            gotData = true;
        }
        System.out.println("Account Validation Res: " + response);
    }

//    private void APICall(String number, AccountValidationCallback callback) {
//        new Thread(() -> {
//            try {
//                Map<String, Object> response = XMLRPCClient.accountBalanceEnquiry(number, "validate_msisdn");
//                runOnUiThread(() -> {
//                    callback.validateUser(response);
//                });
//            } catch (XmlRpcException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
//    }

    private void initialiseViews() {
        SignInLayout = findViewById(R.id.login_page);
        getEmailTextInLogin = findViewById(R.id.email_in_login_page);
        getPasswordTextInLogin = findViewById(R.id.password_in_login_page);
        ShowPasswordInLogin = findViewById(R.id.show_password_in_login_page);
        SignInBtn = findViewById(R.id.sign_in_btn);
        SignUpLayout = findViewById(R.id.sign_up_page);
        RegScreen = findViewById(R.id.create_profile_screen);
        RegisterBtn = findViewById(R.id.register_btn);
        RememberMeCheckBox = findViewById(R.id.remember_me_check_box);
        ForgotPasswordBtn = findViewById(R.id.forgot_password);
        languagesSpinner = findViewById(R.id.languages);
        SignUpBtn = findViewById(R.id.sign_up_btn);
        getEmailTextInRegister = findViewById(R.id.email_in_register_page);
        getPasswordTextInRegister = findViewById(R.id.password_in_register_page);
        getConfirmPasswordTextInRegister = findViewById(R.id.confirm_password_in_register_page);
        ShowPasswordInRegister = findViewById(R.id.show_password_in_register_page);
        ShowConformPasswordInRegister = findViewById(R.id.show_confirm_password_in_register_page);
        SignUp = findViewById(R.id.create_account);
        CreateAccBtn = findViewById(R.id.CreateAccBtn);
        Firstname = findViewById(R.id.Fname);
        Lastname = findViewById(R.id.Lname);
        phoneNumber = findViewById(R.id.mNumber);
        email = findViewById(R.id.email);
        emailConfirmation = findViewById(R.id.emailC);
        backButton = findViewById(R.id.back);
        password = findViewById(R.id.password);
        RememberMeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rememberMe = isChecked;

        });
        CountryCode = findViewById(R.id.country_code);
        CountryFlag = findViewById(R.id.country_flag);
    }

    private void screenToLoad(int screenToLoad) {
        if (screenToLoad == R.id.create_profile_screen)
            getProfile();


        if (Utils.RememberMe(this)) {
            getEmailTextInLogin.setText(Utils.getString(this, "profile", "email"));
            RememberMeCheckBox.setChecked(true); // Ensure the checkbox is checked
        }
        SignInLayout.setVisibility(View.GONE);
        SignUpLayout.setVisibility(View.GONE);
        RegScreen.setVisibility(View.GONE);

        ConstraintLayout layout = findViewById(screenToLoad);
        layout.setVisibility(View.VISIBLE);
    }


    private void setOnclickListeners() {
        RegisterBtn.setOnClickListener(v -> handleRegisterClick());
        ShowPasswordInLogin.setOnClickListener(v -> handleShowPassword());
        SignInBtn.setOnClickListener(v -> handleSignIn());
//        ForgotPasswordBtn.setOnClickListener(v -> {
//            try {
////                handleForgotPasswordClick();
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//        });
        SignUpBtn.setOnClickListener(v -> handleBack());
//        SignUp.setOnClickListener(v -> handleCreateClick());
        ShowConformPasswordInRegister.setOnClickListener(v -> handleShowPassword());
        ShowPasswordInRegister.setOnClickListener(v -> handleShowPassword());
        CreateAccBtn.setOnClickListener(v -> {
            try {
                handleAccCreation();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        backButton.setOnClickListener(v -> handleBack());

    }

    private void handleRegisterClick() {
        RegScreen.setVisibility(View.VISIBLE);
        SignInLayout.setVisibility(View.GONE);

    }

    private void handleBack() {
        RegScreen.setVisibility(View.GONE);
        SignInLayout.setVisibility(View.VISIBLE);
    }

    private void handleAccCreation() throws Exception {
        String name = Firstname.getText().toString().trim();
        String surname = Lastname.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();
        String emailAddress = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() ||
                emailAddress.isEmpty() || pass.isEmpty()) {
            Utils.showToast(this, "Please fill all the fields");
        } else if (!Utils.isValidEmail(emailAddress)) {
            Utils.showToast(this, "Invalid email address format");
            email.requestFocus();
        } else if (phone.length() != 9) {
            Utils.showToast(this, "Incorrect mobile number");
            phoneNumber.requestFocus();
        } else {

//            RegScreen.setVisibility(View.GONE);
//            SignInLayout.setVisibility(View.VISIBLE);
//            getEmailTextInLogin.setText(emailAddress);
//            getPasswordTextInLogin.setText(pass);

            // Utils.saveString(this, "LoggedUser", "IsUserLogged", "Yes");


            XMLRPCClient.registerUserAsync(name, phone, emailAddress, pass, new XMLRPCClient.ResponseCallback() {
                @Override
                public void onSuccess(String response) {
                    System.out.println("====================Response:========================");
                    System.out.println(response);

                    if (response.equals("1")) {
                        // Registration was successful
                        RegScreen.setVisibility(View.GONE);
                        SignInLayout.setVisibility(View.VISIBLE);
                        getEmailTextInLogin.setText(emailAddress);
                        getPasswordTextInLogin.setText(pass);
                    } else {
                        // Handle other response codes
                        System.out.println("========================Error============================");
                        Utils.showToast(UserManagement.this, response);
                    }
                }


                @Override
                public void onError(Exception e) {
                    // Handle the error response here
                    Utils.showToast(UserManagement.this, e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

    private void handleSignIn() {
        String email = getEmailTextInLogin.getText().toString().trim();
        String password = getPasswordTextInLogin.getText().toString().trim();

        // Check if email or password is empty
        if (email.isEmpty()) {
            getEmailTextInLogin.setError("Email is required");
            return;
        }

        if (password.isEmpty()) {
            getPasswordTextInLogin.setError("Password is required");
            return;
        }
        XMLRPCClient.userLoginAsync(email, password, new XMLRPCClient.ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(response);
                    // Check if the access_token is present in the response
                    if (jsonResponse.has("authorization")) {
                        String token = jsonResponse.getJSONObject("authorization").getString("access_token");

                        // Extract user details
                        JSONObject userObject = jsonResponse.getJSONObject("user");
                        String fullName = userObject.getString("name");
                        String updatedAt = userObject.getString("updated_at");
                        String email = userObject.getString("email");
                        String phone = userObject.getString("phone_number");
                        String balance = userObject.getString("balance");

                        // Split full name into first name and surname
                        String[] nameParts = fullName.split(" ");
                        String firstName = nameParts.length > 0 ? nameParts[0] : "";
                        String surname = nameParts.length > 1 ? nameParts[1] : "";

                        // Convert the timestamp to a readable format
                        String formattedDate = formatTimestamp(updatedAt);

                        // Show the user details in a toast
                        String userDetails = "First Name: " + firstName +
                                "\nSurname: " + surname +
                                "\nUpdated At: " + formattedDate +
                                "\nEmail: " + email +
                                "\nPhone: " + phone;
                        Utils.showToast(UserManagement.this, userDetails);
                        System.out.println(userDetails);

                        if (!Utils.isTokenExpired(token)) {
                            Utils.showToast(UserManagement.this, "Login Successful");
                            saveAccount(firstName, surname, phone, email, balance, formattedDate);
                            // Navigate to the Dashboard
                            Intent intent = new Intent(UserManagement.this, Dashboard.class);
                            startActivity(intent);
                        } else {
                            Utils.showToast(UserManagement.this, "The token has expired");
                        }
                    } else {
                        // Handle case where authorization token is missing
                        Utils.showToast(UserManagement.this, "Authorization failed. Token missing.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(UserManagement.this, "Failed to parse response");
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle the error response
                Utils.showToast(UserManagement.this, e.getMessage());
                e.printStackTrace();
            }
        });


    }

    // Method to format the timestamp
    private String formatTimestamp(String timestamp) {
        try {
            // Parse the original timestamp with the correct format
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            originalFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set timezone to UTC since the input is in UTC
            Date date = originalFormat.parse(timestamp);

            // Convert to desired format
            SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            return desiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }


    private void handleShowPassword() {
        showPassword = !showPassword; // Invert the value

        if (showPassword) {
            // Show password
            getPasswordTextInLogin.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            getConfirmPasswordTextInRegister.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            getPasswordTextInRegister.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ShowPasswordInLogin.setImageResource(R.drawable.closed_eye);
            ShowPasswordInRegister.setImageResource(R.drawable.closed_eye);
            ShowConformPasswordInRegister.setImageResource(R.drawable.closed_eye);

        } else {
            // Hide password
            getPasswordTextInLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            getPasswordTextInRegister.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            getConfirmPasswordTextInRegister.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ShowPasswordInLogin.setImageResource(R.drawable.open_eye);
            ShowPasswordInRegister.setImageResource(R.drawable.open_eye);
            ShowConformPasswordInRegister.setImageResource(R.drawable.open_eye);

        }
    }

    public static List<Country> getCountryList() {

        List<Country> countryList = new ArrayList<>();

        countryList.add(new Country("+27", "South Africa", "za"));
        countryList.add(new Country("+93", "Afghanistan", "af"));
        countryList.add(new Country("+355", "Albania", "al"));
        countryList.add(new Country("+213", "Algeria", "dz"));
        countryList.add(new Country("+1-684", "American Samoa", "as"));
        countryList.add(new Country("+376", "Andorra", "ad"));
        countryList.add(new Country("+244", "Angola", "ao"));
        countryList.add(new Country("+1-264", "Anguilla", "ai"));
        countryList.add(new Country("+672", "Antarctica", "aq"));
        countryList.add(new Country("+1-268", "Antigua and Barbuda", "ag"));
        countryList.add(new Country("+54", "Argentina", "ar"));
        countryList.add(new Country("+374", "Armenia", "am"));
        countryList.add(new Country("+297", "Aruba", "aw"));
        countryList.add(new Country("+61", "Australia", "au"));
        countryList.add(new Country("+43", "Austria", "at"));
        countryList.add(new Country("+994", "Azerbaijan", "az"));
        countryList.add(new Country("+1-242", "Bahamas", "bs"));
        countryList.add(new Country("+973", "Bahrain", "bh"));
        countryList.add(new Country("+880", "Bangladesh", "bd"));
        countryList.add(new Country("+1-246", "Barbados", "bb"));
        countryList.add(new Country("+375", "Belarus", "by"));
        countryList.add(new Country("+32", "Belgium", "be"));
        countryList.add(new Country("+501", "Belize", "bz"));
        countryList.add(new Country("+229", "Benin", "bj"));
        countryList.add(new Country("+1-441", "Bermuda", "bm"));
        countryList.add(new Country("+975", "Bhutan", "bt"));
        countryList.add(new Country("+591", "Bolivia", "bo"));
        countryList.add(new Country("+387", "Bosnia and Herzegovina", "ba"));
        countryList.add(new Country("+267", "Botswana", "bw"));
        countryList.add(new Country("+55", "Brazil", "br"));
        countryList.add(new Country("+246", "British Indian Ocean Territory", "io"));
        countryList.add(new Country("+1-284", "British Virgin Islands", "vg"));
        countryList.add(new Country("+673", "Brunei", "bn"));
        countryList.add(new Country("+359", "Bulgaria", "bg"));
        countryList.add(new Country("+226", "Burkina Faso", "bf"));
        countryList.add(new Country("+257", "Burundi", "bi"));
        countryList.add(new Country("+855", "Cambodia", "kh"));
        countryList.add(new Country("+237", "Cameroon", "cm"));
        countryList.add(new Country("+1", "Canada", "ca"));
        countryList.add(new Country("+238", "Cape Verde", "cv"));
        countryList.add(new Country("+1-345", "Cayman Islands", "ky"));
        countryList.add(new Country("+236", "Central African Republic", "cf"));
        countryList.add(new Country("+235", "Chad", "td"));
        countryList.add(new Country("+56", "Chile", "cl"));
        countryList.add(new Country("+86", "China", "cn"));
        countryList.add(new Country("+61", "Christmas Island", "cx"));
        countryList.add(new Country("+61", "Cocos Islands", "cc"));
        countryList.add(new Country("+57", "Colombia", "co"));
        countryList.add(new Country("+269", "Comoros", "km"));
        countryList.add(new Country("+682", "Cook Islands", "ck"));
        countryList.add(new Country("+506", "Costa Rica", "cr"));
        countryList.add(new Country("+385", "Croatia", "hr"));
        countryList.add(new Country("+53", "Cuba", "cu"));
        countryList.add(new Country("+599", "Curacao", "cw"));
        countryList.add(new Country("+357", "Cyprus", "cy"));
        countryList.add(new Country("+420", "Czech Republic", "cz"));
        countryList.add(new Country("+243", "Democratic Republic of the Congo", "cd"));
        countryList.add(new Country("+45", "Denmark", "dk"));
        countryList.add(new Country("+253", "Djibouti", "dj"));
        countryList.add(new Country("+1-767", "Dominica", "dm"));
        countryList.add(new Country("+1-809", "Dominican Republic", "do"));
        countryList.add(new Country("+670", "East Timor", "tl"));
        countryList.add(new Country("+593", "Ecuador", "ec"));
        countryList.add(new Country("+20", "Egypt", "eg"));
        countryList.add(new Country("+503", "El Salvador", "sv"));
        countryList.add(new Country("+240", "Equatorial Guinea", "gq"));
        countryList.add(new Country("+291", "Eritrea", "er"));
        countryList.add(new Country("+372", "Estonia", "ee"));
        countryList.add(new Country("+251", "Ethiopia", "et"));
        countryList.add(new Country("+500", "Falkland Islands", "fk"));
        countryList.add(new Country("+298", "Faroe Islands", "fo"));
        countryList.add(new Country("+679", "Fiji", "fj"));
        countryList.add(new Country("+358", "Finland", "fi"));
        countryList.add(new Country("+33", "France", "fr"));
        countryList.add(new Country("+689", "French Polynesia", "pf"));
        countryList.add(new Country("+241", "Gabon", "ga"));
        countryList.add(new Country("+220", "Gambia", "gm"));
        countryList.add(new Country("+995", "Georgia", "ge"));
        countryList.add(new Country("+49", "Germany", "de"));
        countryList.add(new Country("+233", "Ghana", "gh"));
        countryList.add(new Country("+350", "Gibraltar", "gi"));
        countryList.add(new Country("+30", "Greece", "gr"));
        countryList.add(new Country("+299", "Greenland", "gl"));
        countryList.add(new Country("+1-473", "Grenada", "gd"));
        countryList.add(new Country("+1-671", "Guam", "gu"));
        countryList.add(new Country("+502", "Guatemala", "gt"));
        countryList.add(new Country("+44-1481", "Guernsey", "gg"));
        countryList.add(new Country("+224", "Guinea", "gn"));
        countryList.add(new Country("+245", "Guinea-Bissau", "gw"));
        countryList.add(new Country("+592", "Guyana", "gy"));
        countryList.add(new Country("+509", "Haiti", "ht"));
        countryList.add(new Country("+504", "Honduras", "hn"));
        countryList.add(new Country("+852", "Hong Kong", "hk"));
        countryList.add(new Country("+36", "Hungary", "hu"));
        countryList.add(new Country("+354", "Iceland", "is"));
        countryList.add(new Country("+91", "India", "in"));
        countryList.add(new Country("+62", "Indonesia", "id"));
        countryList.add(new Country("+98", "Iran", "ir"));
        countryList.add(new Country("+964", "Iraq", "iq"));
        countryList.add(new Country("+353", "Ireland", "ie"));
        countryList.add(new Country("+44-1624", "Isle of Man", "im"));
        countryList.add(new Country("+972", "Israel", "il"));
        countryList.add(new Country("+39", "Italy", "it"));
        countryList.add(new Country("+225", "Ivory Coast", "ci"));
        countryList.add(new Country("+1-876", "Jamaica", "jm"));
        countryList.add(new Country("+81", "Japan", "jp"));
        countryList.add(new Country("+44-1534", "Jersey", "je"));
        countryList.add(new Country("+962", "Jordan", "jo"));
        countryList.add(new Country("+7", "Kazakhstan", "kz"));
        countryList.add(new Country("+254", "Kenya", "ke"));
        countryList.add(new Country("+686", "Kiribati", "ki"));

        return countryList;
    }

    public void getProfile() {
        SharedPreferences prefs = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
        // Populate EditText fields
        Firstname.setText(prefs.getString("name", ""));
        Lastname.setText(prefs.getString("surname", ""));
        phoneNumber.setText(prefs.getString("phone", ""));
        email.setText(prefs.getString("emailAddress", ""));

        emailConfirmation.setText(prefs.getString("emailAddress", ""));
        CreateAccBtn.setText("Update Profile");

    }

    //    private void handleForgotPasswordClick() throws JSONException {
//        EmailSender.sendEmail(this, Utils.getEmail(this), "Test Name", Utils.getPassword(this));
//
//    }
    private void saveAccount(String name, String surname, String phone, String emailAddress, String balance, String time) {
        SharedPreferences sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("surname", surname);
        editor.putString("phone", phone);
        editor.putString("time", time);
        editor.putString("emailAddress", emailAddress);
        editor.putString("balance", balance);
        editor.apply();


    }


//    private void sendPasswordEmail() {
//        SharedPreferences prefs = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
//        String email = prefs.getString("emailAddress", "");
//        SharedPreferences pref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        String password = pref.getString(PASSWORD_KEY, "");
//        String subject = "Your Password Reset Request";
//        String body = "Dear user,\n\nYour password is: " + password + "\n\nPlease keep it safe.";
//        EmailSender emailSender = new EmailSender();
////        emailSender.execute();
//    }
}
