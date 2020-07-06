package com.indra.android.updatingindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Scanner;

public class ScanAlert extends AppCompatActivity {



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

        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        user_mail=user.getEmail();
        user_id=user_mail.replace(".",",");


        dataref= FirebaseDatabase.getInstance().getReference().child("users");



        setContentView(R.layout.activity_scan_alert);
        scannerView= findViewById(R.id.scanner_view);
        codeScanner= new CodeScanner(this,scannerView);
        final TextView textView=findViewById(R.id.textView2);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getBaseContext(), "Clickeddddd"+result.getText() , Toast.LENGTH_SHORT).show();


                       // dataref.child(user_id).child("qr generated").child(input_location).setValue(qrdata);

                        textView.setText(result.getText());
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

        Toast.makeText(this, "Clickeddddd"+item.getItemId(), Toast.LENGTH_SHORT).show();
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