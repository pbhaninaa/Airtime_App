package com.example.testingmyskills.JavaClasses;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyTextWatcher implements TextWatcher {
    private final EditText editText;
    private String current = "";

    public CurrencyTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // No action needed
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (!s.toString().equals(current)) {
//            String cleanString = s.toString().replaceAll("[^\\d]", "");
//            double parsed = cleanString.isEmpty() ? 0.0 : Double.parseDouble(cleanString);
//
//            // Create a formatter for South African currency
//            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "ZW"));
//            String formatted = formatter.format(parsed / 100);
//
//            current = formatted;
//            editText.setText(formatted);
//            editText.setSelection(formatted.length());
//        }
        if (!s.toString().equals(current)) {
            String cleanString = s.toString().replaceAll("[^\\d]", ""); // Remove all non-numeric characters
            double parsed = cleanString.isEmpty() ? 0.0 : Double.parseDouble(cleanString);

            // Create a formatter for South African numeric format (without currency symbol)
            NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("en", "ZW")); // Zimbabwe locale for number format

            String formatted = formatter.format(parsed / 100); // Keep formatting but no currency sign

            current = formatted;
            editText.setText(formatted);
            editText.setSelection(formatted.length());
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        // No action needed
    }
}
