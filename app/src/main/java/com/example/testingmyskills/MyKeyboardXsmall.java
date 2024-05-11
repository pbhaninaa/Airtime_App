package com.example.testingmyskills;


import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.content.Context;

import androidx.core.content.ContextCompat;



public class MyKeyboardXsmall extends LinearLayout implements View.OnClickListener {

    // constructors
    public MyKeyboardXsmall(Context context) {
        this(context, null, 0);
    }

    public MyKeyboardXsmall(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyKeyboardXsmall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // keyboard keys (buttons)
    private boolean isUpperCase = false;
    private  boolean showNumbers = false;
    private ImageButton mButtonDelete;
    private ImageButton mButtonToUpperCase;
    private ImageButton mButtonEnter;
    private Button mButtonGmail;
    private Button mButtonOutlook;
    private Button mButtonUnknownEmail;
    private Button mButtonSpace;
    private Button mButtonAtt;
    private Button mButtonDot;


    private Button mButtonq;
    private Button mButtonw;
    private Button mButtone;
    private Button mButtonr;
    private Button mButtont;
    private Button mButtony;
    private Button mButtonu;
    private Button mButtoni;
    private Button mButtono;
    private Button mButtonp;
    private Button mButtona;
    private Button mButtons;
    private Button mButtond;
    private Button mButtonf;
    private Button mButtong;
    private Button mButtonh;
    private Button mButtonj;
    private Button mButtonk;
    private Button mButtonl;
    private Button mButtonz;
    private Button mButtonx;
    private Button mButtonc;
    private Button mButtonv;
    private Button mButtonb;
    private Button mButtonn;
    private Button mButtonm;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButton0;


    // This will map the button resource id to the String value that we want to
    // input when that button is clicked.
    SparseArray<String> keyValues = new SparseArray<>();

    // Our communication link to the EditText
    InputConnection inputConnection;

    private void init(Context context, AttributeSet attrs) {

        // initialize buttons
        LayoutInflater.from(context).inflate(R.layout.keyboard_xsmall, this, true);

        mButtonDelete = (ImageButton) findViewById(R.id.button_alphabet_delete);
        mButtonToUpperCase = (ImageButton) findViewById(R.id.button_to_upper_case);
        mButtonEnter = (ImageButton) findViewById(R.id.button_enter);
        mButtonSpace = (Button) findViewById(R.id.button_space_bar);
        mButtonAtt = (Button) findViewById(R.id.button_at);
        mButtonDot = (Button) findViewById(R.id.button_full_stop);



        mButtonq = (Button) findViewById(R.id.button_q);
        mButtonw = (Button) findViewById(R.id.button_w);
        mButtone = (Button) findViewById(R.id.button_e);
        mButtonr = (Button) findViewById(R.id.button_r);
        mButtont = (Button) findViewById(R.id.button_t);
        mButtony = (Button) findViewById(R.id.button_y);
        mButtonu = (Button) findViewById(R.id.button_u);
        mButtoni = (Button) findViewById(R.id.button_i);
        mButtono = (Button) findViewById(R.id.button_o);
        mButtonp = (Button) findViewById(R.id.button_p);
        mButtona = (Button) findViewById(R.id.button_a);
        mButtons = (Button) findViewById(R.id.button_s);
        mButtond = (Button) findViewById(R.id.button_d);
        mButtonf = (Button) findViewById(R.id.button_f);
        mButtong = (Button) findViewById(R.id.button_g);
        mButtonh = (Button) findViewById(R.id.button_h);
        mButtonj = (Button) findViewById(R.id.button_j);
        mButtonk = (Button) findViewById(R.id.button_k);
        mButtonl = (Button) findViewById(R.id.button_l);
        mButtonz = (Button) findViewById(R.id.button_z);
        mButtonx = (Button) findViewById(R.id.button_x);
        mButtonc = (Button) findViewById(R.id.button_c);
        mButtonv = (Button) findViewById(R.id.button_v);
        mButtonb = (Button) findViewById(R.id.button_b);
        mButtonn = (Button) findViewById(R.id.button_n);
        mButtonm = (Button) findViewById(R.id.button_m);

        mButton1 = (Button) findViewById(R.id.button_1);
        mButton2 =(Button) findViewById(R.id.button_2);
        mButton3 = (Button)findViewById(R.id.button_3);
        mButton4 = (Button) findViewById(R.id.button_4);
        mButton5 =(Button) findViewById(R.id.button_5);
        mButton6 = (Button)findViewById(R.id.button_6);
        mButton7 =(Button) findViewById(R.id.button_7);
        mButton8 = (Button)findViewById(R.id.button_8);
        mButton9 =(Button) findViewById(R.id.button_9);
        mButton0 = (Button)findViewById(R.id.button_0);
        // set button click listeners

        mButtonDelete.setOnClickListener(this);
        mButtonSpace.setOnClickListener(this);
        mButtonAtt.setOnClickListener(this);
        mButtonDot.setOnClickListener(this);
        mButtonEnter.setOnClickListener(this);

        mButtonOutlook.setOnClickListener(this);
        mButtonGmail.setOnClickListener(this);
        mButtonUnknownEmail.setOnClickListener(this);

        mButtonq.setOnClickListener(this);
        mButtonw.setOnClickListener(this);
        mButtone.setOnClickListener(this);
        mButtonr.setOnClickListener(this);
        mButtont.setOnClickListener(this);
        mButtony.setOnClickListener(this);
        mButtonu.setOnClickListener(this);
        mButtoni.setOnClickListener(this);
        mButtono.setOnClickListener(this);
        mButtonp.setOnClickListener(this);
        mButtona.setOnClickListener(this);
        mButtons.setOnClickListener(this);
        mButtond.setOnClickListener(this);
        mButtonf.setOnClickListener(this);
        mButtong.setOnClickListener(this);
        mButtonh.setOnClickListener(this);
        mButtonj.setOnClickListener(this);
        mButtonk.setOnClickListener(this);
        mButtonl.setOnClickListener(this);
        mButtonz.setOnClickListener(this);
        mButtonx.setOnClickListener(this);
        mButtonc.setOnClickListener(this);
        mButtonv.setOnClickListener(this);
        mButtonb.setOnClickListener(this);
        mButtonn.setOnClickListener(this);
        mButtonm.setOnClickListener(this);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton8.setOnClickListener(this);
        mButton9.setOnClickListener(this);
        mButton0.setOnClickListener(this);

        mButtonToUpperCase.setOnClickListener(this);

        // map buttons IDs to input strings
        keyValues.put(R.id.button_1, "01");
        keyValues.put(R.id.button_2, "02");
        keyValues.put(R.id.button_3, "3");
        keyValues.put(R.id.button_4, "4");
        keyValues.put(R.id.button_5, "5");
        keyValues.put(R.id.button_6, "6");
        keyValues.put(R.id.button_7, "7");
        keyValues.put(R.id.button_8, "8");
        keyValues.put(R.id.button_9, "9");
        keyValues.put(R.id.button_0, "0");
        keyValues.put(R.id.button_q, "q");
        keyValues.put(R.id.button_w, "w");
        keyValues.put(R.id.button_e, "e");
        keyValues.put(R.id.button_r, "r");
        keyValues.put(R.id.button_t, "t");
        keyValues.put(R.id.button_y, "y");
        keyValues.put(R.id.button_u, "u");
        keyValues.put(R.id.button_i, "i");
        keyValues.put(R.id.button_o, "o");
        keyValues.put(R.id.button_p, "p");
        keyValues.put(R.id.button_a, "a");
        keyValues.put(R.id.button_s, "s");
        keyValues.put(R.id.button_d, "d");
        keyValues.put(R.id.button_f, "f");
        keyValues.put(R.id.button_g, "g");
        keyValues.put(R.id.button_h, "h");
        keyValues.put(R.id.button_j, "j");
        keyValues.put(R.id.button_k, "k");
        keyValues.put(R.id.button_l, "l");
        keyValues.put(R.id.button_z, "z");
        keyValues.put(R.id.button_x, "x");
        keyValues.put(R.id.button_c, "c");
        keyValues.put(R.id.button_v, "v");
        keyValues.put(R.id.button_b, "b");
        keyValues.put(R.id.button_n, "n");
        keyValues.put(R.id.button_m, "m");

        keyValues.put(R.id.button_1, "1");
        keyValues.put(R.id.button_2, "2");
        keyValues.put(R.id.button_3, "3");
        keyValues.put(R.id.button_4, "4");
        keyValues.put(R.id.button_5, "5");
        keyValues.put(R.id.button_6, "6");
        keyValues.put(R.id.button_7, "7");
        keyValues.put(R.id.button_8, "8");
        keyValues.put(R.id.button_9, "9");
        keyValues.put(R.id.button_0, "0");

        keyValues.put(R.id.button_space_bar, " ");
        keyValues.put(R.id.button_enter, "\n");
        keyValues.put(R.id.button_at, "@");
        keyValues.put(R.id.button_full_stop, ".");

    }


    @Override
    public void onClick(View v) {
        // do nothing if the InputConnection has not been set yet
        if (inputConnection == null) return;

        if (v.getId() == R.id.button_to_upper_case) {
            // Toggle between uppercase and lowercase
            isUpperCase = !isUpperCase;

            // Update button texts accordingly
            updateButtonCase();
        } else {
            // Handle other button clicks
            // Delete text or input key value
            // All communication goes through the InputConnection
            if (v.getId() == R.id.button_alphabet_delete) {
                // Handle delete button
                CharSequence selectedText = inputConnection.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    // no selection, so delete previous character
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    // delete the selection
                    inputConnection.commitText("", 1);
                }
            } else if (v.getId() == R.id.button_enter || v.getId() == R.id.button_at ||
                    v.getId() == R.id.button_full_stop || v.getId() == R.id.button_space_bar) {
                // Handle special buttons (enter, @, ., space)
                String stringValue = keyValues.get(v.getId());
                inputConnection.commitText(stringValue, 1);
            } else {
                // Handle regular character buttons
                String value = keyValues.get(v.getId());
                // Toggle case if needed
                if (isUpperCase && Character.isLetter(value.charAt(0))) {
                    value = value.toUpperCase();
                }
                inputConnection.commitText(value, 1);
            }
        }
    }

    // Helper method to update button texts according to the current case state
    private void updateButtonCase() {
        ImageButton toggleBtn = findViewById(R.id.button_to_upper_case);
        if (isUpperCase) {
            // Tint the image with lime color
            toggleBtn.setColorFilter(ContextCompat.getColor(getContext(), R.color.lime), PorterDuff.Mode.SRC_IN);
        } else {
            // Tint the image with light blue color
            toggleBtn.setColorFilter(ContextCompat.getColor(getContext(), R.color.primary_color), PorterDuff.Mode.SRC_IN);
        }


        for (int i = 0; i < keyValues.size(); i++) {
            int key = keyValues.keyAt(i);
            String value = keyValues.valueAt(i);
            // Check if the value is an alphabetic character
            if (Character.isLetter(value.charAt(0))) {
                // Toggle case
                if (isUpperCase) {
                    value = value.toUpperCase();
                } else {
                    value = value.toLowerCase();
                }
                // Update the button text if it corresponds to this key
                Button button = findViewById(key);
                if (button != null) {
                    button.setText(value);
                }
            }
        }
    }
    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }
}