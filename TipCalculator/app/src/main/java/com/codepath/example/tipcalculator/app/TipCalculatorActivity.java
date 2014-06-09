package com.codepath.example.tipcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class TipCalculatorActivity extends Activity {

    private EditText etInputAmount;
    private TextView tvTipAmount;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private Button btnOkService;
    private Button btnGoodService;
    private Button btnGreatService;
    private Button btnPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        etInputAmount = (EditText) findViewById(R.id.etInputAmount);
        tvTipAmount = (TextView) findViewById(R.id.tvTipAmount);
        btnOkService = (Button) findViewById(R.id.btnOkService);
        btnGoodService = (Button) findViewById(R.id.btnGoodService);
        btnGreatService = (Button) findViewById(R.id.btnGreatService);

        setUpButtonClickListener();
        setUpInputChangedListener();
    }

    private void setUpButtonClickListener() {
        btnOkService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGoodService.setEnabled(true);
                btnGreatService.setEnabled(true);
                calculateTip(v);
            }
        });

        btnGoodService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOkService.setEnabled(true);
                btnGreatService.setEnabled(true);
                calculateTip(v);
            }
        });

        btnGreatService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOkService.setEnabled(true);
                btnGoodService.setEnabled(true);
                calculateTip(v);
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

        double inputAmount;
        try {
            inputAmount = Double.parseDouble(etInputAmount.getText().toString());
        } catch (NumberFormatException e) {
            Toast toast = Toast.makeText(getApplicationContext(), String.format("Invalid input: %s", etInputAmount.getText().toString()), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        btnPressed = (Button) v;
        v.setEnabled(false);
        double percentage = Double.parseDouble(v.getTag().toString());
        double tipAmount = inputAmount * percentage;
        tvTipAmount.setText(formatter.format(tipAmount));
    }

    public void updateTip() {
        if (btnPressed != null) {
            String strInputAmount = etInputAmount.getText().toString();
            if (strInputAmount.isEmpty()) {
                tvTipAmount.setText("");
                return;
            }

            double inputAmount;
            try {
                inputAmount = Double.parseDouble(etInputAmount.getText().toString());
            } catch (NumberFormatException e) {
                Toast toast = Toast.makeText(getApplicationContext(), String.format("Invalid input: %s", etInputAmount.getText().toString()), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }

            double percentage = Double.parseDouble(btnPressed.getTag().toString());
            double tipAmount = inputAmount * percentage;
            tvTipAmount.setText(formatter.format(tipAmount));
        }
    }

}
