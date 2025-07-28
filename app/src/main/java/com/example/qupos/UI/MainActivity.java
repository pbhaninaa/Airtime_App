package com.example.qupos.UI;

import static com.example.qupos.JavaClasses.Utils.isUserLogged;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qupos.JavaClasses.Utils;
import com.example.qupos.R;

public class MainActivity extends AppCompatActivity {

    private Button continueButton;
    private ImageView logoImageView;

    public static final String[] ECONET_ITEMS = {
            "Data", "Voice", "SMS", "WhatsApp"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.hideSoftNavBar(this);
        Utils.requestPermissions(this);

        initializeViews();
        setAppBranding();

        if (isUserLogged(this)) {
            navigateToDashboard();
            return; // Skip setting click listeners if user is already logged in
        }

        setClickListeners();
    }

    private void initializeViews() {
        continueButton = findViewById(R.id.candidate_btn);
        logoImageView = findViewById(R.id.app_logo);
    }

    private void setClickListeners() {
        if (continueButton != null) {
            continueButton.setOnClickListener(view -> {
                Utils.triggerHapticFeedback(this);
                navigateToUserManagement();
            });
        }
    }

    private void setAppBranding() {
        String appName = getString(R.string.app_name);
        if (logoImageView == null) return;

        switch (appName.toLowerCase()) {
            case "qupos":
                logoImageView.setImageResource(R.drawable.qupos_app_logo);
                break;
            case "rebtel":
                logoImageView.setImageResource(R.drawable.rebtel_icon_logo);
                break;
            default:
                logoImageView.setImageResource(R.drawable.keshapp_full_logo);
                break;
        }
    }



    private void navigateToDashboard() {
        startActivity(new Intent(this, Dashboard.class));
        finish(); // Optional: close MainActivity
    }

    private void navigateToUserManagement() {
        Intent intent = new Intent(this, UserManagement.class);
        intent.putExtra("constraintLayoutId", R.id.login_page);
        startActivity(intent);
    }
}
