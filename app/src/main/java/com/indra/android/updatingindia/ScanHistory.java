package com.indra.android.updatingindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScanHistory extends AppCompatActivity {

    static ArrayList<scan_history_info> scan_history_infos_array=new ArrayList<scan_history_info>();
    ListView listView;

    String user_mail;
    String user_id;
    FirebaseAuth mauth;
    FirebaseUser user;
    DatabaseReference dataref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_history);
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.generate);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ScanHistory.this,QrCodeGenerate.class);
                intent.setData(Uri.parse(""));
                startActivity(intent);
            }
        });

        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        user_mail=user.getEmail();
        user_id=user_mail.replace(".",",");
        dataref= FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("qr generated");
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ScanGenFireData qrdata=ds.getValue(ScanGenFireData.class);

                    scan_history_infos_array.add(new scan_history_info(qrdata.getLocation_name()));
                    listView=findViewById(R.id.listview_scan_history);
                    ScanHistoryAddapter list_addapter = new ScanHistoryAddapter(ScanHistory.this, scan_history_infos_array);
                    listView.setAdapter(list_addapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listView=findViewById(R.id.listview_scan_history);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(ScanHistory.this,QrCodeGenerate.class);

                scan_history_info infoCard=(scan_history_info) parent.getItemAtPosition(position);
                intent.setData(Uri.parse(infoCard.getLocation_name()));
                startActivity(intent);
            }
        });
    }
}