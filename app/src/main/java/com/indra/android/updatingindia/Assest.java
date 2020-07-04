package com.indra.android.updatingindia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class Assest extends AppCompatActivity {



    TextView nameid,reportno,reportdetails;
    Button submitreport;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assest);

        nameid = findViewById(R.id.nid);
        reportno =findViewById(R.id.rno);
        reportdetails = findViewById(R.id.rd);
        submitreport = findViewById(R.id.submittest);


        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("tests");



        submitreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameid.getText().toString().trim().length()==0)
                {
                    Toast.makeText(Assest.this,"Name / id Can't be left blank",Toast.LENGTH_SHORT).show();
                }
                else if(reportno.getText().toString().trim().length()==0)
                {
                    Toast.makeText(Assest.this,"Report no Can't be left blank",Toast.LENGTH_SHORT).show();
                }
                else {


                    Calendar calendar=Calendar.getInstance();
                    String time= DateFormat.getDateInstance().format(calendar.getTime());
                    Report report = new Report(nameid.getText().toString().trim(),reportno.getText().toString().trim(),time);


                    databaseReference.child(nameid.getText().toString().trim()).setValue(report);

                    Toast.makeText(getBaseContext(), "Test submitted ..!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }
}