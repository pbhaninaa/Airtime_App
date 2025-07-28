package com.example.qupos.UI;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.qupos.UI.UserManagement.getZimCode;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
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
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qupos.JavaClasses.ApiService;
import com.example.qupos.JavaClasses.CurrencyTextWatcher;
import com.example.qupos.JavaClasses.PayNowPaymentProcessor;
import com.example.qupos.JavaClasses.MyJavaScriptInterface;
import com.example.qupos.JavaClasses.Country;
import com.example.qupos.JavaClasses.PaymentProcessor;
import com.example.qupos.R;
import com.example.qupos.JavaClasses.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;

import java.text.ParseException;
import java.util.Calendar;

import android.content.ComponentName;
import android.view.ContextThemeWrapper;

import okhttp3.internal.Util;

public class Dashboard extends AppCompatActivity {
    private ConstraintLayout ConfirmationScreen, AppFrame, LoadingLayout;
    private LinearLayout userSummaryLayout, adminLayout, collect_layout, expected_collection_layout, job_list_screen, SelectedItem, AmountCapture, WebScree, Navbar, ItemsLayout, ISPsLayout, BuyLayout, EconetIsp, TelecelIsp, NetoneIsp, LoadBalanceLayout;
    private FrameLayout LogoutButton, BackToHome;
    private WebView Web;
    private ImageView App_logo_in_balance_display;
    private TextView selectedAgentBalance1, selectedAgentBalance, commission_currency_symbol, collect_currency_symbol, currencySymbolInBuy, AmountToLoadSymbol, SelectedIsp, AvailableBalance, StatusMessage, MoreBtn, ItemToBuyText, SelectedItemType, SelectedItemPrice, SelectedItemLifeTime;
    private EditText Phone, AmountTLoad, AmountTLoadInBuy, LoadingNote, commissionAmount, collectAmount;
    private ImageButton NavAdminBtn, backFromList, NavHomeBtn,
            NavCollectBtn, NavStatementBtn, NavBuyBtn, NavProfileBtn, NavIPSBtn,
            NavMoreBtn, expected_collection, NavLoadBalanceBtn1, NavLoadBalanceBtn;
    private LinearLayout AdminNavBtn, backFromListLayout, HomeNavBtn,
            CollectNavBtn, BuyNavBtn, ProfileNavBtn,
            MoreNavBtn, SummaryNavBtn, StatementNavBtn, DirectBuyNavBtn, TopUpNavBtn;
    private Spinner ItemFilterSpinner, ItemToBuySpinner, CountryCode, Agents, Agents1;
    private ImageView statusLight, CountryFlag, load, LoadingImage,cashier_filter;
    private Button BuyBtn, BuyBtn1, Yes, No, LoadBalanceByCash, LoadBalance;

    static String currencySymbol, ItemCode, startDate, endDate;
    private RecyclerView ItemRecyclerView, jobListRecyclerView, expected_collection_summary, expected_collection_summary1, total_expected_collection_summary;

    private JSONArray paramList = new JSONArray();
    private String selectedAgentId = "";
    private String selectedAgentId1 = "";
    private TableLayout selectedUserBalanceTextView, selectedUserBalanceTextView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        Utils.hideSoftNavBar(Dashboard.this);

        getProfile();

        initialiseViews();
        setOnclickListeners();
        recyclerViews();
        adaptors();

        getLastTransaction(null);
        formLoadEvents();

        addTextWatchers();
        spinners();
        getAccount("");
        Utils.rotateImageView(load);

        startDate = Utils.getTodayDate();
        endDate = Utils.getTodayDate();
        setNavBaeToDisplayOnly4Buttons();
        underConstruction(adminLayout);
        appBranding();
        showNavbar();
        expected_collection_summary.setLayoutManager(new LinearLayoutManager(this));
        expected_collection_summary1.setLayoutManager(new LinearLayoutManager(this));
        total_expected_collection_summary.setLayoutManager(new LinearLayoutManager(this));
    }

    public void appBranding() {
        // Get the app name
        String appName = getString(R.string.app_name);

        ImageView logo_in_balance_display = findViewById(R.id.logo_in_balance_display);
        ImageView logo_in_tenant_select = findViewById(R.id.logo_in_tenant_select);

        // Match and set logos
        if ("Qupos".equalsIgnoreCase(appName)) {
            logo_in_balance_display.setImageResource(R.drawable.icon_logo);
            logo_in_tenant_select.setImageResource(R.drawable.qupos_app_logo);
        } else if ("Rebtel".equalsIgnoreCase(appName)) {
            logo_in_balance_display.setImageResource(R.drawable.rebtel_red_logo);
            logo_in_tenant_select.setImageResource(R.drawable.rebtel_red_logo);
        }else{
            logo_in_balance_display.setImageResource(R.drawable.keshapp_icon_with_no_bg);
            logo_in_tenant_select.setImageResource(R.drawable.keshapp1_removebg_preview);
        }
    }

    public void adminRights() {
        SharedPreferences sharedPreference = getSharedPreferences("LoggedUserCredentials", Context.MODE_PRIVATE);
        String role = sharedPreference.getString("role", "Agent");



        CollectNavBtn.setVisibility(role.equalsIgnoreCase("Admin") ? VISIBLE : GONE);
        AdminNavBtn.setVisibility(role.equalsIgnoreCase("Admin") ? VISIBLE : GONE);
        SummaryNavBtn.setVisibility(role.equalsIgnoreCase("Admin") ? VISIBLE : GONE);

        selectedUserBalanceTextView.setVisibility(role.equalsIgnoreCase("Agent") ? GONE : VISIBLE);
        selectedUserBalanceTextView1.setVisibility(role.equalsIgnoreCase("Agent") ? GONE : VISIBLE);
        LoadBalanceByCash.setVisibility(role.equalsIgnoreCase("Agent") ?GONE  : VISIBLE);

        StatementNavBtn.setVisibility(role.equalsIgnoreCase("Cashier") ? VISIBLE : GONE);
        cashier_filter.setVisibility(role.equalsIgnoreCase("Cashier") ? VISIBLE : GONE);

       /*
        BuyNavBtn.setVisibility(GONE);
        ProfileNavBtn .setVisibility(GONE);
        MoreNavBtn .setVisibility(GONE);
        DirectBuyNavBtn .setVisibility(GONE);
        TopUpNavBtn.setVisibility(GONE);
        backFromListLayout.setVisibility(GONE);
        HomeNavBtn.setVisibility(GONE);
        */

    }


    public void showNavbar() {
        if (CollectNavBtn.getVisibility() != View.VISIBLE &&
                SummaryNavBtn.getVisibility() != View.VISIBLE &&
                StatementNavBtn.getVisibility() != View.VISIBLE &&
                AdminNavBtn.getVisibility() != View.VISIBLE &&
                BuyNavBtn.getVisibility() != View.VISIBLE &&
                ProfileNavBtn.getVisibility() != View.VISIBLE &&
                MoreNavBtn.getVisibility() != View.VISIBLE &&
                DirectBuyNavBtn.getVisibility() != View.VISIBLE &&
                TopUpNavBtn.getVisibility() != View.VISIBLE &&
                backFromListLayout.getVisibility() != View.VISIBLE &&
                HomeNavBtn.getVisibility() != View.VISIBLE) {

            // All are not visible; hide the navbar
            Navbar.setVisibility(View.GONE);
        } else {
            // At least one is visible; show the navbar
            Navbar.setVisibility(View.VISIBLE);
        }
    }


    private void underConstruction(LinearLayout layout) {
        View underConstruction = getLayoutInflater().inflate(R.layout.under_construction, layout, false);

        // Now find the TextView from the inflated view
        TextView blinkingText = underConstruction.findViewById(R.id.blinking_text);

        // Start blink animation
        Animation blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        blinkingText.startAnimation(blink);

        // Add the view to the layout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;

        layout.addView(underConstruction, params);
    }


    private void setNavBaeToDisplayOnly4Buttons() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        int buttonWidth = (int) (screenWidth / 4.5);

        LinearLayout navLayout = findViewById(R.id.nav_layout);

        for (int i = 0; i < navLayout.getChildCount(); i++) {
            View navBtn = navLayout.getChildAt(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(buttonWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            navBtn.setLayoutParams(params);
        }


    }

    private void formLoadEvents() {
        getBalance(SelectedIsp.getText().toString());
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        currencySymbol = sharedPreferences.getString("currency_symbol", getString(R.string.default_currency_symbol));
        adminRights();


        AppFrame.setVisibility(VISIBLE);
        BackToHome.setVisibility(SelectedIsp.getText().toString().isEmpty() ? GONE : VISIBLE);
        ISPsLayout.setVisibility(VISIBLE);
        NavHomeBtn.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);


        ArrayAdapter<Country> ada = new ArrayAdapter<>(this, R.layout.spinner_item, getZimCode());
        ada.setDropDownViewResource(R.layout.spinner_item);
        CountryCode.setAdapter(ada);

        CountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Utils.hideSoftKeyboard(Dashboard.this);
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
    }

    private void addTextWatchers() {
        AmountTLoad.addTextChangedListener(new CurrencyTextWatcher(AmountTLoad));
        AmountTLoadInBuy.addTextChangedListener(new CurrencyTextWatcher(AmountTLoadInBuy));

        collectAmount.addTextChangedListener(new CurrencyTextWatcher(collectAmount));
        commissionAmount.addTextChangedListener(new CurrencyTextWatcher(commissionAmount));

    }

    @Override
    public void onBackPressed() {
        if (Web.canGoBack()) {
            Web.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void initialiseViews() {
        collectAmount = findViewById(R.id.collect_amount);
        commissionAmount = findViewById(R.id.commission_amount);
        Agents = findViewById(R.id.agents_spinner);
        Agents1 = findViewById(R.id.agents_spinner1);
        LoadingLayout = findViewById(R.id.load_layout);
        LoadingImage = findViewById(R.id.load_layout_image);
        SelectedItem = findViewById(R.id.selected_item);
        AmountCapture = findViewById(R.id.amount_in_buy);
        load = findViewById(R.id.web_view_loading);
        WebScree = findViewById(R.id.web);
        Web = findViewById(R.id.web_view);
        job_list_screen = findViewById(R.id.Job_list_screen);
        backFromList = findViewById(R.id.back_btn_from_job_list);
        BuyBtn = findViewById(R.id.btn_buy);
        BuyBtn1 = findViewById(R.id.btn_buy_1);
        ConfirmationScreen = findViewById(R.id.confirmation_screen);
        Yes = findViewById(R.id.yes);
        No = findViewById(R.id.no);
        jobListRecyclerView = findViewById(R.id.MoreItemsRecyclerView);
        AppFrame = findViewById(R.id.app_frame);
        LogoutButton = findViewById(R.id.logout_button);
        AvailableBalance = findViewById(R.id.available_balance);
        expected_collection_summary = findViewById(R.id.expected_collection_summary);
        expected_collection_summary1 = findViewById(R.id.expected_collection_summary1);
        total_expected_collection_summary = findViewById(R.id.total_expected_collection_summary);
        StatusMessage = findViewById(R.id.status_message);
        NavHomeBtn = findViewById(R.id.nav_dash_board_btn1);
        NavCollectBtn = findViewById(R.id.nav_collect_layout_btn);
        NavStatementBtn = findViewById(R.id.nav_agent_statement);
        NavAdminBtn = findViewById(R.id.nav_admin_btn1);
        NavLoadBalanceBtn = findViewById(R.id.nav_load_btn);
        NavLoadBalanceBtn1 = findViewById(R.id.nav_load_btn1);
        expected_collection = findViewById(R.id.expected_collection);
        NavBuyBtn = findViewById(R.id.nav_buy_btn1);
        NavProfileBtn = findViewById(R.id.nav_profile_btn1);
        NavIPSBtn = findViewById(R.id.nav_networks_btn1);
        NavMoreBtn = findViewById(R.id.more1);
        ISPsLayout = findViewById(R.id.ISP_display_layout);
        BuyLayout = findViewById(R.id.Buying_layout);
        ItemsLayout = findViewById(R.id.Items_display_layout);
        collect_layout = findViewById(R.id.collect_layout);
        adminLayout = findViewById(R.id.admin_layout);
        userSummaryLayout = findViewById(R.id.user_summary_layout);
        expected_collection_layout = findViewById(R.id.expected_collection_layout);
        ItemRecyclerView = findViewById(R.id.Items_recycler_view);
        ItemFilterSpinner = findViewById(R.id.items_spinner);
        MoreBtn = findViewById(R.id.More_Items);
        ItemToBuySpinner = findViewById(R.id.item_to_buy);
        ItemToBuyText = findViewById(R.id.item_to_buy_text);
        Navbar = findViewById(R.id.navbar);
        SelectedIsp = findViewById(R.id.selected_network_text);
        AmountToLoadSymbol = findViewById(R.id.currency_symbol);
        collect_currency_symbol = findViewById(R.id.collect_currency_symbol);
        commission_currency_symbol = findViewById(R.id.commission_currency_symbol);
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
        LoadBalanceByCash = findViewById(R.id.btn_load_balance_by_cash);
        LoadingNote = findViewById(R.id.loading_notes);
        AmountTLoad = findViewById(R.id.loading_amount);
        statusLight = findViewById(R.id.status_light);
        currencySymbolInBuy = findViewById(R.id.currency_symbol_in_buy);
        AmountTLoadInBuy = findViewById(R.id.loading_amount_in_buy);
        selectedAgentBalance = findViewById(R.id.selected_agents_balance);
        selectedUserBalanceTextView1 = findViewById(R.id.selected_agents_balance_table1);
        selectedUserBalanceTextView = findViewById(R.id.selected_agents_balance_table);

//        =====================================

        AdminNavBtn = findViewById(R.id.nav_admin_btn1_layout);
        backFromListLayout = findViewById(R.id.nav_networks_btn1_layout);
        HomeNavBtn = findViewById(R.id.nav_dash_board_btn1_layout);
        CollectNavBtn = findViewById(R.id.nav_collect_layout_btn_layout);
        BuyNavBtn = findViewById(R.id.nav_buy_btn1_layout);
        ProfileNavBtn = findViewById(R.id.nav_profile_btn1_layout);
        MoreNavBtn = findViewById(R.id.more1_layout);
        SummaryNavBtn = findViewById(R.id.nav_expected_collection_layout);
        StatementNavBtn = findViewById(R.id.nav_agent_statement_layout);
        DirectBuyNavBtn = findViewById(R.id.nav_load_btn1_layout);
        TopUpNavBtn = findViewById(R.id.nav_load_btn_layout);

        cashier_filter= findViewById(R.id.cashier_filter);
    }

    public void showLastTransaction() {
        String last = StatusMessage.getText().toString();

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        if (last.endsWith(currentTime)) {
            getLastTransaction(null);
        } else {
            Utils.showToast(this, "Last transaction does not match the current time.");
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
        Navbar.setVisibility(GONE);

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

                            load.setVisibility(GONE);
                            Web.setVisibility(VISIBLE);
                        }, 5000);
                    } else {
                        Utils.showToast(this, "Failed to get redirect URL.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(this, "Failed to parse JSON response.");
                }
            });
        }).start();
    }

    private void handlePayNowPayment() {
        String amountString = AmountTLoad.getText().toString().trim().replaceAll("[^\\d.]", "");

//
        if (amountString.isEmpty() || amountString.equalsIgnoreCase("0.00") || selectedAgentId1.isEmpty()) {
            if (selectedAgentId1.isEmpty()) {
                Utils.showToast(this, "Please select an agent");
            } else {
                AmountTLoad.setError("Amount is required");
            }
            return;
        }


        Utils.hideSoftKeyboard(Dashboard.this);


        String message = "Please confirm details:\n\n"
                + "Number: " + selectedAgentId1 + "\n"
                + "Amount: " + currencySymbol + AmountTLoad.getText().toString() ;


        showConfirmationDialog(this, message, "OK", "Cancel", result ->
        {
            if (result) {
                double amount = 0.0;
                try {
                    amount = Double.parseDouble(amountString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    AmountTLoad.setError("Invalid amount");
                    return;
                }
                hideLayouts(WebScree, NavBuyBtn);
                Navbar.setVisibility(GONE);
                load.setVisibility(VISIBLE);

                PayNowPaymentProcessor.createPayNowOrder(this, selectedAgentId1, "Load balance", amount, new PayNowPaymentProcessor.PayNowCallback() {
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
                                        view.loadUrl(url);
                                        return false;
                                    }
                                });

                                Web.loadUrl(payNowResponse.getRedirectUrl());
                                load.setVisibility(GONE);
                                Web.setVisibility(VISIBLE);
                            } else {
                                load.setVisibility(GONE);
                                AmountTLoad.setError("Failed to generate payment link");
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() -> {
                            load.setVisibility(GONE);
                            AmountTLoad.setError("Payment failed: " + error);
                        });
                    }
                });
            }
        });
    }

    private void adaptors() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, MainActivity.econetItems);
        adapter.setDropDownViewResource(R.layout.spinner_layout);

        ItemFilterSpinner.setAdapter(adapter);
        ItemToBuySpinner.setAdapter(adapter);


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                List<String> agentNamesList = new ArrayList<>();
                SharedPreferences sharedPreference = getSharedPreferences("LoggedUserCredentials", Context.MODE_PRIVATE);
                String role = sharedPreference.getString("role", "");
                String currentAgentName = Utils.getString(Dashboard.this, "savedCredentials", "agentName");
                String currentAgentId = Utils.getString(Dashboard.this, "savedCredentials", "email");
                agentNamesList.add(role.equalsIgnoreCase("agent") ? currentAgentName : "Select an Agent");
                selectedAgentId1 = role.equalsIgnoreCase("agent") ? currentAgentId : "";


                try {
                    String agentId = Utils.getString(Dashboard.this, "savedCredentials", "email");

                    paramList = getAgentsParamList(agentId, agentId, Dashboard.this);

                    if (!role.equalsIgnoreCase("agent"))
                        for (int i = 0; i < paramList.length(); i++) {
                            JSONObject agentObject = paramList.getJSONObject(i);
                            String agentName = agentObject.getString("agentName");
                            agentNamesList.add(agentName);
                        }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Dashboard.this, R.layout.spinner_layout, agentNamesList);
                            adapter1.setDropDownViewResource(R.layout.spinner_layout);
                            Agents.setAdapter(adapter1);
                            Agents1.setAdapter(adapter1);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error fetching agents: " + e.getMessage());
                }
            }
        });
    }

    public static JSONArray getAgentsParamList(String AgentId, String CollectorId, Context context) throws Exception {
        JSONObject res = ApiService.getAgents(
                "Econet",
                AgentId,
                CollectorId,
                context
        );

        if (res.has("response")) {
            String responseString = res.getString("response");
            JSONObject innerResponse = new JSONObject(responseString);
            if (innerResponse.has("methodResponse")) {
                JSONObject methodResponse = innerResponse.getJSONObject("methodResponse");
                if (methodResponse.has("paramsList")) {
                    JSONArray paramList = methodResponse.getJSONArray("paramsList");
                    return paramList;
                } else {
                    System.out.println("paramsList key not found in methodResponse");
                }
            } else {
                System.out.println("methodResponse key not found in the response");
            }
        } else {
            System.out.println("response key not found in the main response");
        }
        return new JSONArray();
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

    private void setOnclickListeners() {
        backFromList.setOnClickListener(v -> handleBackFromList());
        BuyBtn.setOnClickListener(v -> handleTransaction());
        BuyBtn1.setOnClickListener(v -> buy());
        No.setOnClickListener(v -> handleNo());
        Yes.setOnClickListener(v -> handleYes());
        LogoutButton.setOnClickListener(v -> logout());
        NavHomeBtn.setOnClickListener(v -> hideLayouts(ISPsLayout, NavHomeBtn));
        NavCollectBtn.setOnClickListener(v -> hideLayouts(collect_layout, NavCollectBtn));
        NavAdminBtn.setOnClickListener(v -> hideLayouts(adminLayout, NavAdminBtn));
        NavLoadBalanceBtn.setOnClickListener(v -> hideLayouts(LoadBalanceLayout, NavLoadBalanceBtn));
        NavLoadBalanceBtn1.setOnClickListener(v -> hideLayouts(BuyLayout, NavLoadBalanceBtn1));
        NavIPSBtn.setOnClickListener(v -> hideLayouts(ItemsLayout, NavIPSBtn));
        NavMoreBtn.setOnClickListener(v -> handleShowMore());
        NavBuyBtn.setOnClickListener(v -> hideLayouts(BuyLayout, NavBuyBtn));
        MoreBtn.setOnClickListener(v -> handleShowMore());
        NavProfileBtn.setOnClickListener(v -> showProfile());
        EconetIsp.setOnClickListener(v -> setISP("Econet"));
        NetoneIsp.setOnClickListener(v -> setISP("NetOne"));
        TelecelIsp.setOnClickListener(v -> setISP("Telecel"));
        NavStatementBtn.setOnClickListener(v -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

            SharedPreferences sharedPreferences = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
            String phone = sharedPreferences.getString("phone", "");
            hideLayouts(userSummaryLayout, NavStatementBtn);
            populateSelectedUserList(phone);
        });
        expected_collection.setOnClickListener(v -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

            Calendar calendar = Calendar.getInstance();
            String startDate = sdf.format(calendar.getTime());

            showSummary(startDate, startDate);
            hideLayouts(expected_collection_layout, expected_collection);
        });

        LogoutButton.setOnLongClickListener(view -> {
            finishAffinity();
            return true;
        });
        BackToHome.setOnClickListener(v -> {
            Utils.hideSoftKeyboard(Dashboard.this);

            Navbar.setVisibility(VISIBLE);
            if (ItemsLayout.getVisibility() == VISIBLE) {
                hideLayouts(ISPsLayout, NavHomeBtn);
            } else {
                hideLayouts(ItemsLayout, NavHomeBtn);

            }
        });
//      LoadBalance.setOnClickListener(v -> handleLoadBalance());
        LoadBalance.setOnClickListener(v -> handlePayNowPayment());
        LoadBalanceByCash.setOnClickListener(v -> handleManualLoadBalance());
    }

    private void showSummary(String startDate, String endDate) {

        Utils.LoadingLayout(this, this);

        new Thread(() -> {
            try {
                String agentId = Utils.getString(Dashboard.this, "savedCredentials", "email");

                JSONObject res = ApiService.getCollectorSummary(
                        "Econet",
                        agentId,
                        "Collector Summary",
                        agentId,
                        startDate,
                        endDate
                );

                Utils.CloseLoadingLayout(this, this);
                if (res.getInt("responseCode") == 200) {
                    String responseString = res.getString("response");
                    JSONObject responseJson = new JSONObject(responseString);
                    JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                    JSONArray agentsArray = methodResponse.getJSONArray("agents");

                    List<JSONObject> summaries = new ArrayList<>();
                    List<JSONObject> total_summaries = new ArrayList<>();

                    for (int i = 0; i < agentsArray.length(); i++) {
                        JSONObject agent = agentsArray.getJSONObject(i);
                        summaries.add(agent);
                    }

                    // Total summary row

                    JSONObject totalRow = new JSONObject();
                    totalRow.put("agentName", "Total");
                    totalRow.put("agentRechargeAmount",
                            Double
                                    .parseDouble(methodResponse
                                            .getString("totalRechargeAmount")
                                            .replace(",", "")));

                    totalRow.put("targetCollection",
                            Double
                                    .parseDouble(methodResponse
                                            .getString("totalTargetCollection")
                                            .replace(",", "")));


                    totalRow.put("agentCumulativeBalance",
                            Double.parseDouble(methodResponse
                                    .getString("totalCumulativeBalance")
                                    .replace(",", "")));

                    total_summaries.add(totalRow);
                    TextView total = findViewById(R.id.float_amount);
                    total.setText(String.format("%s %s%s", "Econet Float :", currencySymbol, methodResponse.getString("totalCumulativeBalance").replace(",", "")));


                    runOnUiThread(() -> {
                        total_expected_collection_summary.setVisibility(summaries.size() > 1 ? VISIBLE : GONE);
                        // Normal adapter for agents
                        expected_collection_summary.setAdapter(
                                new CollectionSummaryAdapter(summaries, currencySymbol, false)
                        );
                        // Bold adapter for total summary
                        total_expected_collection_summary.setAdapter(
                                new CollectionSummaryAdapter(total_summaries, currencySymbol, true)
                        );
                    });

                } else {
                    runOnUiThread(() -> Toast.makeText(Dashboard.this, "Error: " + res.toString(), Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Dashboard.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }


    class CollectionSummaryAdapter extends RecyclerView.Adapter<CollectionSummaryAdapter.SimpleViewHolder> {

        private final List<JSONObject> data;
        private final String currencySymbol;
        private final boolean isBold;

        public CollectionSummaryAdapter(List<JSONObject> data, String currencySymbol, boolean isBold) {
            this.data = data;
            this.currencySymbol = currencySymbol;
            this.isBold = isBold;
        }

        class SimpleViewHolder extends RecyclerView.ViewHolder {
            final TextView name;
            final TextView collect;
            final TextView balance;
            final TextView deposit;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.text_name);
                collect = itemView.findViewById(R.id.collect_amount);
                balance = itemView.findViewById(R.id.text_balance);
                deposit = itemView.findViewById(R.id.text_deposit);
            }
        }

        @NonNull
        @Override
        public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_collection, parent, false);
            return new SimpleViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
            JSONObject agent = data.get(position);
            String agentName = agent.optString("agentName", "N/A");
            holder.name.setText(agentName);
            holder.collect.setText(String.format("%.2f", agent.optDouble("targetCollection", 0)));
            holder.balance.setText(String.format("%.2f", agent.optDouble("agentRechargeAmount", 01)));
            holder.deposit.setText(String.format("%.2f", agent.optDouble("agentCumulativeBalance", 01)));

            // Apply bold styling if needed
            int style = isBold ? Typeface.BOLD : Typeface.NORMAL;


            holder.name.setTypeface(null, style);
            holder.collect.setTypeface(null, style);
            holder.balance.setTypeface(null, style);
            holder.deposit.setTypeface(null, style);

            // Set background color if bold
            if (isBold) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_blue)
                );
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT); // or a default color
            }

            // Set click listener on name
            holder.name.setOnClickListener(v -> {
                Utils.LoadingLayout(Dashboard.this, Dashboard.this);

                TextView selectedAgent = findViewById(R.id.selectedName);
                TextView dateRange = findViewById(R.id.selectedDateRange);
                selectedAgent.setText(agentName);
                dateRange.setText(String.format("%s - %s", startDate, endDate));
                hideLayouts(userSummaryLayout, expected_collection);
                populateSelectedUserList(agent.optString("agentID", "0"));

            });
        }


        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    @SuppressLint("SetTextI18n")
    public void populateSelectedUserList(String agentId) {
        new Thread(() -> {
            try {
                JSONObject res = ApiService.getCollectorSummary(
                        "Econet",
                        agentId,
                        "Collector Statement",
                        agentId,
                        startDate,
                        endDate
                );

                if (res.getInt("responseCode") == 200) {
                    String responseString = res.getString("response");
                    JSONObject responseJson = new JSONObject(responseString);
                    JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                    JSONArray agentsArray = methodResponse.getJSONArray("paramsList");

                    List<JSONObject> summaries = new ArrayList<>();
                    double depositA = 0, rechargeA = 0, cashA = 0, agentA = 0;

                    for (int i = 0; i < agentsArray.length(); i++) {
                        JSONObject agent = agentsArray.getJSONObject(i);
                        JSONObject summaryItem = new JSONObject();

                        double deposit = agent.optDouble("depositAmount", 0);
                        double recharge = agent.optDouble("rechargeAmount", 0);
                        double cash = agent.optDouble("cashAmount", 0);
                        double commission = agent.optDouble("agentSplit", 0);


                        depositA += deposit;
                        rechargeA += recharge;
                        cashA += cash;
                        agentA += commission;

                        summaryItem.put("entryDate", agent.optString("entryDate", "0"));
                        summaryItem.put("depositAmount", deposit);
                        summaryItem.put("rechargeAmount", recharge);
                        summaryItem.put("cashAmount", cash);
                        summaryItem.put("agentSplit", commission);

                        summaries.add(summaryItem);
                    }

                    double finalDepositA = depositA;
                    double finalAgentA = agentA;
                    double finalCashA = cashA;
                    double finalRechargeA = rechargeA;
                    runOnUiThread(() -> {
                        // Set up the RecyclerView adapter
                        expected_collection_summary1.setAdapter(
                                new CollectionSummaryAdapter1(summaries, currencySymbol, false)
                        );

                        // Find and set total TextViews
                        TextView commission_total = findViewById(R.id.commission_total),
                                collection_total = findViewById(R.id.collection_total),
                                recharge_total = findViewById(R.id.recharge_total),
                                deposit_total = findViewById(R.id.deposit_total);
                        LinearLayout totalsLayout = findViewById(R.id.summary_total);

                        // Format the totals nicely
                        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
                        formatter.setMinimumFractionDigits(2);
                        formatter.setMaximumFractionDigits(2);

                        commission_total.setText(String.format("%.2f", finalAgentA));
                        collection_total.setText(String.format("%.2f", finalCashA));
                        recharge_total.setText(String.format("%.2f", finalRechargeA));
                        deposit_total.setText(String.format("%.2f", finalDepositA));

                        totalsLayout.setVisibility(VISIBLE);
                    });

                } else {
                    runOnUiThread(() -> Toast.makeText(Dashboard.this, "Error: " + res.toString(), Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Dashboard.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }


    class CollectionSummaryAdapter1 extends RecyclerView.Adapter<CollectionSummaryAdapter1.SimpleViewHolder> {

        private final List<JSONObject> data;
        private final String currencySymbol;
        private final boolean isBold;

        public CollectionSummaryAdapter1(List<JSONObject> data, String currencySymbol, boolean isBold) {
            this.data = data;
            this.currencySymbol = currencySymbol;
            this.isBold = isBold;
        }

        class SimpleViewHolder extends RecyclerView.ViewHolder {
            final TextView date, deposit, recharge, collection, commission;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                date = itemView.findViewById(R.id.text_date);
                deposit = itemView.findViewById(R.id.text_deposit);
                recharge = itemView.findViewById(R.id.text_recharge);
                collection = itemView.findViewById(R.id.text_collection);
                commission = itemView.findViewById(R.id.text_commission);
            }
        }

        @NonNull
        @Override
        public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.summary_layout, parent, false);
            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
            JSONObject item = data.get(position);

            String date = item.optString("entryDate", "-");
            double deposit = item.optDouble("depositAmount", 0);
            double recharge = item.optDouble("rechargeAmount", 0);
            double collection = item.optDouble("cashAmount", 0);
            double commission = item.optDouble("agentSplit", 0);

            String entryDate = item.optString("entryDate", "-");
            String timeOnly = "-";

            if (entryDate != null && entryDate.contains(" ")) {
                String[] parts = entryDate.split(" ");
                if (parts.length == 2 && parts[1].length() >= 5) {
                    timeOnly = parts[1].substring(0, 5); // Extracts "HH:mm"
                }
            }

            holder.date.setText(timeOnly);

            holder.deposit.setText("" + String.format("%.2f", deposit));
            holder.recharge.setText("" + String.format("%.2f", recharge));
            holder.collection.setText("" + String.format("%.2f", collection));
            holder.commission.setText("" + String.format("%.2f", commission));

            int style = isBold ? Typeface.BOLD : Typeface.NORMAL;
            holder.date.setTypeface(null, style);
            holder.deposit.setTypeface(null, style);
            holder.recharge.setTypeface(null, style);
            holder.collection.setTypeface(null, style);
            holder.commission.setTypeface(null, style);
            Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


    private void showSuccessDialog(final boolean goBack, final Context context, final String title, String body) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(body)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (goBack) {
                            setISP(SelectedIsp.getText().toString());
                            Navbar.setVisibility(VISIBLE);
                        } else {
                            dialog.dismiss();
                        }
                    }
                })
                .show();
    }


    private void handleManualLoadBalance() {
        String amount = AmountTLoad.getText().toString().trim();
        amount = amount.replaceAll("[^\\d.]", "").replaceAll("[^\\d,]", "");


        if (amount.isEmpty() || amount.equalsIgnoreCase("0.00") || selectedAgentId1.isEmpty()) {
            if (selectedAgentId1.isEmpty()) {
                Utils.showToast(this, "Please select an agent");
            } else {
                AmountTLoad.setError("Amount is required");
            }
            return;
        }

        String message = "Please confirm details:\n\n"
                + "Number: " + selectedAgentId1 + "\n"
                + "Amount: " + currencySymbol + AmountTLoad.getText().toString() ;


        showConfirmationDialog(this, message, "OK", "Cancel", result ->
        {
            if (result) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject res = ApiService.depositFunds(selectedAgentId1, Utils.getString(Dashboard.this, "LoggedUserCredentials", "phone"), AmountTLoad.getText().toString(), "840", Dashboard.this);
                            if (res.getInt("responseCode") == 200) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Utils.hideSoftKeyboard(Dashboard.this);
                                            String responseString = res.getString("response");
                                            JSONObject responseJson = new JSONObject(responseString);
                                            JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                            JSONArray paramsList = methodResponse.getJSONArray("paramsList");

                                            JSONObject responseDetails = paramsList.getJSONObject(0);

                                            String balance = responseDetails.getString("decimalBalance");
                                            String agentName = responseDetails.getString("agentName");
                                            String date = responseDetails.getString("entryDate");

                                            selectedAgentId1 = "";
                                            Agents1.setSelection(0);

                                            String successMessage = "Date: " + date + "\n\n"
                                                    + "Agent Name: " + agentName + "\n"
                                                    + "Topup Amount: " + currencySymbol + balance;

                                            showSuccessDialog(
                                                    true,
                                                    Dashboard.this,
                                                    "Transaction Succesfully ",
                                                    successMessage);

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
                                    Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);
                                }
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        });

    }

    private void logout() {
        Utils.hideSoftKeyboard(Dashboard.this);
        Utils.hideSoftNavBar(Dashboard.this);
        Navbar.setVisibility(GONE);
        AppFrame.setVisibility(GONE);
        ConfirmationScreen.setVisibility(VISIBLE);
    }

    private void handleNo() {
        AppFrame.setVisibility(VISIBLE);
        ConfirmationScreen.setVisibility(GONE);
    }

    private void handleYes() {
        Utils.logout(this);
        Intent intent = new Intent(this, UserManagement.class);
        intent.putExtra("constraintLayoutId", R.id.login_page);
        startActivity(intent);

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
        String phone = Phone.getText().toString().trim();
        if (phone.isEmpty()) {
            Phone.setError("Phone is required");
            Phone.requestFocus();
            return;
        }

        String price = AmountTLoadInBuy.getText().toString().trim().replace(currencySymbol, "");
        if (price.equals("0.00")) {
            AmountTLoadInBuy.setError("Price is required");
            AmountTLoadInBuy.requestFocus();
            return;
        }


        String countryCode = String.valueOf(CountryCode.getSelectedItem());
        String fullPhoneNumber = countryCode + phone;

        String message = "Please confirm details:\n\n" +
                AmountTLoadInBuy.getText().toString() + "\n" + "Number: " + fullPhoneNumber ;

        showConfirmationDialog(this, message, "OK", "Cancel", result ->
        {
            if (result) {
                clearFields();
                buyNumberConfirmed(fullPhoneNumber, price);
            }
        });
    }


    private void buyNumberConfirmed(String phone, String price) {
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
                            JSONObject res = ApiService.loadValue(SelectedIsp.getText().toString(), Utils.getString(Dashboard.this, "LoggedUserCredentials", "phone"), phone, price.replace(",", "").replace(".", ""), "Airtime", "Airtime", Dashboard.this);
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
                                                Utils.saveRefs(Dashboard.this, Network, AgentID, CustomerID, basketID);
                                                getAccount(balance);
                                                showSuccessDialog(
                                                        true,
                                                        Dashboard.this,
                                                        "Transaction was successful",
                                                        "Date: " + date + "\n\nRecharge Number: " + CustomerID +
                                                                "\nRecharge Amount: " + currencySymbol + amount +
                                                                "\nRecharge Serial: " + Serial);

                                            } else {
                                                Utils.showToast(Dashboard.this, description);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Utils.showToast(Dashboard.this, "Error parsing response data: " + (e.getMessage().contains("connect") ? "Service Provider Offline" : e.getMessage()));
                                            Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);
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
                                    Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);
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
        String phoneText = Phone.getText().toString().trim();

        if (phoneText.isEmpty()) {
            Phone.requestFocus();
            return;
        }

        // Step 2: Prepare confirmation message
        String fullPhoneNumber = CountryCode.getSelectedItem() + phoneText;
        String message = "Please confirm details:\n\n" +
                "Number: " + fullPhoneNumber ;

        // Step 3: Ask for confirmation
        showConfirmationDialog(this, message, "OK", "Cancel", result -> {
            if (result) {
                clearFields(); // Clear only after confirmation
                numberConfirmed(fullPhoneNumber); // Proceed with the confirmed number
            }
        });
    }

    private void numberConfirmed(String phone) {


        String price = SelectedItemPrice.getText().toString().trim().replace(currencySymbol, "");
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
                            SharedPreferences sharedPreference = Dashboard.this.getSharedPreferences("profile", Context.MODE_PRIVATE);
                            String name = sharedPreference.getString("name", "");
                            String surname = sharedPreference.getString("surname", "");
                            String agentId = sharedPreferences.getString("phone", "");
                            String agentPassword = sharedPreferences.getString("password", "");
                            String agentName = name + " " + surname;

                            JSONObject res = ApiService.loadBundle(SelectedIsp.getText().toString(),
                                    agentId,
                                    agentName,
                                    agentPassword,
                                    phone.replace("+", ""),
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

    public void spinners() {
        ItemFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                getProducts(new ProductsCallback() {
                    @Override
                    public void onProductsLoaded(List<Map<String, Object>> products) {
                        Utils.hideSoftKeyboard(Dashboard.this);
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

        setupAgentSpinner(Agents, selectedUserBalanceTextView1, true);
        setupAgentSpinner(Agents1, selectedUserBalanceTextView, false); // For Agents1


    }

    private void setupAgentSpinner(AdapterView<?> spinner, TableLayout tableLayout, boolean isFirstAgent) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                Utils.hideSoftKeyboard(Dashboard.this);
                for (int i = 0; i < paramList.length(); i++) {
                    try {
                        JSONObject agent = paramList.getJSONObject(i);
                        String agentName = agent.optString("agentName", "");

                        if (agentName.equals(selectedItem)) {
                            String agentId = agent.optString("agentID", "");

                            if (isFirstAgent) {
                                selectedAgentId = agentId;
                            } else {
                                selectedAgentId1 = agentId;
                            }

                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            Handler handler = new Handler(Looper.getMainLooper());

                            executor.execute(() -> {
                                try {
                                    JSONObject res = ApiService.balanceEnquiry(
                                            SelectedIsp.getText().toString(),
                                            agentId,
                                            Dashboard.this
                                    );

                                    handler.post(() -> {
                                        try {

                                            if (res != null && res.getInt("responseCode") == 200) {
                                                String responseString = res.getString("response");
                                                JSONObject responseJson = new JSONObject(responseString);
                                                JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                                JSONArray paramsList = methodResponse.getJSONArray("paramsList");

                                                if (paramsList.length() > 0) {
                                                    JSONObject userObject1 = paramsList.getJSONObject(0);
                                                    String decimalBalance1 = userObject1.getString("decimalBalance");
                                                    String cycleNetCollection1 = userObject1.getString("cycleNetCollection");
                                                    String cycleTargetCommission1 = userObject1.getString("cycleTargetCommission");
                                                    String cycleRechargeValue1 = userObject1.getString("cycleRechargeValue");
                                                    String cycleDepositValue = userObject1.getString("cycleDepositValue");


                                                    List<String[]> data = new ArrayList<>();
                                                    data.add(new String[]{"Agent Balance", currencySymbol + decimalBalance1});
                                                    data.add(new String[]{"", ""});
                                                    data.add(new String[]{"Cycle Net Collection", currencySymbol + cycleNetCollection1});
                                                    data.add(new String[]{"Agent Commission", currencySymbol + cycleTargetCommission1});
                                                    data.add(new String[]{"Cycle TopUp", currencySymbol + cycleDepositValue});
                                                    data.add(new String[]{"Cycle Recharge Value", currencySymbol + cycleRechargeValue1});

                                                    populateTable(data, tableLayout);

                                                } else {
                                                    Utils.showToast(Dashboard.this, "No balance data found.");
                                                }
                                            } else {
                                                Utils.showToast(Dashboard.this, "No Balance to display");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Utils.showToast(Dashboard.this, "Error processing balance response.");
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    handler.post(() -> Utils.showToast(Dashboard.this, "Error retrieving balance."));
                                }
                            });

                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showToast(Dashboard.this, "Error retrieving agent details.");
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optional
            }
        });
    }

    private void populateTable(List<String[]> data, TableLayout tableLayout) {
        tableLayout.removeAllViews();

        for (String[] row : data) {
            TableRow tableRow = new TableRow(Dashboard.this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            TextView label = new TextView(Dashboard.this);
            label.setText(row[0]);
            label.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.primary_color));
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            label.setPadding(2, 2, 2, 2);

            TableRow.LayoutParams labelParams = new TableRow.LayoutParams(
                    0, TableRow.LayoutParams.WRAP_CONTENT, 0.8f // 80%
            );
            label.setLayoutParams(labelParams);

            TextView value = new TextView(Dashboard.this);
            value.setText(row[1]);
            value.setTextColor(ContextCompat.getColor(Dashboard.this, R.color.primary_color));
            value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            value.setPadding(2, 2, 2, 2);
            value.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            value.setGravity(Gravity.END);

            TableRow.LayoutParams valueParams = new TableRow.LayoutParams(
                    0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f
            );

            value.setLayoutParams(valueParams);

            tableRow.addView(label);
            tableRow.addView(value);
            tableLayout.addView(tableRow);
        }
    }


    private void handleBackFromList() {
        BackToHome.setVisibility(GONE);
        Navbar.setVisibility(VISIBLE);
        job_list_screen.setVisibility(GONE);
        AppFrame.setVisibility(VISIBLE);
    }

    private void handleShowMore() {
        if (SelectedIsp.getText().toString().isEmpty()) {
            Utils.showToast(this, "Select Network");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        String endDate = sdf.format(calendar.getTime());

        calendar.add(Calendar.DATE, -2);
        String startDate = sdf.format(calendar.getTime());

        populateHistory(startDate, endDate);

        Navbar.setVisibility(GONE);
        AppFrame.setVisibility(GONE);
        job_list_screen.setVisibility(VISIBLE);
    }

    private void clearFields() {
        Phone.setText("");
        AmountTLoad.setText("0.00");
        AmountTLoadInBuy.setText("0.00");

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

                new Handler().postDelayed(() -> closePaymentView(null), 1);
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
                    Utils.showToast(this, "Webhook response: " + response.toString());
                } else {
                    Utils.showToast(this, "Webhook request failed. Response code: " + responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void hideLayouts(LinearLayout layoutToDisplay, ImageButton imageButton) {
        if (SelectedIsp.getText().toString().isEmpty()) {
            Utils.showToast(this, "Select Network");
            return;
        }
        // Remove all rows
        selectedUserBalanceTextView1.removeAllViews();
        selectedUserBalanceTextView.removeAllViews();
        Agents.setSelection(0);
        Agents1.setSelection(0);

        clearFields();
        if (imageButton.getId() == R.id.nav_buy_btn1) {
            Phone.requestFocus();
            SelectedItem.setVisibility(VISIBLE);
            AmountCapture.setVisibility(GONE);
            BuyBtn1.setVisibility(GONE);
            BuyBtn.setVisibility(VISIBLE);
        } else if (imageButton.getId() == R.id.nav_load_btn1) {
            Phone.requestFocus();
            SelectedItem.setVisibility(GONE);
            AmountCapture.setVisibility(VISIBLE);
            BuyBtn1.setVisibility(VISIBLE);
            BuyBtn.setVisibility(GONE);
        } else if (imageButton.getId() == R.id.more1) {
            SharedPreferences sharedPreferences = this.getSharedPreferences("LoggedUserCredentials", Context.MODE_PRIVATE);
            SharedPreferences sharedPreference = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
            String name = sharedPreference.getString("name", "");
            String surname = sharedPreference.getString("surname", "");
            String AgentID = sharedPreferences.getString("phone", "");
            String AgentEmail = sharedPreferences.getString("email", "");
            String AgentPassword = sharedPreferences.getString("password", "");
            String AgentName = name + " " + surname;

        }

        defaultColoring(imageButton);
        imageButton.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);
        BackToHome.setVisibility(VISIBLE);
        ISPsLayout.setVisibility(GONE);
        BuyLayout.setVisibility(GONE);
        ItemsLayout.setVisibility(GONE);
        LoadBalanceLayout.setVisibility(GONE);
        WebScree.setVisibility(GONE);
        expected_collection_layout.setVisibility(GONE);
        collect_layout.setVisibility(GONE);
        adminLayout.setVisibility(GONE);
        userSummaryLayout.setVisibility(GONE);
        layoutToDisplay.setVisibility(VISIBLE);


    }

    public void populateHistory(String startDate, String endDate) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("LoggedUserCredentials", Context.MODE_PRIVATE);
        SharedPreferences sharedPreference = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
        String name = sharedPreference.getString("name", "");
        String surname = sharedPreference.getString("surname", "");
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

    public void showConfirmationDialog(Context context, String message, String positive, String negative, Consumer<Boolean> callback) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(positive, (d, which) -> {
                    d.dismiss();
                    callback.accept(true);
                })
                .setNegativeButton(negative, (d, which) -> {
                    d.dismiss();
                    callback.accept(false);
                })
                .create();

        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            // Set fixed text colors only — no background
            int primaryColor = ContextCompat.getColor(context, R.color.primary_color);
            int vodacomColor = ContextCompat.getColor(context, R.color.vodacom_color);
            int black = ContextCompat.getColor(context, R.color.black);

            positiveButton.setTextColor(primaryColor);
            negativeButton.setTextColor(vodacomColor);
            TextView messageView = dialog.findViewById(android.R.id.message);
            messageView.setTextColor(black);
        });

        // Set dialog background only
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.edit_text_background));
        }


        dialog.show();
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
        BackToHome.setVisibility(VISIBLE);
        ISPsLayout.setVisibility(GONE);
        ItemsLayout.setVisibility(VISIBLE);

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
                            Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);
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

        commission_currency_symbol.setText(currencySymbol);
        collect_currency_symbol.setText(currencySymbol);
        currencySymbolInBuy.setText(currencySymbol);
        clearFields();
    }

    public void showDateDialog(final Context context) {
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
                                    if (job_list_screen.getVisibility() == VISIBLE) {
                                        populateHistory(start, end);
                                    } else {
                                        showSummary(start, end);
                                        SharedPreferences sharedPreferences = this.getSharedPreferences("profile", Context.MODE_PRIVATE); String phone = sharedPreferences.getString("phone", "");
                                        populateSelectedUserList(phone);
                                    }
                                }
                            }
                        } catch (ParseException e) {
                            Utils.showToast(themedContext, "Invalid date format.");
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialog12, which) -> dialog12.dismiss());

        AlertDialog dialog = dialogBuilder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setBackgroundResource(R.drawable.button_background);
            positiveButton.setTextColor(Color.WHITE);
            positiveButton.setText("OK");

            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setBackgroundResource(R.drawable.cancel_button_background);
            negativeButton.setTextColor(Color.WHITE);
            negativeButton.setText("Cancel");
        });

        dialog.show();

        startDateInput.setOnClickListener(v -> showDatePicker(themedContext, startDateInput, dialog, dateFormat, endDateInput, false));

        new Handler().postDelayed(startDateInput::performClick, 300);

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

    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void getStatement(String AgentID, String AgentName, String AgentPassword, String AgentEmail, Context context, String startDate, String endDate, StatementCallback callback) {

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

                            JSONObject methodResponse = nestedResponse.getJSONObject("methodResponse");

                            String totalDepositAmount = methodResponse.optString("totalDepositAmount", "0.00");
                            String totalRechargeAmount = methodResponse.optString("totalRechargeAmount", "0.00");
                            String totalCollectionAmount = methodResponse.optString("totalCashAmount", "0.00");
                            String totalCommissionAmount = methodResponse.optString("totalAgentSplit", "0.00");

                            if (context instanceof Activity) {
                                Activity activity = (Activity) context;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView TotalRecharge = activity.findViewById(R.id.totalRechargeAmount_textView);
                                        TextView TotalDeposit = activity.findViewById(R.id.totalDepositAmount_textView);

                                        TextView TotalCollection = activity.findViewById(R.id.totalCollectionAmount_textView);
                                        TextView TotalCommission = activity.findViewById(R.id.totalCommissionAmount_textView);

                                        TotalDeposit.setText(currencySymbol + Utils.FormatAmount(totalDepositAmount));
                                        TotalRecharge.setText(currencySymbol + Utils.FormatAmount(totalRechargeAmount));

                                        TotalCollection.setText(currencySymbol + Utils.FormatAmount(totalCollectionAmount));
                                        TotalCommission.setText(currencySymbol + Utils.FormatAmount(totalCommissionAmount));
                                    }
                                });
                            }

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

    private void defaultColoring(ImageButton icon) {
        NavBuyBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavIPSBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavHomeBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavLoadBalanceBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavCollectBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavStatementBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavAdminBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavLoadBalanceBtn1.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        expected_collection.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        icon.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);
    }

    public void closePaymentView(View view) {
        Web.loadUrl("about:blank");
        handleManualLoadBalance();


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
                                    Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);
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
                            Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    public void onCollectClick(View view) {
        String collectValue = collectAmount.getText().toString().trim();
        String commissionValue = commissionAmount.getText().toString().trim();
        if (collectValue.isEmpty() || commissionValue.isEmpty() || selectedAgentId.isEmpty()) {
            Utils.showToast(this, "Please fill all fields");
            return;
        }
        String fullPhoneNumber = "+" + selectedAgentId;

        String message = "Please confirm details:\n\n"
                + "Number: " + fullPhoneNumber + "\n"
                + "Collection: " + currencySymbol + collectValue + "\n"
                + "Commission: " + currencySymbol + commissionValue ;


        showConfirmationDialog(this, message, "OK", "Cancel", result ->
        {
            if (result) {


                try {
                    double collect = Double.parseDouble(collectValue);
                    double commission = Double.parseDouble(commissionValue);

                    if (collect == 0.0 || commission == 0.0) {
                        Utils.showToast(this, "Amounts must be greater than 0.00");
                        return;
                    }
                    SharedPreferences sharedPreference = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
                    String name = sharedPreference.getString("name", "");
                    String surname = sharedPreference.getString("surname", "");
                    new Thread(() -> {
                        try {
                            JSONObject res = ApiService.collectFunds("Econet", selectedAgentId, collectValue.replace(".", ""), commissionValue.replace(".", ""), "840", Utils.getString(Dashboard.this, "LoggedUserCredentials", "phone"), name + " " + surname, Dashboard.this);

                            if (res.getInt("responseCode") == 200) {
                                runOnUiThread(() -> {
                                    try {
                                        String responseString = res.getString("response");
                                        JSONObject responseJson = new JSONObject(responseString);
                                        JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                        JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                        JSONObject resultObj = paramsList.getJSONObject(0);
                                        selectedAgentId = "";
                                        Agents.setSelection(0);

                                        String balance = resultObj.getString("decimalBalance");
                                        String agentName = resultObj.getString("agentName");
                                        String date = resultObj.getString("entryDate");


                                        String successMessage = "Date: " + date + "\n\n"
                                                + "Agent Name: " + agentName + "\n"
                                                + "Collected Amount: " + currencySymbol + balance;

                                        showSuccessDialog(
                                                true,
                                                Dashboard.this,
                                                "Transaction Succesfully ",
                                                successMessage);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Utils.showToast(Dashboard.this, "Invalid response");
                                    }
                                });
                            } else {
                                runOnUiThread(() -> {
                                    try {
                                        Utils.showToast(Dashboard.this, res.getString("response"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> {
                                Utils.showToast(Dashboard.this, "Service Provider Offline");
                                Utils.CloseLoadingLayout(Dashboard.this, Dashboard.this);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Utils.showToast(Dashboard.this, "Something went wrong"));
                        }
                    }).start();


                } catch (NumberFormatException e) {
                    Utils.showToast(this, "Invalid number format");
                }
            }
        });
    }

    // Returning Methods
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

    public List<Map<String, Object>> filterProductsByType(List<Map<String, Object>> products, String filterType) {
        List<Map<String, Object>> filteredProducts = new ArrayList<>();
        for (Map<String, Object> product : products) {

            if (product.get("type") != null && product.get("type").toString().toLowerCase().contains(filterType.toLowerCase())) {
                filteredProducts.add(product);
            }

        }
        return filteredProducts;
    }

    private static int convertDpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    public void showManualLoad(View view) {
    }

    public void selectDateRange(View view) {
        showDateDialog(Dashboard.this);
    }

    // Helper Classes and Interfaces
    public interface ProductsCallback {
        void onProductsLoaded(List<Map<String, Object>> products);
    }

    public interface StatementCallback {
        void onResult(List<Map<String, Object>> statements);
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
                    if (job_list_screen.getVisibility() == VISIBLE) {
                        job_list_screen.setVisibility(GONE);
                        AppFrame.setVisibility(VISIBLE);
                        Navbar.setVisibility(VISIBLE);
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
            }

            public void bind(Map<String, Object> statement) {
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

            }

            private String getValueFromMap(Map<String, Object> map, String key, String defaultValue) {
                Object value = map.get(key);

                return value != null ? value.toString() : defaultValue;
            }
        }
    }

}