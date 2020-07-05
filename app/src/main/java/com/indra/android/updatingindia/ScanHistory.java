package com.indra.android.updatingindia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ScanHistory extends AppCompatActivity {

    static ArrayList<scan_history_info> scan_history_infos_array=new ArrayList<scan_history_info>();
    ListView listView;
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
        scan_history_infos_array.add(new scan_history_info("bhawanathpur"));
        scan_history_infos_array.add(new scan_history_info("garhwa",23));
        listView=findViewById(R.id.listview_scan_history);
        ScanHistoryAddapter list_addapter = new ScanHistoryAddapter(this, scan_history_infos_array);
        listView.setAdapter(list_addapter);
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