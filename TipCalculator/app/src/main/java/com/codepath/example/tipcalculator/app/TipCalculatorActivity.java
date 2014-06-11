package com.codepath.example.tipcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class TipCalculatorActivity extends Activity {

    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
    private static final NumberFormat PERCENTAGE_FORMATTER = NumberFormat.getNumberInstance();
    private EditText etInputAmount;
    private TextView tvTipPerPerson;
    private TextView tvTipTotal;
    private TextView tvBillTotal;
    private TextView tvBillPerPerson;
    private TextView tvTipPercentage;
    private SeekBar sbTipPercentage;
    private EditText etPeopleCount;
    private int tipPercentage;
    private double inputAmount;

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
        etPeopleCount = (EditText) findViewById(R.id.etPeopleCount);
        tipPercentage = sbTipPercentage.getProgress();

        setUpPeopleCountChangedListener();
        setUpInputChangedListener();
        setUpTipSliderChangedListener();
    }

    private void setUpTipSliderChangedListener() {
        sbTipPercentage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                String strInput = etInputAmount.getText().toString();
                String strPeopleCount = etPeopleCount.getText().toString();

                // prevent user from applying a percentage before entering an amount
                if (strInput.isEmpty() || Double.parseDouble(strInput) == 0) {
                    notifyInvalidInputAmount();
                    sbTipPercentage.setProgress(tipPercentage);
                    return;
                } else if (strPeopleCount.isEmpty() || Integer.parseInt(strPeopleCount) == 0) {
                    notifyInvalidPeopleCount();
                    sbTipPercentage.setProgress(tipPercentage);
                    return;
                } else {
                    tvTipPercentage.setText(String.valueOf(progress));
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

    private void notifyInvalidPeopleCount() {
        Toast toast = Toast.makeText(getApplicationContext(), "Number of people cannot be empty or zero", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void notifyInvalidInputAmount() {
        Toast toast = Toast.makeText(getApplicationContext(), "Bill Amount cannot be empty or zero", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void setUpPeopleCountChangedListener() {
        etPeopleCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if user try to enter a zero count or remove count, display warning msg
                // and undo user edit
                String strPeopleCount = etPeopleCount.getText().toString();
                if (strPeopleCount.isEmpty() || Integer.parseInt(strPeopleCount) == 0) {
                    notifyInvalidPeopleCount();
                    String itemText = String.valueOf(before);
                    etPeopleCount.setText(itemText);
                    // set cursor at end of text
                    etPeopleCount.setSelection(itemText.length());
                    return;
                }

                // update if input amount is not empty and tip percentage is greater than zero
                String strInput = etInputAmount.getText().toString();
                if (!strInput.isEmpty() && Double.parseDouble(strInput) > 0 && sbTipPercentage.getProgress() > 0) {
                    updateTip();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
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
                String strInput = etInputAmount.getText().toString();
                String strPeopleCount = etPeopleCount.getText().toString();

                // if tip percentage is not zero and user try to enter a zero amount or remove amount,
                // display warning msg and undo user edit
                if (sbTipPercentage.getProgress() > 0) {
                    if (strInput.isEmpty() || Double.parseDouble(strInput) == 0) {
                        notifyInvalidInputAmount();
                        String strInputAmount = String.valueOf(inputAmount);
                        etInputAmount.setText(strInputAmount);
                        return;
                    }

                    // if tip percentage is greater than zero and input is not empty, update
                    updateTip();
                } else {
                    if (strInput.isEmpty() || Double.parseDouble(strInput) == 0) {
                        notifyInvalidInputAmount();
                        reset();
                        return;
                    }
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

        tipPercentage = sbTipPercentage.getProgress();
        inputAmount = Double.parseDouble(etInputAmount.getText().toString());
        int peopleCount = Integer.parseInt(etPeopleCount.getText().toString());
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
