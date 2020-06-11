package com.example.android.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;


public class PredictionActivity extends AppCompatActivity {



    SeekBar days,factor;

    TextView textView,active,deaths,rec,dttt,dtta,dttr,dttd,pred,tday,place;



    int[][] prediction = new int[3][11];
    int minc,maxc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);


        textView =(TextView)findViewById(R.id.total);
        active = (TextView)findViewById(R.id.active);
        deaths = (TextView)findViewById(R.id.deaths);
        rec = (TextView)findViewById(R.id.recovered);


        dttt = (TextView)findViewById(R.id.dtotal);
        dtta = (TextView)findViewById(R.id.dactive);
        dttr = (TextView)findViewById(R.id.drecovered);
        dttd = (TextView)findViewById(R.id.ddeaths);


        place=(TextView)findViewById(R.id.place);


        pred=(TextView)findViewById(R.id.prediction);
        tday=(TextView)findViewById(R.id.tday);

        days = (SeekBar) findViewById(R.id.days);
        factor = (SeekBar) findViewById(R.id.social);


        String cardclicked = getIntent().getData().toString();
        place.setText(cardclicked);

        info_card infoCard=QueryUtils.detacard.get(cardclicked);
        textView.setText(infoCard.getLocation_total_cases()+ "");
        dttt.setText("" +infoCard.getDela_confirmed());


        final int currr=infoCard.getLocation_total_cases();

        prediction[0][0]=prediction[1][0]=prediction[2][0]=infoCard.getDela_confirmed();

        for(int i=0;i<3;i++)
        {
            for(int j=1;j<11;j++)
            {
                if(i==0)
                    prediction[i][j]=(int)(prediction[i][0]*j*(100/99));
                if(i==1)
                    prediction[i][j]=prediction[i][0]*j;
                if(i==3)
                    prediction[i][j]=(int)(prediction[i-1][j]*(1/100));

            }
        }

        pred.setText(currr+"");
        days.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(progress==0)
                {
                 pred.setText(currr+"");
                }
                else {
                    minc = prediction[factor.getProgress()][progress] - Math.max(0,(int) (progress * currr) / 100);
                    maxc = prediction[factor.getProgress()][progress] + (int) (progress * currr) / 100;
                    tday.setText("No of days after today : " + progress);
                    pred.setText((currr+minc) + " - "+ (currr+maxc));
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        factor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (days.getProgress()==0)
                {
                    pred.setText(currr+"");
                }
                else
                {

                    minc=prediction[progress][days.getProgress()]-Math.max(0,(int)(factor.getProgress()*currr)/100);
                    maxc=prediction[progress][factor.getProgress()]+(int)(factor.getProgress()*currr)/100;
                    pred.setText((currr+minc)+" - "+ (maxc+currr));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        active.setText(infoCard.getLocation_active()+ "");
        dtta.setText("" + (infoCard.getDela_confirmed()-infoCard.getDelta_recover()-infoCard.getDelta_deaths()) );

        rec.setText(infoCard.getLocation_recovery()+ "");
        dttr.setText("" +infoCard.getDelta_recover());

        deaths.setText(infoCard.getLocation_death()+ "");
        dttd.setText("" +infoCard.getDelta_deaths());

        TextView recra = (TextView)findViewById(R.id.rra);
        TextView ddra = (TextView)findViewById(R.id.dra);

        double rra=(double)infoCard.getLocation_recovery()/infoCard.getLocation_total_cases();

        double dra=(double)infoCard.getLocation_death()/infoCard.getLocation_total_cases();

        recra.setText("Recovery rate : " + String.format("%.2f", rra*100) + "%");
        ddra.setText("Mortality rate : " + String.format("%.2f", dra*100)+ "%");





    }



}
