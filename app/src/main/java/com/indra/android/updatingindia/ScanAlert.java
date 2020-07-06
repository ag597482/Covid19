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

import java.util.Scanner;

public class ScanAlert extends AppCompatActivity {

    TextView location_name_scan,total_people_inside,covid_cases_in_last_7_days;
    String user_mail;
    String user_id;
    FirebaseAuth mauth;
    FirebaseUser user;
    DatabaseReference dataref;
    CodeScanner codeScanner;
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
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        String location_name = "nothing";
                                        String qr_text;
                                        long qr_num,total_scan_Count,present_scan_count=0,total_covid_cases_in_last_7_days=0;
                                        for(DataSnapshot ds: dataSnapshot.getChildren())
                                        {
                                            Log.i("-------","-------------------------------"+ds.getKey());
                                            if(ds.getKey().equals("location_name"))
                                                location_name=ds.getValue(String.class);
                                            if(ds.getKey().equals("total_covid_cases_in_last_7_days"))
                                                total_covid_cases_in_last_7_days=ds.getValue(long.class);
                                            if(ds.getKey().equals("present_scan_count"))
                                            {
                                                present_scan_count=ds.getValue(long.class)+1;
                                                dataref.child("globle").child("qr location").child(result.getText()).child("qr detail").child("present_scan_count").setValue(present_scan_count);
                                            }
                                            if(ds.getKey().equals("total_scan_Count"))
                                            {
                                                total_scan_Count=ds.getValue(long.class)+1;
                                                dataref.child("globle").child("qr location").child(result.getText()).child("qr detail").child("total_scan_Count").setValue(total_scan_Count);

                                            }
                                        }

                                        location_name_scan.setText(location_name);
                                        total_people_inside.setText(String.valueOf(present_scan_count));
                                        covid_cases_in_last_7_days.setText(String.valueOf(total_covid_cases_in_last_7_days));
                                        Toast.makeText(getBaseContext(), "location  "+dataSnapshot.getValue() , Toast.LENGTH_SHORT).show();
                                        Log.i("location","--------------------------"+location_name+" --------"+result.getText());

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


    @Override
    public void onResume() {
        super.onResume();
        requestForCamera();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
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