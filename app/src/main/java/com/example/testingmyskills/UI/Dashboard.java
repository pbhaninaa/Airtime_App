package com.example.testingmyskills.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingmyskills.Dao.XMLRPCClient;
import com.example.testingmyskills.Interfaces.BalanceResponseCallback;
import com.example.testingmyskills.JavaClasses.ApiService;
import com.example.testingmyskills.JavaClasses.CurrencyTextWatcher;
import com.example.testingmyskills.JavaClasses.MyJavaScriptInterface;
import com.example.testingmyskills.JavaClasses.Bundles;
import com.example.testingmyskills.JavaClasses.Country;
import com.example.testingmyskills.JavaClasses.PaymentProcessor;
import com.example.testingmyskills.R;
import com.example.testingmyskills.JavaClasses.Utils;


import org.apache.xmlrpc.XmlRpcException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Dashboard extends AppCompatActivity implements BalanceResponseCallback {
    private ConstraintLayout AppFrame;
    private LinearLayout WebScree, Header, Navbar, ItemsLayout, ISPsLayout, BuyLayout, EconetIsp, TelecelIsp, NetoneIsp, ZesaIsp, LoadBalanceLayout;
    private FrameLayout LogoutButton, BackToHome;
    private WebView Web;
    private TextView HeaderTitle, SelectedIsp, AvailableBalance, StatusMessage, SelectedItem, MoreBtn, ItemToBuyText, SelectedItemType, SelectedItemPrice, SelectedItemLifeTime;
    private EditText Phone, AmountTLoad, LoadingRef, LoadingNote;
    private ImageButton NavHomeBtn, NavBuyBtn, NavProfileBtn, NavIPSBtn, NavMoreBtn, NavLaodBalanceBtn;
    private RecyclerView ItemRecyclerView;
    private Spinner ItemFilterSpinner, ItemToBuySpinner;
    private Spinner CountryCode;
    private ConstraintLayout ConfirmationScreen;
    private LinearLayout job_list_screen;
    private ImageButton backFromList;
    private Button BuyBtn, Yes, No, LoadBalance;
    private TextView number_of_posts;
    private Spinner filter_spinner;


    private ImageButton FilterButton;
    private ScrollView scrollView;
    private LinearLayout filterSection;
    //    ApiCalls api = new ApiCalls();
    private boolean show;
    //    private AlphaKeyboard MyKeyboard;
//    private Button hideKeyboardBtn;
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
        String transactionType = "account_balance_enquiry";
        MSISDN = "263781801174";
        APICall(MSISDN, transactionType, this);
        show = true;
        initialiseViews();
        getSelectedCategory = false;
        Utils.hideSoftNavBar(Dashboard.this);
        setOnclickListeners();
        recyclerViews();
        adaptors();


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        currencySymbol = sharedPreferences.getString("currency_symbol", getString(R.string.default_currency_symbol));
        spinners();


//=======================================================================================================================
        AppFrame.setVisibility(View.VISIBLE);
        BackToHome.setVisibility(SelectedIsp.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
        ISPsLayout.setVisibility(View.VISIBLE);
        NavHomeBtn.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);
        getAccount();

        ArrayAdapter<Country> ada = new ArrayAdapter<>(this, R.layout.spinner_item, UserManagement.getCountryList());
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CountryCode.setAdapter(ada);
        CountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country selectedCountry = (Country) parent.getItemAtPosition(position);
                String flagName = selectedCountry.getCountryFlag();
                String countryName = selectedCountry.getCountryName();

                // Get the resource ID of the drawable dynamically
                int flagResourceId = getResources().getIdentifier(flagName, "drawable", getPackageName());
                // Set the ImageView with the corresponding flag
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
        Header = findViewById(R.id.balance_display_layout);
        LogoutButton = findViewById(R.id.logout_button);
        HeaderTitle = findViewById(R.id.balances_text);
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
        // Undone yet
        MoreBtn = findViewById(R.id.More_Items);
        SelectedItem = findViewById(R.id.SelectedItem);

        ItemToBuySpinner = findViewById(R.id.item_to_buy);
        ItemToBuyText = findViewById(R.id.item_to_buy_text);

        Navbar = findViewById(R.id.navbar);
        SelectedIsp = findViewById(R.id.selected_network_text);

        EconetIsp = findViewById(R.id.econetISP);
        TelecelIsp = findViewById(R.id.telecelISP);
        NetoneIsp = findViewById(R.id.netoneISP);
        ZesaIsp = findViewById(R.id.zesaISP);
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

    }


    //         Manual Load
    //        String loggedUserId = Utils.getString(this, "profile", "id");
//
//        // Get the current date and time in the required format
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
//        String currentTime = sdf.format(new Date());
//        String UID = Utils.ref().replace("QU", "PAY");
//        // Proceed with the logic if all fields are filled
//        XMLRPCClient.addManualPaymentAsync(
//                Integer.parseInt(loggedUserId),
//                UID,
//                Double.parseDouble(amount),
//                notes,
//                currentTime,
//                new XMLRPCClient.ResponseCallback() {
//                    @Override
//                    public void onSuccess(String response) {
//                        try {
//                            AmountTLoad.setText("");
//                            LoadingNote.setText("");
//                            // Parse the JSON response
//                            JSONObject jsonResponse = new JSONObject(response);
//                            System.out.println("Response " + response);
//
//                            if (response.contains("success")) {
//                                Utils.showToast(Dashboard.this, "Successfully loaded");
//                                Intent iu = new Intent(Dashboard.this, Dashboard.class);
//                                startActivity(iu);
//                            } else {
//                                Utils.showToast(Dashboard.this, "Error ");
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Utils.showToast(Dashboard.this, "Failed to parse response");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Utils.showToast(Dashboard.this, e.getMessage());
//                        e.printStackTrace();
//                    }
//                });

    private void handleLoadBalance() {
        // Retrieve and trim the input values
        String amount = AmountTLoad.getText().toString().trim();
        String notes = LoadingNote.getText().toString().trim();

// Remove currency code (e.g., "Z$") and commas
        amount = amount.replaceAll("[^\\d.]", ""); // Keeps digits and decimal points
        notes = notes.replaceAll("[^\\dA-Za-z\\s]", ""); // Keeps letters and spaces (modify as needed)

// Now, amount contains only numbers and decimal point
// And notes contains only letters and spaces


        // Check if the amount field is empty
        if (amount.isEmpty() || amount.equalsIgnoreCase("0.00")) {
            AmountTLoad.setError("Amount is required");
            return;
        }

        // Check if the notes field is empty
        if (notes.isEmpty()) {
            LoadingNote.setError("Notes are required");
            return;
        }
//        Phila

        // Configure the WebView to display content inside the app
        Web.getSettings().setJavaScriptEnabled(true);  // Enable JavaScript
        Web.getSettings().setDomStorageEnabled(true);  // Enable DOM storage
        Web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  // Enable caching

        // Add the JavaScript interface to the WebView
        Web.addJavascriptInterface(new MyJavaScriptInterface(this), "Android");

        // Set WebViewClient to prevent redirects outside the app
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

        // Show the WebView layout and hide other layouts
        hideLayouts(WebScree, NavBuyBtn);
        Navbar.setVisibility(View.GONE);

        // Start a background thread to fetch the payment URL
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

    // Method to process the payment result
    private void handlePaymentResult(String result) {
        runOnUiThread(() -> {
            // Parse and handle the payment result here
            Utils.showToast(this, result);  // Show toast on the UI thread
            System.out.println("Payment Result: " + result);
            // Update UI or perform other actions based on the result
        });
    }


    // Method to process the payment result


    public void closePaymentView(View view) {
        Navbar.setVisibility(View.VISIBLE);
        hideLayouts(LoadBalanceLayout, NavBuyBtn);
    }


    @Override
    public void onBalanceReceived(Map<String, Object> response) {
        try {
            String amount = Objects.requireNonNull(response.get("Amount")).toString();
            amount = amount;
            String Balance = amount.isEmpty() ? "No Balance to display" : (String.format("Account Balance %s%s", currencySymbol, Utils.FormatAmount(amount)));


//            AvailableBalance.setText(Balance);
            double balance = Double.parseDouble(amount);

//            Utils.setMessage(this, balance, StatusMessage);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        System.out.println("Dash Res: " + response);
    }

    public void spinners() {
//        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // Retrieve the selected item
//                String selectedItem = parentView.getItemAtPosition(position).toString();
//
//
//                RecyclerView recyclerView = findViewById(R.id.MoreItemsRecyclerView);
//                recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
//                recyclerView.setAdapter(new RecommendedAd(filterProductsByType(getProducts(), selectedItem)));
//                numItems = filterProductsByType(getProducts(), selectedItem).size();
//
//                number_of_posts.setText(String.format("%s%s", numItems, getString(R.string.items_found)));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Handle no selection if needed
//            }
//        });
//        ItemFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // Retrieve the selected item
//                String selectedItem = parentView.getItemAtPosition(position).toString();
//                List<Map<String, Object>> array = filterProductsByType(getProducts(), selectedItem);
//                SelectedItem.setText(selectedItem + " is selected");
//                ItemRecyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
//                ItemRecyclerView.setAdapter(new RecommendedAd(array));
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Handle no selection if needed
//            }
//        });


    }

    @Override
    public void onBackPressed() {
        if (Web.canGoBack()) {
            Web.goBack();  // Go back in WebView's history
        } else {
            super.onBackPressed();  // Default behavior
        }
    }

    @Override
    public void onLoadValues(Map<String, Object> response) {

        int status = (int) response.get("Status");
        String des = Objects.requireNonNull(response.get("Description")).toString();
        if (status == 0) {
            Utils.showToast(this, des);
        } else if (status == 1) {
            clearFields();
            Utils.success(this, des);
        }
        System.out.println("Load Res: " + response);
    }

    @Override
    public void onLoadBalance(Map<String, Object> response) {

        int status = (int) response.get("Status");
        String des = Objects.requireNonNull(response.get("Description")).toString();
        if (status == 0) {
            Utils.showToast(this, des);
        } else if (status == 1) {
            clearFields();
            Utils.success(this, des);
        }
        System.out.println("Load Balance Res: " + response);
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

    private void APICall(String number, String transactionType, BalanceResponseCallback callback) {
        new Thread(() -> {
            try {
                Map<String, Object> response = XMLRPCClient.accountBalanceEnquiry(number, transactionType);
                runOnUiThread(() -> {
                    callback.onBalanceReceived(response);
                });
            } catch (XmlRpcException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void Load(String number, String transactionType, BalanceResponseCallback callback) {
        new Thread(() -> {
            try {
                Map<String, Object> response = XMLRPCClient.accountBalanceEnquiry(number, transactionType);
                runOnUiThread(() -> {
                    callback.onLoadValues(response);
                });
            } catch (XmlRpcException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void LoadBalance(int UserId, String ref, String note, String amount, BalanceResponseCallback callback) {
        new Thread(() -> {
            try {
                Map<String, Object> response = XMLRPCClient.LoadBalanceEnquiry(UserId, ref, note, amount);
                runOnUiThread(() -> {
                    callback.onLoadBalance(response);
                });
            } catch (XmlRpcException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
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
        jobListRecyclerView.setAdapter(new RecommendedAd(getProducts()));

        ItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemRecyclerView.setAdapter(new RecommendedAd(getProducts()));
    }

    private void openUrlWithFallback(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        String packageName = getPackageNameForCustomTabs();

        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } else {
            // Fallback to a regular browser intent
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Utils.showToast(this, "Download a browser");
                System.out.println("No application can handle this URL.");
            }
        }
    }

    private void updateCurrencySymbol(String newCurrencySymbol) {
        // Update the currency symbol string resource
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currency_symbol", newCurrencySymbol);
        editor.apply();
    }

    private void setOnclickListeners() {
        backFromList.setOnClickListener(v -> handleBackFromList());
        BuyBtn.setOnClickListener(v -> handleTransaction());
        FilterButton.setOnClickListener(v -> hideFilter());
//        hideKeyboardBtn.setOnClickListener(v -> hideKeyboard());
        No.setOnClickListener(v -> handleNo());
        Yes.setOnClickListener(v -> handleYes());
        //===================================new===============================
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
//        ZesaIsp.setOnClickListener(v -> setISP("Electricity"));
        TelecelIsp.setOnClickListener(v -> setISP("Telecel"));
        BackToHome.setOnClickListener(v -> hideLayouts(ISPsLayout, NavHomeBtn));
        LoadBalance.setOnClickListener(v -> handleLoadBalance());
    }

    private String getPackageNameForCustomTabs() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://example.com"));
        for (ResolveInfo resolveInfo : pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)) {
            if (resolveInfo.activityInfo != null && resolveInfo.activityInfo.packageName != null) {
                return resolveInfo.activityInfo.packageName;
            }
        }
        return null;
    }

    public void setISP(String ISPName) {
        if (!ISPName.equals("Econet")) {
            Utils.showToast(this, "Not yet available");
            return;
        }
        BackToHome.setVisibility(View.VISIBLE);

        NavIPSBtn.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);
        defaultColoring(NavHomeBtn);
        SelectedIsp.setText(ISPName);
        String transactionType = "account_balance_enquiry";
        MSISDN = "263781801174";
        APICall(MSISDN, transactionType, this);
        ISPsLayout.setVisibility(View.GONE);
        ItemsLayout.setVisibility(View.VISIBLE);
    }


    private void getAccount() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("profile", Context.MODE_PRIVATE);

        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        String phone = sharedPreferences.getString("phone", "");
        String emailAddress = sharedPreferences.getString("emailAddress", "");
        String balance = sharedPreferences.getString("balance", "");
        String updated = sharedPreferences.getString("time", "");

        String formatedSalutation = "Hello " + name + " " + surname + " " + phone + "  Last updated " + updated;
        String Balance = balance.isEmpty() ? "No Balance to display" : (String.format("Account Balance %s%s", currencySymbol, Utils.FormatAmount(balance)));


        StatusMessage.setText(formatedSalutation);

        AvailableBalance.setText(Balance);


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
        String token = Utils.getString(this, "LoggedUser", "token");

//        XMLRPCClient.logoutAsync(token, new XMLRPCClient.ResponseCallback() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    // Parse the JSON response
//                    JSONObject jsonResponse = new JSONObject(response);
//                    // Check if the access_token is present in the response
//                    if (jsonResponse.has("authorization")) {
//                        String token = jsonResponse.getJSONObject("authorization").getString("access_token");
//                        Intent intent = new Intent(this, UserManagement.class);
//                        intent.putExtra("constraintLayoutId", R.id.login_page);
//                        startActivity(intent);
//                    } else {
//                        // Handle case where authorization token is missing
//                        JSONObject error = jsonResponse.getJSONObject("error");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//                // Handle the error response
//                e.printStackTrace();
//            }
//        });
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
        // Populate EditText fields
        String name = prefs.getString("name", "");
        String phoneNumber = name + "(" + prefs.getString("phone", "") + ")";

        String salutation = "Hello " + phoneNumber;
        String updateTime = "last updated a minute ago";
        MSISDN = prefs.getString("phone", "");

        // Create a SpannableStringBuilder to apply different colors
        SpannableStringBuilder builder = new SpannableStringBuilder();

        // Append the main text "Hello, 0782141216" with black color
        builder.append(salutation);
        int start = 0;
        int end = salutation.length();
        builder.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, 0);

        // Append the small text "last updated a minute ago" with gray color
        builder.append("\n").append(updateTime);
        start = end + 1; // Start just after the salutation text
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

        // Clean the AvailableBalance string to remove non-numeric characters
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


                // Start the transaction in a separate thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SharedPreferences sharedPreferences = Dashboard.this.getSharedPreferences("profile", Context.MODE_PRIVATE);
                            String name = sharedPreferences.getString("name", "");
                            String surname = sharedPreferences.getString("surname", "");
                            String agentId = sharedPreferences.getString("phone", "");

//                            JSONObject res = ApiService.loadValue("Econet", "27649045091", "Lewis", "ruffgunz", "263781801175", "50", "VALUE", "VALUE");
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
                            // Format the request parameters into a readable string
                            String params = String.format("ISP Name: %s\nAgent ID: %s\nFull Name: %s\nUser Identifier: %s\nPhone: %s\nPrice: %s\nItem Code: %s\nItem Type: %s",
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
                            System.out.println(params);

                            // Handle the response as needed
                            System.out.println("Load Bundle Response: " + res.toString());

                            if (res.getInt("responseCode") == 200) {
                                // Handle success and UI updates on the main thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            // Extract the response string and parse it into a JSON object
                                            String responseString = res.getString("response");
                                            JSONObject responseJson = new JSONObject(responseString); // Parse the response string

                                            // Now extract the methodResponse and paramsList
                                            JSONObject methodResponse = responseJson.getJSONObject("methodResponse");
                                            JSONArray paramsList = methodResponse.getJSONArray("paramsList");
                                            JSONObject responseDetails = paramsList.getJSONObject(0);

                                            // Get the StatusCode and Description
                                            String Serial = responseDetails.getString("providerSerial");
                                            String description = responseDetails.getString("providerStatus");
                                            String balance = responseDetails.getString("cumulativeBalance");
                                            AvailableBalance.setText(Utils.FormatAmount(balance));

                                            if (!Serial.isEmpty()) {

                                                new AlertDialog.Builder(Dashboard.this)
                                                        .setTitle("Bundle Loaded Successfully")
                                                        .setMessage("Serial: " + Serial)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // Call your method here when OK is clicked
                                                                hideLayouts(ItemsLayout, NavIPSBtn);
                                                            }
                                                        }) // Dismiss the alert when OK is clicked
                                                        .show();

                                            } else {
                                                // Failure: Show the error description in a toast
                                                Utils.showToast(Dashboard.this, "Error: " + description);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Utils.showToast(Dashboard.this, "Error parsing response data: " + e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                // Handle error response on UI thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.showToast(Dashboard.this, res.toString());
                                    }
                                });
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                            // Handle IOException on UI thread
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

    public void hideOtherLayout(int layoutToShow, ImageButton icon) {

        if (layoutToShow == R.id.create_profile_screen) {
            Intent intent = new Intent(this, UserManagement.class);
            intent.putExtra("constraintLayoutId", layoutToShow);
            startActivity(intent);

        } else {
            defaultColoring(icon);
            clearFields();
            job_list_screen.setVisibility(View.GONE);
            ConstraintLayout l = findViewById(layoutToShow);
            l.setVisibility(View.VISIBLE);
        }
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

                // Set click listener
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
//                    hideOtherLayout(R.id.buy_screen, btnNotifications);
//                    BuyTittle.setText(String.format("%s\n that last for %s", Type.getText().toString(), time));
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
//                    ItemTypeSpinner2.setVisibility(View.GONE);
//                    Item.setVisibility(View.VISIBLE);

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


    public List<Map<String, Object>> getProducts() {

        Bundles[] bundles = Utils.readJsonFile(Dashboard.this, filePath);

        List<Map<String, Object>> items = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject catalogRequestResponse = ApiService.catalogRequest();
                    System.out.println("API Response: " + catalogRequestResponse.toString());

                    // Check if the main response contains "response" key
                    if (catalogRequestResponse.has("response")) {
                        // Parse the nested response JSON string
                        String nestedResponseString = catalogRequestResponse.getString("response");
                        JSONObject nestedResponse = new JSONObject(nestedResponseString);

                        // Now check for "methodResponse" in the nested JSON
                        if (nestedResponse.has("methodResponse")) {
                            JSONArray paramsList = nestedResponse.getJSONObject("methodResponse").getJSONArray("paramsList");

                            for (int i = 0; i < paramsList.length(); i++) {
                                JSONObject product = paramsList.getJSONObject(i);
                                Map<String, Object> item = new HashMap<>();

                                item.put("type", product.getString("productCategory"));
                                item.put("amount", product.getString("amount"));
                                item.put("lifeTime", product.getString("validity"));
                                item.put("price", product.getString("costPrice"));

                                // Additional product details
                                item.put("num", product.getString("num"));
                                item.put("productID", product.getString("productID"));
                                item.put("productDescription", product.getString("productDescription"));
                                item.put("shortDescription", product.getString("shortDescription"));
                                item.put("network", product.getString("network"));
                                item.put("costPrice", product.getString("costPrice"));
                                item.put("entryDate", product.getString("entryDate"));
                                item.put("providerSplit", product.getString("providerSplit"));


                                items.add(item);


                                System.out.println("Product At : " + (i + 1) + "\n" + product);
                                System.out.println("Product Description : " + product.get("productDescription"));
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


//        if (bundles != null) {
//            for (Bundles bundle : bundles) {
//                Map<String, Object> item = new HashMap<>();
//                item.put("type", bundle.getCategory());
//                item.put("amount", bundle.getBundle());
//                String l = bundle.getValidity().contains("day") ? bundle.getValidity() : bundle.getValidity() + " days";
//                item.put("lifeTime", l);
//                item.put("price", bundle.getCurrentUSDCharge());
//                items.add(item);
//
//            }
//        }

        return items;
    }


    public List<Map<String, Object>> filterProductsByType(List<Map<String, Object>> products, String filterType) {
        // Initialize a list to hold the filtered product data
        List<Map<String, Object>> filteredProducts = new ArrayList<>();

        // Iterate over each product in the input list
        for (Map<String, Object> product : products) {
            // Check if the product type contains the specified filterType
            if (product.get("type") != null && product.get("type").toString().toLowerCase().contains(filterType.toLowerCase())) {
                // Add the product to the filtered list
                filteredProducts.add(product);
            }
        }

        // Return the filtered list of products
        return filteredProducts;
    }


    private void handleBackFromList() {
        BackToHome.setVisibility(View.GONE);
        Navbar.setVisibility(View.VISIBLE);
        job_list_screen.setVisibility(View.GONE);
        AppFrame.setVisibility(View.VISIBLE);
//        dash_board_screen.setVisibility(View.VISIBLE);
//        landingScreen.setVisibility(View.GONE);
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

//    private void hideKeyboard() {
//        Utils.hideAlphaKeyboard(MyKeyboard);
//    }

    private void clearFields() {
        Phone.setText("");
//        Item.setText("");
//        Amount.setText("");
//        ItemPrice.setText("");
    }

}
