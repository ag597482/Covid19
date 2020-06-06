package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button ct,st,dt;
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://api.covid19india.org/state_district_wise.json";
    TextView textView,active,deaths,rec;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ct=(Button)findViewById(R.id.ct);
        st=(Button)findViewById(R.id.st);
        dt=(Button)findViewById(R.id.dt);



        textView =(TextView)findViewById(R.id.total);
        active = (TextView)findViewById(R.id.active);
        deaths = (TextView)findViewById(R.id.deaths);
        rec = (TextView)findViewById(R.id.recovered);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        QueryUtils.disArray.add("Aman");

        ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,DetailedActivity.class);

              //  Uri currentUri = ContentUris.withAppendedId("A");
                i.setData(Uri.parse("A"));
                startActivity(i);

            }
        });

        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,DetailedActivity.class);

                //  Uri currentUri = ContentUris.withAppendedId("A");
                i.setData(Uri.parse("B"));
                startActivity(i);

            }
        });

        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,PredictionActivity.class);
                startActivity(i);

            }
        });

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

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

                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
