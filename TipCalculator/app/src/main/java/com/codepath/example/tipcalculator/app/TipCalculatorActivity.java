package com.codepath.example.tipcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class TipCalculatorActivity extends Activity {

    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance();
    private EditText etInputAmount;
    private TextView tvTipPerPerson;
    private TextView tvTotalTip;
    private TextView tvTotalBill;
    private TextView tvTotalPerPerson;
    private RadioGroup rgTipPercentage;
    private NumberPicker npPeopleCount;
    private int tipPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        etInputAmount = (EditText) findViewById(R.id.etInputAmount);
        tvTipPerPerson = (TextView) findViewById(R.id.tvTipPerPerson);
        tvTotalTip = (TextView) findViewById(R.id.tvTotalTip);
        tvTotalBill = (TextView) findViewById(R.id.tvTotalBill);
        tvTotalPerPerson = (TextView) findViewById(R.id.tvTotalPerPerson);
        rgTipPercentage = (RadioGroup) findViewById(R.id.rgTipPercentage);
        npPeopleCount = (NumberPicker) findViewById(R.id.npPeopleCount);

        setUpPeopleCountChangedListener();
        setUpInputChangedListener();
        setUpTipSliderChangedListener();
    }

    private void setUpPeopleCountChangedListener() {
        npPeopleCount.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateTip();
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

    public void calculateTip(View v) {

        String strInputAmount = etInputAmount.getText().toString();
        if (strInputAmount.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter an amount", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            tvTipAmount.setText("");
            return;
        }

        // disabled button once it's pressed
        v.setEnabled(false);

        // change slider to reflect button pressed
        sbTipPercentage.setProgress(tipPercentage);

        double inputAmount = Double.parseDouble(etInputAmount.getText().toString());
        double tipAmount = inputAmount * (tipPercentage / 100.0);
        tvTipAmount.setText(FORMATTER.format(tipAmount));
        tvTotalAmount.setText(FORMATTER.format(tipAmount + inputAmount));
    }

    public void updateTip() {
        // only update tip amount if input is not zero and a percentage is selected
        tipPercentage = rgTipPercentage.getCheckedRadioButtonId().get
        if (tipPercentage != 0) {
            String strInputAmount = etInputAmount.getText().toString();
            if (strInputAmount.isEmpty()) {
                tvTipAmount.setText("");
                return;
            }

            double inputAmount = Double.parseDouble(etInputAmount.getText().toString());
            double tipAmount = inputAmount * (tipPercentage / 100.0);
            tvTipAmount.setText(FORMATTER.format(tipAmount));
            tvTotalAmount.setText(FORMATTER.format(tipAmount + inputAmount));

        }
    }

}
