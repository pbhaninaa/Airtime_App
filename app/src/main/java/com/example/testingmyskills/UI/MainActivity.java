package com.example.testingmyskills.UI;

import static com.example.testingmyskills.JavaClasses.Utils.isUserLogged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.testingmyskills.JavaClasses.ApiService;
import com.example.testingmyskills.R;
import com.example.testingmyskills.JavaClasses.Utils;

import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static String MSISDN = "263781801175";
    private Button CandidateBtn;
    private ConstraintLayout landing_page;
    public static SQLiteDatabase db;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseViews();

        // Check if user is logged in
        if (isUserLogged(this)) {
            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            startActivity(intent);
        } else {
            landing_page.setVisibility(View.VISIBLE);
        }

        Utils.hideSoftNavBar(MainActivity.this);
        setOnclickListeners();


    }

    // Method to post results to the main UI thread
    private void postResultToMainThread(String result) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // Update UI or show a toast with the result
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static String[] econetItems = {
            "Data",
            "Voice",
            "SMS"
    };

}
