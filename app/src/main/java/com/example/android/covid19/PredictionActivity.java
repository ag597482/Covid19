package com.example.android.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class PredictionActivity extends AppCompatActivity {



    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://api.covid19india.org/data.json";
    TextView tv;
    GraphView graph;
    LineGraphSeries<DataPoint> series,series1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        tv=(TextView)findViewById(R.id.tv);

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
        protected void onPostExecute(List<Integer> data) {
            if (data != null && !data.isEmpty()) {
                //Toast.makeText(PredictionActivity.this,"aman",Toast.LENGTH_SHORT).show();
                // textView.setText(data.toString());
                tv.setText("Total cases : "+ QueryUtils1.total + "\nActive :"+ QueryUtils1.active + "\nRows - " + data.size());

                for(int i=1;i<data.size();i++)
                {
                    series.appendData(new DataPoint(i,data.get(i)),true,130);
                }
                graph.addSeries(series);

                for(int i=1;i<130;i++)
                {
                    series1.appendData(new DataPoint(i,Math.exp((i-50)/4)),true,130);
                }

                series1.setColor(R.color.colorAccent);
                graph.addSeries(series1);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMaxX(130);
                graph.getViewport().setMaxY(250000);

            }
        }
    }
}
