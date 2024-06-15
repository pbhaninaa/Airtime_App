package com.example.testingmyskills;

import static com.example.testingmyskills.MainActivity.MSISDN;
import static com.example.testingmyskills.MainActivity.PASSWORD;
import static com.example.testingmyskills.MainActivity.SERVER_URL;
import static com.example.testingmyskills.MainActivity.USERNAME;
import static com.example.testingmyskills.MainActivity.econetItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.testingmyskills.Dao.ApiCalls;

import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity {
    private static ConstraintLayout dash_board_screen;
    private static ConstraintLayout BuyScreen;
    private static ConstraintLayout job_list_screen;
    private static EditText Phone, Item, Amount, ItemPrice;
    private ImageButton backFromList;
    private Button BuyBtn;
    private static TextView number_of_posts, BuyTittle;
    private Spinner filter_spinner, ItemTypeSpinner;


    private TextView salutation, selectedNet, AccountBalance, Message, Notifications;
    private ImageView profilePicture;
    private TextView moreBtn;
    private ImageButton btnHome;
    private ImageButton btnNotifications, FilterButton;
    private ImageButton btnProfile, moreItemsBtn;
    private ScrollView scrollView;


    private LinearLayout bottomNav, EconetBtn, TelnetBtn, NetoneBtn, SpecialBtn, filterSection;
    ApiCalls api = new ApiCalls(SERVER_URL, USERNAME, PASSWORD);
    private boolean show;

    public Dashboard() throws MalformedURLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        getDefaultBalance(MSISDN);
        show = true;

        Utils.hideSoftNavBar(Dashboard.this);
        initialiseViews(); // Initialize views first
        setOnclickListeners();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecommendedAd(getProducts()));

        RecyclerView jobListRecyclerView = findViewById(R.id.jobListRecyclerView);
        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobListRecyclerView.setAdapter(new RecommendedAd(getProducts()));//


        dash_board_screen.setVisibility(View.VISIBLE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, econetItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_spinner.setAdapter(adapter);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, econetItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ItemTypeSpinner.setAdapter(adapter3);


        String n = String.valueOf(getProducts().size());
        number_of_posts.setText(n + " Items found");

        ItemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected item
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedItem.equals("All")) {
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(getProducts()));
                } else {
                    // we will call a function wil param,s
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection if needed
            }
        });
        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected item
                String selectedItem = parentView.getItemAtPosition(position).toString();

                if (selectedItem.equals("All")) {
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
                    recyclerView.setAdapter(new RecommendedAd(getProducts()));
                } else {
                    // we will call a function wil param,s
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no selection if needed
            }
        });
        btnHome.setColorFilter(ContextCompat.getColor(this, R.color.tertiary_color), PorterDuff.Mode.SRC_IN);
        getProfile();
    }

    private void initialiseViews() {
        dash_board_screen = findViewById(R.id.dash_board_screen);
        salutation = findViewById(R.id.user_salutation);
        profilePicture = findViewById(R.id.user_profile_picture);
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
        Notifications = findViewById(R.id.notifications);
        FilterButton = findViewById(R.id.filter_button);
        filterSection = findViewById(R.id.filter_section);
        scrollView = findViewById(R.id.scroll_view);

    }

    private void setOnclickListeners() {
        moreBtn.setOnClickListener(v -> handleShowMore());
        moreItemsBtn.setOnClickListener(v -> hideOtherLayout(R.id.Job_list_screen, moreItemsBtn));
        btnHome.setOnClickListener(v -> hideOtherLayout(R.id.dash_board_screen, btnHome));
        btnProfile.setOnClickListener(v -> hideOtherLayout(R.id.create_profile_screen, btnProfile));
        btnNotifications.setOnClickListener(v -> hideOtherLayout(R.id.buy_screen, btnNotifications));
        backFromList.setOnClickListener(v -> handleBackFromList());
        BuyBtn.setOnClickListener(v -> handleTransaction());
        NetoneBtn.setOnClickListener(v -> getNetoneBalance(MSISDN));
        TelnetBtn.setOnClickListener(v -> getTelnetBalance(MSISDN));
        EconetBtn.setOnClickListener(v -> getEconetBalance(MSISDN));
        SpecialBtn.setOnClickListener(v -> getSpecials());
        FilterButton.setOnClickListener(v -> hideFilter());
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
        int notifications = 35;
        // Check if notifications count is less than 1
        if (notifications < 1) {
            Notifications.setVisibility(View.GONE); // Hide the Notifications view
        } else {
            Notifications.setVisibility(View.VISIBLE); // Ensure the Notifications view is visible
        }

// Set the text of Notifications
        Notifications.setText(String.valueOf(notifications));
        SharedPreferences prefs = this.getSharedPreferences("profile", Context.MODE_PRIVATE);

        // Populate EditText fields
        String name = prefs.getString("name", "");
        String phoneNumber = name + "(" + prefs.getString("phone", "") + ")";

        String salutation = "Hello " + phoneNumber;
        String updateTime = "last updated a minute ago";

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
    }

    private void getSpecials() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecommendedAd(getProducts()));
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

    private void getDefaultBalance(String number) {
        new Thread(() -> {
            try {
                Map<String, Object> response = api.accountBalanceEnquiry(number);
//
                // Handle the response
                selectedNet.setText("Selected Network: Econet");
                int balance = (int) response.get("Amount");
                AccountBalance.setText(("Current Balance " + response.get("Amount")));
                if (balance < 1000) {
                    Message.setText("You need to recharge.");
                } else if (balance >= 1000 && balance < 5000) {
                    Message.setText("Your balance is low. Consider recharging soon.");
                } else if (balance >= 5000 && balance < 10000) {
                    Message.setText("Your balance is sufficient.");
                } else {
                    Message.setText("Your balance is high.");
                }
            } catch (XmlRpcException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleTransaction() {
        String phone = Phone.getText().toString().trim();
        String ItemType = Item.getText().toString().trim();
        String ItemAmount = Amount.getText().toString().trim();
        String Price = ItemPrice.getText().toString().trim();
        String s = "You are attempting to buy" + "\n" +
                ItemAmount + " " +
                ItemType + "\n for " +
                phone + "\n";
//        Utils.showToast(this, s);
        clearFields();

        new Thread(() -> {
            try {
                Map<String, Object> response = api.loadValue(MSISDN, 10, "load airtime test XMLRPC HM 1", 840);
                if ((int) response.get("Status") == 1) {
                    runOnUiThread(() -> Utils.success(this, String.valueOf(response.get("Description"))));
                } else if ((int) response.get("Status") == 0) {
                    runOnUiThread(() -> Utils.showToast(this, String.valueOf(response.get("Description"))));
                }
            } catch (XmlRpcException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void hideOtherLayout(int layoutToShow, ImageButton icon) {
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
            ConstraintLayout l = findViewById(layoutToShow);
            l.setVisibility(View.VISIBLE);
        }
    }

    public static class RecommendedAd extends RecyclerView.Adapter<RecommendedAd.ViewHolder> {

        private List<Map<String, Object>> jobPosts;

        public RecommendedAd(List<Map<String, Object>> jobPosts) {
            this.jobPosts = jobPosts;
        }

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

        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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


                    BuyTittle.setText(Type.getText().toString() + "\n that last for " + time);
                    Item.setText(type);
                    Amount.setText(amount);

                    BuyScreen.setVisibility(View.VISIBLE);
                    dash_board_screen.setVisibility(View.GONE);
                    job_list_screen.setVisibility(View.GONE);

                }
            }

            public void bind(Map<String, Object> jobPost) {
                amount = jobPost.get("amount").toString();
                type = jobPost.get("type").toString();
                time = jobPost.get("lifeTime").toString();
                price = jobPost.get("price").toString();

                Type.setText(amount + " " + type);
                LifeTime.setText(time);
                Price.setText(price);


            }
        }

    }

    public static List<Map<String, Object>> getProducts() {
        List<Map<String, Object>> Items = new ArrayList<>();

        // Add 10 different items
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("type", " GB MTN Data");
            item.put("amount", i * 10);
            item.put("lifeTime", i * 3 + " Days");
            item.put("price", "R " + (i * 50 + 10.00));
            Items.add(item);
        }

        return Items;
    }

    private void handleBackFromList() {
        bottomNav.setVisibility(View.VISIBLE);
        job_list_screen.setVisibility(View.GONE);
        dash_board_screen.setVisibility(View.VISIBLE);
    }

    private void handleShowMore() {
        dash_board_screen.setVisibility(View.GONE);
        BuyScreen.setVisibility(View.GONE);
        job_list_screen.setVisibility(View.VISIBLE);
    }

    private void defaultColoring(ImageButton icon) {
        btnNotifications.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        btnProfile.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        btnHome.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        moreItemsBtn.setColorFilter(ContextCompat.getColor(this, R.color.primary_color), PorterDuff.Mode.SRC_IN);
        icon.setColorFilter(ContextCompat.getColor(this, R.color.tertiary_color), PorterDuff.Mode.SRC_IN);
    }

    private void clearFields() {
        Phone.setText("");
        Item.setText("");
        Amount.setText("");
        ItemPrice.setText("");
    }
}
