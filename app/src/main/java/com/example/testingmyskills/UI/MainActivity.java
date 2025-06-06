package com.example.testingmyskills.UI;

import static com.example.testingmyskills.JavaClasses.Utils.isUserLogged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.testingmyskills.JavaClasses.Utils;
import com.example.testingmyskills.R;

import okhttp3.internal.Util;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
public class MainActivity extends AppCompatActivity {
    private Button CandidateBtn;
    private ConstraintLayout landing_page;
    private TextView version ;
    public static String[] econetItems = {
            "Data",
            "Voice",
            "SMS",
            "WhatsApp"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);Utils.hideSoftNavBar(this);
        Utils.requestPermissions(this);
        initialiseViews();
        Utils.hideSoftNavBar(this);

//        String version = getAppVersion();
        version.setText("Version : "+getAppVersion());
        if (isUserLogged(this)) {
            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            startActivity(intent);
        } else {
            landing_page.setVisibility(View.VISIBLE);
        }

        setOnclickListeners();
    }

    private void setOnclickListeners() {
        CandidateBtn.setOnClickListener(v -> handleCompanyClick());
    }

    private void handleCompanyClick() {
        Utils.triggerHapticFeedback(this);
        Intent intent = new Intent(MainActivity.this, UserManagement.class);
        intent.putExtra("constraintLayoutId", R.id.login_page);
        startActivity(intent);


    }

    private void initialiseViews() {
        CandidateBtn = findViewById(R.id.candidate_btn);
        landing_page = findViewById(R.id.landing_page);
        version = findViewById(R.id.version);
    }
//    Returning Methods
    public String getAppVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName; // Returns the versionName from build.gradle
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "Unknown"; // Return a default value in case of an error
        }
    }
}
