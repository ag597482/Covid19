package com.indra.android.updatingindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
    ArrayList<String> list,s1,e1,s2,e2,noti;

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
        s1=new ArrayList<>();
        e1= new ArrayList<>();
        s2 = new ArrayList<>();
        e2 = new ArrayList<>();
        noti = new ArrayList<>();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("tests");


        mauth= FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        user_mail=user.getEmail();
        user_id=user_mail.replace(".",",");

        userref = firebaseDatabase.getReference().child("users").child(user_id).child("qr scan history").child("qr scan history list");
        shopref = firebaseDatabase.getReference().child("globle").child("qr location");



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
                                    list.clear();
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                        String shopname = ds.getKey().toString();
                                       // list.add(shopname);



                                        s1.clear();
                                        e1.clear();
                                        for(DataSnapshot d : ds.getChildren())
                                        {

                                            String entryt = d.child("entry time").getValue(String.class);
                                            String exitt = d.child("exit time").getValue(String.class);


                                            s1.add(entryt);
                                            e1.add(exitt);
                                            startd.put(shopname,entryt);
                                            endd.put(shopname,exitt);



                                            Log.e("TAG",s1.toString()+ "A" + e1.toString() );


//                                            shopref.child(shopname).child("scan history").addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    for (DataSnapshot dsa : dataSnapshot.getChildren()) {
//
//
//
//                                                        String usname1  = dsa.getKey().toString();
//
//                                                        s2.clear();
//                                                        e2.clear();
//
//                                                        for(DataSnapshot d1 : dsa.getChildren()) {
//
//                                                            String entryt1 = d1.child("entry time").getValue(String.class);
//                                                            String exitt1 = d1.child("exit time").getValue(String.class);
//
//                                                            s2.add(entryt1);
//                                                            e2.add(exitt1);
//
//
//                                                        }
//                                                        Log.e("TAG",s2.toString()+ "Aa" + e2.toString() );
//                                                            list.add(usname1);
//
//
//
//
//                                                    }
//
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                }
//                                            });
                                    eventListener1 = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                            for (DataSnapshot dsa : dataSnapshot.getChildren()) {



                                                String usname1  = dsa.getKey().toString();

                                                s2.clear();
                                                e2.clear();

                                                for(DataSnapshot d1 : dsa.getChildren()) {

                                                    String entryt1 = d1.child("entry time").getValue(String.class);
                                                    String exitt1 = d1.child("exit time").getValue(String.class);

                                                    s2.add(entryt1);
                                                    e2.add(exitt1);


                                                }
                                                Log.e("TAG",s2.toString()+ "Aa" + e2.toString());

                                                if(fun(s1,e1,s2,e2))
                                                {


                                                    firebaseDatabase.getReference().child("users").child(usname1).child("profile").child("title_ofcall")
                                                            .setValue("N");
                                                    Log.e("TAG","#" + usname1);
                                                    list.add(usname1);
                                                }






                                                }



                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    };

                                    shopref.child(shopname).child("scan history").addValueEventListener(eventListener1);


                                        }



                                    }

//                                    for(int j = 0 ;j< list.size();j++) {
//                                        eventListener1 = new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                                                Log.e("TAG", "AaA@");
//                                                for (DataSnapshot dsa : dataSnapshot.getChildren()) {
//
//
//                                                    String usname1 = dsa.getKey().toString();
//
//                                                    noti.add(usname1);
//
//
//                                                    Log.e("TAG", "AA@"+usname1);
//
//
//
//                                                }
//
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        };
//
//                                        shopref.child(list.get(j)).child("scan history").addListenerForSingleValueEvent(eventListener1);//ValueEventListener(eventListener1);
//
//                                    }
//
//                                    fetch.setText("Fetching - > "+  list.toString()+ "@" + s1.toString() +"@"+ noti.toString());


                                    fetch.setText("Fetching - > "+  list.toString()+ "@" + s1.toString() +"@"+ noti.toString());

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

    private boolean fun(ArrayList<String> s1, ArrayList<String> e1, ArrayList<String> s2, ArrayList<String> e2) {


        if(s2.size()>0)
            return true;

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userref.removeEventListener(eventListener);
    }
}