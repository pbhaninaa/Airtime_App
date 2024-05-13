package com.example.testingmyskills;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.textservice.TextInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobsActivity extends AppCompatActivity {
    private ConstraintLayout dash_board_screen;
    private ConstraintLayout job_list_screen;
    private EditText text_to_search;
    private ImageButton search_btn;
    private ImageButton backFromList;
    private TextView number_of_posts;
    private Spinner filter_spinner;


    private TextView salutation;
    private ImageView profilePicture;
    private ProgressBar progressBar;
    private TextView progressBarLabel;
    private TextView CompletionLabel;
    private ImageButton CompleteProfileButton;
    private TextView moreBtn;
    private ImageButton btnHome;
    private ImageButton btnApplications;
    private ImageButton btnNotifications;
    private ImageButton btnProfile;

    private TextView Interviews;
    private TextView Applications;
    private LinearLayout bottomNav;
    private LinearLayout baseLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        Utils.hideSoftNavBar(JobsActivity.this);
        initialiseViews(); // Initialize views first
        displayTexts(); // Then display texts
        setOnclickListeners();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new JobsAdapter(getJobPosts()));

        RecyclerView jobListRecyclerView = findViewById(R.id.jobListRecyclerView);
        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobListRecyclerView.setAdapter(new JobsListAdapter(getJobPosts()));

        // Load screen based on the constraintLayoutId received from Intent
        int constraintLayoutId = getIntent().getIntExtra("constraintLayoutId", R.id.dash_board_screen);
        screenToLoad(constraintLayoutId);
        dash_board_screen.setVisibility(View.VISIBLE);
    }

    private void initialiseViews() {
        dash_board_screen = findViewById(R.id.dash_board_screen);
        salutation = findViewById(R.id.user_salutation);
        profilePicture = findViewById(R.id.user_profile_picture);
        progressBar = findViewById(R.id.progressBar);
        progressBarLabel = findViewById(R.id.progressLabel);
        CompletionLabel = findViewById(R.id.comple_profile_label);
        CompleteProfileButton = findViewById(R.id.complete_profile_btn);
        moreBtn = findViewById(R.id.show_more_posts);
        btnHome = findViewById(R.id.nav_dash_board_btn);
        btnApplications = findViewById(R.id.nav_applications_btn);
        btnNotifications = findViewById(R.id.nav_notifications_btn);
        btnProfile = findViewById(R.id.nav_profile_btn);
        Interviews = findViewById(R.id.number_of_interviews);
        Applications = findViewById(R.id.number_of_applications);
        bottomNav = findViewById(R.id.bottom_nav);
        baseLine = findViewById(R.id.base_line);

        job_list_screen = findViewById(R.id.Job_list_screen);
        text_to_search = findViewById(R.id.text_to_search);
        search_btn = findViewById(R.id.search_btn);
        number_of_posts = findViewById(R.id.number_of_posts);
        filter_spinner = findViewById(R.id.filter_spinner);
        backFromList = findViewById(R.id.back_btn_from_job_list);

    }

    @SuppressLint("NonConstantResourceId")
    private void screenToLoad(int screenToLoad) {
//        switch (screenToLoad) {
//            case R.id.dash_board_screen:
//                // Load the dashboard screen
//                ConstraintLayout dashboardLayout = findViewById(R.id.dash_board_screen);
//                dashboardLayout.setVisibility(View.VISIBLE);
//                break; case R.id.Job_list_screen:
//                // Load the dashboard screen
//                ConstraintLayout dashboardLayout1 = findViewById(R.id.dash_board_screen);
//                dashboardLayout1.setVisibility(View.VISIBLE);
//                break;
//            default:
//                // Load a default screen if screenToLoad doesn't match any predefined cases
//                ConstraintLayout defaultLayout = findViewById(R.id.dash_board_screen);
//                dash_board_screen.setVisibility(View.VISIBLE);
//                break;
//        }
    }

    private void setOnclickListeners() {
        CompleteProfileButton.setOnClickListener(v -> handleCompleteProfile());
        moreBtn.setOnClickListener(v -> handleShowMore());
        btnHome.setOnClickListener(v -> hideOtherLayout(1, btnHome));
        btnProfile.setOnClickListener(v -> hideOtherLayout(1, btnProfile));
        btnApplications.setOnClickListener(v -> hideOtherLayout(1, btnApplications));
        btnNotifications.setOnClickListener(v -> hideOtherLayout(1, btnNotifications));
        backFromList.setOnClickListener(v -> handleBackFromList());
        search_btn.setOnClickListener(v -> handleSearchBtn());
    }

    // ===================================================================== Home page ======================================================================
    private void displayTexts() {
        // ===================================================================== Home page ==================================================================
        // Ensure all TextViews are initialized before setting text
        if (salutation != null && progressBarLabel != null && progressBar != null
                && Interviews != null && Applications != null && CompletionLabel != null) {
            salutation.setText("hello, " + getUserProfile().get("firstName"));
            progressBarLabel.setText(getUserProfile().get("profileCompletionPercentage").toString() + "%");
            progressBar.setProgress(Integer.parseInt(getUserProfile().get("profileCompletionPercentage").toString()));
            Interviews.setText(getUserProfile().get("interviews").toString());
            Applications.setText(getUserProfile().get("applications").toString());


            int profilePictureResource = R.drawable.image; // Default profile picture resource ID
            Object profilePictureObject = getUserProfile().get("profilePicture");

            if (profilePictureObject != null && profilePictureObject instanceof Integer) {
                profilePictureResource = (int) profilePictureObject;
            }

            profilePicture.setImageResource(profilePictureResource);

            profilePicture.requestLayout(); // Apply the changes
            profilePicture.setClipToOutline(true); // Set to true to clip the corners
            profilePicture.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20); // Set corner radius to 20
                }
            });


            if (Integer.parseInt(getUserProfile().get("profileCompletionPercentage").toString()) < 100) {
                CompletionLabel.setText("Complete your\n profile");
            } else {
                CompletionLabel.setText("Profile Completed");
            }
        }

        // ===================================================================== List Jobs page =============================================================
        String[] values = {"Mostly searched", "Most recent", "Least recent",};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_spinner.setAdapter(adapter);
        String n = String.valueOf(getJobPosts().size());
        number_of_posts.setText(n + " Posts found");


    }

    private void hideOtherLayout(int layoutToShow, ImageButton icon) {
        defaultColoring(icon);

        Utils.showToast(JobsActivity.this, "Not yet done");
    }

    private void handleShowMore() {
        bottomNav.setVisibility(View.GONE);
        dash_board_screen.setVisibility(View.GONE);
        job_list_screen.setVisibility(View.VISIBLE);
    }

    private void handleCompleteProfile() {
        Utils.showToast(JobsActivity.this, "Not yet done");
    }

    public static class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

        private List<Map<String, Object>> jobPosts;

        public JobsAdapter(List<Map<String, Object>> jobPosts) {
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
            TextView jobTitle;
            TextView location;
            TextView jobType;
            TextView salary;
            TextView ratings;
            ImageView companyIcon;
            LinearLayout RatingsDisplay;
            String description, companyName;

            public ViewHolder(View itemView) {
                super(itemView);
                jobTitle = itemView.findViewById(R.id.post_job_title);
                location = itemView.findViewById(R.id.post_job_location);
                jobType = itemView.findViewById(R.id.post_job_type);
                salary = itemView.findViewById(R.id.post_job_salary);
                ratings = itemView.findViewById(R.id.post_company_ratings);
                companyIcon = itemView.findViewById(R.id.post_company_icon);
                RatingsDisplay = itemView.findViewById(R.id.post_ratings_layout);

                // Set click listener
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Show position in a toast
//                    jobPost1.put("jobTitle", "Software Developer");
//                    jobPost1.put("companyName", "Reverside/Geeks4learning");
//                    jobPost1.put("location", "Sandton");
//                    jobPost1.put("jobType", "Part Time");
//                    jobPost1.put("salary", "R80 000.00/Monthly");
//                    jobPost1.put("ratings", 5);
//                    jobPost1.put("companyIcon", R.drawable.baseline_work_icon);
//                    jobPost1.put("description", "Manthul Inc. is seeking a talented Graphic Designer to join our creative team in London. The ideal candidate should have a strong portfolio showcasing their design skills and proficiency in Adobe Creative Suite. Responsibilities include designing marketing materials, branding assets, and digital graphics. We offer competitive salary, benefits, and a collaborative work environment. If you're passionate about design and want to work with a dynamic team, apply now!");
//                    jobPosts.add(jobPost1);
                    Context context = v.getContext();
                    String s = companyName + "\n" +
                            jobTitle.getText().toString() + "\n" +
                            jobType.getText().toString() + "\n" +
                            salary.getText().toString()+"\n"+
                            location.getText().toString() + "\n" +
                            description;
                    Utils.showToast(context, s);
                }
            }

            public void bind(Map<String, Object> jobPost) {
                jobTitle.setText(jobPost.get("jobTitle").toString());
                location.setText(jobPost.get("location").toString());
                jobType.setText(jobPost.get("jobType").toString());
                salary.setText(jobPost.get("salary").toString());
                String ratingValue = String.valueOf(jobPost.get("ratings"));
                if (ratingValue == null || ratingValue.isEmpty()) {
                    RatingsDisplay.setVisibility(View.GONE);
                } else {
                    ratings.setText(ratingValue);
                    RatingsDisplay.setVisibility(View.VISIBLE);
                }
                // Set the size of the companyIcon ImageView
                companyIcon.getLayoutParams().width = 40; // Set width to 40 pixels
                companyIcon.getLayoutParams().height = 40; // Set height to 40 pixels
                companyIcon.requestLayout(); // Apply the changes
                companyIcon.setImageResource((int) jobPost.get("companyIcon"));
                description = jobPost.get("description").toString();
                companyName = jobPost.get("companyName").toString();
            }
        }

    }

    private void defaultColoring(ImageButton icon) {
        btnNotifications.setColorFilter(ContextCompat.getColor(this, R.color.secondary_color), PorterDuff.Mode.SRC_IN);
        btnApplications.setColorFilter(ContextCompat.getColor(this, R.color.secondary_color), PorterDuff.Mode.SRC_IN);
        btnProfile.setColorFilter(ContextCompat.getColor(this, R.color.secondary_color), PorterDuff.Mode.SRC_IN);
        btnHome.setColorFilter(ContextCompat.getColor(this, R.color.secondary_color), PorterDuff.Mode.SRC_IN);
        icon.setColorFilter(ContextCompat.getColor(this, R.color.dark_magenta), PorterDuff.Mode.SRC_IN);
    }

    public static List<Map<String, Object>> getJobPosts() {
        List<Map<String, Object>> jobPosts = new ArrayList<>();

        // Job Post 1
        Map<String, Object> jobPost1 = new HashMap<>();
        jobPost1.put("jobTitle", "Software Developer");
        jobPost1.put("companyName", "Reverside/Geeks4learning");
        jobPost1.put("location", "Sandton");
        jobPost1.put("jobType", "Part Time");
        jobPost1.put("salary", "R80 000.00/Monthly");
        jobPost1.put("ratings", 5);
        jobPost1.put("companyIcon", R.drawable.baseline_work_icon);
        jobPost1.put("description", "Manthul Inc. is seeking a talented Graphic Designer to join our creative team in London. The ideal candidate should have a strong portfolio showcasing their design skills and proficiency in Adobe Creative Suite. Responsibilities include designing marketing materials, branding assets, and digital graphics. We offer competitive salary, benefits, and a collaborative work environment. If you're passionate about design and want to work with a dynamic team, apply now!");
        jobPosts.add(jobPost1);

        // Job Post 2
        Map<String, Object> jobPost2 = new HashMap<>();
        jobPost2.put("jobTitle", "Software Developer (Front End Developer)");
        jobPost2.put("companyName", "Confluence");
        jobPost2.put("location", "Hurlingham");
        jobPost2.put("jobType", "Remote");
        jobPost2.put("salary", "R90 000.00/Monthly");
        jobPost2.put("ratings", 5);
        jobPost2.put("companyIcon", R.drawable.image);
        jobPost2.put("description", "Manthul Inc. is seeking a talented Graphic Designer to join our creative team in London. The ideal candidate should have a strong portfolio showcasing their design skills and proficiency in Adobe Creative Suite. Responsibilities include designing marketing materials, branding assets, and digital graphics. We offer competitive salary, benefits, and a collaborative work environment. If you're passionate about design and want to work with a dynamic team, apply now!");
        jobPosts.add(jobPost2);
        // Job Post 3
        Map<String, Object> jobPost3 = new HashMap<>();
        jobPost3.put("jobTitle", "Data Scientist");
        jobPost3.put("companyName", "TechSolutions");
        jobPost3.put("location", "Johannesburg");
        jobPost3.put("jobType", "Full Time");
        jobPost3.put("salary", "R100 000.00/Monthly");
        jobPost3.put("ratings", 4.8);
        jobPost3.put("companyIcon", R.drawable.image);
        jobPost3.put("description", "TechSolutions is seeking a skilled Data Scientist to join our team in Johannesburg. The ideal candidate should have expertise in data analysis, machine learning, and statistical modeling. Responsibilities include developing predictive models, analyzing large datasets, and communicating insights to stakeholders. We offer competitive salary, benefits, and opportunities for professional growth. If you're passionate about data and want to make an impact, apply now!");
        jobPosts.add(jobPost3);

        // Job Post 4
        Map<String, Object> jobPost4 = new HashMap<>();
        jobPost4.put("jobTitle", "Mobile App Developer");
        jobPost4.put("companyName", "TechGurus");
        jobPost4.put("location", "Cape Town");
        jobPost4.put("jobType", "Contract");
        jobPost4.put("salary", "R85 000.00/Monthly");
        jobPost4.put("ratings", 4.5);
        jobPost4.put("companyIcon", R.drawable.background);
        jobPost4.put("description", "TechGurus is looking for a talented Mobile App Developer to join our team in Cape Town. The ideal candidate should have experience in mobile app development for iOS and Android platforms. Responsibilities include designing, developing, and maintaining mobile applications. We offer competitive compensation, flexible work hours, and a collaborative work environment. If you're passionate about mobile technology, apply now!");
        jobPosts.add(jobPost4);

        // Job Post 5
        Map<String, Object> jobPost5 = new HashMap<>();
        jobPost5.put("jobTitle", "UI/UX Designer");
        jobPost5.put("companyName", "DesignWorks");
        jobPost5.put("location", "Durban");
        jobPost5.put("jobType", "Freelance");
        jobPost5.put("salary", "R70 000.00/Monthly");
        jobPost5.put("ratings", 4.7);
        jobPost5.put("companyIcon", R.drawable.icon_profile);
        jobPost5.put("description", "DesignWorks is seeking a creative UI/UX Designer to work on freelance projects in Durban. The ideal candidate should have a strong portfolio demonstrating their design skills and user-centric approach. Responsibilities include wireframing, prototyping, and creating engaging user experiences. We offer competitive compensation and flexible work arrangements. If you're passionate about design and want to work on exciting projects, apply now!");
        jobPosts.add(jobPost5);

        // Add more job posts as needed...

        return jobPosts;
    }

    public static Map<String, Object> getUserProfile() {
        Map<String, Object> userProfile = new HashMap<>();

        // Add user profile information
        userProfile.put("firstName", "Philasande");
        userProfile.put("lastName", "Bhani");
        userProfile.put("email", "pbhanina@gmail.com");
        userProfile.put("phoneNumber", "0782141216");
        userProfile.put("address", "5622 Midfielder St, Linbro park, Sandton");
        userProfile.put("resume", "path_to_resume");
        userProfile.put("coverLetter", "path_to_cover_letter");
        userProfile.put("profileCompletionPercentage", 90);
        userProfile.put("interviews", 7);
        userProfile.put("applications", 20);
        userProfile.put("profilePicture", R.drawable.image); // Add profile picture resource ID

        // Add additional information as needed

        return userProfile;
    }

    // ===================================================================== Job List page =============================================================================
    private void handleBackFromList() {
        job_list_screen.setVisibility(View.GONE);
        dash_board_screen.setVisibility(View.VISIBLE);
    }

    private void handleSearchBtn() {
        String text = text_to_search.getText().toString().trim();
        if (!text.isEmpty()) {
            // Perform search or show toast
            Utils.showToast(JobsActivity.this, text);
        } else {
            // Display error message
            text_to_search.setError("Please enter text to search");
        }
    }

    public static class JobsListAdapter extends RecyclerView.Adapter<JobsListAdapter.ViewHolder> {

        private List<Map<String, Object>> jobPosts;

        public JobsListAdapter(List<Map<String, Object>> jobPosts) {
            this.jobPosts = jobPosts;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_dispaled_in_list_screen, parent, false);
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
            TextView jobTitle;
            TextView location;
            TextView jobType;
            ImageView companyIcon;
            String description, companyName, salary;

            public ViewHolder(View itemView) {
                super(itemView);
                jobTitle = itemView.findViewById(R.id.lis_post_job_title);
                location = itemView.findViewById(R.id.lis_post_job_location);
                jobType = itemView.findViewById(R.id.lis_post_job_type);
                companyIcon = itemView.findViewById(R.id.lis_post_company_icon);

                // Set click listener
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Show position in a toast
                    Context context = v.getContext();
                    String s = companyName + "\n" +
                            jobTitle.getText().toString() + "\n" +
                            jobType.getText().toString() + "\n" +
                            salary+"\n"+
                            location.getText().toString() + "\n" +
                            description;
                    Utils.showToast(context, s);
                }
            }

            public void bind(Map<String, Object> jobPost) {

                jobTitle.setText(jobPost.get("jobTitle").toString());
                location.setText(jobPost.get("location").toString());
                jobType.setText(jobPost.get("jobType").toString());


                // Set the size of the companyIcon ImageView
                companyIcon.getLayoutParams().width = 40; // Set width to 40 pixels
                companyIcon.getLayoutParams().height = 40; // Set height to 40 pixels
                companyIcon.requestLayout(); // Apply the changes
                companyIcon.setImageResource((int) jobPost.get("companyIcon"));
                description = jobPost.get("description").toString();
                salary= jobPost.get("salary").toString();
                companyName = jobPost.get("companyName").toString();
            }
        }

    }

}
