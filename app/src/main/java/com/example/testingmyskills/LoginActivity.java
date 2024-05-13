package com.example.testingmyskills;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout SignInLayout;
    private ConstraintLayout SignUpLayout;
    private TextView RegisterBtn;
    private EditText getEmailTextInLogin;
    private EditText getPasswordTextInLogin;
    private ImageButton ShowPasswordInLogin;
    private Button SignInBtn;
    private CheckBox RememberMeCheckBox;
    private TextView ForgotPasswordBtn;
    private ImageButton GoogleBtn;
    private ImageButton FacebookBtn;
    private boolean showPassword;

    private TextView SignUpBtn;
    private EditText getEmailTextInRegister;
    private EditText getPasswordTextInRegister;
    private EditText getConfirmPasswordTextInRegister;
    private ImageButton ShowPasswordInRegister;
    private ImageButton ShowConformPasswordInRegister;
    private Button CreateAccountBtn;
    private Spinner languagesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils.hideSoftNavBar(LoginActivity.this);
        showPassword = false;

        initialiseViews();
        setOnclickListeners();

        // Load screen based on the constraintLayoutId received from Intent
        int constraintLayoutId = getIntent().getIntExtra("constraintLayoutId", R.id.login_page);
        screenToLoad(constraintLayoutId);


        String[] values = {"IsiXhosa", "IsiZulu", "Tswana","IsiPedi","Ndebele","English"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languagesSpinner.setAdapter(adapter);

    }

    private void screenToLoad(int screenToLoad) {
        if (screenToLoad == R.id.login_page) {
            SignInLayout.setVisibility(View.VISIBLE);
            SignUpLayout.setVisibility(View.GONE); // Hide the other layout if necessary
        } else if (screenToLoad == R.id.sign_up_page) {
            SignInLayout.setVisibility(View.GONE); // Hide the other layout if necessary
            SignUpLayout.setVisibility(View.VISIBLE);
        } else {
            SignInLayout.setVisibility(View.VISIBLE);
            SignUpLayout.setVisibility(View.GONE);
        }


    }

    private void initialiseViews() {
        SignInLayout = findViewById(R.id.login_page);
        SignUpLayout = findViewById(R.id.sign_up_page);
        RegisterBtn = findViewById(R.id.register_btn);
        getEmailTextInLogin = findViewById(R.id.email_in_login_page);
        getPasswordTextInLogin = findViewById(R.id.password_in_login_page);
        ShowPasswordInLogin = findViewById(R.id.show_password_in_login_page);
        SignInBtn = findViewById(R.id.sign_in_btn);
        RememberMeCheckBox = findViewById(R.id.remember_me_check_box);
        ForgotPasswordBtn = findViewById(R.id.forgot_password);
        GoogleBtn = findViewById(R.id.google_btn);
        FacebookBtn = findViewById(R.id.facebook_btn);
        languagesSpinner = findViewById(R.id.languages);

        SignUpBtn = findViewById(R.id.sign_up_btn);
        getEmailTextInRegister = findViewById(R.id.email_in_register_page);
        getPasswordTextInRegister = findViewById(R.id.password_in_register_page);
        getConfirmPasswordTextInRegister = findViewById(R.id.confirm_password_in_register_page);
        ShowPasswordInRegister = findViewById(R.id.show_password_in_register_page);
        ShowConformPasswordInRegister = findViewById(R.id.show_confirm_password_in_register_page);
        CreateAccountBtn = findViewById(R.id.create_account);

        RememberMeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Utils.showToast(LoginActivity.this, "Remember me checked");
            } else {
                Utils.showToast(LoginActivity.this, "Remember me unchecked");
            }
        });
    }

    private void setOnclickListeners() {
        RegisterBtn.setOnClickListener(v -> handleRegisterClick());
        ShowPasswordInLogin.setOnClickListener(v -> handleShowPassword());
        SignInBtn.setOnClickListener(v -> handleSignIn());
        ForgotPasswordBtn.setOnClickListener(v -> handleForgotPasswordClick());
        GoogleBtn.setOnClickListener(v -> handleGoogleClick());
        FacebookBtn.setOnClickListener(v -> handleFacebookClick());
        SignUpBtn.setOnClickListener(v -> handleSignUp());
        CreateAccountBtn.setOnClickListener(v -> handleCreateClick());
        ShowConformPasswordInRegister.setOnClickListener(v -> handleShowPassword());
        ShowPasswordInRegister.setOnClickListener(v -> handleShowPassword());
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
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            getEmailTextInRegister.setError("Invalid email format");
            return;
        }

        // Check if password and confirm password match
        if (!password.equals(confirmPassword)) {
            getConfirmPasswordTextInRegister.setError("Passwords do not match");
            return;
        }

        // If all validations pass, save email and password to SharedPreferences
        Utils.saveCredentials(LoginActivity.this, email, password);
        SignInLayout.setVisibility(View.VISIBLE);
        SignUpLayout.setVisibility(View.GONE);
        getConfirmPasswordTextInRegister.setText("");
        getPasswordTextInRegister.setText("");
        getEmailTextInRegister.setText("");
        // Optionally, you can show a toast indicating successful account creation
        Utils.showToast(LoginActivity.this, "Account created successfully");
    }

    private void handleSignUp() {
        SignInLayout.setVisibility(View.VISIBLE);
        SignUpLayout.setVisibility(View.GONE);
    }

    private void handleFacebookClick() {
        Utils.showToast(LoginActivity.this, "Facebook");
    }

    private void handleGoogleClick() {
        Utils.showToast(LoginActivity.this, "Google");
    }

    private void handleForgotPasswordClick() {
        Utils.showToast(LoginActivity.this, "Forgot password");
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
        String storedEmail = Utils.getEmail(LoginActivity.this);
        String storedPassword = Utils.getPassword(LoginActivity.this);

        // Check if entered email and password match the stored credentials
        if (storedEmail.equals(email) && storedPassword.equals(password)) {
            Intent intent = new Intent(LoginActivity.this, JobsActivity.class);
            intent.putExtra("constraintLayoutId", R.id.login_page);
            startActivity(intent);
            // If matched, show a toast indicating successful login
            Utils.showToast(LoginActivity.this, "Logged In");
            getEmailTextInLogin.setText("");
            getPasswordTextInLogin.setText("");
        } else {
            // If not matched, show a toast indicating incorrect credentials
            Utils.showToast(LoginActivity.this, "Incorrect email or password");
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

    private void handleRegisterClick() {
        SignInLayout.setVisibility(View.GONE);
        SignUpLayout.setVisibility(View.VISIBLE);
    }


}
