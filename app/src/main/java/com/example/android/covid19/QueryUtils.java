package com.example.android.covid19;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QueryUtils {


    /** Sample JSON response for a USGS query */

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    static List<String> spinnerArray;
    static int total1;
   // public String val;

    private QueryUtils() {
    }

    public static List<String> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<String> casenumbers = extractFeatureFromJson(jsonResponse);
        spinnerArray = casenumbers;
        // Return the list of {@link Earthquake}s
        return casenumbers;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static List<String> extractFeatureFromJson(String earthquakeJSON) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<String> earthquakes = new ArrayList<String>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject jsonObject1=new JSONObject(earthquakeJSON);


            //earthquakes = (ArrayList<String>) jsonObject.keys();



           // Map<String, JSONObject> map = (Map<String,JSONObject>)jsonObject;

           // ArrayList<String> list = new ArrayList<String>(map.keySet());


            //JSONObject jsonObject1 = jsonObject.getJSONObject("rates");

            total1=0;
            Iterator keys = jsonObject1.keys();
            while(keys.hasNext()) {
                // loop to get the dynamic key
                String currentDynamicKey = (String)keys.next();

                // get the value of the dynamic key
                JSONObject currentDynamicValue = jsonObject1.getJSONObject(currentDynamicKey);
                JSONObject jsonObject = currentDynamicValue.getJSONObject("districtData");
                Iterator keys1 = jsonObject.keys();
                String val = null;
                int statesum = 0;
                while(keys1.hasNext()) {


                    String currentDynamicKey1 = (String)keys1.next();

                    JSONObject cdv = jsonObject.getJSONObject(currentDynamicKey1);

                     val = cdv.getString("confirmed");
                     statesum+=Integer.valueOf(val);
                    total1 = total1 + Integer.valueOf(val);


                }
                earthquakes.add(currentDynamicKey+ " -> " + statesum);

                // do something here with the value...
            }

//            earthquakes.add(jsonObject1.getString("USD"));
//            earthquakes.add(jsonObject.toString());
//            earthquakes.add(jsonObject1.getString("INR"));



            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }





}
