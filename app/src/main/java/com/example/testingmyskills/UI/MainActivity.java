package com.example.testingmyskills.UI;

import static com.example.testingmyskills.JavaClasses.Utils.isUserLogged;
import static com.example.testingmyskills.JavaClasses.Utils.isUserRegistered;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testingmyskills.Dao.DatabaseHelper;
import com.example.testingmyskills.JavaClasses.Bundles;
import com.example.testingmyskills.R;
import com.example.testingmyskills.JavaClasses.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String MSISDN = "263781801175";
    private Button CandidateBtn;
    private ConstraintLayout landing_page;
    public static SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initialiseViews();
        if (isUserLogged(this)) {
            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            startActivity(intent);
        } else {
            landing_page.setVisibility(View.VISIBLE);
        }
        Utils.hideSoftNavBar(MainActivity.this);
        setOnclickListeners();
    }

    private void setOnclickListeners() {
        CandidateBtn.setOnClickListener(v -> handleCompanyClick());
    }

    private void handleCompanyClick() {
//        if (isUserRegistered(this)) {
//            if (isUserLogged(this)) {
//                Intent intent = new Intent(MainActivity.this, Dashboard.class);
//                startActivity(intent);
//            } else {
        Utils.triggerHapticFeedback(this);
        Intent intent = new Intent(MainActivity.this, UserManagement.class);
        intent.putExtra("constraintLayoutId", R.id.login_page);
        startActivity(intent);
//            }
//        } else {
//            Intent intent = new Intent(MainActivity.this, UserManagement.class);
//            intent.putExtra("constraintLayoutId", R.id.create_profile_screen);
//            startActivity(intent);
//        }

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

