package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    TextView name,mobile,feedbackmessage;
    Button submit;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        feedbackmessage = findViewById(R.id.feedback);
        submit=findViewById(R.id.submit);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("feedbacks");



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Feedback feedback = new Feedback(name.getText().toString().trim(),mobile.getText().toString().trim(),feedbackmessage.getText().toString().trim());
                databaseReference.push().setValue(feedback);

                feedbackmessage.setText("");
            }
        });



    }
}
