package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static String spinner_location;

    public ArrayList<info_card> global_info;

    private static final String USGS_REQUEST_URL =
            "https://api.covid19india.org/state_district_wise.json";

    String [] state= {"India","Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
            "Dadra and Nagar Haveli", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"} ;
    String [] sort_str={"Alphabetical","Total Case","Recovery Cases","Death Cases","Active Cases","Recovery Rate","Death Rate"};

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "just under creat");
        super.onCreate(savedInstanceState);
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);
        setContentView(R.layout.activity_main);
        ArrayList<String> statelist = new ArrayList<>();
        for(int i=0;i<state.length;i++)
        statelist.add(state[i]);

        //spinner location
        Spinner location_spinner = (Spinner) findViewById(R.id.location_name);
        location_spinner.setOnItemSelectedListener(this );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, statelist);
        location_spinner.setAdapter(adapter);

        //spinner sort
        Spinner sort_list = (Spinner) findViewById(R.id.sort_list);
//        sort_list.setOnItemSelectedListener(this );
        ArrayAdapter<String> sort_list_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, sort_str);
        sort_list.setAdapter(sort_list_adapter);


        //intend to prediction activity
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,PredictionActivity.class);
                info_card infoCard=(info_card)parent.getItemAtPosition(position);
                intent.setData(Uri.parse(infoCard.getLocation_name()));
                startActivity(intent);
            }
        });
    }

// spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner_location = parent.getItemAtPosition(position).toString();
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this,spinner_location,Toast.LENGTH_LONG);
        Update_location_card();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

// sum for total in big circle
    public void total_sum(ArrayList<info_card>arr)
    {
        TextView total_cases=(TextView) findViewById(R.id.total_cases);
        TextView total_recovery=(TextView) findViewById(R.id.total_recovery);
        TextView total_death=(TextView) findViewById(R.id.total_death);
        TextView delta_active=(TextView) findViewById(R.id.one_day_active);
        TextView delta_recovery=(TextView) findViewById(R.id.one_day_recovery);
        TextView delta_death=(TextView) findViewById(R.id.one_day_death);
        TextView delta_total=(TextView) findViewById(R.id.one_day_cases);

        TextView total_active=(TextView) findViewById(R.id.total_active);
        int tc=0,tr=0,td=0,ta=0,dtc=0,dtr=0,dta=0,dtd=0;
//        Log.i(LOG_TAG,"Size is "+arr.size());
//        Log.i(LOG_TAG,"my message "+Utils.state_district_info.get("Jharkhand").size());
        for(int i=0;i<arr.size();i++)
        {
            tc=tc+arr.get(i).getLocation_total_cases();
            tr=tr+arr.get(i).getLocation_recovery();
            td=td+arr.get(i).getLocation_death();
            ta=ta+arr.get(i).getLocation_active();
            dtc=dtc+arr.get(i).getDela_confirmed();
            dtr+=arr.get(i).getDelta_recover();
            dtd=dtd+arr.get(i).getDelta_deaths();
            dta=dta+arr.get(i).getDelta_active();
        }
        total_cases.setText(String.valueOf(tc));
        total_recovery.setText(String.valueOf(tr));
        total_death.setText(String.valueOf(td));
        total_active.setText(String.valueOf(ta));
        delta_active.setText(String.valueOf(dta));
        delta_death.setText(String.valueOf(dtd));
        delta_total.setText(String.valueOf(dtc));
        delta_recovery.setText(String.valueOf(dtr));
    }

    public void Update_location_card()
    {
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        Location_card_addapter flavorAdapter;
        if(spinner_location.equals("India"))
        {flavorAdapter = new Location_card_addapter(this, Utils.india_info);total_sum(Utils.india_info);}
        else
        {flavorAdapter = new Location_card_addapter(this, Utils.state_district_info.get(spinner_location));
            total_sum(Utils.state_district_info.get(spinner_location));}
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(flavorAdapter);
    }


    //search_bar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_edit,menu);
//        MenuItem item=findViewById(R.id.search);
//        SearchView searchView =(SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                ArrayList<info_card> results= new ArrayList<info_card>();
//
//                for(int i=0;i<global_info.size();i++)
//                {
//                    if(global_info.get(i).getLocation_name().toLowerCase().contains(newText.toLowerCase()))
//                    {
//                        results.add(global_info.get(i));
//                    }
//                }
//                Location_card_addapter resadapter=new Location_card_addapter(MainActivity.this,results);
//                ListView listView = (ListView) findViewById(R.id.list);
//                listView.setAdapter(resadapter);
//                return true;
//            }
//        });
//
//        return true;
//    }

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
            if (result == null) {
                return;
            }
            global_info=result;
            Update_location_card();
        }
    }
}
