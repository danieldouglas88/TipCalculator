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

import java.time.Instant;
import java.util.Random;

public class MainActivity extends Activity {

    TextView textViewDisplay;
    TextView textViewUn;
    TextView textViewRandom;
    Button buttonSmall;
    Button buttonLarge;
    Firebase firebaseRef;
    Firebase firebaseRefTwo;
    Firebase firebaseRefThree;
    Random rand = new Random();
    String[] array = {"Earth", "Pluto", "Uranus", "Mars", "Neptune", "Jupiter", "Cosmos", "Milky Way", "Venus", "The Moon", "Black Hole"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();

        textViewDisplay = (TextView) findViewById(R.id.textViewDisplay);
        textViewUn = (TextView) findViewById(R.id.textViewUn);
        textViewRandom = (TextView) findViewById(R.id.textViewRandom);
        buttonLarge = (Button) findViewById(R.id.buttonLarge);
        buttonSmall = (Button) findViewById(R.id.buttonSmall);
        firebaseRef = new Firebase("https://db-app-6edfc.firebaseio.com/data/type");
        firebaseRefTwo = new Firebase("https://db-app-6edfc.firebaseio.com/stuff");
        firebaseRefThree = new Firebase("https://db-app-6edfc.firebaseio.com/random");


        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                textViewUn.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        firebaseRefThree.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                textViewRandom.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        firebaseRefTwo.addValueEventListener(new ValueEventListener() {
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

                firebaseRefTwo.setValue("Button Click Time: " + new java.util.Date());
                int  n = rand.nextInt(1000000000) + 1;
                int  i = rand.nextInt(array.length) + 0;
                firebaseRef.setValue(array[i]);
                firebaseRefThree.setValue("Random number: " + n);
                buttonSmall.setText(array[i]);

            }
        });

        buttonLarge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                firebaseRefTwo.setValue("Button Click Time: " + new java.util.Date());
                int  n = rand.nextInt(1000000000) + 1;
                int  i = rand.nextInt(array.length) + 0;
                firebaseRef.setValue(array[i]);
                firebaseRefThree.setValue("Random number: " + n);
                buttonLarge.setText(array[i]);
            }
        });
    }
}
