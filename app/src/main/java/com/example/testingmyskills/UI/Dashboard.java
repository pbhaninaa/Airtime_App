package com.example.testingmyskills.UI;

import static com.example.testingmyskills.UI.UserManagement.getCountryList;
import static com.example.testingmyskills.UI.UserManagement.getZimCode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingmyskills.JavaClasses.ApiService;
import com.example.testingmyskills.JavaClasses.CurrencyTextWatcher;
import com.example.testingmyskills.JavaClasses.IveriPaymentProcessor;
import com.example.testingmyskills.JavaClasses.PayNowPaymentProcessor;
import com.example.testingmyskills.JavaClasses.MyJavaScriptInterface;
import com.example.testingmyskills.JavaClasses.Country;
import com.example.testingmyskills.JavaClasses.PaymentProcessor;
import com.example.testingmyskills.R;
import com.example.testingmyskills.JavaClasses.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.internal.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ContextWrapper;
import android.view.ContextThemeWrapper;

public class Dashboard extends AppCompatActivity {
    private ConstraintLayout AppFrame, LoadingLayout;
    private LinearLayout SelectedItem, AmountCapture, WebScree, Navbar, ItemsLayout, ISPsLayout, BuyLayout, EconetIsp, TelecelIsp, NetoneIsp, LoadBalanceLayout;
    private FrameLayout LogoutButton, BackToHome;
    private WebView Web;
    private TextView version, currencySymbolInBuy, AmountToLoadSymbol, SelectedIsp, AvailableBalance, StatusMessage, MoreBtn, ItemToBuyText, SelectedItemType, SelectedItemPrice, SelectedItemLifeTime;
    private EditText Phone, AmountTLoad, AmountTLoadInBuy, LoadingNote;
    private ImageButton NavHomeBtn, NavBuyBtn, NavProfileBtn, NavIPSBtn, NavMoreBtn, NavLaodBalanceBtn1, NavLaodBalanceBtn;
    private RecyclerView ItemRecyclerView;
    private Spinner ItemFilterSpinner, ItemToBuySpinner;
    private Spinner CountryCode;
    private ConstraintLayout ConfirmationScreen;
    private LinearLayout job_list_screen;
    private ImageButton backFromList;
    private ImageView statusLight, CountryFlag, load, LoadingImage;
    private Button BuyBtn, BuyBtn1, Yes, No, LoadBalance1, LoadBalance;
    private TextView number_of_posts;
    private Spinner filter_spinner;


    private ImageButton FilterButton;
    private boolean show;
    static String currencySymbol, ItemCode;
    private RecyclerView jobListRecyclerView;
    private int numItems;
    private static String startDate, endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        Utils.hideSoftNavBar(Dashboard.this);
        getProfile();
        show = true;
        initialiseViews();

// To be removed when top up functionality works
        LinearLayout topUp;
        topUp = (LinearLayout) findViewById(R.id.topUpBtn);

        topUp.setVisibility(Utils.getString(this, "savedCredentials", "email").contains("649045091") || Utils.getString(this, "savedCredentials", "email").contains("782141216") ? View.VISIBLE : View.GONE);
//==========================================
        Utils.hideSoftNavBar(Dashboard.this);
        setOnclickListeners();
        recyclerViews();
        adaptors();

        getLastTransaction(null);
        getBalance(SelectedIsp.getText().toString());
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        currencySymbol = sharedPreferences.getString("currency_symbol", getString(R.string.default_currency_symbol));

        AppFrame.setVisibility(View.VISIBLE);
        BackToHome.setVisibility(SelectedIsp.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
        ISPsLayout.setVisibility(View.VISIBLE);
        NavHomeBtn.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);


        ArrayAdapter<Country> ada = new ArrayAdapter<>(this, R.layout.spinner_item, getZimCode());
        ada.setDropDownViewResource(R.layout.spinner_item);
        CountryCode.setAdapter(ada);

        CountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country selectedCountry = (Country) parent.getItemAtPosition(position);
                String flagName = selectedCountry.getCountryFlag();
                Phone.requestFocus();
                int flagResourceId = getResources().getIdentifier(flagName, "drawable", getPackageName());
                if (flagResourceId != 0) {
                    CountryFlag.setImageResource(flagResourceId);
                } else {
                    Toast.makeText(getApplicationContext(), "Flag not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        AmountTLoad.addTextChangedListener(new CurrencyTextWatcher(AmountTLoad));
        AmountTLoadInBuy.addTextChangedListener(new CurrencyTextWatcher(AmountTLoadInBuy));
        spinners();
        getAccount("");
        Utils.rotateImageView(load);

        startDate = Utils.getTodayDate();
        endDate = Utils.getTodayDate();

    }

    private void initialiseViews() {
        LoadingLayout = findViewById(R.id.load_layout);
        LoadingImage = findViewById(R.id.load_layout_image);
        SelectedItem = findViewById(R.id.selected_item);
        AmountCapture = findViewById(R.id.amount_in_buy);
        load = findViewById(R.id.web_view_loading);
        WebScree = findViewById(R.id.web);
        Web = findViewById(R.id.web_view);

        job_list_screen = findViewById(R.id.Job_list_screen);
        number_of_posts = findViewById(R.id.number_of_posts);
        filter_spinner = findViewById(R.id.filter_spinner);
        backFromList = findViewById(R.id.back_btn_from_job_list);
        BuyBtn = findViewById(R.id.btn_buy);
        BuyBtn1 = findViewById(R.id.btn_buy_1);
        FilterButton = findViewById(R.id.filter_button);
        ConfirmationScreen = findViewById(R.id.confirmation_screen);
        Yes = findViewById(R.id.yes);
        No = findViewById(R.id.no);
        jobListRecyclerView = findViewById(R.id.MoreItemsRecyclerView);
        AppFrame = findViewById(R.id.app_frame);
        LogoutButton = findViewById(R.id.logout_button);
        AvailableBalance = findViewById(R.id.available_balance);
        StatusMessage = findViewById(R.id.status_message);
        NavHomeBtn = findViewById(R.id.nav_dash_board_btn1);
        NavLaodBalanceBtn = findViewById(R.id.nav_load_btn);
        NavLaodBalanceBtn1 = findViewById(R.id.nav_load_btn1);
        NavBuyBtn = findViewById(R.id.nav_buy_btn1);
        NavProfileBtn = findViewById(R.id.nav_profile_btn1);
        NavIPSBtn = findViewById(R.id.nav_networks_btn1);
        NavMoreBtn = findViewById(R.id.more1);

        ISPsLayout = findViewById(R.id.ISP_display_layout);
        BuyLayout = findViewById(R.id.Buying_layout);
        ItemsLayout = findViewById(R.id.Items_display_layout);


        ItemRecyclerView = findViewById(R.id.Items_recycler_view);
        ItemFilterSpinner = findViewById(R.id.items_spinner);
        MoreBtn = findViewById(R.id.More_Items);

        ItemToBuySpinner = findViewById(R.id.item_to_buy);
        ItemToBuyText = findViewById(R.id.item_to_buy_text);

        Navbar = findViewById(R.id.navbar);
        SelectedIsp = findViewById(R.id.selected_network_text);
        AmountToLoadSymbol = findViewById(R.id.currency_symbol);

        EconetIsp = findViewById(R.id.econetISP);
        TelecelIsp = findViewById(R.id.telecelISP);
        NetoneIsp = findViewById(R.id.netoneISP);
        Phone = findViewById(R.id.mobile_number);
        BackToHome = findViewById(R.id.back_to_home);

        SelectedItemType = findViewById(R.id.item_type1);
        SelectedItemPrice = findViewById(R.id.item_price1);
        SelectedItemLifeTime = findViewById(R.id.item_life_time1);
        CountryCode = findViewById(R.id.login_country_codes);
        CountryFlag = findViewById(R.id.login_country_flag);

        LoadBalanceLayout = findViewById(R.id.Load_balance_layout);
        LoadBalance = findViewById(R.id.btn_load_balance);
        LoadBalance1 = findViewById(R.id.btn_load_balance1);
        LoadingNote = findViewById(R.id.loading_notes);
        AmountTLoad = findViewById(R.id.loading_amount);
        statusLight = findViewById(R.id.status_light);

        currencySymbolInBuy = findViewById(R.id.currency_symbol_in_buy);
        AmountTLoadInBuy = findViewById(R.id.loading_amount_in_buy);

    }

    public void showLastTransaction() {
        String last = StatusMessage.getText().toString();

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        if (last.endsWith(currentTime)) {
            getLastTransaction(null);
        } else {
            Utils.showToast(this,"Last transaction does not match the current time.");
        }
    }


    private void handleLoadBalance() {
        String amount = AmountTLoad.getText().toString().trim();
        amount = amount.replaceAll("[^\\d.]", "").replaceAll("[^\\d,]", "");

        if (amount.isEmpty() || amount.equalsIgnoreCase("0.00")) {
            AmountTLoad.setError("Amount is required");
            return;
        }

        Web.getSettings().setJavaScriptEnabled(true);
        Web.getSettings().setDomStorageEnabled(true);
        Web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        Web.addJavascriptInterface(new MyJavaScriptInterface(this), "Android");
        Web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();


                if (url.contains("payment-success")) {
                    String transactionId = Uri.parse(url).getQueryParameter("transactionId");
                    handlePaymentResult("{\"status\": \"success\", \"transactionId\": \"" + transactionId + "\"}");

                    return true;
                } else if (url.contains("payment-failure")) {
                    handlePaymentResult("{\"status\": \"failure\"}");
                    return true;
                }

                view.loadUrl(url);
                return false;
            }
        });

        hideLayouts(WebScree, NavBuyBtn);
        Navbar.setVisibility(View.GONE);

        String finalAmount = amount;
        new Thread(() -> {
            PaymentProcessor processor = new PaymentProcessor();
            String response = processor.createOrder(finalAmount, getResources().getString(R.string.yoco_api_key), getResources().getString(R.string.successRedirectUrl), getResources().getString(R.string.yoco_failureUrl));

            runOnUiThread(() -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String redirectUrl = jsonResponse.optString("redirectUrl");

                    if (redirectUrl != null && !redirectUrl.isEmpty()) {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Web.loadUrl(redirectUrl);

                            load.setVisibility(View.GONE);
                            Web.setVisibility(View.VISIBLE);
                        }, 5000);
                    } else {
                        Utils.showToast(this, "Failed to get redirect URL.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(this,"Failed to parse JSON response.");
                }
            });
        }).start();
    }
    private void handlePayNowPayment() {
        String amountString = AmountTLoad.getText().toString().trim().replaceAll("[^\\d.]", "");

        if (amountString.isEmpty() || amountString.equalsIgnoreCase("0.00")) {
            AmountTLoad.setError("Amount is required");
            return;
        }

        Utils.hideSoftKeyboard(Dashboard.this);

        double amount = 0.0;
        try {
            amount = Double.parseDouble(amountString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            AmountTLoad.setError("Invalid amount");
            return;
        }

        hideLayouts(WebScree, NavBuyBtn);
        Navbar.setVisibility(View.GONE);
        load.setVisibility(View.VISIBLE);

        PayNowPaymentProcessor.createPayNowOrder(this, "Load balance", amount, new PayNowPaymentProcessor.PayNowCallback() {
            @Override
            public void onSuccess(PayNowPaymentProcessor.PayNowResponse payNowResponse) {
                runOnUiThread(() -> {
                    if (payNowResponse != null && payNowResponse.getRedirectUrl() != null) {
                        Web.getSettings().setJavaScriptEnabled(true);
                        Web.getSettings().setDomStorageEnabled(true);
                        Web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

                        Web.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                String url = request.getUrl().toString();

                                if (url.contains("payment-success")) {
                                    String transactionId = Uri.parse(url).getQueryParameter("transactionId");
                                    handlePaymentResult("{\"status\": \"success\", \"transactionId\": \"" + transactionId + "\"}");
                                    return true;
                                } else if (url.contains("payment-failure")) {
                                    handlePaymentResult("{\"status\": \"failure\"}");
                                    return true;
                                }
                                Utils.showToast(Dashboard.this, "URL : " + url);
                                view.loadUrl(url);
                                return false;
                            }
                        });

                        Web.loadUrl(payNowResponse.getRedirectUrl());
                        load.setVisibility(View.GONE);
                        Web.setVisibility(View.VISIBLE);
                    } else {
                        load.setVisibility(View.GONE);
                        AmountTLoad.setError("Failed to generate payment link");
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    load.setVisibility(View.GONE);
                    AmountTLoad.setError("Payment failed: " + error);
                });
            }
        });

    }


    private void handlePaymentResult(String result) {
        runOnUiThread(() -> {
            try {
                JSONObject resultJson = new JSONObject(result);
                String status = resultJson.getString("status");
                String message;
                String transactionId = resultJson.optString("transactionId");

                if ("success".equals(status)) {
                    message = "Payment successful!";

                    confirmPaymentWithWebhook(transactionId);
                } else {
                    message = "Payment failed. Please try again.";
                }

                Utils.showToast(this, message);

                new Handler().postDelayed(() -> closePaymentView(null), 200);
            } catch (JSONException e) {
                e.printStackTrace();
                Utils.showToast(this, "Error processing payment result.");
            }
        });
    }

    private void confirmPaymentWithWebhook(String transactionId) {
        new Thread(() -> {
            try {
                String webhookUrl = "https://your-server.com/webhook";
                JSONObject payload = new JSONObject();
                payload.put("status", "successful");
                payload.put("transactionId", transactionId);

                HttpURLConnection connection = (HttpURLConnection) new URL(webhookUrl).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                os.write(payload.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    Utils.showToast(this,"Webhook response: " + response.toString());
                } else {
                    Utils.showToast(this,"Webhook request failed. Response code: " + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void closePaymentView(View view) {
        Web.loadUrl("about:blank");
        handleManualLoadBalance();


    }

    @Override
    public void onBackPressed() {
        if (Web.canGoBack()) {
            Web.goBack();
        } else {
            super.onBackPressed();
        }
    }


    private void hideLayouts(LinearLayout layoutToDisplay, ImageButton imageButton) {
        if (SelectedIsp.getText().toString().isEmpty()) {
            Utils.showToast(this, "Select Network");
            return;
        }
        clearFields();
        if (imageButton.getId() == R.id.nav_buy_btn1) {
            Phone.requestFocus();
            SelectedItem.setVisibility(View.VISIBLE);
            AmountCapture.setVisibility(View.GONE);
            BuyBtn1.setVisibility(View.GONE);
            BuyBtn.setVisibility(View.VISIBLE);
        } else if (imageButton.getId() == R.id.nav_load_btn1) {
            Phone.requestFocus();
            SelectedItem.setVisibility(View.GONE);
            AmountCapture.setVisibility(View.VISIBLE);
            BuyBtn1.setVisibility(View.VISIBLE);
            BuyBtn.setVisibility(View.GONE);
        } else if (imageButton.getId() == R.id.more1) {
            SharedPreferences sharedPreferences = this.getSharedPreferences("LoggedUserCredentials", Context.MODE_PRIVATE);
            String name = sharedPreferences.getString("name", "");
            String surname = sharedPreferences.getString("surname", "");
            String AgentID = sharedPreferences.getString("phone", "");
            String AgentEmail = sharedPreferences.getString("email", "");
            String AgentPassword = sharedPreferences.getString("password", "");
            String AgentName = name + " " + surname;

        }
        LoadBalance1.setVisibility(View.GONE);
        LoadBalance.setVisibility(View.VISIBLE);

        defaultColoring(imageButton);
        imageButton.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);
        BackToHome.setVisibility(View.VISIBLE);
        ISPsLayout.setVisibility(View.GONE);
        BuyLayout.setVisibility(View.GONE);
        ItemsLayout.setVisibility(View.GONE);
        LoadBalanceLayout.setVisibility(View.GONE);
        WebScree.setVisibility(View.GONE);
        layoutToDisplay.setVisibility(View.VISIBLE);


    }


    private void adaptors() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, MainActivity.econetItems);
        adapter.setDropDownViewResource(R.layout.spinner_layout);

        filter_spinner.setAdapter(adapter);
        ItemFilterSpinner.setAdapter(adapter);
        ItemToBuySpinner.setAdapter(adapter);
    }

    private void recyclerViews() {
        getProducts(new ProductsCallback() {
            @Override
            public void onProductsLoaded(List<Map<String, Object>> products) {
                ItemRecyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                ItemRecyclerView.setAdapter(new RecommendedAd(products));
            }
        });
    }


    public void populateHistory(String startDate, String endDate) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("LoggedUserCredentials", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        String AgentID = sharedPreferences.getString("phone", "");
        String AgentEmail = sharedPreferences.getString("email", "");
        String AgentPassword = sharedPreferences.getString("password", "");
        String AgentName = name + " " + surname;


        getStatement(AgentID, AgentName, AgentPassword, AgentEmail, this, startDate, endDate, new StatementCallback() {
            @Override
            public void onResult(List<Map<String, Object>> statements) {
                runOnUiThread(() -> {
                    if (jobListRecyclerView.getLayoutManager() == null) {
                        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    }

                    if (jobListRecyclerView.getAdapter() != null && jobListRecyclerView.getAdapter() instanceof Statement) {
                        ((Statement) jobListRecyclerView.getAdapter()).updateStatements(statements);
                    } else {
                        jobListRecyclerView.setAdapter(new Statement(Dashboard.this, statements));
                    }
                });
            }
        });
    }


    public void AlertString(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }


    private void setOnclickListeners() {
        backFromList.setOnClickListener(v -> handleBackFromList());
        BuyBtn.setOnClickListener(v -> handleTransaction());
        BuyBtn1.setOnClickListener(v -> buy());
        FilterButton.setOnClickListener(v -> selectDateRange());
        No.setOnClickListener(v -> handleNo());
        Yes.setOnClickListener(v -> handleYes());
        LogoutButton.setOnClickListener(v -> logout());
        NavHomeBtn.setOnClickListener(v -> hideLayouts(ISPsLayout, NavHomeBtn));
        NavLaodBalanceBtn.setOnClickListener(v -> hideLayouts(LoadBalanceLayout, NavLaodBalanceBtn));
        NavLaodBalanceBtn1.setOnClickListener(v -> hideLayouts(BuyLayout, NavLaodBalanceBtn1));
        NavIPSBtn.setOnClickListener(v -> hideLayouts(ItemsLayout, NavIPSBtn));
        NavMoreBtn.setOnClickListener(v -> handleShowMore());

        NavBuyBtn.setOnClickListener(v -> hideLayouts(BuyLayout, NavBuyBtn));
        MoreBtn.setOnClickListener(v -> handleShowMore());
        NavProfileBtn.setOnClickListener(v -> showProfile());
        EconetIsp.setOnClickListener(v -> setISP("Econet"));
        NetoneIsp.setOnClickListener(v -> setISP("NetOne"));
        TelecelIsp.setOnClickListener(v -> setISP("Telecel"));
        LogoutButton.setOnLongClickListener(view -> {
            finishAffinity();
            return true;
        });
        BackToHome.setOnClickListener(v -> {
            Utils.hideSoftKeyboard(Dashboard.this);

            Navbar.setVisibility(View.VISIBLE);
            if (ItemsLayout.getVisibility() == View.VISIBLE) {
                hideLayouts(ISPsLayout, NavHomeBtn);
            } else {
                hideLayouts(ItemsLayout, NavHomeBtn);

            }
        });
//        LoadBalance.setOnClickListener(v -> handleLoadBalance());
        LoadBalance.setOnClickListener(v -> handlePayNowPayment());
        LoadBalance1.setOnClickListener(v -> handleManualLoadBalance());
    }

    private void handleManualLoadBalance() {
        String amount = AmountTLoad.getText().toString().trim();
        amount = amount.replaceAll("[^\\d.]", "").replaceAll("[^\\d,]", "");

        if (amount.isEmpty() || amount.equalsIgnoreCase("0.00")) {
            AmountTLoad.setError("Amount is required");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject res = ApiService.depositFunds(Utils.getString(Dashboard.this, "LoggedUserCredentials", "phone"), AmountTLoad.getText().toString(), "840", Dashboard.this);
                    if (res.getInt("responseCode") == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String responseString = res.getString("response");
                                    JSONObject responseJson = new JSONObject(responseString);
                                    JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                    JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                    JSONObject userObject = paramsList.getJSONObject(0);
                                    String balance = userObject.getString("decimalBalance");
                                    Utils.saveString(Dashboard.this, "profile", "balance", balance);
                                    getAccount(balance);
                                    setISP(SelectedIsp.getText().toString());
                                    Navbar.setVisibility(View.VISIBLE);
                                    LoadBalance1.setVisibility(View.GONE);
                                    LoadBalance.setVisibility(View.VISIBLE);
                                    hideLayouts(ItemsLayout, NavIPSBtn);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    try {
                                        Utils.showToast(Dashboard.this, res.getString("response"));
                                    } catch (JSONException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Utils.showToast(Dashboard.this, res.getString("response"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast(Dashboard.this, "Service Provider Offline");
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }


    public void setISP(String ISP) {

        if (!ISP.equals("Econet")) {

            Utils.showToast(this, "Not yet available");
            return;
        }
        Utils.LoadingLayout(this, this);

        getBalance(ISP);
        SelectedIsp.setText(ISP);
        hideLayouts(ItemsLayout, NavIPSBtn);
        BackToHome.setVisibility(View.VISIBLE);
        ISPsLayout.setVisibility(View.GONE);
        ItemsLayout.setVisibility(View.VISIBLE);

    }

    public void getBalance(String ISP) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject res = ApiService.balanceEnquiry(ISP, Utils.getString(Dashboard.this, "profile", "phone"), Dashboard.this);
                    if (res.getInt("responseCode") == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getLastTransaction(null);
                                    String responseString = res.getString("response");
                                    JSONObject responseJson = new JSONObject(responseString);
                                    JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                    JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                    JSONObject userObject = paramsList.getJSONObject(0);
                                    String balance = userObject.getString("decimalBalance");
                                    getAccount(balance);
                                    Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    try {
                                        Utils.showToast(Dashboard.this, res.getString("response"));
                                    } catch (JSONException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Utils.showToast(Dashboard.this, res.getString("response"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast(Dashboard.this, "Service Provider Offline");
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void getAccount(String bal) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
        if (!bal.isEmpty()) {
            Utils.saveString(Dashboard.this, "profile", "balance", bal);
        } else {
            Utils.saveString(Dashboard.this, "profile", "balance", sharedPreferences.getString("balance", ""));
        }


        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        String phone = sharedPreferences.getString("phone", "");
        String balance = sharedPreferences.getString("balance", "");
        String updated = sharedPreferences.getString("time", "");
        String formatedSalutation = "Hello, " + name + " " + surname + " \n" + "Agent ID:  " + phone + "\n";
        String Balance = balance.isEmpty() ? "No Balance to display" : (String.format("Account Balance %s%s", currencySymbol, Utils.FormatAmount(bal.isEmpty() ? balance : bal)));
        StatusMessage.setText(formatedSalutation);
        Utils.setStatusColor(this, (bal.isEmpty() ? balance : bal), statusLight);
        AvailableBalance.setText(Balance);
        AmountToLoadSymbol.setText(currencySymbol);
        currencySymbolInBuy.setText(currencySymbol);
        clearFields();
    }

    private void logout() {
        Utils.hideSoftKeyboard(Dashboard.this);
        Utils.hideSoftNavBar(Dashboard.this);
        Navbar.setVisibility(View.GONE);
        AppFrame.setVisibility(View.GONE);
        ConfirmationScreen.setVisibility(View.VISIBLE);
    }

    private void handleNo() {
        AppFrame.setVisibility(View.VISIBLE);
        ConfirmationScreen.setVisibility(View.GONE);
    }

    private void handleYes() {
        Utils.logout(this);
        Intent intent = new Intent(this, UserManagement.class);
        intent.putExtra("constraintLayoutId", R.id.login_page);
        startActivity(intent);

    }

    private void selectDateRange() {
        showDateDialog(Dashboard.this, this, this);
    }


public void showDateDialog(final Context context, final Activity activity, final Dashboard dashboard) {
    Context themedContext = new ContextThemeWrapper(context, R.style.AppThemes);
    LinearLayout layout = new LinearLayout(themedContext);
    layout.setOrientation(LinearLayout.VERTICAL);
    final EditText startDateInput = new EditText(themedContext);
    final EditText endDateInput = new EditText(themedContext);
    startDateInput.setBackgroundResource(R.drawable.edit_text_background);
    startDateInput.setHint("Select Start Date");
    startDateInput.setFocusable(false);
    startDateInput.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);
    endDateInput.setBackgroundResource(R.drawable.edit_text_background);
    endDateInput.setHint("Select End Date");
    endDateInput.setFocusable(false);
    endDateInput.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );
    int marginInDp = 20;
    int marginInPx = convertDpToPx(context, marginInDp);
    params.setMargins(marginInPx, 5, marginInPx, 5);
    startDateInput.setLayoutParams(params);
    endDateInput.setLayoutParams(params);

    layout.addView(startDateInput);
    layout.addView(endDateInput);

    final Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

    final String currentDate = dateFormat.format(calendar.getTime());
    endDateInput.setText(currentDate);

    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(themedContext)
            .setTitle("Select Start Date")
            .setView(layout)
            .setPositiveButton("OK", (dialog1, which) -> {
                String start = startDateInput.getText().toString().trim();
                String end = endDateInput.getText().toString().trim();


                if (start.isEmpty()) {
                    Utils.showToast(themedContext, "Start date must be selected.");
                } else {
                    try {
                        Date startD = dateFormat.parse(start);
                        Date endD = dateFormat.parse(end);

                        if (startD != null && endD != null) {
                            if (startD.after(endD)) {
                                Utils.showToast(themedContext, "Start date cannot be after end date.");

                            } else {
                                startDate = start;
                                endDate = end;
                                populateHistory(start, end);
                            }
                        }
                    } catch (ParseException e) {
                        Utils.showToast(themedContext, "Invalid date format.");
                    }
                }
            })
            .setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());

    AlertDialog dialog = dialogBuilder.create();
    dialog.show();

    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
    positiveButton.setBackgroundResource(R.drawable.button_background);
    positiveButton.setTextColor(Color.WHITE);
    positiveButton.setText("OK");

    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
    negativeButton.setBackgroundResource(R.drawable.button_background);
    negativeButton.setTextColor(Color.WHITE);
    negativeButton.setText("Cancel");

    startDateInput.setOnClickListener(v -> showDatePicker(themedContext, startDateInput, dialog, dateFormat, endDateInput, false));

    new Handler().postDelayed(() -> startDateInput.performClick(), 300);

    endDateInput.setOnClickListener(v -> showDatePicker(themedContext, endDateInput, dialog, dateFormat, startDateInput, true));
}

    private void showDatePicker(Context context, EditText dateInput, AlertDialog dialog, SimpleDateFormat dateFormat, EditText otherDateInput, boolean isEndDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            String selectedDate = dateFormat.format(calendar.getTime());
            dateInput.setText(selectedDate);

            try {
                Date selected = dateFormat.parse(selectedDate);
                Date otherDate = dateFormat.parse(otherDateInput.getText().toString());

                if (isEndDate && selected != null && otherDate != null && selected.before(otherDate)) {
                    otherDateInput.setText(selectedDate);
                    Utils.showToast(context, "Start date updated to match end date.");
                } else if (!isEndDate && selected != null && otherDate != null && selected.after(otherDate)) {
                    otherDateInput.setText(selectedDate);
                    Utils.showToast(context, "End date updated to match start date.");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!isEndDate) {
                dialog.setTitle("Select End Date");
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1);

        datePickerDialog.setOnShowListener(dialogInterface -> {
            Button okButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE);
            if (okButton != null) {
                okButton.setBackgroundResource(R.drawable.button_background);
                okButton.setTextColor(Color.WHITE);
            }
        });

        datePickerDialog.show();
    }

    private static int convertDpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    private void getProfile() {
        SharedPreferences prefs = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "");
        String phoneNumber = name + "(" + prefs.getString("phone", "") + ")";
        String salutation = "Hello " + phoneNumber;
        String updateTime = "last updated a minute ago";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(salutation);
        int start = 0;
        int end = salutation.length();
        builder.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, 0);
        builder.append("\n").append(updateTime);
        start = end + 1;
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.GRAY), start, end, 0);
        TextView salute = findViewById(R.id.salutation_text);
        salute.setText(builder);

    }

    public void buy() {
        String price = AmountTLoadInBuy.getText().toString().trim().replace(currencySymbol, "");
        String phone = Phone.getText().toString().trim();
        if (phone.isEmpty()) {
            Phone.setError("Phone is required");
            return;
        }
        clearFields();
        String p = CountryCode.getSelectedItem() + phone;

        if (price.equals("0.00")) {
            AmountTLoadInBuy.setError("Price is required");
        }
        String balanceStr = AvailableBalance.getText().toString().replace(currencySymbol, "").replace(",", "").replace("Account Balance", "").trim();

        Utils.LoadingLayout(this, this);

        try {
            double priceValue = price.isEmpty() ? 0 : Double.parseDouble(price);
            if (priceValue < 0) {
                AmountTLoadInBuy.setError("Price is required");
                return;
            }
            double balanceValue = Double.parseDouble(balanceStr);
            if (priceValue < balanceValue) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject res = ApiService.loadValue(SelectedIsp.getText().toString(), Utils.getString(Dashboard.this, "LoggedUserCredentials", "phone"), p, price.replace(",", "").replace(".", ""), "Airtime", "Airtime", Dashboard.this);
                            if (res.getInt("responseCode") == 200) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String responseString = res.getString("response");
                                            JSONObject responseJson = new JSONObject(responseString);
                                            JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                            JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                            JSONObject responseDetails = paramsList.getJSONObject(0);
                                            String Serial = responseDetails.getString("providerSerial");
                                            String description = responseDetails.getString("providerStatus");
                                            String balance = responseDetails.getString("decimalBalance");
                                            String Network = responseDetails.getString("network");
                                            String basketID = responseDetails.getString("basketID");
                                            String CustomerID = responseDetails.getString("customerID");
                                            String AgentID = responseDetails.getString("agentID");
                                            String date = responseDetails.getString("entryDate");
                                            String amount = responseDetails.getString("decimalAmount");
                                            Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);

                                            if (!Serial.isEmpty()) {
                                                Utils.hideSoftKeyboard(Dashboard.this);
                                                new AlertDialog.Builder(Dashboard.this).setTitle("Transaction was successful").setMessage("Date: " + date + "\n\nRecharge Number: " + CustomerID + "\nRecharge Amount: " + currencySymbol + amount + "\nRecharge Serial: " + Serial).setPositiveButton("OK", null).show();
                                                hideLayouts(ISPsLayout, NavIPSBtn);
                                                setISP(SelectedIsp.getText().toString());
                                                Utils.saveRefs(Dashboard.this, Network, AgentID, CustomerID, basketID);
                                                getAccount(balance);
                                            } else {
                                                Utils.showToast(Dashboard.this, description);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Utils.showToast(Dashboard.this, "Error parsing response data: " + (e.getMessage().contains("connect") ? "Service Provider Offline" : e.getMessage()));
                                        }
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.showToast(Dashboard.this, res.toString());
                                    }
                                });
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showToast(Dashboard.this, (e.getMessage().contains("connect") ? "Service Provider Offline" : e.getMessage()));
                                }
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();

            } else {
                Utils.showToast(this, "Insufficient Funds");
            }

        } catch (NumberFormatException e) {
            Utils.showToast(this, "Invalid balance or price format");
            e.printStackTrace();
        }
    }

    private void handleTransaction() {
        String phone = Phone.getText().toString().trim();
        String price = SelectedItemPrice.getText().toString().trim().replace(currencySymbol, "");
        if (phone.isEmpty()) {
            return;
        }
        clearFields();
        String p = CountryCode.getSelectedItem() + phone;

        String balanceStr = AvailableBalance.getText().toString().replace(currencySymbol, "")
                .replace(",", "")
                .replace("Account Balance", "")
                .trim();
        Utils.LoadingLayout(this, this);
        try {
            double balanceValue = Double.parseDouble(balanceStr);
            double priceValue = Double.parseDouble(price);
            if (priceValue < balanceValue) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SharedPreferences sharedPreferences = Dashboard.this.getSharedPreferences("LoggedUserCredentials", Context.MODE_PRIVATE);
                            String name = sharedPreferences.getString("name", "");
                            String surname = sharedPreferences.getString("surname", "");
                            String agentId = sharedPreferences.getString("phone", "");
                            String agentPassword = sharedPreferences.getString("password", "");
                            String agentName = name + " " + surname;

                            JSONObject res = ApiService.loadBundle(SelectedIsp.getText().toString(),
                                    agentId,
                                    agentName,
                                    agentPassword,
                                    p.replace("+", ""),
                                    SelectedItemPrice.getText().toString().replace(currencySymbol, "")
                                            .replace(".", ""),
                                    ItemCode,
                                    SelectedItemType.getText().toString()
                                    , Dashboard.this);
                            if (res.getInt("responseCode") == 200) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String responseString = res.getString("response");
                                            JSONObject responseJson = new JSONObject(responseString);
                                            JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                            JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                            JSONObject responseDetails = paramsList.getJSONObject(0);
                                            String Serial = responseDetails.getString("providerSerial");
                                            String description = responseDetails.getString("providerStatus");
                                            String Network = responseDetails.getString("network");
                                            String basketID = responseDetails.getString("basketID");
                                            String CustomerID = responseDetails.getString("customerID");
                                            String AgentID = responseDetails.getString("agentID");
                                            String amount = responseDetails.getString("decimalAmount");
                                            String date = responseDetails.getString("entryDate");
                                            Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);
                                            if (!Serial.isEmpty()) {
                                                Utils.hideSoftKeyboard(Dashboard.this);
                                                new AlertDialog.Builder(Dashboard.this).setTitle("Transaction was successful").setMessage("Date: " + date + "\n\nRecharge Number: " + CustomerID + "\nRecharge Amount: " + currencySymbol + amount + "\nRecharge Serial: " + Serial).setPositiveButton("OK", null).show();
                                                hideLayouts(ISPsLayout, NavIPSBtn);
                                                setISP(SelectedIsp.getText().toString());
                                                Utils.saveRefs(Dashboard.this, Network, AgentID, CustomerID, basketID);
                                            } else {
                                                Utils.showToast(Dashboard.this, description);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Utils.showToast(Dashboard.this, "Error parsing response data: " + e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.showToast(Dashboard.this, res.toString());
                                    }
                                });
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showToast(Dashboard.this, "Error: " + e.getMessage());
                                }
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();

            } else {
                Utils.showToast(this, "Insufficient Funds");
            }

        } catch (NumberFormatException e) {
            Utils.showToast(this, "Invalid balance or price format");
            e.printStackTrace();
        }

    }

    public void showProfile() {
        Intent intent = new Intent(this, UserManagement.class);
        intent.putExtra("constraintLayoutId", R.id.create_profile_screen);
        startActivity(intent);
    }

    public void goToOtherApp(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.finance", "com.example.finance.MainActivity"));
        startActivity(intent);

    }

    public void getLastTransaction(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String agentID = Utils.getString(Dashboard.this, "savedCredentials", "email");


                    JSONObject res = ApiService.getLastTransaction(agentID, Dashboard.this);


                    if (res.getInt("responseCode") == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String responseString = res.getString("response");
                                    JSONObject responseJson = new JSONObject(responseString);
                                    JSONObject methodResponse = responseJson.getJSONObject("methodResponse");

                                    String paramsKey = null;
                                    Iterator<String> keys = methodResponse.keys();
                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        if (key.startsWith("paramsList")) {
                                            paramsKey = key;
                                            break;
                                        }
                                    }

                                    if (paramsKey != null) {
                                        JSONArray paramsList = methodResponse.getJSONArray(paramsKey);
                                        if (paramsList.length() == 0) {
                                            double balance = Double.parseDouble(Utils.getString(Dashboard.this, "profile", "balance"));
                                            return;
                                        }
                                        JSONObject responseDetails = paramsList.getJSONObject(0);
                                        String serial = responseDetails.optString("providerSerial", "N/A");

                                        if (!serial.equals("N/A") && !serial.isEmpty()) {
                                            String currentM = StatusMessage.getText().toString();
                                            String LastTransactionDate = "Last Transaction: " + responseDetails.optString("entryDate", "N/A");
                                            String updatedMessage = currentM + LastTransactionDate;
                                            runOnUiThread(() -> {
                                                if (!StatusMessage.getText().toString().contains(LastTransactionDate)) {
                                                    StatusMessage.setText(updatedMessage);
                                                }
                                                if (view != null) {

                                                    String amount = responseDetails.optString("rechargeAmount", "N/A");
                                                    String customerNumber = responseDetails.optString("customerID", "N/A");
                                                    String date = responseDetails.optString("entryDate", "N/A");

                                                    new AlertDialog.Builder(Dashboard.this).setTitle("Transaction was successful").setMessage("Date: " + date + "\n\nRecharge Number: " + customerNumber + "\nRecharge Amount: " + currencySymbol + amount + "\nRecharge Serial: " + serial).setPositiveButton("OK", null).show();
                                                }
                                            });

                                        } else {
                                            runOnUiThread(() -> Utils.showToast(Dashboard.this, "No transaction to display"));
                                        }
                                    } else {
                                        runOnUiThread(() -> Utils.showToast(Dashboard.this, "Error: paramsList not found in the response."));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    runOnUiThread(() -> Utils.showToast(Dashboard.this, "Error parsing response data: " + (e.getMessage().contains("connect") ? "Service Provider Offline" : e.getMessage())));
                                }

                            }

                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showToast(Dashboard.this, res.toString());
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast(Dashboard.this, (e.getMessage().contains("connect") ? "Service Provider Offline" : e.getMessage()));
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void showManualLoad(View view) {
        Utils.showToast(this, "Manual Deposit funds activated");
        LoadBalance1.setVisibility(View.VISIBLE);
        LoadBalance.setVisibility(View.GONE);

    }

    public class RecommendedAd extends RecyclerView.Adapter<RecommendedAd.ViewHolder> {

        private List<Map<String, Object>> jobPosts;

        public RecommendedAd(List<Map<String, Object>> jobPosts) {
            this.jobPosts = jobPosts;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_structure_display, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Map<String, Object> jobPost = jobPosts.get(position);
            holder.bind(jobPost);
        }

        @Override
        public int getItemCount() {
            return jobPosts.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView Type;
            TextView LifeTime;
            TextView Price;
            String amount, type, time, price, itemDescription, itemCode;


            public ViewHolder(View itemView) {
                super(itemView);
                Type = itemView.findViewById(R.id.ite_type);
                LifeTime = itemView.findViewById(R.id.item_life_time);
                Price = itemView.findViewById(R.id.item_price);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ItemToBuyText.setText(String.format("%s\n thatef last for %s", Type.getText().toString(), time));
                    SelectedItemType.setText(itemDescription);
                    SelectedItemLifeTime.setText(time);
                    SelectedItemPrice.setText(String.format("%s%s", currencySymbol, price));
                    ItemCode = itemCode;
                    Utils.setFieldFocus(Phone, Dashboard.this);
                    if (job_list_screen.getVisibility() == View.VISIBLE) {
                        job_list_screen.setVisibility(View.GONE);
                        AppFrame.setVisibility(View.VISIBLE);
                        Navbar.setVisibility(View.VISIBLE);
                    }

                    hideLayouts(BuyLayout, NavBuyBtn);
                }
            }

            public void bind(Map<String, Object> jobPost) {
                amount = Objects.requireNonNull(jobPost.get("amount")).toString();
                type = Objects.requireNonNull(jobPost.get("type")).toString();
                time = Objects.requireNonNull(jobPost.get("lifeTime")).toString();
                itemCode = Objects.requireNonNull(jobPost.get("productID")).toString();
                itemDescription = Objects.requireNonNull(jobPost.get("productDescription")).toString();
                price = Utils.FormatAmount(Objects.requireNonNull(jobPost.get("price")).toString());

                Type.setText(String.format(itemDescription));
                LifeTime.setText(time);
                Price.setText(String.format("%s %s", currencySymbol, price));

            }
        }

    }

    public static class Statement extends RecyclerView.Adapter<Statement.ViewHolder> {

        private List<Map<String, Object>> statements;
        private final Context context;
        public Statement(Context context, List<Map<String, Object>> statements) {
            this.context = context;
            this.statements = statements != null ? statements : new ArrayList<>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Map<String, Object> statement = statements.get(position);
            holder.bind(statement);

            holder.itemView.setOnClickListener(v -> {
                int primaryColor = context.getResources().getColor(R.color.vodacom_color);
                String colorHex = String.format("#%06X", (0xFFFFFF & primaryColor));
                StringBuilder detailsBuilder = new StringBuilder();
                for (Map.Entry<String, Object> entry : statement.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null && !value.toString().trim().isEmpty()) {
                        String key = entry.getKey();
                        String formattedKey = key.replaceAll("([a-z])([A-Z])", "$1 $2");
                        String capitalizedKey = formattedKey.substring(0, 1).toUpperCase() + formattedKey.substring(1);
                         detailsBuilder.append("<font color='").append(colorHex).append("'>")
                                .append("<small>").append(capitalizedKey).append(":\t</small></font> ")
                                .append("<small>").append(value).append("</small><br>");
                    }
                }

                String details = detailsBuilder.toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    new AlertDialog.Builder(context).setTitle("Transaction Details").setMessage(details.isEmpty() ? "No details available." : Html.fromHtml(details, Html.FROM_HTML_MODE_LEGACY))
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return statements.size();
        }
        public void updateStatements(List<Map<String, Object>> newStatements) {
            this.statements = newStatements != null ? newStatements : new ArrayList<>();
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView referenceID, entryDate, transactionType, amount, balance;
            LinearLayout amountRow, balanceRow;

            public ViewHolder(View itemView) {
                super(itemView);
                referenceID = itemView.findViewById(R.id.referenceId);
                entryDate = itemView.findViewById(R.id.entryDate);
                transactionType = itemView.findViewById(R.id.transactionType);
                amount = itemView.findViewById(R.id.amount);
                balance = itemView.findViewById(R.id.cumulativeBalance);
                amountRow = itemView.findViewById(R.id.amount_row);
                balanceRow = itemView.findViewById(R.id.balance_row);
            }public void bind(Map<String, Object> statement) {
                String transactionTypeValue = getValueFromMap(statement, "transactionType", "N/A");

                referenceID.setText(getValueFromMap(statement, "basketID", "N/A"));
                entryDate.setText(getValueFromMap(statement, "entryDate", "N/A"));
                String decimalBalance = getValueFromMap(statement, "decimalBalance", "0.00");

                if (decimalBalance == null || decimalBalance.trim().isEmpty()) {
                    decimalBalance = "0.00";
                }
                String decimalTotal = getValueFromMap(statement, "decimalTotal", "0.00");

                if (decimalTotal == null || decimalTotal.trim().isEmpty()) {
                    decimalTotal = "0.00";
                }

                String formattedBalance = String.format("%s%s", currencySymbol, Utils.FormatAmount(decimalBalance));
                balance.setText(formattedBalance);

                transactionType.setText(transactionTypeValue);
                String amountKey = transactionTypeValue.contains("Deposit") ? "depositAmount" : "rechargeAmount";
                String transactionAmount = getValueFromMap(statement, amountKey, "N/A");
                transactionAmount = (transactionAmount == null || transactionAmount.isEmpty()) ? "0.00" : transactionAmount;
                String formattedAmount = Utils.FormatAmount(transactionAmount);
                amount.setText(String.format("%s%s", currencySymbol, formattedAmount));
//                amountRow.setVisibility(transactionAmount.equals("0.00") ? View.GONE : View.VISIBLE);
//                balanceRow.setVisibility(decimalBalance.equals("0.00") ? View.GONE : View.VISIBLE);

            }

            private String getValueFromMap(Map<String, Object> map, String key, String defaultValue) {
                Object value = map.get(key);

                return value != null ? value.toString() : defaultValue;
            }
        }
    }

    public interface StatementCallback {
        void onResult(List<Map<String, Object>> statements);
    }

    public static void getStatement(
            String AgentID, String AgentName, String AgentPassword, String AgentEmail,
            Context context, String startDate, String endDate,
            StatementCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, Object>> items = new ArrayList<>();
                try {
                    JSONObject catalogRequestResponse = ApiService.statement(
                            AgentID, AgentName, AgentPassword, AgentEmail, context, startDate, endDate
                    );

                    if (catalogRequestResponse.has("response")) {

                        String nestedResponseString = catalogRequestResponse.getString("response");
                        JSONObject nestedResponse = new JSONObject(nestedResponseString);

                        if (nestedResponse.has("methodResponse")) {
                            JSONArray paramsList = nestedResponse.getJSONObject("methodResponse").getJSONArray("paramsList");

                            for (int i = 0; i < paramsList.length(); i++) {
                                JSONObject product = paramsList.getJSONObject(i);
                                Map<String, Object> item = new HashMap<>();
                                item.put("customerID", product.optString("customerID", ""));
                                item.put("basketID", product.optString("basketID", ""));
                                item.put("network", product.optString("network", ""));
                                item.put("transactionType", product.optString("transactionType", ""));
                                item.put("productID", product.optString("productID", ""));
                                item.put("productDescription", product.optString("productDescription", ""));
                                item.put("productCategory", product.optString("productCategory", ""));
                                item.put("rechargeAmount", product.optString("rechargeAmount", ""));
                                item.put("depositAmount", product.optString("DepositAmount", ""));
                                item.put("providerSerial", product.optString("providerSerial", ""));
                                item.put("providerReference", product.optString("providerReference", ""));
                                item.put("providerStatus", product.optString("providerStatus", ""));
                                item.put("providerStatusCode", product.optString("providerStatusCode", ""));
                                item.put("currency", product.optString("currency", ""));
                                item.put("costPrice", product.optString("costPrice", ""));
                                item.put("agentID", product.optString("agentID", ""));
                                item.put("agentName", product.optString("agentName", ""));
                                item.put("entryDate", product.optString("entryDate", ""));
                                item.put("providerID", product.optString("providerID", ""));
                                item.put("providerSplit", product.optString("providerSplit", ""));
                                item.put("decimalBalance", product.optString("decimalBalance", ""));
                                item.put("providerBalance", product.optString("providerBalance", ""));
                                item.put("cumulativeBalance", product.optString("cumulativeBalance", ""));
                                item.put("agentSplit", product.optString("agentSplit", ""));
                                items.add(item);
                            }
                        } else {
                            System.out.println("Error: methodResponse not found in nested response.");
                        }
                    } else {
                        System.out.println("Error: response not found in catalogRequestResponse.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    System.out.println("JSON Parsing Error: " +
                            (e.getMessage().contains("connect") ? "Service Provider Offline" : e.getMessage()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (callback != null) {
                    callback.onResult(items);
                }
            }
        }).start();
    }


    public void spinners() {
        ItemFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                getProducts(new ProductsCallback() {
                    @Override
                    public void onProductsLoaded(List<Map<String, Object>> products) {
                        List<Map<String, Object>> filteredProducts = filterProductsByType(products, selectedItem.equals("WhatsApp") ? "WHATSAPP_BUNDLES" : selectedItem);
                        ItemRecyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                        ItemRecyclerView.setAdapter(new RecommendedAd(filteredProducts));
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public interface ProductsCallback {
        void onProductsLoaded(List<Map<String, Object>> products);
    }
    public void getProducts(ProductsCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Map<String, Object>> items = new ArrayList<>();
                try {
                    JSONObject catalogRequestResponse = ApiService.catalogRequest();
                    if (catalogRequestResponse.has("response")) {
                        String nestedResponseString = catalogRequestResponse.getString("response");
                        JSONObject nestedResponse = new JSONObject(nestedResponseString);
                        if (nestedResponse.has("methodResponse")) {
                            JSONArray paramsList = nestedResponse.getJSONObject("methodResponse").getJSONArray("paramsList");
                            for (int i = 0; i < paramsList.length(); i++) {
                                JSONObject product = paramsList.getJSONObject(i);
                                Map<String, Object> item = new HashMap<>();
                                item.put("type", product.getString("productCategory"));
                                item.put("amount", product.getString("amount"));
                                item.put("lifeTime", product.getString("validity"));
                                item.put("price", product.getString("costPrice"));
                                item.put("num", product.getString("num"));
                                item.put("productID", product.getString("productID"));
                                item.put("productDescription", product.getString("productDescription"));
                                item.put("shortDescription", product.getString("shortDescription"));
                                item.put("network", product.getString("network"));
                                item.put("costPrice", product.getString("costPrice"));
                                item.put("agentSplit", product.getString("agentSplit"));
                                item.put("providerSplit", product.getString("providerSplit"));
                                items.add(item);
                            }
                        } else {
                            System.out.println("Error: methodResponse not found in nested response.");
                        }
                    } else {
                        System.out.println("Error: response not found in catalogRequestResponse.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(() -> callback.onProductsLoaded(items));
            }
        }).start();
    }
    public List<Map<String, Object>> filterProductsByType(List<Map<String, Object>> products, String filterType) {
        List<Map<String, Object>> filteredProducts = new ArrayList<>();
        for (Map<String, Object> product : products) {

            if (product.get("type") != null && product.get("type").toString().toLowerCase().contains(filterType.toLowerCase())) {
                filteredProducts.add(product);
            }

        }
        return filteredProducts;
    }

    private void handleBackFromList() {
        BackToHome.setVisibility(View.GONE);
        Navbar.setVisibility(View.VISIBLE);
        job_list_screen.setVisibility(View.GONE);
        AppFrame.setVisibility(View.VISIBLE);
    }

    private void handleShowMore() {
        if (SelectedIsp.getText().toString().isEmpty()) {
            Utils.showToast(this, "Select Network");
            return;
        }
        populateHistory(null, null);
        Navbar.setVisibility(View.GONE);
        AppFrame.setVisibility(View.GONE);
        job_list_screen.setVisibility(View.VISIBLE);
    }

    private void defaultColoring(ImageButton icon) {
        NavBuyBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavIPSBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavHomeBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavLaodBalanceBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavLaodBalanceBtn1.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        icon.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);
    }

    private void clearFields() {
        Phone.setText("");
        AmountTLoad.setText("0.00");
        AmountTLoadInBuy.setText("0.00");

    }

    public String getAppVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

}