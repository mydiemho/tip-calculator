package com.codepath.example.tipcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    
    public void calculateTip(View v) {

        String strInputAmount = etInputAmount.getText().toString();
        double inputAmount;

        if (!strInputAmount.equals("")) {

            v.setEnabled(false);
            try {
                inputAmount = Double.parseDouble(etInputAmount.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), String.format("Invalid input: %s", etInputAmount.getText().toString()), Toast.LENGTH_SHORT).show();
                return;
            }

            double percentage = Double.parseDouble(v.getTag().toString());

            double tipAmount = inputAmount * percentage;
            tvTipAmount.setText(formatter.format(tipAmount));
        }
    }
}
