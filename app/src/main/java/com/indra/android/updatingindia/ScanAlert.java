package com.indra.android.updatingindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

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

    TextView location_name_scan,total_people_inside,covid_cases_in_last_7_days,in_location_time;
    Guideline guideline;
    Button exit_button;
    String user_mail;
    String user_id;
    FirebaseAuth mauth;
    FirebaseUser user;
    DatabaseReference dataref;
    CodeScanner codeScanner;
    String pre_entry_time,pre_qr_text;
//    Button exit_button;

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



        scannerView= findViewById(R.id.scanner_view);
        codeScanner= new CodeScanner(this,scannerView);

        location_name_scan=findViewById(R.id.location_name_scan);
        total_people_inside=findViewById(R.id.total_people_inside);
        covid_cases_in_last_7_days=findViewById(R.id.covid_cases_in_last);
        in_location_time=findViewById(R.id.in_location_time);
        guideline=findViewById(R.id.guideline);
        guideline.setGuidelinePercent(0.0f);

        scannerView.setVisibility(View.GONE);


        exit_button=findViewById(R.id.exit_button);
        exit_button.setVisibility(View.GONE);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                DateFormat df = new SimpleDateFormat("dd-MM-yy_HH:mm:ss");
                final String time= df.format(calendar.getTime());
                dataref.child("users").child(user_id).child("qr scan history").child("scan detail")
                        .child("in location").setValue("nothing");
                set_scan_history_end_time(pre_qr_text,pre_entry_time,time);
                check_person_entry_detail_and_action();
            }
        });


        check_person_entry_detail_and_action();
        final TextView textView=findViewById(R.id.scan_text_view);


        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }
    private void check_person_entry_detail_and_action()
    {

        dataref.child("users").child(user_id).child("qr scan history").child("scan detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((!dataSnapshot.exists())||dataSnapshot.child("in location").getValue(String.class).equals("nothing"))
                {
                    scannerView.setVisibility(View.VISIBLE);
                    guideline.setGuidelinePercent(1);
                    codeScanner.setDecodeCallback(new DecodeCallback() {
                        @Override
                        public void onDecoded(@NonNull final Result result) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

//                        Toast.makeText(getBaseContext(), "Clickeddddd"+result.getText() , Toast.LENGTH_SHORT).show();
                                    if(result.getText().contains("Indra_co"))
                                    {
                                        Calendar calendar=Calendar.getInstance();
                                        DateFormat df = new SimpleDateFormat("dd-MM-yy_HH:mm:ss");
                                        final String time= df.format(calendar.getTime());


                                        get_qr_detail_from_firebase_set_ui(result.getText());
                                        guideline.setGuidelinePercent(0.0f);

                                        scannerView.setVisibility(View.GONE);

                                        Log.i("location","--------inside scan real he he  detail ------------------"+result.getText());
                                        dataref.child("users").child(user_id).child("qr scan history").child("scan detail")
                                                .child("in location").setValue(result.getText());
                                        dataref.child("users").child(user_id).child("qr scan history").child("scan detail")
                                                .child("in entry time").setValue(time);
                                        pre_qr_text=result.getText();
                                        pre_entry_time=time;
                                        set_scan_history_start_time(result.getText(),time);
                                    }
                                    else
                                    {
                                        Toast.makeText(getBaseContext(), "Incorect QR code not indra"+result.getText() , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
                else
                {
                    pre_qr_text=dataSnapshot.child("in location").getValue(String.class);
                    pre_entry_time=dataSnapshot.child("in entry time").getValue(String.class);
                    get_qr_detail_from_firebase_set_ui(dataSnapshot.child("in location").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void check_if_already_in_location()
    {

    }

    private void get_qr_detail_from_firebase_set_ui(final String qr_text)
    {
        guideline.setGuidelinePercent(0.0f);

        scannerView.setVisibility(View.GONE);
        exit_button.setVisibility(View.VISIBLE);

        dataref.child("globle").child("qr location").child(qr_text).child("qr detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    in_location_time.setText(pre_entry_time);
                    location_name=dataSnapshot.child("location_name").getValue(String.class);
                    total_covid_cases_in_last_7_days=dataSnapshot.child("total_covid_cases_in_last_7_days").getValue(long.class);

                    present_scan_count=dataSnapshot.child("present_scan_count").getValue(long.class);


                    total_scan_Count=dataSnapshot.child("total_scan_Count").getValue(long.class);


                    location_name_scan.setText(location_name);
                    total_people_inside.setText(String.valueOf(present_scan_count+1));
                    covid_cases_in_last_7_days.setText(String.valueOf(total_covid_cases_in_last_7_days));



                    Log.i("IN SET UI","--------------------------"+pre_entry_time+" --------"+qr_text);

                }
                else
                {
                    Toast.makeText(getBaseContext(), "Incorect QR in ondatachange code"+qr_text , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "Error in class ScanAlert in get_qr_detail_from_firebase_set_ui function"+qr_text , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void set_scan_history_end_time(final String text, String enter_time, String exit_time) {
        Log.i("in history end time","-----------------------------------"+text+"          "+enter_time+"      "+exit_time);

        dataref.child("globle").child("qr location").child(text).child("qr detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Long present_scan_count_end;
                present_scan_count_end=dataSnapshot.child("present_scan_count").getValue(long.class);

                dataref.child("globle").child("qr location").child(text).child("qr detail").child("present_scan_count").setValue(present_scan_count_end-1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        dataref.child("users").child(user_id).child("qr scan history").child("qr scan history list").child(text).child(enter_time).child("exit time").setValue(exit_time);
//        In globle
        dataref.child("globle").child("qr location").child(text).child("scan history").child(user_id).child(enter_time).child("exit time").setValue(exit_time);

    }

    private void set_scan_history_start_time(final String qr_text, String time) {


        dataref.child("globle").child("qr location").child(qr_text).child("qr detail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Long present_scan_count_end,total_scan_start;
                present_scan_count_end=dataSnapshot.child("present_scan_count").getValue(long.class);
                total_scan_start=dataSnapshot.child("total_scan_Count").getValue(long.class);
                dataref.child("globle").child("qr location").child(qr_text).child("qr detail").child("present_scan_count").setValue(present_scan_count_end+1);
                dataref.child("globle").child("qr location").child(qr_text).child("qr detail").child("total_scan_Count").setValue(total_scan_start+1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
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