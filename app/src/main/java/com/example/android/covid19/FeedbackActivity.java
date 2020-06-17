package com.example.android.covid19;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;
import java.util.Date;

public class FeedbackActivity extends AppCompatActivity {

    TextView name,mobile,feedbackmessage,name1,name2,mobi1,mobi2;
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

        name1=findViewById(R.id.name1);
        mobi1=findViewById(R.id.mobi1);
        name2=findViewById(R.id.name2);
        mobi2=findViewById(R.id.mobi2);

        long i = (long) new Date().getTime();
        i=i%2;
        int random=(int)i;
        Log.i(String.valueOf(this),"---------time------ time"+i);
        if(random==1)
        {
            name1.setText("Rajan Jaiswal");
            mobi1.setText("8404855885");
            name2.setText("Aman Gupta");
            mobi2.setText("7737476484");
        }
        else
        {
            name2.setText("Rajan Jaiswal");
            mobi2.setText("8404855885");
            name1.setText("Aman Gupta");
            mobi1.setText("7737476484");
        }

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("feedbacks");



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().trim().length()==0)
                {
                    Toast.makeText(FeedbackActivity.this,"Name Can't be left blank",Toast.LENGTH_SHORT).show();
                }
                else if(feedbackmessage.getText().toString().trim().length()==0)
                {
                    Toast.makeText(FeedbackActivity.this,"Feedback Can't be left blank",Toast.LENGTH_SHORT).show();
                }
                else {
                    Feedback feedback = new Feedback(name.getText().toString().trim(), mobile.getText().toString().trim(), feedbackmessage.getText().toString().trim());


                    databaseReference.push().setValue(feedback);

                    feedbackmessage.setText("");
                    name.setText("");
                    mobile.setText("");
                    Toast.makeText(FeedbackActivity.this, "Feedback Recorded..!", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
