package com.example.testingmyskills;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private LinearLayout CandidateBtn;
    private LinearLayout CompanyBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.hideSoftNavBar(MainActivity.this);



        initialiseViews();
        setOnclickListeners();

    }
    private void setOnclickListeners() {
        CandidateBtn.setOnClickListener(v->handleCandidateClick());
        CompanyBtn.setOnClickListener(v->handleCompanyClick());
    }

    private void handleCompanyClick() {
        Intent intent = new Intent(MainActivity.this, JobsActivity.class);
        intent.putExtra("constraintLayoutId", R.id.login_page);
        startActivity(intent);
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        intent.putExtra("constraintLayoutId", R.id.login_page);
//        startActivity(intent);

    }

    private void handleCandidateClick() {

    }

    private void initialiseViews() {
        CandidateBtn = findViewById(R.id.candidate_btn);
        CompanyBtn = findViewById(R.id.company_btn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

