package com.example.testingmyskills.UI;

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

import com.example.testingmyskills.Dao.XMLRPCClient;
import com.example.testingmyskills.Interfaces.BalanceResponseCallback;
import com.example.testingmyskills.JavaClasses.AlphaKeyboard;
import com.example.testingmyskills.JavaClasses.Bundles;
import com.example.testingmyskills.R;
import com.example.testingmyskills.JavaClasses.Utils;
import com.fasterxml.jackson.databind.JsonNode;


import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Dashboard extends AppCompatActivity implements BalanceResponseCallback {
    private ConstraintLayout dash_board_screen, landingScreen;
    private ConstraintLayout BuyScreen;
    private ConstraintLayout ConfirmationScreen;
    private ConstraintLayout job_list_screen;
    private EditText Phone, Item, Amount, ItemPrice;
    private ImageButton backFromList;
    private Button BuyBtn, Yes, No;
    private TextView number_of_posts, BuyTittle;
    private Spinner filter_spinner, ItemTypeSpinner, ItemTypeSpinner2, SelectedCurrency;


    private TextView selectedNet, AccountBalance, Message;

    private TextView moreBtn;
    private ImageButton btnHome;
    private ImageButton btnNotifications;
    private ImageButton FilterButton;
    private ImageButton btnProfile, moreItemsBtn, BackToISPs;
    private ScrollView scrollView;
    private FrameLayout LogoutBtn, LogoutBtn1, BackToISPsLayout;
    private LinearLayout bottomNav, EconetBtn, TelnetBtn, NetoneBtn, SpecialBtn, filterSection;
    //    ApiCalls api = new ApiCalls();
    private boolean show;
    //    private AlphaKeyboard MyKeyboard;
//    private Button hideKeyboardBtn;
    static String currencySymbol;
    private String ItemToBuy;
    public static String MSISDN;
    private TextView selectedCategory;
    private boolean getSelectedCategory;
    private RecyclerView ISPRecyclerView, DataRecyclerView, jobListRecyclerView;
    private String filePath = "JSON.json";
    private int numItems;

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
//        setupFocusListeners();
        setOnclickListeners();

        recyclerViews();
        adaptors();


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        currencySymbol = sharedPreferences.getString("currency_symbol", getString(R.string.default_currency_symbol));


        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected item
                String selectedItem = parentView.getItemAtPosition(position).toString();
                selectedCategory.setText(selectedItem);
                if (selectedItem.equals("All")) {
                    RecyclerView recyclerView = findViewById(R.id.jobListRecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(getProducts()));
                    numItems = getProducts().size();
                } else if (selectedItem.equals("Airtime")) {
                    RecyclerView recyclerView = findViewById(R.id.jobListRecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(filterProductsByType(getProducts(), "Airtime")));
                    numItems = filterProductsByType(getProducts(), "Airtime").size();
                } else {

                    RecyclerView recyclerView = findViewById(R.id.jobListRecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(filterProductsByType(getProducts(), selectedItem)));
                    numItems = filterProductsByType(getProducts(), selectedItem).size();
                }
                number_of_posts.setText(String.format("%s%s", numItems, getString(R.string.items_found)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection if needed
            }
        });
        ItemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected item
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedItem.equals("All")) {
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(getProducts()));
                } else if (selectedItem.equals("Airtime")) {
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(filterProductsByType(getProducts(), "Airtime")));
                } else {
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(filterProductsByType(getProducts(), selectedItem)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection if needed
            }
        });
        ItemTypeSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected item
                String a = parentView.getItemAtPosition(position).toString();

                if (a.equals("All")) {
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(getProducts()));
                } else if (a.equals("Airtime")) {
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(filterProductsByType(getProducts(), "Airtime")));
                } else {
                    ItemToBuy = a;
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(filterProductsByType(getProducts(), a)));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection if needed
            }
        });
        SelectedCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected item (currency symbol)
                String selectedCurrencySymbol = parentView.getItemAtPosition(position).toString();
                // Update the currency symbol string resource
                updateCurrencySymbol(selectedCurrencySymbol);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                recyclerView.setAdapter(new RecommendedAd(getProducts()));
//                recyclerViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection if needed
            }
        });

        BackToISPs.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);

        // Load screen based on the constraintLayoutId received from Intent
        int constraintLayoutId = getIntent().getIntExtra("constraintLayoutId", R.id.landing_page1);
        ConstraintLayout layout = findViewById(constraintLayoutId);
        if (constraintLayoutId == R.id.dash_board_screen) {
            dash_board_screen.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
            bottomNav.setVisibility(View.GONE);
            dash_board_screen.setVisibility(getSelectedCategory ? View.VISIBLE : View.GONE);
        }


    }

    @Override
    public void onBalanceReceived(Map<String, Object> response) {
        try {
            String amount = Objects.requireNonNull(response.get("Amount")).toString();
            String Balance = amount.isEmpty() ? "No Balance to display" : (String.format("Account Balance %s%s", currencySymbol, Utils.FormatAmount(amount)));

            AccountBalance.setText(Balance);

            double balance = Double.parseDouble(amount);

            Utils.setMessage(this, balance, Message);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        System.out.println("Dash Res: " + response);
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

    private void adaptors() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, MainActivity.econetItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_spinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_item, MainActivity.econetItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ItemTypeSpinner2.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, R.layout.spinner_item, MainActivity.econetItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ItemTypeSpinner.setAdapter(adapter3);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, R.layout.spinner_item, MainActivity.Currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SelectedCurrency.setAdapter(adapter4);


    }

    private void recyclerViews() {

        DataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DataRecyclerView.setAdapter(new RecommendedAd(getProducts()));

        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobListRecyclerView.setAdapter(new RecommendedAd(getProducts()));

        ISPRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ISPRecyclerView.setAdapter(new ISP(this, MainActivity.ISPs()));//

    }

    private void updateCurrencySymbol(String newCurrencySymbol) {
        // Update the currency symbol string resource
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currency_symbol", newCurrencySymbol);
        editor.apply();

        // Notify any parts of your app that need to react to this change
        // For example, update UI elements or perform calculations using the new symbol
    }

    private void initialiseViews() {

//        MyKeyboard = new AlphaKeyboard(this);
//        hideKeyboardBtn = MyKeyboard.findViewById(R.id.button_enter);
        dash_board_screen = findViewById(R.id.dash_board_screen);
        moreBtn = findViewById(R.id.show_more_posts);
        btnHome = findViewById(R.id.nav_dash_board_btn);
        btnNotifications = findViewById(R.id.nav_notifications_btn);
        btnProfile = findViewById(R.id.nav_profile_btn);
        bottomNav = findViewById(R.id.bottom_nav);
        job_list_screen = findViewById(R.id.Job_list_screen);
        number_of_posts = findViewById(R.id.number_of_posts);
        filter_spinner = findViewById(R.id.filter_spinner);
        backFromList = findViewById(R.id.back_btn_from_job_list);
        BuyScreen = findViewById(R.id.buy_screen);
        BackToISPs = findViewById(R.id.nav_networks_btn);
        BuyTittle = findViewById(R.id.buy_tittle);
        BuyBtn = findViewById(R.id.buy);
        Phone = findViewById(R.id.phone_number);
        Item = findViewById(R.id.item_type);
        Amount = findViewById(R.id.item_amount);
        ItemPrice = findViewById(R.id.price);
        selectedNet = findViewById(R.id.selected_network);
        AccountBalance = findViewById(R.id.balance);
        Message = findViewById(R.id.message);
        EconetBtn = findViewById(R.id.EconetBtn);
        TelnetBtn = findViewById(R.id.TelnetBtn);
        NetoneBtn = findViewById(R.id.NetoneBtn);
        ItemTypeSpinner = findViewById(R.id.item_type_spinner);
        SpecialBtn = findViewById(R.id.specials);
        moreItemsBtn = findViewById(R.id.more);
        FilterButton = findViewById(R.id.filter_button);
        filterSection = findViewById(R.id.filter_section);
        scrollView = findViewById(R.id.scroll_view);
        LogoutBtn = findViewById(R.id.profile_picture_container);
        LogoutBtn1 = findViewById(R.id.profile_picture_container1);
        ConfirmationScreen = findViewById(R.id.confirmation_screen);
        Yes = findViewById(R.id.yes);
        No = findViewById(R.id.no);
        ItemTypeSpinner2 = findViewById(R.id.item_type_spinner1);
        SelectedCurrency = findViewById(R.id.currency_type_spinner);
        selectedCategory = findViewById(R.id.SelectedCategory);

        ISPRecyclerView = findViewById(R.id.CategoryRecyclerView);
        DataRecyclerView = findViewById(R.id.recyclerView);
        jobListRecyclerView = findViewById(R.id.jobListRecyclerView);
        landingScreen = findViewById(R.id.landing_page1);
        BackToISPsLayout = findViewById(R.id.back_to_ISPs);

    }

    private void setOnclickListeners() {
        moreBtn.setOnClickListener(v -> handleShowMore());
        moreItemsBtn.setOnClickListener(v -> hideOtherLayout(R.id.Job_list_screen, moreItemsBtn));
        btnHome.setOnClickListener(v -> hideOtherLayout(R.id.dash_board_screen, btnHome));
        btnProfile.setOnClickListener(v -> hideOtherLayout(R.id.create_profile_screen, btnProfile));
        btnNotifications.setOnClickListener(v -> hideOtherLayout(R.id.buy_screen, btnNotifications));
        BackToISPs.setOnClickListener(v -> hideOtherLayout(R.id.landing_page1, BackToISPs));
        BackToISPsLayout.setOnClickListener(v -> hideOtherLayout(R.id.landing_page1, BackToISPs));
        backFromList.setOnClickListener(v -> handleBackFromList());
        BuyBtn.setOnClickListener(v -> handleTransaction());
        NetoneBtn.setOnClickListener(v -> getNetoneBalance(MSISDN));
        TelnetBtn.setOnClickListener(v -> getTelnetBalance(MSISDN));
        EconetBtn.setOnClickListener(v -> getEconetBalance(MSISDN));
        SpecialBtn.setOnClickListener(v -> getSpecials());
        FilterButton.setOnClickListener(v -> hideFilter());
//        hideKeyboardBtn.setOnClickListener(v -> hideKeyboard());
        LogoutBtn.setOnClickListener(v -> logout());
        LogoutBtn1.setOnClickListener(v -> logout());
        No.setOnClickListener(v -> handleNo());
        Yes.setOnClickListener(v -> handleYes());
    }

    private void logout() {
        dash_board_screen.setVisibility(View.GONE);
        ConfirmationScreen.setVisibility(View.VISIBLE);
        bottomNav.setVisibility(View.GONE);
    }

    private void handleNo() {
        dash_board_screen.setVisibility(View.VISIBLE);
        landingScreen.setVisibility(View.GONE);
        bottomNav.setVisibility(View.VISIBLE);
        ConfirmationScreen.setVisibility(View.GONE);
    }

    private void handleYes() {
//          Utils.logout(this);
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

        // Display the formatted text in a TextView
        TextView textView = findViewById(R.id.user_salutation);
        textView.setText(builder);
        TextView salute = findViewById(R.id.user_salutation1);
        salute.setText(builder);
    }

    private void getSpecials() {
        DataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DataRecyclerView.setAdapter(new RecommendedAd(getProducts()));
    }

    private void getEconetBalance(String msisdn) {
        selectedNet.setText("Selected Network: Econet");
    }

    private void getTelnetBalance(String msisdn) {
        selectedNet.setText("Selected Network: Telnet");

    }

    private void getNetoneBalance(String msisdn) {
        selectedNet.setText("Selected Network: Netone");
    }

    private void handleTransaction() {
        String phone = Phone.getText().toString().trim();
        String ItemType = Item.getText().toString().trim();
        String ItemAmount = Amount.getText().toString().trim();
        String Price = ItemPrice.getText().toString().trim();
        if (phone.isEmpty() || ItemAmount.isEmpty() || Price.isEmpty()) {
            return;
        }
        if (!ItemToBuy.isEmpty() || !ItemType.isEmpty()) {
//            Utils.hideAlphaKeyboard(MyKeyboard);
            Load(phone, "load_value", this);
        } else {
            Utils.showToast(this, "Select Item");
        }
    }

    public void hideOtherLayout(int layoutToShow, ImageButton icon) {
        if (layoutToShow == R.id.buy_screen) {
            Item.setVisibility(View.GONE);
            ItemTypeSpinner2.setVisibility(View.VISIBLE);
            landingScreen.setVisibility(View.GONE);
//            Utils.showAlphaKeyboard(MyKeyboard, this, Gravity.BOTTOM);
//            Phone.setShowSoftInputOnFocus(false);
//            Phone.setTextIsSelectable(true);
//            InputConnection ic = Phone.onCreateInputConnection(new EditorInfo());
//            MyKeyboard.setInputConnection(ic);
        }

        if (layoutToShow == R.id.create_profile_screen) {
            Intent intent = new Intent(this, UserManagement.class);
            intent.putExtra("constraintLayoutId", layoutToShow);
            startActivity(intent);

        } else {
            defaultColoring(icon);
            clearFields();
            dash_board_screen.setVisibility(View.GONE);
            job_list_screen.setVisibility(View.GONE);
            BuyScreen.setVisibility(View.GONE);
            landingScreen.setVisibility(View.GONE);
            ConstraintLayout l = findViewById(layoutToShow);
            l.setVisibility(View.VISIBLE);
        }
    }

//    private void setupFocusListeners() {
//        Phone.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                MyKeyboard.setInputConnection(Phone.onCreateInputConnection(new EditorInfo()));
//                Phone.setShowSoftInputOnFocus(false);
//                Utils.showAlphaKeyboard(MyKeyboard, this, Gravity.BOTTOM);
//            }
//        });
//
//        ItemPrice.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                MyKeyboard.setInputConnection(ItemPrice.onCreateInputConnection(new EditorInfo()));
//                ItemPrice.setShowSoftInputOnFocus(false);
//                Utils.showAlphaKeyboard(MyKeyboard, this, Gravity.BOTTOM);
//            }
//        });
//        Amount.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                MyKeyboard.setInputConnection(Amount.onCreateInputConnection(new EditorInfo()));
//                Amount.setShowSoftInputOnFocus(false);
//                Utils.showAlphaKeyboard(MyKeyboard, this, Gravity.BOTTOM);
//            }
//        });
//        Item.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                MyKeyboard.setInputConnection(Item.onCreateInputConnection(new EditorInfo()));
//                Item.setShowSoftInputOnFocus(false);
//                Utils.showAlphaKeyboard(MyKeyboard, this, Gravity.BOTTOM);
//            }
//        });
//    }

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
                    hideOtherLayout(R.id.buy_screen, btnNotifications);
                    BuyTittle.setText(String.format("%s\n that last for %s", Type.getText().toString(), time));
                    Item.setText(type);
                    Amount.setText(amount);
                    ItemPrice.setText(String.format("%s%s", currencySymbol, price));
                    ItemTypeSpinner2.setVisibility(View.GONE);
                    Item.setVisibility(View.VISIBLE);

                }
            }

            public void bind(Map<String, Object> jobPost) {
                amount = Objects.requireNonNull(jobPost.get("amount")).toString();
                type = Objects.requireNonNull(jobPost.get("type")).toString();
                time = Objects.requireNonNull(jobPost.get("lifeTime")).toString();
                price = Utils.FormatAmount(Objects.requireNonNull(jobPost.get("price")).toString());

                Type.setText(String.format("%s %s", amount, type));
                LifeTime.setText(time);
                Price.setText(String.format("%s %s", currencySymbol, price));

            }
        }

    }

    public class ISP extends RecyclerView.Adapter<ISP.ViewHolder> {
        Context context;

        private String[] ispNames;

        public ISP(Context context, String[] ispNames) {
            this.context = context;
            this.ispNames = ispNames;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.isp_category_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String ispName = ispNames[position];
            // Set the ISP name to the TextView
            holder.name.setText(ispName);

            // Set background color and icon based on ISP name
            int backgroundColor1;
            switch (ispName) {
                case "Econet":
                    backgroundColor1 = ContextCompat.getColor(context, R.color.white);
                    holder.icon.setImageResource(R.drawable.econet_icon);
                    break;
                case "Telecel":
                    backgroundColor1 = ContextCompat.getColor(context, R.color.white);
                    holder.icon.setImageResource(R.drawable.telnet_icon);
                    break;
                case "Netone":
                    backgroundColor1 = ContextCompat.getColor(context, R.color.white);
                    holder.icon.setImageResource(R.drawable.netone_icon_one);
                    break;
                case "Electricity":
                    backgroundColor1 = ContextCompat.getColor(context, R.color.white);
                    holder.icon.setImageResource(R.drawable.zesa_icon);
                    break;
                default:
                    // Set default colors or handle other cases as needed
                    backgroundColor1 = ContextCompat.getColor(context, R.color.white);
                    break;
            }

            // Set background tint color to the views
            holder.itemView.setBackgroundTintList(ColorStateList.valueOf(backgroundColor1));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add your desired action here
                    // Example: show the dashboard screen and hide the landing screen
                    landingScreen.setVisibility(View.GONE);
                    dash_board_screen.setVisibility(View.VISIBLE);
                    defaultColoring(btnHome);
                    bottomNav.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return ispNames.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView icon;
            TextView name;

            public ViewHolder(View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.isp_icon);
                name = itemView.findViewById(R.id.isp_name);
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
                item.put("amount", bundle.getVolumeMB());
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
        bottomNav.setVisibility(View.VISIBLE);
        job_list_screen.setVisibility(View.GONE);
        dash_board_screen.setVisibility(View.VISIBLE);
        landingScreen.setVisibility(View.GONE);
    }

    private void handleShowMore() {
        dash_board_screen.setVisibility(View.GONE);
        BuyScreen.setVisibility(View.GONE);
        job_list_screen.setVisibility(View.VISIBLE);
        landingScreen.setVisibility(View.GONE);
    }

    private void defaultColoring(ImageButton icon) {
        btnNotifications.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        btnProfile.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        btnHome.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        moreItemsBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        BackToISPs.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        icon.setColorFilter(ContextCompat.getColor(this, R.color.gold_yellow), PorterDuff.Mode.SRC_IN);
    }

//    private void hideKeyboard() {
//        Utils.hideAlphaKeyboard(MyKeyboard);
//    }

    private void clearFields() {
        Phone.setText("");
        Item.setText("");
        Amount.setText("");
        ItemPrice.setText("");
    }

}
