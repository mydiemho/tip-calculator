package com.codepath.example.tipcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.NumberFormat;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static android.view.Gravity.CENTER_HORIZONTAL;
import static de.keyboardsurfer.android.widget.crouton.Style.Builder;
import static de.keyboardsurfer.android.widget.crouton.Style.holoBlueLight;

public class TipCalculatorActivity extends Activity {

    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
    private static final NumberFormat PERCENTAGE_FORMATTER = NumberFormat.getPercentInstance();
    private EditText etInputAmount;

    private TextView tvTipTotal;
    private TextView tvBillTotal;
    private TextView tvBillPerPerson;

    private RadioGroup rgTipPercentage;
    private SeekBar sbTipPercentage;
    private TextView tvTipPercentage;

    private RadioGroup rgPeopleCount;

    private int tipPercentage;
    private int peopleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        etInputAmount = (EditText) findViewById(R.id.etInputAmount);
        tvTipTotal = (TextView) findViewById(R.id.tvTipTotal);
        tvBillTotal = (TextView) findViewById(R.id.tvBillTotal);
        tvBillPerPerson = (TextView) findViewById(R.id.tvBillPerPerson);

        rgTipPercentage = (RadioGroup) findViewById(R.id.rgTipPercentage);
        sbTipPercentage = (SeekBar) findViewById(R.id.sbTipPercentage);
        sbTipPercentage.setEnabled(false);
        tvTipPercentage = (TextView) findViewById(R.id.tvTipPercentage);

        int radioButtonID = rgTipPercentage.getCheckedRadioButtonId();
        View radioButton = rgTipPercentage.findViewById(radioButtonID);
        tipPercentage = Integer.parseInt(radioButton.getTag().toString());

        rgPeopleCount = (RadioGroup) findViewById(R.id.rgPeopleCount);
        rgPeopleCount.check(0);
        // default
        peopleCount = 1;

        setUpTipPercentageChangedListener();
        setUpInputChangedListener();
        setUpPeopleCountChangedListener();
    }

    private void setUpPeopleCountChangedListener() {
        rgPeopleCount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

//                rgPeopleCount.check(-1);        // this cause stackoverflow
//                rgPeopleCount.check(checkedId);
                for(int i = 0; i < rgPeopleCount.getChildCount(); i++) {
                    ToggleButton toggleButton = (ToggleButton) rgPeopleCount.getChildAt(i);
                    toggleButton.setChecked(toggleButton.getId() == checkedId);
                }

                ToggleButton toggleButton = (ToggleButton) rgPeopleCount.findViewById(checkedId);
                peopleCount = Integer.parseInt(toggleButton.getTag().toString());

                updateTip();
            }
        });
    }

    public void onToggle(View view)
    {
        rgPeopleCount.check(view.getId());
    }

    private void setUpSliderChangedListener() {
        sbTipPercentage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tipPercentage = sbTipPercentage.getProgress();
                tvTipPercentage.setText(PERCENTAGE_FORMATTER.format(sbTipPercentage.getProgress() / 100.0));

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

    private void setUpTipPercentageChangedListener() {
        rgTipPercentage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rgTipPercentage.findViewById(checkedId);
                rgTipPercentage.check(checkedId);

                // if other is checked, enabled slider
                if (radioButton.getTag() == getString(R.string.tag_other_service)) {
                    sbTipPercentage.setEnabled(true);
                    tvTipPercentage.setVisibility(View.VISIBLE);
                } else {
                    // disabled slider and hide text view
                    sbTipPercentage.setEnabled(false);
                    tvTipPercentage.setVisibility(View.INVISIBLE);

                    tipPercentage = Integer.parseInt(radioButton.getTag().toString());
                    updateTip();
                }
            }
        });

        setUpSliderChangedListener();
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

    private void updateTip() {

        if (tipPercentage == 0) {
            showWarning(getString(R.string.msg_missing_invalid_tip_percentage));
        }

        String strInputAmount = etInputAmount.getText().toString();
        if (strInputAmount.isEmpty() || Double.parseDouble(strInputAmount) == 0) {
            // clear result if input is missing or zero
            tvTipTotal.setText("");
            tvBillTotal.setText("");
            showWarning(getString(R.string.msg_missing_invalid_input));
            return;
        }

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
                .setDuration(800).build();
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
