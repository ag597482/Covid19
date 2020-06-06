package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView.SearchAutoComplete;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        ArrayList<String> statelist = new ArrayList<String>(QueryUtils.spinnerArray);
        ArrayList<String> districtlist = new ArrayList<String>(QueryUtils.disArray);

        ListView lv = (ListView) findViewById(R.id.detaillist);

        Intent intent = getIntent();

        if(intent.getData().toString().equals("A"))
        {
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,statelist);
        }
        else if(intent.getData().toString().equals("B"))
        {
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districtlist);
        }
        lv.setAdapter(adapter);

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent(DetailedActivity.this, DetailedListActivity.class);
//
////                Uri currentUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI,id);
////
////                intent.setData(currentUri);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar,menu);
        MenuItem menuItem = menu.findItem(R.id.serch);

        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        }
        );

        return super.onCreateOptionsMenu(menu);
    }
}
