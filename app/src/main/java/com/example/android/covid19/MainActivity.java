package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private static final String USGS_REQUEST_URL =
            "http://api.covid19india.org/state_district_wise.json";


    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "just under creat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> statelist = new ArrayList<>();
        String [] state= {"India","Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir",
                "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan",
                "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
                "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"} ;
        for(int i=0;i<state.length;i++)
        statelist.add(state[i]);

        Spinner location_spinner = (Spinner) findViewById(R.id.location);

        // Create a new {@link ArrayAdapter} of earthquakes
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, statelist);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        location_spinner.setAdapter(adapter);
        //
        //
        //

        Log.v(LOG_TAG, "After spinner");

        ArrayList<info_card> location_card_info =Utils.fetchEarthquakeData(USGS_REQUEST_URL);


//        ArrayList<info_card> location_card_info =new ArrayList<info_card>();
//        // Create an ArrayList of AndroidFlavor objects
//        location_card_info.add(new info_card("Donut", "1.6"));
//        location_card_info.add(new info_card("Eclair", "2.0-2.1"));
//        location_card_info.add(new info_card("Froyo", "2.2-2.2.3"));
//        location_card_info.add(new info_card("GingerBread", "2.3-2.3.7"));
//        location_card_info.add(new info_card("Honeycomb", "3.0-3.2.6"));
//        location_card_info.add(new info_card("Ice Cream Sandwich", "4.0-4.0.4"));
//        location_card_info.add(new info_card("Jelly Bean", "4.1-4.3.1"));

        // Create an {@link AndroidFlavorAdapter}, whose data source is a list of
        // {@link AndroidFlavor}s. The adapter knows how to create list item views for each item
        // in the list.
        Location_card_addapter flavorAdapter = new Location_card_addapter(this, location_card_info);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(flavorAdapter);


        Log.v(LOG_TAG, "After all in the end");

    }
}
