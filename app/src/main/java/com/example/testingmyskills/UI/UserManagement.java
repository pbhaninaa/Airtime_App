package com.example.testingmyskills.UI;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingmyskills.JavaClasses.ApiService;
import com.example.testingmyskills.JavaClasses.Country;
import com.example.testingmyskills.R;
import com.example.testingmyskills.JavaClasses.Utils;

import org.json.*;

import java.io.IOException;
import java.util.*;

//8QGGLHPVSQ3TFEX7QLTCRU2Y
public class UserManagement extends AppCompatActivity {
    private ConstraintLayout SignUpLayout, RegScreen, SignInLayout;
    private EditText getEmailTextInLogin, password, getPasswordTextInLogin, Firstname, Lastname, phoneNumber, email, emailConfirmation, getPasswordTextInRegister, getConfirmPasswordTextInRegister, getEmailTextInRegister;
    private ImageButton ShowConformPasswordInRegister, ShowPasswordInRegister, ShowPasswordInLogin;
    private TextView ForgotPasswordBtn, SignUpBtn, RegisterBtn;
    private boolean showPassword, rememberMe, gotData;
    private Button SignUp, SignInBtn, CreateAccBtn;
    private Spinner languagesSpinner, CountryCode, LoginCountryCode;
    private CheckBox RememberMeCheckBox;
    private ImageView CountryFlag, LoginCountryFlag;
    private FrameLayout backButton;
    //    private TextView version ;
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
        Utils.setCaps(Firstname);
        Utils.setCaps(Lastname);
//        version.setText("Version : "+getAppVersion());
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
                phoneNumber.requestFocus();
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


        ArrayAdapter<Country> adapt = new ArrayAdapter<>(this, R.layout.spinner_item, getCountryList());
        ada.setDropDownViewResource(R.layout.spinner_item);
        LoginCountryCode.setAdapter(adapt);

        LoginCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideBottomNav();
                Country selectedCountry = (Country) parent.getItemAtPosition(position);
                String flagName = selectedCountry.getCountryFlag();
                // Get the resource ID of the drawable dynamically
                int flagResourceId = getResources().getIdentifier(flagName, "drawable", getPackageName());
                // Set the ImageView with the corresponding flag
                if (flagResourceId != 0) {  // Check if the resource was found
                    LoginCountryFlag.setImageResource(flagResourceId);
                } else {
                    Toast.makeText(getApplicationContext(), "Flag not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        RememberMeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rememberMe = isChecked;
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
    private void initialiseViews() {
//        version = findViewById(R.id.version_l);
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
        password = findViewById(R.id.password);


        CountryCode = findViewById(R.id.country_codes);
        CountryFlag = findViewById(R.id.country_flag);
        LoginCountryCode = findViewById(R.id.login_country_codes);
        LoginCountryFlag = findViewById(R.id.login_country_flag);

    }

    private void setOnclickListeners() {
        RegisterBtn.setOnClickListener(v -> handleRegisterClick());
        ShowPasswordInLogin.setOnClickListener(v -> handleShowPassword());
        SignUpBtn.setOnClickListener(v -> handleBack());
        ShowConformPasswordInRegister.setOnClickListener(v -> handleShowPassword());
        ShowPasswordInRegister.setOnClickListener(v -> handleShowPassword());

        SignInBtn.setOnClickListener(v -> {
            try {
                handleSignIn();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        ForgotPasswordBtn.setOnClickListener(v -> Utils.showEmailDialog(this, this));

//        SignUp.setOnClickListener(v -> handleCreateClick());
        CreateAccBtn.setOnClickListener(v -> {
            try {


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
        Context context = getApplicationContext();
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
            Utils.triggerHapticFeedback(this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String p = CountryCode.getSelectedItem() + phone;
                        JSONObject res = ApiService.register(
                                name + " " + surname,
                                pass,
                                p.replace("+", ""),
                                emailAddress,
                                context);

                        if (res.getInt("responseCode") == 200) {
                            // To handle success and UI updates on the main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Registration was successful
                                    RegScreen.setVisibility(GONE);
                                    SignInLayout.setVisibility(View.VISIBLE);

                                    getEmailTextInLogin.setText(phone.startsWith("27") ? phone.substring(2) : phone.startsWith("26") ? phone.substring(3) : phone);
                                    getPasswordTextInLogin.setText(pass);

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
                                Utils.showToast(UserManagement.this, e.getMessage().contains("connect") ? "Service Provider Offline" : e.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();


        }
    }

    private void handleSignIn() throws Exception {

        Utils.triggerHapticFeedback(this);
        String AgentID = getEmailTextInLogin.getText().toString().trim();
        String password = getPasswordTextInLogin.getText().toString().trim();
        Context context = getApplicationContext();
        if (AgentID.isEmpty()) {
            getEmailTextInLogin.setError("AgentID is required");
            return;
        }

        if (password.isEmpty()) {
            getPasswordTextInLogin.setError("Password is required");
            return;
        }
        Utils.LoadingLayout(this, this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String p = LoginCountryCode.getSelectedItem() + AgentID;

                    JSONObject res = ApiService.login(password, p.replace("+", ""), context);

                    if (res.getInt("responseCode") == 200) {
                        // To handle success and UI updates on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    // Extract the string inside the "response" field and parse it as a new JSONObject
                                    String responseString = res.getString("response");
                                    System.out.println(responseString);
                                    JSONObject responseJson = new JSONObject(responseString);

                                    // Now extract "methodResponse" and navigate through the JSON structure
                                    JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                    JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                    JSONObject userObject = paramsList.getJSONObject(0);


                                    String agentID = userObject.getString("agentID");
                                    String agentName = userObject.getString("agentName");
                                    String balance = userObject.getString("decimalBalance");
                                    String agentEmail = userObject.getString("agentEmail");
                                    String statusCode = userObject.getString("statusCode");
                                    String lastConnect = userObject.getString("lastConnect");
                                    String AgentRole = userObject.getString("permissions");


                                    // Split agent name into first name and surname
                                    String[] nameParts = agentName.split(" ");
                                    String firstName = nameParts.length > 0 ? nameParts[0] : "";
                                    String surname = nameParts.length > 1 ? nameParts[1] : "";

                                    // Convert the timestamp to a readable format
//                                    String formattedDate = formatTimestamp(lastConnect);

                                    // Save necessary credentials
                                    Utils.saveAutoFillPermission(UserManagement.this, rememberMe);
                                    Utils.saveString(UserManagement.this, "savedCredentials", "email", agentID);
                                    Utils.saveString(UserManagement.this, "savedCredentials", "password", password);


                                    // Save user account details
                                    saveAccount(AgentRole,firstName, surname, agentID, agentEmail, balance, lastConnect, Integer.parseInt(statusCode), password, true);
                                    RememberMeCheckBox.setChecked(false);
                                    Utils.hideSoftKeyboard(UserManagement.this);
                                    Utils.hideSoftNavBar(UserManagement.this);

                                    // Navigate to the Dashboard
                                    Intent intent = new Intent(UserManagement.this, Dashboard.class);
                                    startActivity(intent);
                                    Utils.CloseLoadingLayout(UserManagement.this, UserManagement.this);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    String responseString = null;
                                    try {
                                        responseString = res.getString("response");
                                        System.out.println(responseString);
                                        JSONObject responseJson = new JSONObject(responseString);

                                        // Now extract "methodResponse" and navigate through the JSON structure
                                        JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                        JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                        JSONObject userObject = paramsList.getJSONObject(0);
                                        String status = userObject.getString("status");

                                        Utils.showToast(UserManagement.this, status);
                                    } catch (JSONException ex) {
                                        throw new RuntimeException(ex);
                                    }

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

                            Utils.showToast(UserManagement.this, e.getMessage().contains("connect") ? "Service Provider Offline" : e.getMessage());

                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    public void getProfile() {
        SharedPreferences prefs = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
        Firstname.setText(prefs.getString("name", ""));
        Lastname.setText(prefs.getString("surname", ""));
        String phone = prefs.getString("phone", "");// Get the selected country code based on the phone number
        String selectedCode = phone.startsWith("27") ? phone.substring(0, 2) : phone.substring(0, 3);
        LinearLayout back = findViewById(R.id.back_to_home_from_Profile);
        back.setVisibility(View.VISIBLE);
        CountryFlag.setImageResource(phone.startsWith("27") ? R.drawable.za : R.drawable.zw);
        phoneNumber.setText(phone.startsWith("27") ? phone.substring(2) : phone.startsWith("26") ? phone.substring(3) : phone);
        email.setText(prefs.getString("emailAddress", ""));
        phoneNumber.setShowSoftInputOnFocus(false);
        phoneNumber.setEnabled(false);
        CountryCode.setEnabled(false);

        emailConfirmation.setText(prefs.getString("emailAddress", ""));
        CreateAccBtn.setText("Update Profile");

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
    private void screenToLoad(int screenToLoad) {
        if (Utils.RememberMe(this)) {
            String phone = Utils.getString(this, "savedCredentials", "email");
            getEmailTextInLogin.setText(phone.startsWith("27") ? phone.substring(2) : phone.startsWith("26") ? phone.substring(3) : phone);
            getPasswordTextInLogin.setText(Utils.getString(this, "savedCredentials", "password"));
            RememberMeCheckBox.setChecked(Utils.RememberMe(this));
        }
        if (screenToLoad == R.id.create_profile_screen)
            getProfile();

        SignInLayout.setVisibility(GONE);
        SignUpLayout.setVisibility(GONE);
        RegScreen.setVisibility(GONE);
        ConstraintLayout layout = findViewById(screenToLoad);
        layout.setVisibility(View.VISIBLE);
    }

    private void saveAccount(String role, String name, String surname, String phone, String emailAddress, String balance, String time, int id, String pass, boolean logged) {
        SharedPreferences sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("surname", surname);
        editor.putString("phone", phone);
        editor.putString("time", time);
        editor.putString("emailAddress", emailAddress);
        editor.putString("balance", balance);
        editor.putString("id", String.valueOf(id));
        editor.apply();

        SharedPreferences sharedPref = getSharedPreferences("LoggedUserCredentials", MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPref.edit();
        ed.putString("name", name);
        ed.putString("surname", surname);
        ed.putString("phone", phone);
        ed.putString("password", pass);
        ed.putString("email", emailAddress);
        ed.putBoolean("isUserLogged", logged);
        ed.putString("role", role); // fixed here

        ed.apply();
    }


    public void backToHome(View view) {
        Intent intent = new Intent(UserManagement.this, Dashboard.class);
        startActivity(intent);
    }

    //     Returning Method
    public static List<Country> getCountryList() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country("+263", "Zimbabwe", "zw"));
        countryList.add(new Country("+27", "South Africa", "za"));
        return countryList;
    }

    public static List<Country> getZimCode() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(new Country("+263", "Zimbabwe", "zw"));

        return countryList;
    }

    public String getAppVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

}