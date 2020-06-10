package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphActivity extends AppCompatActivity {


    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://api.covid19india.org/data.json";

    TextView tv,xc,yc,delc,delr,deld,totc,totr,totd;

    GraphView graph;
    LineGraphSeries<DataPoint> series,series1,sdelc,sdelr,sdeld,stotc,stotr,stotd;
    SeekBar seekBar;


    PointsGraphSeries<DataPoint> ps;
    List<Integer> plotted=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


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

        seekBar = (SeekBar) findViewById(R.id.seekBar);


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


        GraphActivity.EarthquakeAsyncTask task1 = new GraphActivity.EarthquakeAsyncTask();
        task1.execute(USGS_REQUEST_URL);

        delc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.clicked_style));
                delr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                deld.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totd.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(sdelc);
                plotted=QueryUtils1.dc;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.dc)+1);

            }
        });

        totc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.clicked_style));
                delr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                deld.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                delc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totd.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(stotc);
                plotted=QueryUtils1.tc;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.tc)+1);

            }
        });
        delr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.clicked_style));
                delc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                deld.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totd.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(sdelr);
                plotted=QueryUtils1.dr;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.dr)+1);

            }
        });
        totr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.clicked_style));
                delr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                deld.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                delc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totd.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(stotr);
                plotted=QueryUtils1.tr;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.tr)+1);

            }
        });
        deld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deld.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.clicked_style));
                delr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                delc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totd.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                graph.removeAllSeries();
                graph.addSeries(sdeld);
                plotted=QueryUtils1.dd;

                graph.getViewport().setMaxY(Collections.max(QueryUtils1.dd)+1);

            }
        });
        totd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totd.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.clicked_style));
                delr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                deld.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                delc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
                totr.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.sp_style));
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

                delc.setBackgroundDrawable(ContextCompat.getDrawable(GraphActivity.this,R.drawable.clicked_style));



                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        xc.setText("Days : " + String.valueOf(progress+1));
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
