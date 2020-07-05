package com.indra.android.updatingindia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    TextView name,mobile,feedbackmessage;
    Button submit;

    protected Button signout;

    FirebaseAuth mauth;
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


        mauth= FirebaseAuth.getInstance();



        signout=findViewById(R.id.sign_Out);
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


                    //databaseReference.push().setValue(feedback);

                    feedbackmessage.setText("");
                    name.setText("");
                    mobile.setText("");
                    Toast.makeText(FeedbackActivity.this, "Feedback Recorded..!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.signOut();
                Intent intent=new Intent(FeedbackActivity.this,Registration.class);
                startActivity(intent);
                finish();
            }
        });



    }
}
