package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PredictionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        String cardclicked = getIntent().getData().toString();

    }
}
