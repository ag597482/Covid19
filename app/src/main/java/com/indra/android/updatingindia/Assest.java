package com.indra.android.updatingindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Assest extends AppCompatActivity {

    String user_mail;
    String user_id;

    TextView nameid,reportno,reportdetails,fetch;
    Button submitreport;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,userref,shopref;

    FirebaseAuth mauth;
    FirebaseUser user;

    Map<String,String> startd,endd;
    ArrayList<String> list;

    private ValueEventListener eventListener,eventListener1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assest);

        nameid = findViewById(R.id.nid);
        reportno =findViewById(R.id.rno);
        reportdetails = findViewById(R.id.rd);
        submitreport = findViewById(R.id.submittest);
        fetch = findViewById(R.id.fet);

        startd = new HashMap<>();
        endd = new HashMap<>();

        list = new ArrayList<>();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("tests");


        mauth= FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        user_mail=user.getEmail();
        user_id=user_mail.replace(".",",");

        userref = firebaseDatabase.getReference().child("users").child(user_id).child("qr scan history list");
        shopref = firebaseDatabase.getReference().child("globe").child("qr location");



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
                    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                    String time= df.format(calendar.getTime());
                    Report report = new Report(user_id,reportno.getText().toString().trim(),time);


                    databaseReference.child(user_id).setValue(report);


                    eventListener = new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            startd.clear();
                            endd.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                String shopname = ds.getValue(String.class);



                                for(DataSnapshot d : ds.getChildren())
                                {

                                    String entryt = d.child("entry time").getValue(String.class);
                                    String exitt = d.child("exit time").getValue(String.class);

                                    startd.put(shopname,entryt);
                                    endd.put(shopname,exitt);



                                    list.clear();
                                    eventListener1 = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            String usersid = dataSnapshot.getValue(String.class);

                                            list.add(usersid);



                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    };

                                    shopref.child(shopname).child("scan history").addValueEventListener(eventListener1);


                                }


                            }

                            fetch.setText("Fetching - > "+  startd.toString());


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    userref.addValueEventListener(eventListener);


                    ////
                   // Toast.makeText(getBaseContext(), "Test submitted ..! " + list.toString() + startd.toString() , Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userref.removeEventListener(eventListener);
    }
}