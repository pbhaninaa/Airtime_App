package com.example.testingmyskills.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingmyskills.JavaClasses.ApiService;
import com.example.testingmyskills.JavaClasses.CurrencyTextWatcher;
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


public class Dashboard extends AppCompatActivity {
    private ConstraintLayout AppFrame;
    private LinearLayout WebScree, Navbar, ItemsLayout, ISPsLayout, BuyLayout, EconetIsp, TelecelIsp, NetoneIsp, LoadBalanceLayout;
    private FrameLayout LogoutButton, BackToHome;
    private WebView Web;
    private TextView AmountToLoadSymbol, SelectedIsp, AvailableBalance, StatusMessage, MoreBtn, ItemToBuyText, SelectedItemType, SelectedItemPrice, SelectedItemLifeTime;
    private EditText Phone, AmountTLoad, LoadingNote;
    private ImageButton NavHomeBtn, NavBuyBtn, NavProfileBtn, NavIPSBtn, NavMoreBtn, NavLaodBalanceBtn;
    private RecyclerView ItemRecyclerView;
    private Spinner ItemFilterSpinner, ItemToBuySpinner;
    private Spinner CountryCode;
    private ConstraintLayout ConfirmationScreen;
    private LinearLayout job_list_screen;
    private ImageButton backFromList;
    private ImageView statusLight;
    private Button BuyBtn, Yes, No, LoadBalance;
    private TextView number_of_posts;
    private Spinner filter_spinner;


    private ImageButton FilterButton;
    private ScrollView scrollView;
    private LinearLayout filterSection;
    private boolean show;
    static String currencySymbol, ItemCode;
    private String ItemToBuy;
    public static String MSISDN;
    private boolean getSelectedCategory;
    private RecyclerView jobListRecyclerView;
    private String filePath = "JSON.json";
    private int numItems;
    double amount = 0;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        getProfile();
        show = true;
        initialiseViews();
        getSelectedCategory = false;
        Utils.hideSoftNavBar(Dashboard.this);
        setOnclickListeners();
        recyclerViews();
        adaptors();


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        currencySymbol = sharedPreferences.getString("currency_symbol", getString(R.string.default_currency_symbol));
//        spinners();
        AppFrame.setVisibility(View.VISIBLE);
        BackToHome.setVisibility(SelectedIsp.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
        ISPsLayout.setVisibility(View.VISIBLE);
        NavHomeBtn.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);
        getAccount("");

        ArrayAdapter<Country> ada = new ArrayAdapter<>(this, R.layout.spinner_item, UserManagement.getCountryList());
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CountryCode.setAdapter(ada);
        CountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country selectedCountry = (Country) parent.getItemAtPosition(position);
                String flagName = selectedCountry.getCountryFlag();
                String countryName = selectedCountry.getCountryName();

                int flagResourceId = getResources().getIdentifier(flagName, "drawable", getPackageName());
                if (flagResourceId != 0) {  // Check if the resource was found
                    ImageView CountryFlag = findViewById(R.id.country_flag);
                    CountryFlag.setImageResource(flagResourceId);
                } else {
                    Toast.makeText(getApplicationContext(), "Flag not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        AmountTLoad.addTextChangedListener(new CurrencyTextWatcher(AmountTLoad));


    }

    private void initialiseViews() {

        WebScree = findViewById(R.id.web);
        Web = findViewById(R.id.web_view);

        job_list_screen = findViewById(R.id.Job_list_screen);
        number_of_posts = findViewById(R.id.number_of_posts);
        filter_spinner = findViewById(R.id.filter_spinner);
        backFromList = findViewById(R.id.back_btn_from_job_list);
        BuyBtn = findViewById(R.id.btn_buy);
        FilterButton = findViewById(R.id.filter_button);
        filterSection = findViewById(R.id.filter_section);
        scrollView = findViewById(R.id.scroll_view);
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
        NavBuyBtn = findViewById(R.id.nav_buy_btn1);
        NavProfileBtn = findViewById(R.id.nav_profile_btn1);
        NavIPSBtn = findViewById(R.id.nav_networks_btn1);
        NavMoreBtn = findViewById(R.id.more1);

        ISPsLayout = findViewById(R.id.ISP_display_layout);
        BuyLayout = findViewById(R.id.Buying_layout);
        ItemsLayout = findViewById(R.id.Items_display_layout);


//        ISPsRecyclerView = findViewById(R.id.isp_list_recycler_view);
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
        CountryCode = findViewById(R.id.country_code);

        LoadBalanceLayout = findViewById(R.id.Load_balance_layout);
        LoadBalance = findViewById(R.id.btn_load_balance);
        LoadingNote = findViewById(R.id.loading_notes);
        AmountTLoad = findViewById(R.id.loading_amount);
        statusLight = findViewById(R.id.status_light);
    }

    private void handleLoadBalance() {
        String amount = AmountTLoad.getText().toString().trim();
        String notes = LoadingNote.getText().toString().trim();
        amount = amount.replaceAll("[^\\d.]", "");
        notes = notes.replaceAll("[^\\dA-Za-z\\s]", "");

        if (amount.isEmpty() || amount.equalsIgnoreCase("0.00")) {
            AmountTLoad.setError("Amount is required");
            return;
        }
        if (notes.isEmpty()) {
            LoadingNote.setError("Notes are required");
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

                AmountTLoad.setText("");
                LoadingNote.setText("");

                // Check if the URL contains payment result information
                if (url.contains("payment-success")) {
                    // Extract payment success information from the URL
                    String transactionId = Uri.parse(url).getQueryParameter("transactionId");
                    handlePaymentResult("{\"status\": \"success\", \"transactionId\": \"" + transactionId + "\"}");
                    return true; // Prevent further loading
                } else if (url.contains("payment-failure")) {
                    // Handle payment failure
                    handlePaymentResult("{\"status\": \"failure\"}");
                    return true;
                }

                view.loadUrl(url); // Continue loading other URLs in the WebView
                return false;
            }
        });
        hideLayouts(WebScree, NavBuyBtn);
        Navbar.setVisibility(View.GONE);
        String finalAmount = amount;
        new Thread(() -> {
            PaymentProcessor processor = new PaymentProcessor();
            String response = processor.createOrder(finalAmount, getResources().getString(R.string.api_key));

            runOnUiThread(() -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String redirectUrl = jsonResponse.optString("redirectUrl");

                    if (redirectUrl != null && !redirectUrl.isEmpty()) {
                        // Load the payment page into the WebView instead of opening a new browser
                        Web.loadUrl(redirectUrl);
                    } else {
                        System.out.println("Redirect URL is not available.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Failed to parse JSON response.");
                }
            });
        }).start();
    }

    private void handlePaymentResult(String result) {
        runOnUiThread(() -> {
            // Parse and handle the payment result here
            Utils.showToast(this, result);
            // Update UI or perform other actions based on the result
        });
    }

    public void closePaymentView(View view) {
        Navbar.setVisibility(View.VISIBLE);
        hideLayouts(LoadBalanceLayout, NavBuyBtn);
    }


    public void spinners() {
        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected item
                String selectedItem = parentView.getItemAtPosition(position).toString();


                RecyclerView recyclerView = findViewById(R.id.MoreItemsRecyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                recyclerView.setAdapter(new RecommendedAd(filterProductsByType(getProducts(), selectedItem)));
                numItems = filterProductsByType(getProducts(), selectedItem).size();

                number_of_posts.setText(String.format("%s%s", numItems, getString(R.string.items_found)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection if needed
            }
        });
        ItemFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected item
                String selectedItem = parentView.getItemAtPosition(position).toString();
                List<Map<String, Object>> array = filterProductsByType(getProducts(), selectedItem);
//                SelectedItem.setText(selectedItem + " is selected");
                ItemRecyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                ItemRecyclerView.setAdapter(new RecommendedAd(array));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection if needed
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (Web.canGoBack()) {
            Web.goBack();  // Go back in WebView's history
        } else {
            super.onBackPressed();  // Default behavior
        }
    }


    private void hideLayouts(LinearLayout layoutToDisplay, ImageButton imageButton) {

        if (SelectedIsp.getText().toString().isEmpty()) {
            Utils.showToast(this, "Select Network");
            return;
        }
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, MainActivity.econetItems);
        adapter.setDropDownViewResource(R.layout.spinner_item);


        filter_spinner.setAdapter(adapter);
        ItemFilterSpinner.setAdapter(adapter);
        ItemToBuySpinner.setAdapter(adapter);
    }

    private void recyclerViews() {
        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobListRecyclerView.setAdapter(new Statement(getStatement()));

        ItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemRecyclerView.setAdapter(new RecommendedAd(getProducts()));
    }

    private void setOnclickListeners() {
        backFromList.setOnClickListener(v -> handleBackFromList());
        BuyBtn.setOnClickListener(v -> handleTransaction());
        FilterButton.setOnClickListener(v -> hideFilter());
        No.setOnClickListener(v -> handleNo());
        Yes.setOnClickListener(v -> handleYes());
        LogoutButton.setOnClickListener(v -> logout());
        NavHomeBtn.setOnClickListener(v -> hideLayouts(ISPsLayout, NavHomeBtn));
        NavLaodBalanceBtn.setOnClickListener(v -> hideLayouts(LoadBalanceLayout, NavLaodBalanceBtn));
        NavIPSBtn.setOnClickListener(v -> hideLayouts(ItemsLayout, NavIPSBtn));
        NavMoreBtn.setOnClickListener(v -> handleShowMore());
        NavBuyBtn.setOnClickListener(v -> hideLayouts(BuyLayout, NavBuyBtn));
        MoreBtn.setOnClickListener(v -> handleShowMore());
        NavProfileBtn.setOnClickListener(v -> showProfile());
        EconetIsp.setOnClickListener(v -> setISP("Econet"));
        NetoneIsp.setOnClickListener(v -> setISP("NetOne"));
        TelecelIsp.setOnClickListener(v -> setISP("Telecel"));
        BackToHome.setOnClickListener(v -> hideLayouts(ISPsLayout, NavHomeBtn));
        LoadBalance.setOnClickListener(v -> handleLoadBalance());
    }


    public void setISP(String ISP) {

        if (!ISP.equals("Econet")) {
            Utils.showToast(this, "Not yet available");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject res = ApiService.balanceEnquiry(ISP, Utils.getString(Dashboard.this, "profile", "phone"));
                    if (res.getInt("responseCode") == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String responseString = res.getString("response");
                                    System.out.println(responseString);
                                    JSONObject responseJson = new JSONObject(responseString);
                                    JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                    JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                    JSONObject userObject = paramsList.getJSONObject(0);
                                    String balance = userObject.getString("cumulativeBalance");
                                    getAccount(balance);
                                    SelectedIsp.setText(ISP);
                                    hideLayouts(ItemsLayout, NavIPSBtn);
                                    BackToHome.setVisibility(View.VISIBLE);
                                    ISPsLayout.setVisibility(View.GONE);
                                    ItemsLayout.setVisibility(View.VISIBLE);

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
                            Utils.showToast(Dashboard.this, "An Error Occurred");
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
        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        String phone = sharedPreferences.getString("phone", "");
        String balance = sharedPreferences.getString("balance", "");
        String updated = sharedPreferences.getString("time", "");
        String formatedSalutation = "Hello " + name + " " + surname + " " + phone + "  Last updated " + updated;
        String Balance = balance.isEmpty() ? "No Balance to display" : (String.format("Account Balance %s%s", currencySymbol, Utils.FormatAmount(bal.isEmpty() ? balance : bal)));
        StatusMessage.setText(formatedSalutation);
        Utils.setStatusColor(this, (bal.isEmpty() ? balance : bal), statusLight);
        AvailableBalance.setText(Balance);
        AmountToLoadSymbol.setText(currencySymbol);
    }

    private void logout() {
        AppFrame.setVisibility(View.GONE);
        ConfirmationScreen.setVisibility(View.VISIBLE);
    }

    private void handleNo() {
        AppFrame.setVisibility(View.VISIBLE);
        ConfirmationScreen.setVisibility(View.GONE);
    }

    private void handleYes() {
        Intent intent = new Intent(this, UserManagement.class);
        intent.putExtra("constraintLayoutId", R.id.login_page);
        startActivity(intent);

    }

    private void hideFilter() {
        ViewGroup.LayoutParams params = scrollView.getLayoutParams();
        if (show) {
            filterSection.setVisibility(View.VISIBLE);
            params.height = (int) getResources().getDimension(R.dimen.scroll_height_collapsed);
            scrollView.setLayoutParams(params);
            show = false;
        } else {
            filterSection.setVisibility(View.GONE);
            params.height = (int) getResources().getDimension(R.dimen.scroll_height_expanded);
            scrollView.setLayoutParams(params);
            show = true;
        }
    }

    private void getProfile() {
        SharedPreferences prefs = this.getSharedPreferences("profile", Context.MODE_PRIVATE);
        String name = prefs.getString("name", "");
        String phoneNumber = name + "(" + prefs.getString("phone", "") + ")";
        String salutation = "Hello " + phoneNumber;
        String updateTime = "last updated a minute ago";
        MSISDN = prefs.getString("phone", "");
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


    private void handleTransaction() {
        String phone = Phone.getText().toString().trim();
        String price = SelectedItemPrice.getText().toString().trim().replace(currencySymbol, "");
        if (phone.isEmpty()) {
            return;
        }
        String balanceStr = AvailableBalance.getText().toString()
                .replace(currencySymbol, "")  // Remove the currency symbol
                .replace(",", "")             // Remove commas
                .replace("Account Balance", "") // Remove the "Account Balance" text
                .trim();                      // Remove extra spaces

        try {
            // Compare the price and the available balance
            double priceValue = Double.parseDouble(price);
            double balanceValue = Double.parseDouble(balanceStr);

            if (priceValue < balanceValue) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SharedPreferences sharedPreferences = Dashboard.this.getSharedPreferences("profile", Context.MODE_PRIVATE);
                            String name = sharedPreferences.getString("name", "");
                            String surname = sharedPreferences.getString("surname", "");
                            String agentId = sharedPreferences.getString("phone", "");
                            JSONObject res = ApiService.loadBundle(
                                    SelectedIsp.getText().toString(),             // ISP Name
                                    agentId,                                      // Agent ID
                                    name + " " + surname,                         // Full Name
                                    "ruffgunz",                                   // User identifier or password
                                    "263" + (phone.startsWith("0") ? phone.substring(1) : phone), // Phone number formatted with "263"
                                    SelectedItemPrice.getText().toString()
                                            .replace(currencySymbol, "")              // Remove currency symbol
                                            .replace(".", ""),                        // Remove decimal points
                                    ItemCode,                                     // Item code
                                    SelectedItemType.getText().toString()         // Item type
                            );


                            if (res.getInt("responseCode") == 200) {
                                // Handle success and UI updates on the main thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String responseString = res.getString("response");
                                            JSONObject responseJson = new JSONObject(responseString); // Parse the response string
                                            JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                            JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                            JSONObject responseDetails = paramsList.getJSONObject(0);
                                            String Serial = responseDetails.getString("providerSerial");
                                            String description = responseDetails.getString("providerStatus");
                                            String balance = responseDetails.getString("cumulativeBalance");
                                            if (!Serial.isEmpty()) {

                                                new AlertDialog.Builder(Dashboard.this)
                                                        .setTitle("Bundle Loaded Successfully")
                                                        .setMessage("Serial: " + Serial)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // Call your method here when OK is clicked
                                                                hideLayouts(ItemsLayout, NavIPSBtn);
                                                                getAccount(balance);
                                                            }
                                                        }) // Dismiss the alert when OK is clicked
                                                        .show();

                                            } else {
                                                Utils.showToast(Dashboard.this, "Error: " + description);
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

    public void getLastTransaction(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Hardcoded values based on the provided object
                    String network = "Econet";                             // Network
                    String agentID = "27649045091";                        // Agent ID
                    String customerID = "263781801175";                    // Customer ID
                    String referenceID = "LV-1000-1019050613";             // Reference ID

                    // Call the `transactionStatusEnquiry` method with hardcoded parameters
                    JSONObject res = ApiService.transactionStatusEnquiry(
                            network,          // Hardcoded Network value
                            agentID,          // Hardcoded Agent ID
                            customerID,       // Hardcoded Customer ID
                            referenceID       // Hardcoded Reference ID
                    );

                    if (res.getInt("responseCode") == 200) {
                        // Handle success and UI updates on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Parse the response as a string
                                    String responseString = res.getString("response");
                                    JSONObject responseJson = new JSONObject(responseString); // Parse the response JSON
                                    JSONObject methodResponse = responseJson.getJSONObject("methodResponse");

                                    // Dynamically look for keys that start with "paramsList"
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
                                        // Access the paramsList array using the found key
                                        JSONArray paramsList = methodResponse.getJSONArray(paramsKey);
                                        JSONObject responseDetails = paramsList.getJSONObject(0);

                                        // Fetch the "Serial" field
                                        String serial = responseDetails.optString("Serial", "N/A");

                                        if (!serial.equals("N/A")) {
                                            // Show success alert with the serial number
                                            runOnUiThread(() -> new AlertDialog.Builder(Dashboard.this)
                                                    .setTitle("Transaction was successful with")
                                                    .setMessage("Serial Number: " + serial)
                                                    .setPositiveButton("OK", null) // Dismiss the alert when OK is clicked
                                                    .show());
                                        } else {
                                            // Handle the case where "Serial" is not available
                                            runOnUiThread(() -> Utils.showToast(Dashboard.this, "Error: Serial not found in the response."));
                                        }
                                    } else {
                                        // Handle the case where no paramsList key is found
                                        runOnUiThread(() -> Utils.showToast(Dashboard.this, "Error: paramsList not found in the response."));
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    runOnUiThread(() -> Utils.showToast(Dashboard.this, "Error parsing response data: " + e.getMessage()));
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
                    ItemToBuyText.setText(String.format("%s\n that last for %s", Type.getText().toString(), time));
                    SelectedItemType.setText(itemDescription);
                    SelectedItemLifeTime.setText(time);
                    SelectedItemPrice.setText(String.format("%s%s", currencySymbol, price));
                    ItemCode = itemCode;

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

    public class Statement extends RecyclerView.Adapter<Statement.ViewHolder> {

        private List<Map<String, Object>> statements;

        public Statement(List<Map<String, Object>> statements) {
            this.statements = statements != null ? statements : new ArrayList<>(); // Ensure statements is non-null
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate the layout for the ViewHolder
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Get the statement data at the current position and bind it to the ViewHolder
            Map<String, Object> statement = statements.get(position);
            holder.bind(statement);
        }

        @Override
        public int getItemCount() {
            return statements.size();
        }

        public void updateStatements(List<Map<String, Object>> newStatements) {
            this.statements = newStatements != null ? newStatements : new ArrayList<>();
            notifyDataSetChanged(); // Notify adapter to refresh the list
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView referenceID, entryDate, transactionType, amount, balance;

            public ViewHolder(View itemView) {
                super(itemView);
                // Bind the views from the layout to corresponding variables
                referenceID = itemView.findViewById(R.id.referenceId);
                entryDate = itemView.findViewById(R.id.entryDate);
                transactionType = itemView.findViewById(R.id.transactionType);
                amount = itemView.findViewById(R.id.amount);
                balance = itemView.findViewById(R.id.cumulativeBalance);
            }

            // Method to bind the statement data to the UI elements
            public void bind(Map<String, Object> statement) {
                // Get values from the map
                String transactionTypeValue = getValueFromMap(statement, "transactionType", "N/A");

                // Set the text for each UI component
                referenceID.setText(getValueFromMap(statement, "referenceID", "N/A"));
                entryDate.setText(getValueFromMap(statement, "entryDate", "N/A"));

                // Properly format the balance using String.format
                String cumulativeBalance = getValueFromMap(statement, "cumulativeBalance", "000");

                // Check if cumulativeBalance is null, empty, or not a valid number, and default it to "0000"
                if (cumulativeBalance == null || cumulativeBalance.trim().isEmpty()) {
                    cumulativeBalance = "0000";
                }

                String formattedBalance = String.format("%s%s", currencySymbol, Utils.FormatAmount(cumulativeBalance));
                balance.setText(formattedBalance);


                // Set the transaction type
                transactionType.setText(transactionTypeValue);

                // Determine which amount to display based on the transaction type
                String amountKey = transactionTypeValue.contains("Deposit") ? "depositAmount" : "rechargeAmount";

                // Format the amount for display
                String formattedAmount = Utils.FormatAmount(getValueFromMap(statement, amountKey, "N/A"));
                amount.setText(String.format("%s%s", currencySymbol, formattedAmount));
            }


            // Utility method to safely fetch values from the map
            private String getValueFromMap(Map<String, Object> map, String key, String defaultValue) {
                Object value = map.get(key);
                return value != null ? value.toString() : defaultValue;
            }
        }
    }

    public static List<Map<String, Object>> getStatement() {
        List<Map<String, Object>> items = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                JSONObject catalogRequestResponse = ApiService.statement(agentID, agentName, agentPassword, agentEmail);
                    JSONObject catalogRequestResponse = ApiService.statement();
                    if (catalogRequestResponse.has("response")) {
                        String nestedResponseString = catalogRequestResponse.getString("response");
                        JSONObject nestedResponse = new JSONObject(nestedResponseString);
                        if (nestedResponse.has("methodResponse")) {
                            JSONArray paramsList = nestedResponse.getJSONObject("methodResponse").getJSONArray("paramsList");
                            for (int i = 0; i < paramsList.length(); i++) {
                                JSONObject product = paramsList.getJSONObject(i);
                                Map<String, Object> item = new HashMap<>();
                                item.put("customerID", product.optString("customerID", ""));
                                item.put("network", product.optString("network", ""));
                                item.put("transactionType", product.optString("transactionType", ""));
                                item.put("productID", product.optString("productID", ""));
                                item.put("productDescription", product.optString("productDescription", ""));
                                item.put("productCategory", product.optString("productCategory", ""));
                                item.put("rechargeAmount", product.optString("RechargeAmount", ""));
                                item.put("depositAmount", product.optString("DepositAmount", ""));
                                item.put("providerSerial", product.optString("providerSerial", ""));
                                item.put("providerReference", product.optString("providerReference", ""));
                                item.put("providerStatus", product.optString("providerStatus", ""));
                                item.put("providerStatusCode", product.optString("providerStatusCode", ""));
                                item.put("currency", product.optString("currency", ""));
                                item.put("costPrice", product.optString("costPrice", ""));
                                item.put("agentID", product.optString("agentID", ""));
                                item.put("agentName", product.optString("agentName", ""));
                                item.put("agentSplit", product.optString("agentSplit", ""));
                                item.put("providerID", product.optString("providerID", ""));
                                item.put("providerSplit", product.optString("providerSplit", ""));
                                item.put("cumulativeBalance", product.optString("cumulativeBalance", ""));
                                item.put("providerBalance", product.optString("providerBalance", ""));
                                item.put("entryDate", product.optString("entryDate", ""));

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
                    System.out.println("JSON Parsing Error: " + e.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        return items;
    }

    ;

    public List<Map<String, Object>> getProducts() {
        List<Map<String, Object>> items = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                                item.put("entryDate", product.getString("entryDate"));
                                item.put("providerSplit", product.getString("providerSplit"));
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
                    System.out.println("JSON Parsing Error: " + e.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        return items;
    }


    public List<Map<String, Object>> filterProductsByType(List<Map<String, Object>> products, String filterType) {
        List<Map<String, Object>> filteredProducts = new ArrayList<>();
        for (Map<String, Object> product : products) {
            if (product.get("productCategory") != null && product.get("productCategory").toString().toLowerCase().contains(filterType.toLowerCase())) {
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
        Navbar.setVisibility(View.GONE);
        AppFrame.setVisibility(View.GONE);
        job_list_screen.setVisibility(View.VISIBLE);
    }

    private void defaultColoring(ImageButton icon) {
        NavBuyBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavIPSBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavHomeBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        NavLaodBalanceBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        icon.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);
    }

    private void clearFields() {
        Phone.setText("");
    }

}
