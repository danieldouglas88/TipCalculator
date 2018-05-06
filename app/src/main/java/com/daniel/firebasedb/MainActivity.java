package com.daniel.firebasedb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends Activity {

    TextView textViewDisplay;
    Button buttonSmall;
    Button buttonLarge;
    Firebase firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();

        textViewDisplay = (TextView) findViewById(R.id.textViewDisplay);
        buttonLarge = (Button) findViewById(R.id.buttonLarge);
        buttonSmall = (Button) findViewById(R.id.buttonSmall);
        firebaseRef = new Firebase("https://db-app-6edfc.firebaseio.com/data/type");

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                textViewDisplay.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        buttonSmall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                firebaseRef.setValue("Small");
            }
        });

        buttonLarge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                firebaseRef.setValue("Large");
            }
        });
    }
}
