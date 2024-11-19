package com.example.testingmyskills.UI;

import static com.example.testingmyskills.UI.UserManagement.getCountryList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
    private LinearLayout SelectedItem, AmountCapture, WebScree, Navbar, ItemsLayout, ISPsLayout, BuyLayout, EconetIsp, TelecelIsp, NetoneIsp, LoadBalanceLayout;
    private FrameLayout LogoutButton, BackToHome;
    private WebView Web;
    private TextView currencySymbolInBuy, AmountToLoadSymbol, SelectedIsp, AvailableBalance, StatusMessage, MoreBtn, ItemToBuyText, SelectedItemType, SelectedItemPrice, SelectedItemLifeTime;
    private EditText Phone, AmountTLoad, AmountTLoadInBuy, LoadingNote;
    private ImageButton NavHomeBtn, NavBuyBtn, NavProfileBtn, NavIPSBtn, NavMoreBtn, NavLaodBalanceBtn1, NavLaodBalanceBtn;
    private RecyclerView ItemRecyclerView;
    private Spinner ItemFilterSpinner, ItemToBuySpinner;
    private Spinner CountryCode;
    private ConstraintLayout ConfirmationScreen;
    private LinearLayout job_list_screen;
    private ImageButton backFromList;
    private ImageView statusLight, CountryFlag, load;
    private Button BuyBtn, BuyBtn1, Yes, No, LoadBalance1, LoadBalance;
    private TextView number_of_posts;
    private Spinner filter_spinner;


    private ImageButton FilterButton;
    private ScrollView scrollView;
    private LinearLayout filterSection;
    private boolean show;
    static String currencySymbol, ItemCode;
    private RecyclerView jobListRecyclerView;
    private int numItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        getProfile();
        show = true;
        initialiseViews();
        Utils.hideSoftNavBar(Dashboard.this);
        setOnclickListeners();
        recyclerViews();
        adaptors();


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        currencySymbol = sharedPreferences.getString("currency_symbol", getString(R.string.default_currency_symbol));

        AppFrame.setVisibility(View.VISIBLE);
        BackToHome.setVisibility(SelectedIsp.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
        ISPsLayout.setVisibility(View.VISIBLE);
        NavHomeBtn.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);


        ArrayAdapter<Country> ada = new ArrayAdapter<>(this, R.layout.spinner_item, getCountryList());
        ada.setDropDownViewResource(R.layout.spinner_item);
        CountryCode.setAdapter(ada);

        CountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country selectedCountry = (Country) parent.getItemAtPosition(position);
                String flagName = selectedCountry.getCountryFlag();
                // Get the resource ID of the drawable dynamically
                int flagResourceId = getResources().getIdentifier(flagName, "drawable", getPackageName());
                // Set the ImageView with the corresponding flag
                if (flagResourceId != 0) {  // Check if the resource was found
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
        AmountTLoadInBuy.addTextChangedListener(new CurrencyTextWatcher(AmountTLoadInBuy));
        spinners();
        getAccount("");
        Utils.rotateImageView(load);
    }

    private void initialiseViews() {
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
        NavLaodBalanceBtn1 = findViewById(R.id.nav_load_btn1);
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

//                AmountTLoad.setText("");
//                LoadingNote.setText("");

                if (url.contains("payment-success")) {
                    String transactionId = Uri.parse(url).getQueryParameter("transactionId");
                    handlePaymentResult("{\"status\": \"success\", \"transactionId\": \"" + transactionId + "\"}");

                    return true;  // Prevent loading this URL in the WebView
                } else if (url.contains("payment-failure")) {
                    handlePaymentResult("{\"status\": \"failure\"}");
                    return true;  // Prevent loading this URL in the WebView
                }

                view.loadUrl(url); // Allow the WebView to load other URLs
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
                        // Add a delay of 5 seconds (5000 milliseconds)
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Web.loadUrl(redirectUrl);

                            load.setVisibility(View.GONE);
                            Web.setVisibility(View.VISIBLE);
                        }, 5000);
                    } else {
                        // Handle failure or error case
                        Utils.showToast(this, "Failed to get redirect URL.");
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
            try {
                JSONObject resultJson = new JSONObject(result);
                String status = resultJson.getString("status");
                String message;
                String transactionId = resultJson.optString("transactionId");  // Get the transaction ID

                if ("success".equals(status)) {
                    message = "Payment successful!";

                    // Call your server to confirm the payment using the webhook
                    confirmPaymentWithWebhook(transactionId);  // Send the transactionId to your server's webhook
                } else {
                    message = "Payment failed. Please try again.";
                }

                Utils.showToast(this, message);

                // Delay the closure of the payment view to allow user to see the message
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
                    System.out.println("Webhook response: " + response.toString());
                } else {
                    System.out.println("Webhook request failed. Response code: " + responseCode);
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

        if (imageButton.getId() == R.id.nav_buy_btn1) {
            SelectedItem.setVisibility(View.VISIBLE);
            AmountCapture.setVisibility(View.GONE);
            BuyBtn1.setVisibility(View.GONE);
            BuyBtn.setVisibility(View.VISIBLE);

        } else if (imageButton.getId() == R.id.nav_load_btn1) {

            SelectedItem.setVisibility(View.GONE);
            AmountCapture.setVisibility(View.VISIBLE);
            BuyBtn1.setVisibility(View.VISIBLE);
            BuyBtn.setVisibility(View.GONE);
        }
        LoadBalance1.setVisibility(View.GONE);
        LoadBalance.setVisibility(View.VISIBLE);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, MainActivity.econetItems);
        adapter.setDropDownViewResource(R.layout.spinner_layout);

        filter_spinner.setAdapter(adapter);
        ItemFilterSpinner.setAdapter(adapter);
        ItemToBuySpinner.setAdapter(adapter);
    }

    private void recyclerViews() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("LoggedUserCredentials", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        String AgentID = sharedPreferences.getString("phone", "");
        String AgentEmail = sharedPreferences.getString("email", "");
        String AgentPassword = sharedPreferences.getString("password", "");
        String AgentName = name + " " + surname;
        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobListRecyclerView.setAdapter(new Statement(this, getStatement(AgentID, AgentName, AgentPassword, AgentEmail)));
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
        FilterButton.setOnClickListener(v -> hideFilter());
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
        BackToHome.setOnClickListener(v -> {
            Navbar.setVisibility(View.VISIBLE);
            hideLayouts(ISPsLayout, NavHomeBtn);
        });
        LoadBalance.setOnClickListener(v -> handleLoadBalance());
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
                    JSONObject res = ApiService.depositFunds(Utils.getString(Dashboard.this, "LoggedUserCredentials", "phone"), AmountTLoad.getText().toString(), "840");
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
                                    String balance = userObject.getString("cumulativeBalance");
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
                            Utils.showToast(Dashboard.this, "An Error Occurred");
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
        String formatedSalutation = "Hello " + name + " " + surname + " " + phone + "  Last updated " + updated;
        String Balance = balance.isEmpty() ? "No Balance to display" : (String.format("Account Balance %s%s", currencySymbol, Utils.FormatAmount(bal.isEmpty() ? balance : bal)));
        StatusMessage.setText(formatedSalutation);
        Utils.setStatusColor(this, (bal.isEmpty() ? balance : bal), statusLight);
        AvailableBalance.setText(Balance);
        AmountToLoadSymbol.setText(currencySymbol);
        currencySymbolInBuy.setText(currencySymbol);
        clearFields();
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
        Utils.logout(this);
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
        String p = CountryCode.getSelectedItem() + phone;
        if (!p.equals("+263781801175")) {
            Utils.showToast(Dashboard.this, "Invalid Number");
            return;
        }
        if (price.equals("0.00")) {
            AmountTLoadInBuy.setError("Price is required");
        }
        String balanceStr = AvailableBalance.getText().toString().replace(currencySymbol, "").replace(",", "").replace("Account Balance", "").trim();


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
                            JSONObject res = ApiService.loadValue(
                                    SelectedIsp.getText().toString(),
                                    Utils.getString(Dashboard.this, "LoggedUserCredentials", "phone"),
                                    p,
                                    price.replace(",", "").replace(".", ""),
                                    "Airtime",
                                    "Airtime"
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
                                            String Network = responseDetails.getString("network");
                                            String basketID = responseDetails.getString("basketID");
                                            String CustomerID = responseDetails.getString("customerID");
                                            String AgentID = responseDetails.getString("agentID");


                                            if (!Serial.isEmpty()) {

                                                new AlertDialog.Builder(Dashboard.this).setTitle("Bundle Loaded Successfully").setMessage("Serial: " + Serial).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // Call your method here when OK is clicked
                                                                hideLayouts(ItemsLayout, NavIPSBtn);
                                                                getAccount(balance);
                                                                setISP(SelectedIsp.getText().toString());
                                                                Utils.saveRefs(Dashboard.this, Network, AgentID, CustomerID, basketID);
                                                                clearFields();
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

    private void handleTransaction() {
        String phone = Phone.getText().toString().trim();
        String price = SelectedItemPrice.getText().toString().trim().replace(currencySymbol, "");
        if (phone.isEmpty()) {
            return;
        }
        String p = CountryCode.getSelectedItem() + phone;
        if (!p.equals("+263781801175")) {
            Utils.showToast(Dashboard.this, "Invalid Number");
            return;
        }
        String balanceStr = AvailableBalance.getText().toString().replace(currencySymbol, "")  // Remove the currency symbol
                .replace(",", "")             // Remove commas
                .replace("Account Balance", "") // Remove the "Account Balance" text
                .trim();                      // Remove extra spaces

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

                            JSONObject res = ApiService.loadBundle(SelectedIsp.getText().toString(),             // ISP Name
                                    agentId,                                      // Agent ID
                                    agentName,                         // Full Name
                                    agentPassword,                                   // User identifier or password
                                    p.replace("+", ""), // Phone number formatted with "263"
                                    SelectedItemPrice.getText().toString().replace(currencySymbol, "")              // Remove currency symbol
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
                                            String Network = responseDetails.getString("network");
                                            String basketID = responseDetails.getString("basketID");
                                            String CustomerID = responseDetails.getString("customerID");
                                            String AgentID = responseDetails.getString("agentID");


                                            if (!Serial.isEmpty()) {

                                                new AlertDialog.Builder(Dashboard.this).setTitle("Bundle Loaded Successfully").setMessage("Serial: " + Serial).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // Call your method here when OK is clicked
                                                                hideLayouts(ItemsLayout, NavIPSBtn);
                                                                setISP(SelectedIsp.getText().toString());

                                                                Utils.saveRefs(Dashboard.this, Network, AgentID, CustomerID, basketID);

                                                                clearFields();
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

                    String network = Utils.getString(Dashboard.this, "LastTransactionRefs", "network");
                    String agentID = Utils.getString(Dashboard.this, "LastTransactionRefs", "agentID");
                    String customerID = Utils.getString(Dashboard.this, "LastTransactionRefs", "customerID");
                    String referenceID = Utils.getString(Dashboard.this, "LastTransactionRefs", "referenceID");


                    JSONObject res = ApiService.transactionStatusEnquiry(network,
                            agentID,
                            customerID,
                            referenceID
                    );


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
                                        JSONObject responseDetails = paramsList.getJSONObject(0);

                                        String serial = responseDetails.optString("Serial", "N/A");
                                        if (!serial.equals("N/A") && !serial.isEmpty()) {
                                            runOnUiThread(() -> new AlertDialog.Builder(Dashboard.this)
                                                    .setTitle("Transaction was successful")
                                                    .setMessage("Serial Number: " + serial)
                                                    .setPositiveButton("OK", null)
                                                    .show());
                                        } else {
                                            runOnUiThread(() -> Utils.showToast(Dashboard.this, "No transaction to display"));
                                        }

                                    } else {
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
        private final Context context;

        // Constructor that takes both the statements list and context
        public Statement(Context context, List<Map<String, Object>> statements) {
            this.context = context;
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

            // Set up onClick listener for each item
            holder.itemView.setOnClickListener(v -> {
                // Get the primary color defined in colors.xml
                int primaryColor = context.getResources().getColor(R.color.tertiary_color); // replace `colorPrimary` with your color's name if different
                String colorHex = String.format("#%06X", (0xFFFFFF & primaryColor)); // Convert color to hex string

                // Convert the statement map to a readable HTML string format, excluding null or empty values
                StringBuilder detailsBuilder = new StringBuilder();
                for (Map.Entry<String, Object> entry : statement.entrySet()) {
                    Object value = entry.getValue();

                    // Check if value is not null or empty, then add it to the details
                    if (value != null && !value.toString().trim().isEmpty()) {
                        // Capitalize the first letter of the key
                        String key = entry.getKey();
                        String capitalizedKey = key.substring(0, 1).toUpperCase() + key.substring(1);

                        // Append key in bold with primary color and value in normal text
                        detailsBuilder.append("<font color='").append(colorHex).append("'>")
                                .append(capitalizedKey).append(":</font> ")
                                .append(value).append("<br><br>");
                    }
                }
                String details = detailsBuilder.toString();

                // Create and display an AlertDialog showing transaction details
                new AlertDialog.Builder(context)
                        .setTitle("Transaction Details")
                        .setMessage(details.isEmpty() ? "No details available." : Html.fromHtml(details)) // Use Html.fromHtml for styled text
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()) // Dismiss the alert when OK is clicked
                        .show();
            });

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
                referenceID.setText(getValueFromMap(statement, "basketID", "N/A"));
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
                String transactionAmount = getValueFromMap(statement, amountKey, "N/A");
                transactionAmount = (transactionAmount == null || transactionAmount.isEmpty()) ? "000" : transactionAmount;
                // Format the amount for display
                String formattedAmount = Utils.FormatAmount(transactionAmount);
                amount.setText(String.format("%s%s", currencySymbol, formattedAmount));
            }

            // Utility method to safely fetch values from the map
            private String getValueFromMap(Map<String, Object> map, String key, String defaultValue) {
                Object value = map.get(key);
                return value != null ? value.toString() : defaultValue;
            }
        }
    }

    public static List<Map<String, Object>> getStatement(String AgentID, String AgentName, String AgentPassword, String AgentEmail) {
        List<Map<String, Object>> items = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject catalogRequestResponse = ApiService.statement(AgentID, AgentName, AgentPassword, AgentEmail);
//                    JSONObject catalogRequestResponse = ApiService.statement();
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

    public void spinners() {
        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();

                // Fetch products and update the UI when data is available
                getProducts(new ProductsCallback() {
                    @Override
                    public void onProductsLoaded(List<Map<String, Object>> products) {
                        List<Map<String, Object>> filteredProducts = filterProductsByType(products, selectedItem);
                        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                        jobListRecyclerView.setAdapter(new RecommendedAd(filteredProducts));

                        numItems = filteredProducts.size();
                        number_of_posts.setText(String.format("%s %s", numItems, getString(R.string.items_found)));
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection if needed
            }
        });

        ItemFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();

                // Fetch products and update the UI when data is available
                getProducts(new ProductsCallback() {
                    @Override
                    public void onProductsLoaded(List<Map<String, Object>> products) {
                        List<Map<String, Object>> filteredProducts = filterProductsByType(products, selectedItem);
                        ItemRecyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                        ItemRecyclerView.setAdapter(new RecommendedAd(filteredProducts));
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection if needed
            }
        });
    }

    // Callback interface for async product fetching
    public interface ProductsCallback {
        void onProductsLoaded(List<Map<String, Object>> products);
    }

    // Fetch products asynchronously
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Invoke the callback on the main thread after products are loaded
                new Handler(Looper.getMainLooper()).post(() -> callback.onProductsLoaded(items));
            }
        }).start();
    }

    // Filter products by selected type
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

}
