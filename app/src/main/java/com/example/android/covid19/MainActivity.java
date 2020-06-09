package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://api.covid19india.org/state_district_wise.json";
    TextView textView,active,deaths,rec;
    ProgressBar progressBar;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        textView =(TextView)findViewById(R.id.total);
        active = (TextView)findViewById(R.id.active);
        deaths = (TextView)findViewById(R.id.deaths);
        rec = (TextView)findViewById(R.id.recovered);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        spinner = (Spinner)findViewById(R.id.spinner);
        final ListView listView = (ListView)findViewById(R.id.list);




        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();

              //  ArrayList<String> distictsinstate = new ArrayList<String>(QueryUtils.disArray);

                ArrayList<info_card> num= new ArrayList<info_card>(QueryUtils.all.get(tutorialsName));

                Location_card_addapter location_card_addapter=new Location_card_addapter(MainActivity.this,num);

               // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,distictsinstate);
                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,Toast.LENGTH_LONG).show();
                listView.setAdapter(location_card_addapter);
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

    }
    public void total_sum(ArrayList<info_card>arr)
    {
        TextView total_cases=(TextView) findViewById(R.id.total_cases);
        TextView total_recovery=(TextView) findViewById(R.id.total);
        TextView total_death=(TextView) findViewById(R.id.deaths);

        TextView total_active=(TextView) findViewById(R.id.active);
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

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<String> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;

        }

        @Override
        protected void onPostExecute(List<String> data) {
            if (data != null && !data.isEmpty()) {
               // textView.setText(data.toString());
                textView.setText(String.valueOf(QueryUtils.total1));
                active.setText(String.valueOf(QueryUtils.active));
                deaths.setText(String.valueOf(QueryUtils.deaths));
                rec.setText(String.valueOf(QueryUtils.recovered));
                ArrayList<String> states = new ArrayList<>(QueryUtils.all.keySet());

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item,states);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);

                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
