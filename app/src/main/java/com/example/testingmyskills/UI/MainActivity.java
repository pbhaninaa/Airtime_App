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

    // Handler to post back results to the main thread
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

        // Perform network tasks in a background thread
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Call API methods

// Working
//                    postResultToMainThread("Login Response: " + loginResponse);


//Working
//                    postResultToMainThread("Registration Response: " + registrationResponse);

//Working
//                    JSONObject catalogRequestResponse = ApiService.catalogRequest();
//                    postResultToMainThread("Catalog Request Response: " + catalogRequestResponse);

// Working
//                    JSONObject validateMsisdnResponse = ApiService.validateMsisdn("Econet","263781801175","263781801175","Lewis","ruffgunz");
//                    postResultToMainThread("Validate MSISDN Response: " + validateMsisdnResponse);
//

//    Working
//                    JSONObject balanceEnquiryResponse = ApiService.balanceEnquiry("Econet","27649045091","Lewis","ruffgunz");
//                    postResultToMainThread("Balance Enquiry Response: " + balanceEnquiryResponse);

// Not yet working (Back end issue)
//                    JSONObject depositFundsResponse = ApiService.depositFunds("27649045091","Lewis","ruffgunz","lewistiyago26@gmail.com","50","840");
//                    postResultToMainThread("Deposit Funds Response: " + depositFundsResponse);
//{

//Working
//                    JSONObject loadValueResponse = ApiService.loadValue("Econet","27649045091","Lewis","ruffgunz","263781801175","50","VALUE","VALUE");
//                    postResultToMainThread("Load Value Response: " + loadValueResponse);


// Working
//                    JSONObject loadBundleResponse = ApiService.loadBundle("Econet", "27649045091", "Lewis", "ruffgunz", "263781801175", "650", "BOJU5", "$6.50 = BOJ OffNet 50Min OnNet 250Min O/Peak 200Min");
//                    postResultToMainThread("Load Bundle Response: " + loadBundleResponse);



//                    Working
                    JSONObject statementResponse = ApiService.statement("27649045091", "Lewis", "ruffgunz", "lewistiyago26@gmail.com");
//                    postResultToMainThread("Statement Response: " + statementResponse);

                } catch (IOException e) {
                    e.printStackTrace();
                    postResultToMainThread("Error: " + e.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();*/
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
