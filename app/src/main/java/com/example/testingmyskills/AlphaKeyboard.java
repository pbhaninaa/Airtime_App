package com.example.testingmyskills;

import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.content.Context;

import androidx.core.content.ContextCompat;

public class AlphaKeyboard extends LinearLayout implements View.OnClickListener {

    private boolean isUpperCase = false;
    private ImageButton mButtonDelete;
    private ImageButton mButtonToUpperCase;
    private Button mButtonEnter;
    private Button mButtonToNumbers;
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
    // Define new buttons for special characters
    private Button mButtonHash;
    private Button mButtonExclamation;
    private Button mButtonBackslash;
    private Button mButtonForwardslash;
    private Button mButtonSemicolon;
    private Button mButtonColon;
    private Button mButtonQuestion;
    private Button mButtonAmpersand;
    private Button mButtonAsterisk;
    private Button mButtonComma;
    private LinearLayout NumsLayout, CharsLayout;
    private boolean displaNums;
    private SparseArray<String> keyValues = new SparseArray<>();
    private InputConnection inputConnection;

    public AlphaKeyboard(Context context) {
        this(context, null, 0);
    }

    public AlphaKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.keyboard_xsmall, this, true);

        NumsLayout = findViewById(R.id.numbers_layout_In_keyboard);
        CharsLayout = findViewById(R.id.special_chars_layout);
        displaNums = false;
        mButtonDelete = findViewById(R.id.button_alphabet_delete);
        mButtonToUpperCase = findViewById(R.id.button_to_upper_case);
        mButtonEnter = findViewById(R.id.button_enter);
        mButtonSpace = findViewById(R.id.button_space_bar);
        mButtonAtt = findViewById(R.id.button_at);
        mButtonDot = findViewById(R.id.button_full_stop);
        mButtonComma = findViewById(R.id.button_comma);
        mButtonToNumbers = findViewById(R.id.button_to_number_keys);


        mButtonq = findViewById(R.id.button_q);
        mButtonw = findViewById(R.id.button_w);
        mButtone = findViewById(R.id.button_e);
        mButtonr = findViewById(R.id.button_r);
        mButtont = findViewById(R.id.button_t);
        mButtony = findViewById(R.id.button_y);
        mButtonu = findViewById(R.id.button_u);
        mButtoni = findViewById(R.id.button_i);
        mButtono = findViewById(R.id.button_o);
        mButtonp = findViewById(R.id.button_p);
        mButtona = findViewById(R.id.button_a);
        mButtons = findViewById(R.id.button_s);
        mButtond = findViewById(R.id.button_d);
        mButtonf = findViewById(R.id.button_f);
        mButtong = findViewById(R.id.button_g);
        mButtonh = findViewById(R.id.button_h);
        mButtonj = findViewById(R.id.button_j);
        mButtonk = findViewById(R.id.button_k);
        mButtonl = findViewById(R.id.button_l);
        mButtonz = findViewById(R.id.button_z);
        mButtonx = findViewById(R.id.button_x);
        mButtonc = findViewById(R.id.button_c);
        mButtonv = findViewById(R.id.button_v);
        mButtonb = findViewById(R.id.button_b);
        mButtonn = findViewById(R.id.button_n);
        mButtonm = findViewById(R.id.button_m);

        mButton1 = findViewById(R.id.button_1);
        mButton2 = findViewById(R.id.button_2);
        mButton3 = findViewById(R.id.button_3);
        mButton4 = findViewById(R.id.button_4);
        mButton5 = findViewById(R.id.button_5);
        mButton6 = findViewById(R.id.button_6);
        mButton7 = findViewById(R.id.button_7);
        mButton8 = findViewById(R.id.button_8);
        mButton9 = findViewById(R.id.button_9);
        mButton0 = findViewById(R.id.button_0);

        // Find new special character buttons
        mButtonHash = findViewById(R.id.button_hash);
        mButtonExclamation = findViewById(R.id.button_exclamation);
        mButtonBackslash = findViewById(R.id.button_backslash);
        mButtonForwardslash = findViewById(R.id.button_forwardslash);
        mButtonSemicolon = findViewById(R.id.button_semicolon);
        mButtonColon = findViewById(R.id.button_colon);
        mButtonQuestion = findViewById(R.id.button_question);
        mButtonAmpersand = findViewById(R.id.button_ampersand);
        mButtonAsterisk = findViewById(R.id.button_asterisk);

        mButtonDelete.setOnClickListener(this);
        mButtonSpace.setOnClickListener(this);
        mButtonAtt.setOnClickListener(this);
        mButtonDot.setOnClickListener(this);
        mButtonEnter.setOnClickListener(this);
        mButtonToNumbers.setOnClickListener(this);
        mButtonComma.setOnClickListener(this);


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

        // Set click listeners for special character buttons
        mButtonExclamation.setOnClickListener(this);
        mButtonHash.setOnClickListener(this);
        mButtonBackslash.setOnClickListener(this);
        mButtonForwardslash.setOnClickListener(this);
        mButtonSemicolon.setOnClickListener(this);
        mButtonColon.setOnClickListener(this);
        mButtonQuestion.setOnClickListener(this);
        mButtonAmpersand.setOnClickListener(this);
        mButtonAsterisk.setOnClickListener(this);


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

        keyValues.put(R.id.button_space_bar, " ");
        keyValues.put(R.id.button_enter, "\n");
        keyValues.put(R.id.button_full_stop, ".");
        keyValues.put(R.id.button_comma,",");

        keyValues.put(R.id.button_at, "@");
        keyValues.put(R.id.button_hash, "#");
        keyValues.put(R.id.button_exclamation, "!");
        keyValues.put(R.id.button_backslash, "\\");
        keyValues.put(R.id.button_forwardslash, "/");
        keyValues.put(R.id.button_semicolon, ";");
        keyValues.put(R.id.button_colon, ":");
        keyValues.put(R.id.button_question, "?");
        keyValues.put(R.id.button_ampersand, "&");
        keyValues.put(R.id.button_asterisk, "*");
    }

    @Override
    public void onClick(View v) {
        if (inputConnection == null) return;
        if (v.getId() == R.id.button_to_number_keys) {
            updateToNumbers();
        }
        if (v.getId() == R.id.button_to_upper_case) {
            isUpperCase = !isUpperCase;
            updateButtonCase();

        } else {
            if (v.getId() == R.id.button_alphabet_delete) {
                CharSequence selectedText = inputConnection.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    inputConnection.commitText("", 1);
                }
            } else {
                String value = keyValues.get(v.getId());
                if (value != null) {
                    if (isUpperCase && Character.isLetter(value.charAt(0))) {
                        value = value.toUpperCase();
                    }
                    inputConnection.commitText(value, 1);
                }
            }
        }
    }

    private void updateToNumbers() {
        if (displaNums) {
            NumsLayout.setVisibility(VISIBLE);
            mButtonToNumbers.setText("Chars");
            CharsLayout.setVisibility(GONE);
            displaNums = false;
        } else {
            NumsLayout.setVisibility(GONE);
            CharsLayout.setVisibility(VISIBLE);
            mButtonToNumbers.setText("123");
            displaNums = true;
        }
    }

    private void updateButtonCase() {
        ImageButton toggleBtn = findViewById(R.id.button_to_upper_case);
        if (isUpperCase) {
            toggleBtn.setColorFilter(ContextCompat.getColor(getContext(), R.color.lime), PorterDuff.Mode.SRC_IN);
        } else {
            toggleBtn.setColorFilter(ContextCompat.getColor(getContext(), R.color.primary_color), PorterDuff.Mode.SRC_IN);
        }

        for (int i = 0; i < keyValues.size(); i++) {
            int key = keyValues.keyAt(i);
            String value = keyValues.valueAt(i);
            if (Character.isLetter(value.charAt(0))) {
                value = isUpperCase ? value.toUpperCase() : value.toLowerCase();
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
