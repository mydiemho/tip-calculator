package com.codepath.example.tipcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.NumberFormat;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static android.view.Gravity.CENTER_HORIZONTAL;
import static de.keyboardsurfer.android.widget.crouton.Style.Builder;
import static de.keyboardsurfer.android.widget.crouton.Style.holoBlueLight;

public class TipCalculatorActivity extends Activity {

    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
    private EditText etInputAmount;
    private TextView tvTipTotal;
    private Button btnOkService;
    private Button btnGoodService;
    private Button btnGreatService;
    private TextView tvBillTotal;
    private TextView tvBillPerPerson;
    private int tipPercentage;
    private RadioGroup rgTipPercentage;
    private NumberPicker npPeopleCount;
    private EditText etTipPercentage;

    private boolean btnPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        etInputAmount = (EditText) findViewById(R.id.etInputAmount);
        tvTipTotal = (TextView) findViewById(R.id.tvTipTotal);
        tvBillTotal = (TextView) findViewById(R.id.tvBillTotal);
        tvBillPerPerson = (TextView) findViewById(R.id.tvBillPerPerson);

        npPeopleCount = (NumberPicker) findViewById(R.id.npPeopleCount);
        rgTipPercentage = (RadioGroup) findViewById(R.id.rgTipPercentage);
        etTipPercentage = (EditText) findViewById(R.id.etTipPercentage);

        setUpTipPercentageChangedListener();
        setUpInputChangedListener();
        setUpPeopleCountChangedListener();
    }

    private void setUpPeopleCountChangedListener() {
        npPeopleCount.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateTip();
            }
        });
    }

    private void setUpTipPercentageChangedListener() {
        rgTipPercentage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = rgTipPercentage.getCheckedRadioButtonId();
                View radioButton = rgTipPercentage.findViewById(radioButtonID);

                rgTipPercentage.clearCheck();
                rgTipPercentage.check(checkedId);

                // if other is checked, enabled input
                if (radioButton.getTag() == R.string.tag_other_service) {
                    etTipPercentage.isEnabled();

                    // force user to enter a tip percentage
                    tipPercentage = 0;
                } else {
                    if (!etTipPercentage.getText().toString().isEmpty()) {
                        etTipPercentage.setText("");
                    }
                    tipPercentage = Integer.parseInt(radioButton.getTag().toString());
                }
            }
        });
    }

    private void setUpInputChangedListener() {
        etInputAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTip();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void calculateTip(View v) {
        btnPressed = true;
        // disabled button once it's pressed
        v.setEnabled(false);

        tipPercentage = Integer.parseInt(v.getTag().toString());

        updateTip();
    }

    private void updateTip() {

        if (tipPercentage == 0) {
            showWarning(getString(R.string.msg_missing_invalid_input));

        }
        String strInputAmount = etInputAmount.getText().toString();
        if (strInputAmount.isEmpty() || Double.parseDouble(strInputAmount) == 0) {
            // clear result if input is missing or zero
            tvTipTotal.setText("");
            tvBillTotal.setText("");
            showWarning(getString(R.string.msg_missing_invalid_input));
            return;
        }

        int peopleCount = npPeopleCount.getValue();
        double inputAmount = Double.parseDouble(etInputAmount.getText().toString());
        double tipAmount = inputAmount * (tipPercentage / 100.0);
        double totalAmount = inputAmount + tipAmount;
        tvTipTotal.setText(CURRENCY_FORMATTER.format(tipAmount));
        tvBillTotal.setText(CURRENCY_FORMATTER.format(totalAmount));
        tvBillPerPerson.setText(CURRENCY_FORMATTER.format(totalAmount / peopleCount));

    }

    private void showWarning(String msg) {
        // Define configuration options
        Configuration croutonConfiguration = new Configuration.Builder()
                .setDuration(1000).build();
        // Define custom styles for crouton
        Style style = new Builder()
                .setGravity(CENTER_HORIZONTAL)
                .setConfiguration(croutonConfiguration)
                .setBackgroundColorValue(holoBlueLight)
                .build();
        // Display notice with custom style and configuration
        Crouton.makeText(this, msg, style).show();
    }
}
