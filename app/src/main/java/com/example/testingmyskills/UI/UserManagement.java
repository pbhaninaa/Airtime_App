package com.example.testingmyskills.UI;

import static android.view.View.GONE;

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

import com.example.testingmyskills.Interfaces.AccountValidationCallback;
import com.example.testingmyskills.JavaClasses.ApiService;
import com.example.testingmyskills.JavaClasses.Country;
import com.example.testingmyskills.R;
import com.example.testingmyskills.JavaClasses.Utils;
import org.json.*;

import java.io.IOException;
import java.util.*;

//8QGGLHPVSQ3TFEX7QLTCRU2Y
public class UserManagement extends AppCompatActivity implements AccountValidationCallback {
    private ConstraintLayout SignUpLayout, RegScreen, SignInLayout;
    private EditText getEmailTextInLogin, password, getPasswordTextInLogin, Firstname, Lastname, phoneNumber, email, emailConfirmation, getPasswordTextInRegister, getConfirmPasswordTextInRegister, getEmailTextInRegister;
    private ImageButton ShowConformPasswordInRegister, ShowPasswordInRegister, ShowPasswordInLogin;
    private TextView ForgotPasswordBtn, SignUpBtn, RegisterBtn;
    private boolean showPassword, rememberMe, gotData;
    private Button SignUp, SignInBtn, CreateAccBtn;
    private Spinner languagesSpinner, CountryCode;
    private CheckBox RememberMeCheckBox;
    private ImageView CountryFlag;
    private FrameLayout backButton;
    String[] values = {"Select home language", "IsiXhosa", "IsiZulu", "Tswana", "IsiPedi", "Ndebele", "English"};

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
            gotData = true;
        }
        System.out.println("Account Validation Res: " + response);
    }

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
        if (Utils.RememberMe(this)) {
            getEmailTextInLogin.setText(Utils.getString(this, "savedCredentials", "email"));
            getPasswordTextInLogin.setText(Utils.getString(this, "savedCredentials", "password"));
            RememberMeCheckBox.setChecked(Utils.RememberMe(this)); // Ensure the checkbox is checked
        }
        if (screenToLoad == R.id.create_profile_screen)
            getProfile();

        SignInLayout.setVisibility(GONE);
        SignUpLayout.setVisibility(GONE);
        RegScreen.setVisibility(GONE);
        ConstraintLayout layout = findViewById(screenToLoad);
        layout.setVisibility(View.VISIBLE);
    }

    private void setOnclickListeners() {
        RegisterBtn.setOnClickListener(v -> handleRegisterClick());
        ShowPasswordInLogin.setOnClickListener(v -> handleShowPassword());
        SignUpBtn.setOnClickListener(v -> handleBack());
        ShowConformPasswordInRegister.setOnClickListener(v -> handleShowPassword());
        ShowPasswordInRegister.setOnClickListener(v -> handleShowPassword());
        backButton.setOnClickListener(v -> handleBack());
        SignInBtn.setOnClickListener(v -> {
            try {
                handleSignIn();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
//        ForgotPasswordBtn.setOnClickListener(v -> {
//            try {
////                handleForgotPasswordClick();
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        SignUp.setOnClickListener(v -> handleCreateClick());
        CreateAccBtn.setOnClickListener(v -> {
            try {
                Utils.triggerHapticFeedback(this);
                handleAccCreation();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void handleRegisterClick() {
        Utils.triggerHapticFeedback(this);
        RegScreen.setVisibility(View.VISIBLE);
        SignInLayout.setVisibility(GONE);
    }

    private void handleBack() {
        Utils.triggerHapticFeedback(this);
        RegScreen.setVisibility(GONE);
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
            JSONObject res = ApiService.register(name + " " + surname, pass, phone, emailAddress);
            if (res.get("responseCode").toString().equals("200")) {
                // Registration was successful
                RegScreen.setVisibility(GONE);
                SignInLayout.setVisibility(View.VISIBLE);
                getEmailTextInLogin.setText(emailAddress);
                getPasswordTextInLogin.setText(pass);
            } else {
                Utils.showToast(UserManagement.this, res.toString());
            }
        }
    }

    private void handleSignIn() throws Exception {
        Utils.triggerHapticFeedback(this);
        String AgentID = getEmailTextInLogin.getText().toString().trim();
        String password = getPasswordTextInLogin.getText().toString().trim();

        if (AgentID.isEmpty()) {
            getEmailTextInLogin.setError("AgentID is required");
            return;
        }

        if (password.isEmpty()) {
            getPasswordTextInLogin.setError("Password is required");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject res = ApiService.login(password, AgentID);

                    if (res.getInt("responseCode") == 200) {
                        // To handle success and UI updates on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Utils.success(UserManagement.this, "Success");

                                    // Extract the string inside the "response" field and parse it as a new JSONObject
                                    String responseString = res.getString("response");
                                    System.out.println(responseString);
                                    JSONObject responseJson = new JSONObject(responseString);

                                    // Now extract "methodResponse" and navigate through the JSON structure
                                    JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                    JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                    JSONObject userObject = paramsList.getJSONObject(0);

                                    // Extract user details
                                    String agentID = userObject.getString("agentID");
                                    String agentName = userObject.getString("agentName");
                                    String balance = userObject.getString("balance");
                                    String agentEmail = userObject.getString("agentEmail");
                                    String status = userObject.getString("status");
                                    String statusCode = userObject.getString("statusCode");
                                    String lastConnect = userObject.getString("lastConnect");


                                    // Split agent name into first name and surname
                                    String[] nameParts = agentName.split(" ");
                                    String firstName = nameParts.length > 0 ? nameParts[0] : "";
                                    String surname = nameParts.length > 1 ? nameParts[1] : "";

                                    // Convert the timestamp to a readable format
//                                    String formattedDate = formatTimestamp(lastConnect);

                                    // Save necessary credentials
                                    Utils.saveAutoFillPermission(UserManagement.this, rememberMe);
                                    Utils.saveString(UserManagement.this, "savedCredentials", "email", agentName);
                                    Utils.saveString(UserManagement.this, "savedCredentials", "password", password);

                                    // Save user account details
                                    saveAccount(firstName, surname, agentID, agentEmail, balance, lastConnect, Integer.parseInt(statusCode));

                                    // Navigate to the Dashboard
                                    Intent intent = new Intent(UserManagement.this, Dashboard.class);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Utils.showToast(UserManagement.this, "Error parsing response data: " + e.getMessage());
                                }
                            }
                        });

                    } else {
                        // Handle error response on UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showToast(UserManagement.this, res.toString());
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle IOException on UI thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast(UserManagement.this, "Error: " + e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    private void handleShowPassword() {
        Utils.triggerHapticFeedback(this);
        showPassword = !showPassword;
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
        Firstname.setText(prefs.getString("name", ""));
        Lastname.setText(prefs.getString("surname", ""));
        phoneNumber.setText(prefs.getString("phone", ""));
        email.setText(prefs.getString("emailAddress", ""));
        phoneNumber.setShowSoftInputOnFocus(false);
        phoneNumber.setEnabled(false);
        emailConfirmation.setText(prefs.getString("emailAddress", ""));
        CreateAccBtn.setText("Update Profile");

    }

    private void saveAccount(String name, String surname, String phone, String emailAddress, String balance, String time, int id) {
        SharedPreferences sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("surname", surname);
        editor.putString("phone", phone);
        editor.putString("time", time);
        editor.putString("emailAddress", emailAddress);
        editor.putString("balance", balance);
        editor.putString("id", String.valueOf(id));  // Convert the integer to a string

        editor.apply();


    }

}
