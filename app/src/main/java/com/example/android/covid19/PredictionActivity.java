package com.example.android.covid19;

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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;


public class PredictionActivity extends AppCompatActivity {



    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://api.covid19india.org/data.json";
    TextView tv,xc,yc,place,delc,delr,deld,totc,totr,totd;
    GraphView graph;
    LineGraphSeries<DataPoint> series,series1,sdelc,sdelr,sdeld,stotc,stotr,stotd;
    SeekBar seekBar,days,factor;
    PointsGraphSeries<DataPoint> ps;
    List<Integer> plotted=new ArrayList<>();
    TextView textView,active,deaths,rec,dttt,dtta,dttr,dttd,pred,tday;

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
        ps= new PointsGraphSeries<DataPoint>();
        ps.setColor(R.color.RED);
        ps.setSize(5);
        tv=(TextView)findViewById(R.id.tv);
        xc=(TextView)findViewById(R.id.xc);
        yc=(TextView)findViewById(R.id.yc);

        delc=(TextView)findViewById(R.id.dc);
        delr=(TextView)findViewById(R.id.dr);
        deld=(TextView)findViewById(R.id.dd);

        totc=(TextView)findViewById(R.id.tc);
        totr=(TextView)findViewById(R.id.tr);
        totd=(TextView)findViewById(R.id.td);

        pred=(TextView)findViewById(R.id.prediction);
        tday=(TextView)findViewById(R.id.tday);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
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



        graph = (GraphView) findViewById(R.id.graph);

        series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1)
        });

        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1)
        });
        sdelc = new LineGraphSeries<>();
        sdelr = new LineGraphSeries<>();
        sdeld = new LineGraphSeries<>();
        stotc = new LineGraphSeries<>();
        stotr = new LineGraphSeries<>();
        stotd = new LineGraphSeries<>();


        PredictionActivity.EarthquakeAsyncTask task1 = new PredictionActivity.EarthquakeAsyncTask();
        task1.execute(USGS_REQUEST_URL);

        delc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.clicked_style));
                delr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                deld.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totd.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(sdelc);
                plotted=QueryUtils1.dc;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.dc)+1);

            }
        });

        totc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.clicked_style));
                delr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                deld.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                delc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totd.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(stotc);
                plotted=QueryUtils1.tc;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.tc)+1);

            }
        });
        delr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.clicked_style));
                delc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                deld.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totd.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(sdelr);
                plotted=QueryUtils1.dr;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.dr)+1);

            }
        });
        totr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.clicked_style));
                delr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                deld.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                delc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totd.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(stotr);
                plotted=QueryUtils1.tr;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.tr)+1);

            }
        });
        deld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deld.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.clicked_style));
                delr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                delc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totd.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(sdeld);
                plotted=QueryUtils1.dd;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.dd)+1);

            }
        });
        totd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totd.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.clicked_style));
                delr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                deld.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                delc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                totr.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(stotd);
                plotted=QueryUtils1.td;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.td)+1);

            }
        });

    }
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Integer>> {

        @Override
        protected List<Integer> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Integer> result = QueryUtils1.fetchEarthquakeData(urls[0]);
            return result;

        }

        @Override
        protected void onPostExecute(final List<Integer> data) {
            if (data != null && !data.isEmpty()) {
                //Toast.makeText(PredictionActivity.this,"aman",Toast.LENGTH_SHORT).show();
                // textView.setText(data.toString());
                tv.setText("Total cases : "+ QueryUtils1.total + " Rows - " + data.size());


                seekBar.setMax(data.size()-1);
                //ArrayList<Integer> y = new ArrayList<>(data);
//                for(int i=1;i<data.size();i++)
//                {
//                    series.appendData(new DataPoint(i,QueryUtils1.dc.get(i)),true,130);
//                }

                for(int i=0;i<data.size();i++)
                {
                    sdelc.appendData(new DataPoint(i+1,QueryUtils1.dc.get(i)),true,data.size());
                    sdelr.appendData(new DataPoint(i+1,QueryUtils1.dr.get(i)),true,data.size());
                    sdeld.appendData(new DataPoint(i+1,QueryUtils1.dd.get(i)),true,data.size());
                    stotc.appendData(new DataPoint(i+1,QueryUtils1.tc.get(i)),true,data.size());
                    stotr.appendData(new DataPoint(i+1,QueryUtils1.tr.get(i)),true,data.size());
                    stotd.appendData(new DataPoint(i+1,QueryUtils1.td.get(i)),true,data.size());
                }

                graph.addSeries(sdelc);
                plotted=QueryUtils1.dc;



                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);

               // graph.getViewport().setScalable(true);
               // graph.getViewport().setScalableY(true);


//                graph.getViewport().setScrollable(true); // enables horizontal scrolling
//                graph.getViewport().setScrollableY(true); // enables vertical scrolling
//                graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//                graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


                graph.getViewport().setMaxX(data.size()+1);
                graph.getViewport().setMaxY(Collections.max(QueryUtils1.dc)+1);

                delc.setBackgroundDrawable(ContextCompat.getDrawable(PredictionActivity.this,R.drawable.clicked_style));



                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        xc.setText("No. of days : " + String.valueOf(progress+1));
                        yc.setText("People : " + plotted.get(progress));

//
//
//                        int flag=0;
//                        DataPoint cdataPoint = new DataPoint(progress,data.get(progress));
//                        DataPoint pdataPoint = new DataPoint(progress,data.get(progress));
//
//                        ps.appendData(cdataPoint,true,1);
//
//
//                        if(cdataPoint.equals(pdataPoint))
//                        {
//                            graph.removeSeries(ps);
//                            graph.addSeries(ps);
//                            pdataPoint=cdataPoint;
//                        }
//                        else
//                        {
//                            graph.removeSeries(ps);
//                            graph.addSeries(ps);
//                            pdataPoint=cdataPoint;
//                        }






                      ///  ps.appendData(new DataPoint(0,1),true,130);




                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {


                    }
                });

            }
        }
    }
}
