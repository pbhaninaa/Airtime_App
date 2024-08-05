package com.example.testingmyskills.UI;

import static com.example.testingmyskills.JavaClasses.Utils.PASSWORD_KEY;
import static com.example.testingmyskills.JavaClasses.Utils.PREF_NAME;
import static com.example.testingmyskills.JavaClasses.Utils.RememberMe;
import static com.example.testingmyskills.UI.MainActivity.MSISDN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.testingmyskills.Dao.XMLRPCClient;
import com.example.testingmyskills.Interfaces.AccountValidationCallback;
import com.example.testingmyskills.JavaClasses.AlphaKeyboard;
import com.example.testingmyskills.JavaClasses.EmailSender;
import com.example.testingmyskills.R;
import com.example.testingmyskills.JavaClasses.Utils;

import org.apache.xmlrpc.XmlRpcException;
import org.json.JSONException;

import java.util.Map;
import java.util.Objects;

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
    private Spinner languagesSpinner;
    private EditText password,
            Firstname,
            Lastname,
            phoneNumber,
            address,
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
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languagesSpinner.setAdapter(adapter);
//        setupFocusListeners();
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

    private void APICall(String number, AccountValidationCallback callback) {
        new Thread(() -> {
            try {
                Map<String, Object> response = XMLRPCClient.accountBalanceEnquiry(number, "validate_msisdn");
                runOnUiThread(() -> {
                    callback.validateUser(response);
                });
            } catch (XmlRpcException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
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
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        emailConfirmation = findViewById(R.id.emailC);
        backButton = findViewById(R.id.back);
        password = findViewById(R.id.password);
        RememberMeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rememberMe = isChecked;

        });
    }

    private void screenToLoad(int screenToLoad) {
        if (screenToLoad == R.id.login_page) {

            if (Utils.RememberMe(this)) {
                getEmailTextInLogin.setText(Utils.getEmail(this));
                getPasswordTextInLogin.setText(Utils.getPassword(this));
                RememberMeCheckBox.isChecked();
            }

            SignInLayout.setVisibility(View.VISIBLE);
            SignUpLayout.setVisibility(View.GONE);
            RegScreen.setVisibility(View.GONE);
        } else if (screenToLoad == R.id.sign_up_page) {
            SignInLayout.setVisibility(View.GONE);
            RegScreen.setVisibility(View.GONE);
            SignUpLayout.setVisibility(View.VISIBLE);
        } else if (screenToLoad == R.id.create_profile_screen) {
            getProfile();
            RegScreen.setVisibility(View.VISIBLE);
            SignInLayout.setVisibility(View.GONE);
            SignUpLayout.setVisibility(View.GONE);
        }


    }

    private void getProfile() {
        SharedPreferences prefs = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
        // Populate EditText fields
        Firstname.setText(prefs.getString("name", ""));
        Lastname.setText(prefs.getString("surname", ""));
        phoneNumber.setText(prefs.getString("phone", ""));
        address.setText(prefs.getString("addressLine", ""));
        email.setText(prefs.getString("emailAddress", ""));
        emailConfirmation.setText(prefs.getString("emailAddress", ""));
        CreateAccBtn.setText("Update Profile");

    }

    private void setOnclickListeners() {
        RegisterBtn.setOnClickListener(v -> handleRegisterClick());
        ShowPasswordInLogin.setOnClickListener(v -> handleShowPassword());
        SignInBtn.setOnClickListener(v -> handleSignIn());
        ForgotPasswordBtn.setOnClickListener(v -> {
            try {
                handleForgotPasswordClick();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        SignUpBtn.setOnClickListener(v -> handleSignUp());
        SignUp.setOnClickListener(v -> handleCreateClick());
        ShowConformPasswordInRegister.setOnClickListener(v -> handleShowPassword());
        ShowPasswordInRegister.setOnClickListener(v -> handleShowPassword());
        CreateAccBtn.setOnClickListener(v -> {
            try {
                handleAccCreation();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
//        hideKeyboardBtn.setOnClickListener(v -> hideKeyboard());
        backButton.setOnClickListener(v -> handleBack());

    }

    private void handleBack() {
        Intent i = new Intent(this, Dashboard.class);
        i.putExtra("constraintLayoutId", R.id.app_frame);
        startActivity(i);
    }

    private void sendPasswordEmail() {
        SharedPreferences prefs = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
        String email = prefs.getString("emailAddress", "");
        SharedPreferences pref = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String password = pref.getString(PASSWORD_KEY, "");
        String subject = "Your Password Reset Request";
        String body = "Dear user,\n\nYour password is: " + password + "\n\nPlease keep it safe.";
        EmailSender emailSender = new EmailSender();
//        emailSender.execute();
    }

//    private void hideKeyboard() {
//        Utils.hideAlphaKeyboard(alphaKeyboard);
//    }

    private void handleAccCreation() throws Exception {
        // Get the values from the input fields
        String language = languagesSpinner.getSelectedItem().toString().trim();
        String name = Firstname.getText().toString().trim();
        String surname = Lastname.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();
        String addressLine = address.getText().toString().trim();
        String emailAddress = email.getText().toString().trim();
        String emailC = emailConfirmation.getText().toString().trim();
        String pass = password.getText().toString().trim();

        // Check if all fields have values
        if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() ||
                addressLine.isEmpty() || emailAddress.isEmpty() || pass.isEmpty()) {
            // Show an error message to the user
            Utils.showToast(this, "Please fill all the fields");
        } else if (!Utils.isValidEmail(emailAddress)) {
            // Show an error message if email is not valid
            Utils.showToast(this, "Invalid email address format");
//        } else if (!emailAddress.equals(emailC)) {
//            // Show an error message if email and confirmation do not match
//            Utils.showToast(this, "Email addresses do not match");
        } else {
            XMLRPCClient.registerUserAsync(name + " " + surname, phone, emailAddress, pass, new XMLRPCClient.ResponseCallback() {
                @Override
                public void onSuccess(int responseCode) {
//                    System.out.println("Register =Response Code: " + responseCode);
//                    Intent intent = new Intent(UserManagement.this, Dashboard.class);
//                    startActivity(intent);
                    RegScreen.setVisibility(View.GONE);
                    SignInLayout.setVisibility(View.VISIBLE);
                    saveAccount();
                    if (responseCode == 201) {
                        Utils.saveCredentials(UserManagement.this, emailAddress, pass);
                        RegScreen.setVisibility(View.GONE);
                        SignInLayout.setVisibility(View.VISIBLE);
                    }
//                    else {
//                        Utils.showToast(UserManagement.this,getMessage);
//                    }

                }

                @Override
                public void onError(Exception e) {
                    Utils.showToast(UserManagement.this, e.getMessage());
                    e.printStackTrace();
                }
            });
//            Intent intent = new Intent(this, Dashboard.class);
//            startActivity(intent);
//            saveAccount();
//            APICall(phone, this);
//            if (!gotData)
//                Utils.showToast(this, "Sever is Offline try again later");
        }
    }

    private void handleCreateClick() {
        String email = getEmailTextInRegister.getText().toString().trim();
        String password = getPasswordTextInRegister.getText().toString().trim();
        String confirmPassword = getConfirmPasswordTextInRegister.getText().toString().trim();


        // Check if any field is empty
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // Show error message if any field is empty
            if (email.isEmpty()) {
                getEmailTextInRegister.setError("Email is required");
            }
            if (password.isEmpty()) {
                getPasswordTextInRegister.setError("Password is required");
            }
            if (confirmPassword.isEmpty()) {
                getConfirmPasswordTextInRegister.setError("Confirm password is required");
            }
            return;
        }

        // Check if email is in correct format using regular expression
        if (!Utils.isValidEmail(email)) {
            getEmailTextInRegister.setError("Invalid email format");
            return;
        }

        // Check if password and confirm password match
        if (!password.equals(confirmPassword)) {
            getConfirmPasswordTextInRegister.setError("Passwords do not match");
            return;
        }


        // If all validations pass, save email and password to SharedPreferences
        Utils.saveCredentials(UserManagement.this, email, password);
        SignInLayout.setVisibility(View.VISIBLE);
        SignUpLayout.setVisibility(View.GONE);
        getConfirmPasswordTextInRegister.setText("");
        getPasswordTextInRegister.setText("");
        getEmailTextInRegister.setText("");
        // Optionally, you can show a toast indicating successful account creation
        Utils.showToast(UserManagement.this, "Account created successfully");
    }

    private void handleSignUp() {
        SignInLayout.setVisibility(View.VISIBLE);
        SignUpLayout.setVisibility(View.GONE);
    }

    private void handleForgotPasswordClick() throws JSONException {

        EmailSender.sendEmail(this, Utils.getEmail(this), "Test Name", Utils.getPassword(this));

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

        // Retrieve stored email and password from SharedPreferences
        String storedEmail = Utils.getEmail(UserManagement.this);
        String storedPassword = Utils.getPassword(UserManagement.this);


        XMLRPCClient.userLoginAsync(email, password, new XMLRPCClient.ResponseCallback() {
            @Override
            public void onSuccess(int responseCode) {
                System.out.println("Login Response Code: " + responseCode);
                if (responseCode == 200) {
                    Intent intent = new Intent(UserManagement.this, Dashboard.class);
                    startActivity(intent);
                }
//                else {
//                   Utils.showToast(UserManagement.this,);
//                }
                Utils.saveAutoFillPermission(UserManagement.this, rememberMe);
                getEmailTextInLogin.setText("");
                getPasswordTextInLogin.setText("");
            }

            @Override
            public void onError(Exception e) {
                Utils.showToast(UserManagement.this, e.getMessage());
                e.printStackTrace();
            }
        });


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

    private void handleRegisterClick() {
        SignInLayout.setVisibility(View.GONE);
        SignUpLayout.setVisibility(View.VISIBLE);
    }

    private boolean isProfileEmpty() {
        SharedPreferences sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "");
        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        String phone = sharedPreferences.getString("phone", "");
        String addressLine = sharedPreferences.getString("addressLine", "");
        String emailAddress = sharedPreferences.getString("emailAddress", "");

        return language.isEmpty() || name.isEmpty() || surname.isEmpty() ||
                phone.isEmpty() || addressLine.isEmpty() || emailAddress.isEmpty();
    }

    private void saveAccount() {
        String language = languagesSpinner.getSelectedItem().toString().trim();
        String name = Firstname.getText().toString().trim();
        String surname = Lastname.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();
        String addressLine = address.getText().toString().trim();
        String emailAddress = email.getText().toString().trim();


        SharedPreferences sharedPreferences = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", language);
        editor.putString("name", name);
        editor.putString("surname", surname);
        editor.putString("phone", phone);
        editor.putString("addressLine", addressLine);
        editor.putString("emailAddress", emailAddress);
        editor.apply();


    }

}
