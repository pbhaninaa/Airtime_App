package com.example.testingmyskills.UI;

import static com.example.testingmyskills.JavaClasses.Utils.isUserLogged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testingmyskills.JavaClasses.EmailSender;
import com.example.testingmyskills.JavaClasses.Utils;
import com.example.testingmyskills.R;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private Button CandidateBtn;
    private ConstraintLayout landing_page;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseViews();
        Utils.hideSoftNavBar(this);
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
    }

    public static String[] econetItems = {
            "Data",
            "Voice",
            "SMS"
    };
}

//package com.example.testingmyskills.UI;
//
//import static com.example.testingmyskills.JavaClasses.Utils.isUserLogged;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.example.testingmyskills.JavaClasses.EmailSender;
//import com.example.testingmyskills.R;
//
//import org.json.JSONException;
//
//public class MainActivity extends AppCompatActivity {
//    private Button CandidateBtn;
//    private ConstraintLayout landing_page;
//    private Handler mainHandler = new Handler(Looper.getMainLooper());
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        initialiseViews();
//
//        if (isUserLogged(this)) {
//            Intent intent = new Intent(MainActivity.this, Dashboard.class);
//            startActivity(intent);
//        } else {
//            landing_page.setVisibility(View.VISIBLE);
//        }
//
//        setOnclickListeners();
//    }
//
//    private void setOnclickListeners() {
//        CandidateBtn.setOnClickListener(v -> handleCompanyClick());
//    }
//
//    private void handleCompanyClick() {
//
////        Utils.triggerHapticFeedback(this);
////        Intent intent = new Intent(MainActivity.this, UserManagement.class);
////        intent.putExtra("constraintLayoutId", R.id.login_page);
////        startActivity(intent);
//
//        String recipientEmail = "pbhanina@gmail.com";
//        String recipientPassword = "Philas@12345";
//
//        try {
//            EmailSender.sendEmail(this, recipientEmail, recipientName, recipientPassword);
//            Toast.makeText(this, "Email sent successfully!", Toast.LENGTH_SHORT).show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Failed to send email.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void initialiseViews() {
//        CandidateBtn = findViewById(R.id.candidate_btn);
//        landing_page = findViewById(R.id.landing_page);
//    }
//    public static String[] econetItems = {
//            "Data",
//            "Voice",
//            "SMS"
//    };
//}
