package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class PredictionActivity extends AppCompatActivity {

    private static final String USGS_REQUEST_URL =
            "https://api.rootnet.in/covid19-in/hospitals/medical-colleges";


    public static final String LOG_TAG = PredictionActivity.class.getSimpleName();
    public static String location_slected;
    public static String spinner_location=MainActivity.spinner_location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//
//        prog.setVisibility(View.VISIBLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        location_slected = getIntent().getData().toString();
        Log.i(LOG_TAG,location_slected+" "+spinner_location);
        TextView top_location_name=findViewById(R.id.hospital_name);
        top_location_name.setText(location_slected);


        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

        if(spinner_location.equals("India"))
        {total_sum(Utils.india_info);}
        else
        {total_sum(Utils.state_district_info.get(spinner_location));}
    }



    public void total_sum(ArrayList<info_card> arr)
    {
        TextView total_cases=(TextView) findViewById(R.id.total_cases);
        TextView total_recovery=(TextView) findViewById(R.id.total_recovery);
        TextView total_death=(TextView) findViewById(R.id.total_death);
        TextView delta_active=(TextView) findViewById(R.id.one_day_active);
        TextView delta_recovery=(TextView) findViewById(R.id.one_day_recovery);
        TextView delta_death=(TextView) findViewById(R.id.one_day_death);
        TextView delta_total=(TextView) findViewById(R.id.one_day_cases);
        TextView recovery_rate=findViewById(R.id.recover_rate_value);
        TextView death_rate=findViewById(R.id.Mortality_rate_value);

        TextView survival_rate=findViewById(R.id.survival_rate_value);

        TextView total_active=(TextView) findViewById(R.id.total_active);
        double tc=0,tr=0,td=0,ta=0,dtc=0,dtr=0,dta=0,dtd=0;
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
        int pos=0;
        for(int i=0;i<arr.size();i++)
        {
            if(arr.get(i).getLocation_name().equals(location_slected))
            {
                pos=i;
                break;
            }
        }



        Log.i(LOG_TAG,"Pos--"+arr.get(pos).getLocation_recovery());
        total_cases.setText(String.valueOf(arr.get(pos).getLocation_total_cases()));
        total_recovery.setText(String.valueOf(arr.get(pos).getLocation_recovery()));
        total_death.setText(String.valueOf(arr.get(pos).getLocation_death()));
        total_active.setText(String.valueOf(arr.get(pos).getLocation_active()));
        delta_active.setText(String.valueOf(arr.get(pos).getDelta_active()));
        delta_death.setText(String.valueOf(arr.get(pos).getDelta_deaths()));
        delta_total.setText(String.valueOf(arr.get(pos).getDela_confirmed()));
        delta_recovery.setText(String.valueOf(arr.get(pos).getDelta_recover()));

        double rrf=arr.get(pos).getLocation_recovery(),tcf=arr.get(pos).getLocation_total_cases();
        rrf=rrf/tcf*10000;
        int rr=(int)rrf;
        rrf=rr;
        rrf=rrf/100;


        recovery_rate.setText(String.valueOf(rrf));

         rrf=arr.get(pos).getLocation_death();
         tcf=arr.get(pos).getLocation_total_cases();
        rrf=rrf/tcf*10000;
         rr=(int)rrf;
        rrf=rr;
        rrf=rrf/100;
        death_rate.setText(String.valueOf(rrf));
        rrf=100-rrf;
        survival_rate.setText(String.valueOf(rrf));
    }

    public void medi_sum(ArrayList<medi_info_card> list)
    {
        int total_bed=0,total_hospital=list.size();
        for(int i=0;i<list.size();i++)
        total_bed+=Integer.valueOf(list.get(i).getBed());

        TextView total_hospitals=findViewById(R.id.hospital_value);

        total_hospitals.setText(String.valueOf(total_hospital));

        TextView total_beds=findViewById(R.id.bed_value);
        total_beds.setText(String.valueOf(total_bed));
    }

    private void Update_location_card() {
        medi_card_addapter flavorAdapter;
        ArrayList<medi_info_card> required_list=null;
        if(spinner_location.equals("India"))
        {
            if(Utils1.state_medi_info.containsKey(location_slected))
            required_list=new ArrayList<medi_info_card>(Utils1.state_medi_info.get(location_slected));
        }
        else
        {
            if(Utils1.state_medi_info.containsKey(spinner_location))
            required_list=new ArrayList<medi_info_card>(Utils1.state_medi_info.get(spinner_location));
        }
        if(required_list.size()==0)
            return ;
        medi_sum(required_list);
        flavorAdapter = new medi_card_addapter(this, required_list);
        ListView listView = (ListView) findViewById(R.id.list_pre);
        listView.setAdapter(flavorAdapter);

    }



    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<medi_info_card> > {

        protected ArrayList<medi_info_card> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            ArrayList<medi_info_card>  result = Utils1.fetchEarthquakeData(urls[0]);
            return result;
        }


        protected void onPostExecute(ArrayList<medi_info_card>  result) {
            if (result == null) {
                return;
            }

            Update_location_card();
        }
    }

}
