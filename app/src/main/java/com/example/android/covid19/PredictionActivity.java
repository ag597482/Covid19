package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class PredictionActivity extends AppCompatActivity {



    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://api.covid19india.org/data.json";
    TextView tv,xc,yc,place;
    GraphView graph;
    LineGraphSeries<DataPoint> series,series1;
    SeekBar seekBar;
    PointsGraphSeries<DataPoint> ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);



        place=(TextView)findViewById(R.id.place);
        ps= new PointsGraphSeries<DataPoint>();
        ps.setColor(R.color.RED);
        ps.setSize(5);
        tv=(TextView)findViewById(R.id.tv);
        xc=(TextView)findViewById(R.id.xc);
        yc=(TextView)findViewById(R.id.yc);

        seekBar = (SeekBar) findViewById(R.id.seekBar);


        String cardclicked = getIntent().getData().toString();
        place.setText(cardclicked);


        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1)
        });

        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1)
        });



        PredictionActivity.EarthquakeAsyncTask task1 = new PredictionActivity.EarthquakeAsyncTask();
        task1.execute(USGS_REQUEST_URL);

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
                for(int i=1;i<data.size();i++)
                {
                    series.appendData(new DataPoint(i,QueryUtils1.dc.get(i)),true,130);

                }

                //Toast.makeText(PredictionActivity.this,String.valueOf(data.get(10)),Toast.LENGTH_SHORT).show();

                graph.addSeries(series);




                series1.setColor(R.color.colorAccent);
                graph.addSeries(series1);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);

                graph.getViewport().setScalable(true);
                graph.getViewport().setScalableY(true);


//                graph.getViewport().setScrollable(true); // enables horizontal scrolling
//                graph.getViewport().setScrollableY(true); // enables vertical scrolling
//                graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//                graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling


                graph.getViewport().setMaxX(data.size()+1);
                graph.getViewport().setMaxY(15000);

                DataPoint p = series.findDataPointAtX(10);
                series.findDataPointAtX(0);



                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        xc.setText("X : " + progress);
                        yc.setText("Y : " + QueryUtils1.dc.get(progress));

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
