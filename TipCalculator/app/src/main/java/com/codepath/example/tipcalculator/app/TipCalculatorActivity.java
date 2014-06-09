package com.codepath.example.tipcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
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
    private int tipPercentage;
    private SeekBar sbTipPercentage;
    private TextView tvTipPercentageLabel;
    private TextView tvTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        etInputAmount = (EditText) findViewById(R.id.etInputAmount);
        tvTipAmount = (TextView) findViewById(R.id.tvTipAmount);
        btnOkService = (Button) findViewById(R.id.btnOkService);
        btnGoodService = (Button) findViewById(R.id.btnGoodService);
        btnGreatService = (Button) findViewById(R.id.btnGreatService);
        sbTipPercentage = (SeekBar) findViewById(R.id.sbTipPercentage);
        tvTipPercentageLabel = (TextView) findViewById(R.id.tvTipPercentageLabel);
        tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);

        setUpButtonClickListener();
        setUpInputChangedListener();
        setUpTipSliderChangedListener();
    }

    private void setUpTipSliderChangedListener() {
        sbTipPercentage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Display error message when trying to calculate missing input.
                String strInputAmount = etInputAmount.getText().toString();
                if (strInputAmount.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter an amount", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    tvTipAmount.setText("");
                    // return slider to its previous position
                    seekBar.setProgress(tipPercentage);
                    return;
                }

                tipPercentage = seekBar.getProgress();
                tvTipPercentageLabel.setText(String.valueOf(progress) + "%");
                updateTip();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setUpButtonClickListener() {
        btnOkService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGoodService.setEnabled(true);
                btnGreatService.setEnabled(true);
                tipPercentage = Integer.parseInt(v.getTag().toString());
                calculateTip(v);
            }
        });

        btnGoodService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipPercentage = Integer.parseInt(v.getTag().toString());
                btnOkService.setEnabled(true);
                btnGreatService.setEnabled(true);
                calculateTip(v);
            }
        });

        btnGreatService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipPercentage = Integer.parseInt(v.getTag().toString());
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

        // disabled button once it's pressed
        v.setEnabled(false);

        // change slider to reflect button pressed
        sbTipPercentage.setProgress(tipPercentage);

        double inputAmount = Double.parseDouble(etInputAmount.getText().toString());
        double tipAmount = inputAmount * (tipPercentage / 100.0);
        tvTipAmount.setText(formatter.format(tipAmount));
        tvTotalAmount.setText(formatter.format(tipAmount + inputAmount));
    }

    public void updateTip() {
        // only update tip amount after input change if one of the button is clicked
        // or if the slider is changed to a nonzero value.
        if (tipPercentage != 0) {
            String strInputAmount = etInputAmount.getText().toString();
            if (strInputAmount.isEmpty()) {
                tvTipAmount.setText("");
                return;
            }

            double inputAmount = Double.parseDouble(etInputAmount.getText().toString());
            double tipAmount = inputAmount * (tipPercentage / 100.0);
            tvTipAmount.setText(formatter.format(tipAmount));
            tvTotalAmount.setText(formatter.format(tipAmount + inputAmount));

        }
    }

}
