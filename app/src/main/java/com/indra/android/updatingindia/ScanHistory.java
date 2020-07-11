package com.indra.android.updatingindia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScanHistory extends AppCompatActivity {

    static ArrayList<scan_history_info> scan_history_infos_array;//=new ArrayList<scan_history_info>();


    ListView listView;
    String user_mail;
    String user_id;
    FirebaseAuth mauth;
    FirebaseUser user;
    DatabaseReference dataref;
    ScanHistoryAddapter list_addapter;
    scan_history_recycle_view scan_history_recycle_view_addapter;
    RecyclerView recyclerView;

    private ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_history);


        scan_history_infos_array=new ArrayList<scan_history_info>();

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


//        listView=findViewById(R.id.listview_scan_history);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scan_history_infos_array.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String qr_text=ds.getKey();
                    Log.i("ScanHistory","------------------------------- "+qr_text);
                    FirebaseDatabase.getInstance().getReference().child("globle").child("qr location").child(qr_text).child("qr detail")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    ScanGenFireData qrdata=dataSnapshot2.getValue(ScanGenFireData.class);
                                    scan_history_infos_array.add(new scan_history_info(qrdata.getLocation_name(),qrdata.getTotal_scan_Count(),qrdata.getQr_text()));
                                    Log.i("array size"," ------------------------------------- "+scan_history_infos_array.size());
                                    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
                                    scan_history_recycle_view_addapter=new scan_history_recycle_view(ScanHistory.this,scan_history_infos_array);
                                    recyclerView.setAdapter(scan_history_recycle_view_addapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dataref.addValueEventListener(eventListener);



//        recyclerView.s
//                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent=new Intent(ScanHistory.this,QrCodeGenerate.class);
//
//                scan_history_info infoCard=(scan_history_info) parent.getItemAtPosition(position);
//                intent.setData(Uri.parse(infoCard.getQr_text()));
//                startActivity(intent);
//            }
//        });



    }
    scan_history_info deteted_data=null;
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deteted_data=scan_history_infos_array.get(viewHolder.getAdapterPosition());
            final int pos=viewHolder.getAdapterPosition();
            scan_history_infos_array.remove(viewHolder.getAdapterPosition());
            scan_history_recycle_view_addapter.notifyDataSetChanged();
            Snackbar.make(recyclerView,deteted_data.getLocation_name(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scan_history_infos_array.add(pos,deteted_data);
                            scan_history_recycle_view_addapter.notifyDataSetChanged();
                        }
                    }).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataref.removeEventListener(eventListener);
    }
}