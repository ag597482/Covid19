package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class PredictionActivity extends AppCompatActivity {
    public static final String LOG_TAG = PredictionActivity.class.getSimpleName();
    public static String location_slected;
    public static String spinner_location=MainActivity.spinner_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        location_slected = getIntent().getData().toString();
        Log.i(LOG_TAG,location_slected+" "+spinner_location);
        TextView top_location_name=findViewById(R.id.location_name);
        top_location_name.setText(location_slected);
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
    }
}
