package com.example.testingmyskills;

import static com.example.testingmyskills.Utils.isUserLogged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String SERVER_URL = "http://102.219.85.66:7022/xmlrpc";
    public static final String USERNAME = "QuposUSDTest";
    public static final String PASSWORD = "pass123";
    public static final String MSISDN = "263781801175";
    public static final int PROVIDER_CODE = 100;
    private Button CandidateBtn;
    private ConstraintLayout landing_page;

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
        if (isUserLogged(this)) {
            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, UserManagement.class);
            intent.putExtra("constraintLayoutId", R.id.login_page);
            startActivity(intent);
        }

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
            "All",
            "Airtime",
            "Data Bundles",
            "Voice Bundles",
            "SMS Bundles"
    };

    public static String[] getAllCountries() {
        return new String[]{
                "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda",
                "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
                "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia",
                "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso",
                "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Central African Republic",
                "Chad", "Chile", "China", "Colombia", "Comoros", "Congo, Democratic Republic of the",
                "Congo, Republic of the", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic",
                "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador",
                "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France",
                "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea",
                "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia",
                "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan",
                "Kenya", "Kiribati", "Korea, North", "Korea, South", "Kosovo", "Kuwait", "Kyrgyzstan",
                "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania",
                "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
                "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro",
                "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand",
                "Nicaragua", "Niger", "Nigeria", "North Macedonia", "Norway", "Oman", "Pakistan", "Palau",
                "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland",
                "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
                "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia",
                "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia",
                "Solomon Islands", "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan",
                "Suriname", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
                "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
                "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States",
                "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia",
                "Zimbabwe"
        };

    }
}

