package com.example.testingmyskills.UI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


import static com.example.testingmyskills.UI.MainActivity.MSISDN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
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
import com.example.testingmyskills.JavaClasses.AlphaKeyboard;
import com.example.testingmyskills.JavaClasses.Bundles;
import com.example.testingmyskills.JavaClasses.Country;
import com.example.testingmyskills.JavaClasses.PaymentProcessor;
import com.example.testingmyskills.R;
import com.example.testingmyskills.JavaClasses.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.mailjet.client.resource.User;


import org.apache.xmlrpc.XmlRpcException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.internal.Util;


public class Dashboard extends AppCompatActivity implements BalanceResponseCallback {
    private ConstraintLayout AppFrame;
    private LinearLayout Header, Navbar, ItemsLayout, ISPsLayout, BuyLayout, EconetIsp, TelecelIsp, NetoneIsp, ZesaIsp, LoadBalanceLayout;
    private FrameLayout LogoutButton, BackToHome;
    private TextView salutationText, HeaderTitle, SelectedIsp, AvailableBalance, StatusMessage, SelectedItem, MoreBtn, ItemToBuyText, SelectedItemType, SelectedItemPrice, SelectedItemLifeTime;
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
    static String currencySymbol;
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
                SelectedItem.setText(selectedItem + " is selected");
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

    private void initialiseViews() {

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
        salutationText = findViewById(R.id.salutation_text);
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


    private void handleLoadBalance() {
        // Example usage


        // Retrieve and trim the input values
        String amount = AmountTLoad.getText().toString().trim();
        String notes = LoadingNote.getText().toString().trim();

        // Check if the amount field is empty
        if (amount.isEmpty()) {
            AmountTLoad.setError("Amount is required");
            return;
        }

        // Check if the notes field is empty
        if (notes.isEmpty()) {
            LoadingNote.setError("Notes are required");
            return;
        }


        new Thread(() -> {
            PaymentProcessor processor = new PaymentProcessor();
            String response = processor.createOrder(AmountTLoad.getText().toString(), getResources().getString(R.string.api_key));

            runOnUiThread(() -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String redirectUrl = jsonResponse.optString("redirectUrl");

                    if (redirectUrl != null && !redirectUrl.isEmpty()) {
                        openUrlWithFallback(redirectUrl);
                    } else {
                        System.out.println("Redirect URL is not available.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Failed to parse JSON response.");
                }
            });
        }).start();


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
        layoutToDisplay.setVisibility(View.VISIBLE);

    }

    private void getAccount() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("profile", Context.MODE_PRIVATE);

        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        String phone = sharedPreferences.getString("phone", "");
        String emailAddress = sharedPreferences.getString("emailAddress", "");
        String balance = sharedPreferences.getString("balance", "");
        String updated = sharedPreferences.getString("time", "");

        String formatedSalutation = "Hello " + name + " " + surname + "  Last updated " + updated;
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
        String itemToBuy = SelectedItemType.getText().toString().trim();
        String price = SelectedItemPrice.getText().toString().trim().replace(currencySymbol, "");
        String lifeTime = SelectedItemLifeTime.getText().toString().trim();

        if (phone.isEmpty()) {
            return;
        }

        if (Double.parseDouble(price) < amount) {
            // Create a descriptive toast message
            String toast = String.format("Phone: %s\nItem: %s\nPrice: %s\nLifeTime: %s\nBalance: %s",
                    phone, itemToBuy, price, lifeTime, String.valueOf(amount));
            Utils.showToast(this, toast);

            Load(phone, "load_value", this);

        } else {
            Utils.showToast(this, "Insufficient Funds");
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
            String amount, type, time, price;


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
                    SelectedItemType.setText(amount);
                    SelectedItemLifeTime.setText(time);
                    SelectedItemPrice.setText(String.format("%s%s", currencySymbol, price));

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
                price = Utils.FormatAmount(Objects.requireNonNull(jobPost.get("price")).toString());

                Type.setText(String.format(amount));
                LifeTime.setText(time);
                Price.setText(String.format("%s %s", currencySymbol, price));

            }
        }

    }


    public List<Map<String, Object>> getProducts() {

        Bundles[] bundles = Utils.readJsonFile(Dashboard.this, filePath);

        List<Map<String, Object>> items = new ArrayList<>();
        if (bundles != null) {
            for (Bundles bundle : bundles) {
                Map<String, Object> item = new HashMap<>();
                item.put("type", bundle.getCategory());
                item.put("amount", bundle.getBundle());
                String l = bundle.getValidity().contains("day") ? bundle.getValidity() : bundle.getValidity() + " days";
                item.put("lifeTime", l);
                item.put("price", bundle.getCurrentUSDCharge());
                items.add(item);

            }
        }

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
