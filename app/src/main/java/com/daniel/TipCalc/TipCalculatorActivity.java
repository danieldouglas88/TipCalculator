package com.daniel.TipCalc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences.Editor;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.NumberFormat;


public class TipCalculatorActivity extends Activity implements OnEditorActionListener, OnClickListener {

    Firebase firebaseRef;
    Firebase firebaseRefTwo;
    Firebase firebaseRefThree;
    Firebase firebaseRefFour;

    // define variables for the widgets
    private EditText billAmountEditText;
    private TextView percentTextView;
    private Button percentUpButton;
    private Button percentDownButton;
    private TextView tipTextView;
    private TextView totalTextView;

    // define the SharedPreferences object
    private SharedPreferences savedValues;

    // define instance variables that should be saved
    private String billAmountString = "";
    private float tipPercent = .15f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_calculator);

        // get references to the widgets
        billAmountEditText = (EditText) findViewById(R.id.billAmountEditText);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        percentUpButton = (Button) findViewById(R.id.percentUpButton);
        percentDownButton = (Button) findViewById(R.id.percentDownButton);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);

        // set the listeners
        billAmountEditText.setOnEditorActionListener(this);
        percentUpButton.setOnClickListener(this);
        percentDownButton.setOnClickListener(this);

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }
    @Override
    protected void onStart(){
        super.onStart();

        firebaseRef = new Firebase("https://db-app-6edfc.firebaseio.com/Tip");
        firebaseRefTwo = new Firebase("https://db-app-6edfc.firebaseio.com/Tip2");
        firebaseRefThree = new Firebase("https://db-app-6edfc.firebaseio.com/Total");
        firebaseRefFour = new Firebase("https://db-app-6edfc.firebaseio.com/BillAmount");

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                percentTextView.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        firebaseRefFour.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                percentTextView.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        firebaseRefThree.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                totalTextView.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        firebaseRefTwo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                tipTextView.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        }); }

    @Override
    public void onClick(View v){

        switch (v.getId()) {
            case R.id.percentDownButton:
                tipPercent = tipPercent - .01f;
                calculateAndDisplay();

                firebaseRef.setValue(percentTextView.getText().toString());
                firebaseRefTwo.setValue(tipTextView.getText().toString());
                firebaseRefThree.setValue(totalTextView.getText().toString());
                firebaseRefFour.setValue(billAmountEditText.getText().toString());
                break;

            case R.id.percentUpButton:
                tipPercent = tipPercent + .01f;
                calculateAndDisplay();

                firebaseRef.setValue(percentTextView.getText().toString());
                firebaseRefTwo.setValue(tipTextView.getText().toString());
                firebaseRefThree.setValue(totalTextView.getText().toString());
                firebaseRefFour.setValue(billAmountEditText.getText().toString());
                break;
        }
    }

    @Override
    public void onPause() {
        // save the instance variables
        Editor editor = savedValues.edit();
        editor.putString("billAmountString", billAmountString);
        editor.putFloat("tipPercent", tipPercent);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        billAmountString = savedValues.getString("billAmountString", "");
        tipPercent = savedValues.getFloat("tipPercent", 0.15f);

        // set the bill amount on its widget
        billAmountEditText.setText(billAmountString);

        // calculate and display
        calculateAndDisplay();
    }

    public void calculateAndDisplay() {

        // get the bill amount
        billAmountString = billAmountEditText.getText().toString();
        float billAmount;
        if (billAmountString.equals("")) {
            billAmount = 0;
        }
        else {
            billAmount = Float.parseFloat(billAmountString);
        }

        // calculate tip and total
        float tipAmount = billAmount * tipPercent;
        float totalAmount = billAmount + tipAmount;

        // display the other results with formatting
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));

        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(tipPercent));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
            firebaseRef.setValue(percentTextView.getText().toString());
            firebaseRefTwo.setValue(tipTextView.getText().toString());
            firebaseRefThree.setValue(totalTextView.getText().toString());
            firebaseRefFour.setValue(billAmountEditText.getText().toString());
        }
        return false;
    }

    }

