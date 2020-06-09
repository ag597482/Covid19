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
            "https://api.covid19india.org/state_district_wise.json";


    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);
        Log.v(LOG_TAG, "just under creat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> statelist = new ArrayList<>();
        String [] state= {"India","Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
                "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"} ;
        for(int i=0;i<state.length;i++)
        statelist.add(state[i]);

        Spinner location_spinner = (Spinner) findViewById(R.id.location);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, statelist);

        location_spinner.setAdapter(adapter);
        //

    }


    public void total_sum(ArrayList<info_card>arr)
    {
        TextView total_cases=(TextView) findViewById(R.id.total_cases);
        TextView total_recovery=(TextView) findViewById(R.id.total_recovery);
        TextView total_death=(TextView) findViewById(R.id.total_death);

        TextView total_active=(TextView) findViewById(R.id.total_death);
        int tc=0,tr=0,td=0,ta=0;
        for(int i=0;i<arr.size();i++)
        {
            tc=tc+arr.get(i).getLocation_total_cases();
            tr=tr+arr.get(i).getLocation_recovery();
            td=td+arr.get(i).getLocation_death();
            ta=ta+arr.get(i).getLocation_active();
        }
        total_cases.setText(String.valueOf(tc));
        total_recovery.setText(String.valueOf(tr));
        total_death.setText(String.valueOf(td));
        total_active.setText(String.valueOf(ta));

    }

    public void Update_location_card(ArrayList<info_card> git)
    {
        total_sum(git);
        Location_card_addapter flavorAdapter = new Location_card_addapter(this, git);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(flavorAdapter);
    }



    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<info_card> > {

        protected ArrayList<info_card> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            ArrayList<info_card>  result = Utils.fetchEarthquakeData(urls[0]);
            return result;
        }


        protected void onPostExecute(ArrayList<info_card>  result) {
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }
            Update_location_card(result);
        }
    }
}
