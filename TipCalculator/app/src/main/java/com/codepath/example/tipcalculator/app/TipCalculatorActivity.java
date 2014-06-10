package com.codepath.example.tipcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class TipCalculatorActivity extends Activity {

    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
    private static final NumberFormat PERCENTAGE_FORMATTER = NumberFormat.getPercentInstance();
    private EditText etInputAmount;
    private TextView tvTipPerPerson;
    private TextView tvTipTotal;
    private TextView tvBillTotal;
    private TextView tvBillPerPerson;
    private TextView tvTipPercentage;
    private SeekBar sbTipPercentage;
    private NumberPicker npPeopleCount;
    private int tipPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        etInputAmount = (EditText) findViewById(R.id.etInputAmount);
        tvTipPerPerson = (TextView) findViewById(R.id.tvTipPerPerson);
        tvTipTotal = (TextView) findViewById(R.id.tvTipTotal);
        tvBillTotal = (TextView) findViewById(R.id.tvBillTotal);
        tvBillPerPerson = (TextView) findViewById(R.id.tvBillPerPerson);
        tvTipPercentage = (TextView) findViewById(R.id.tvTipPercentage);
        sbTipPercentage = (SeekBar) findViewById(R.id.sbTipPercentage);
        npPeopleCount = (NumberPicker) findViewById(R.id.npPeopleCount);

        setUpPeopleCountChangedListener();
        setUpInputChangedListener();
        setUpTipSliderChangedListener();
    }

    private void setUpTipSliderChangedListener() {
        sbTipPercentage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (etInputAmount.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bill Amount cannot be empty", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else if (npPeopleCount.getValue() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Number of people must be at least 1", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else {
                    tvTipPercentage.setText(PERCENTAGE_FORMATTER.format(progress));
                    updateTip();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setUpPeopleCountChangedListener() {
        npPeopleCount.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Number of people must be at least 1", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                // update if input amount is not empty and tip percentage is greater than zero
                if (!etInputAmount.getText().toString().isEmpty() && sbTipPercentage.getProgress() > 0) {
                    updateTip();
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
                // clean out all result if input is empty
                if (etInputAmount.getText().toString().isEmpty()) {
                    reset();
                }
                // if tip percentage is greater than zero and input is not empty, update
                if (sbTipPercentage.getProgress() > 0) {
                    updateTip();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void reset() {
        tvTipPerPerson.setText("");
        tvTipTotal.setText("");
        tvBillTotal.setText("");
        tvBillPerPerson.setText("");
    }

    public void updateTip() {
        // only update tip amount if input is not zero, people count is greater than 1, and
        // a percentage is selected

        int tipPercentage = sbTipPercentage.getProgress();
        int peopleCount = npPeopleCount.getValue();
        double inputAmount = Double.parseDouble(etInputAmount.getText().toString());
        double tipTotal = inputAmount * (tipPercentage / 100.0);
        double tipPerPerson = tipTotal / peopleCount;
        double billTotal = inputAmount + tipTotal;
        double billPerPerson = billTotal / peopleCount;
        tvTipPerPerson.setText(CURRENCY_FORMATTER.format(tipPerPerson));
        tvTipTotal.setText(CURRENCY_FORMATTER.format(tipTotal));
        tvBillTotal.setText(CURRENCY_FORMATTER.format(billTotal));
        tvBillPerPerson.setText(CURRENCY_FORMATTER.format(billPerPerson));
    }
}
