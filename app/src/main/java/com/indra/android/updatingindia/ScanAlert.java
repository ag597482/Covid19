package com.indra.android.updatingindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class ScanAlert extends AppCompatActivity {

    TextView location_name_scan,total_people_inside,covid_cases_in_last_7_days;
    String user_mail;
    String user_id;
    FirebaseAuth mauth;
    FirebaseUser user;
    DatabaseReference dataref;
    CodeScanner codeScanner;
    Button exit_button;

    int inlocation=0;

    String location_name = "nothing";
    String qr_text;
    long qr_num,total_scan_Count=0,present_scan_count=0,total_covid_cases_in_last_7_days=0;
    CodeScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_alert);

        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        user_mail=user.getEmail();
        user_id=user_mail.replace(".",",");
        dataref= FirebaseDatabase.getInstance().getReference();


        location_name_scan=findViewById(R.id.location_name_scan);
        total_people_inside=findViewById(R.id.total_people_inside);
        covid_cases_in_last_7_days=findViewById(R.id.covid_cases_in_last);

//        exit_button.findViewById(R.id.exit_button);
//        exit_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        if(inlocation==1)
//        {
//            location_name_scan.setText(location_name);
//            total_people_inside.setText(String.valueOf(present_scan_count));
//            covid_cases_in_last_7_days.setText(String.valueOf(total_covid_cases_in_last_7_days));
//        }

        scannerView= findViewById(R.id.scanner_view);
        codeScanner= new CodeScanner(this,scannerView);
        final TextView textView=findViewById(R.id.scan_text_view);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        Toast.makeText(getBaseContext(), "Clickeddddd"+result.getText() , Toast.LENGTH_SHORT).show();
                        if(result.getText().contains("Indra_co"))
                        {
                            dataref.child("globle").child("qr location").child(result.getText()).child("qr detail").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        Calendar calendar=Calendar.getInstance();
                                        DateFormat df = new SimpleDateFormat("dd-MM-yy_HH:mm");
                                        final String time= df.format(calendar.getTime());

                                        location_name=dataSnapshot.child("location_name").getValue(String.class);
                                        total_covid_cases_in_last_7_days=dataSnapshot.child("total_covid_cases_in_last_7_days").getValue(long.class);

                                        present_scan_count=dataSnapshot.child("present_scan_count").getValue(long.class)+1;
                                        dataref.child("globle").child("qr location").child(result.getText()).child("qr detail").child("present_scan_count").setValue(present_scan_count);

                                        total_scan_Count=dataSnapshot.child("total_scan_Count").getValue(long.class)+1;
                                        dataref.child("globle").child("qr location").child(result.getText()).child("qr detail").child("total_scan_Count").setValue(total_scan_Count);


                                        location_name_scan.setText(location_name);
                                        total_people_inside.setText(String.valueOf(present_scan_count));
                                        covid_cases_in_last_7_days.setText(String.valueOf(total_covid_cases_in_last_7_days));


                                        Toast.makeText(getBaseContext(), "location  "+dataSnapshot.getValue() , Toast.LENGTH_SHORT).show();
                                        Log.i("location","--------------------------"+time+" --------"+result.getText());


                                        dataref.child("users").child(user_id).child("qr scan history").child("scan detail").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                                if(dataSnapshot2.exists())
                                                {
                                                    Log.i("location","--------inside scan detail ------------------"+dataSnapshot2.child("in location").getValue(String.class)+" --------"+result.getText());
                                                    if(dataSnapshot2.child("in location").getValue(String.class).equals("nothing"))
                                                    {
                                                        Log.i("location","--------inside scan detail ------------------"+qr_text);
                                                        dataref.child("users").child(user_id).child("qr scan history").child("scan detail")
                                                                .child("in location").setValue(result.getText());
                                                        dataref.child("users").child(user_id).child("qr scan history").child("scan detail")
                                                                .child("in entry time").setValue(time);
                                                        set_scan_history_start_time(result.getText(),time);

                                                    }
                                                    else
                                                    {
                                                        Log.i("location","--------inside scan detail ------------------"+dataSnapshot2.child("in location").getValue(String.class)+" --------"+result.getText());
                                                        String pre_time=dataSnapshot2.child("in entry time").getValue(String.class);
//                                                        set_scan_history_end_time(result.getText(),time);
                                                        set_scan_history_start_time(result.getText(),time);

                                                        dataref.child("users").child(user_id).child("qr scan history").child("scan detail")
                                                                .child("in location").setValue("nothing");
                                                    }
                                                }
                                                else
                                                {
                                                    dataref.child("users").child(user_id).child("qr scan history").child("scan detail")
                                                            .child("in location").setValue(result.getText());
                                                    dataref.child("users").child(user_id).child("qr scan history").child("scan detail")
                                                            .child("in entry time").setValue(time);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                    else
                                    {
                                        Toast.makeText(getBaseContext(), "Incorect QR in ondatachange code"+result.getText() , Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getBaseContext(), "Error in class ScanAlert in onDecode"+result.getText() , Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(), "Incorect QR code not indra"+result.getText() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    private void set_scan_history_end_time(String text, String time) {

        Log.i("---end time","000000000000------------------------------"+text+" "+time);
        dataref.child("users").child(user_id).child("qr scan history").child("qr scan history list").child(qr_text).child(time).child("entry time").setValue(time);
//        In globle
//        dataref.child("globle").child("qr location").child(qr_text).child("scan history").child(user_id).child(time).child("entry time").setValue(time);

    }

    private void set_scan_history_start_time(String qr_text,String time) {

//        In user history
        dataref.child("users").child(user_id).child("qr scan history").child("qr scan history list").child(qr_text).child(time).child("entry time").setValue(time);
//        In globle
        dataref.child("globle").child("qr location").child(qr_text).child("scan history").child(user_id).child(time).child("entry time").setValue(time);

    }


    @Override
    public void onResume() {
        super.onResume();
        requestForCamera();
    }

    @Override
    public void onPause() {
//        codeScanner.releaseResources();
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qr_generate, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.qr_gen:

                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(ScanAlert.this,ScanHistory.class);
                startActivity(intent);
                return true;
        }


        return super.onOptionsItemSelected(item);

    }


    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(ScanAlert.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }

}