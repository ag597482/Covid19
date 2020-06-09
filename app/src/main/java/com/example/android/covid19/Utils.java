/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.covid19;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
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

/**
 * Utility class with methods to help perform the HTTP request and
 * parse the response.
 */
public final class Utils {

    /** Tag for the log messages */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Query the USGS dataset and return an {@link ArrayList<info_card>} object to represent a single earthquake.
     */
    public static ArrayList<info_card> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<info_card> earthquake = extractFeatureFromJson(jsonResponse);

//         Return the {@link Event}
        return earthquake;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
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


    private static ArrayList<info_card> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if(MainActivity.spinner_location=="India") {
            ArrayList<info_card> location_card_info = india_info_api(earthquakeJSON);
            return location_card_info;
        }
        else
        {
            ArrayList<info_card> location_card_info = state_info_api(earthquakeJSON);
            return location_card_info;
        }
    }



    public static ArrayList<info_card> india_info_api(String earthquakeJSON)
    {
        ArrayList<info_card> location_card_info =new ArrayList<info_card>();
        if (TextUtils.isEmpty(earthquakeJSON)) {

            return location_card_info;
        }

        int total1=0;
        try {
            JSONObject jsonObject1=new JSONObject(earthquakeJSON);

            Iterator keys = jsonObject1.keys();
            while(keys.hasNext()) {
                // loop to get the dynamic key
                String currentDynamicKey = (String)keys.next();

                // get the value of the dynamic key
                JSONObject currentDynamicValue = jsonObject1.getJSONObject(currentDynamicKey);
                JSONObject jsonObject = currentDynamicValue.getJSONObject("districtData");
                Iterator keys1 = jsonObject.keys();
                String val = null;
                String act=null;
                String det=null;
                String rec=null;
                int active=0;
                int deaths=0;
                int recovered=0;
                int statesum = 0;
                while(keys1.hasNext()) {


                    String currentDynamicKey1 = (String)keys1.next();

                    JSONObject cdv = jsonObject.getJSONObject(currentDynamicKey1);

                    val = cdv.getString("confirmed");
                    act = cdv.getString("active");
                    rec = cdv.getString("recovered");
                    det = cdv.getString("deceased");


                    statesum+=Integer.valueOf(val);
                    total1 = total1 + Integer.valueOf(val);
                    active+=Integer.valueOf(act);
                    deaths+=Integer.valueOf(det);
                    recovered+=Integer.valueOf(rec);


                }

                location_card_info.add(new info_card(currentDynamicKey, MainActivity.spinner_location,active,deaths,recovered,statesum));

            }
        } catch (JSONException e) {

            location_card_info.add(new info_card("state name", MainActivity.spinner_location,1,2,3,4));
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return location_card_info;

    }

    public static ArrayList<info_card> state_info_api(String earthquakeJSON)
    {
        ArrayList<info_card> location_card_info =new ArrayList<info_card>();
        if (TextUtils.isEmpty(earthquakeJSON)) {

            return location_card_info;
        }

        int total1=0;
        try {
            JSONObject jsonObject1=new JSONObject(earthquakeJSON);
            Iterator keys = jsonObject1.keys();
                    JSONObject currentDynamicValue = jsonObject1.getJSONObject(MainActivity.spinner_location);
                    JSONObject jsonObject = currentDynamicValue.getJSONObject("districtData");
                    Iterator keys1 = jsonObject.keys();
                    String val = null;
                    String act = null;
                    String det = null;
                    String rec = null;
                    int active = 0;
                    int deaths = 0;
                    int recovered = 0;
                    int statesum = 0;
                    while (keys1.hasNext()) {


                        String district_name = (String) keys1.next();

                        JSONObject cdv = jsonObject.getJSONObject(district_name);

                        val = cdv.getString("confirmed");
                        act = cdv.getString("active");
                        rec = cdv.getString("recovered");
                        det = cdv.getString("deceased");


                        statesum = Integer.valueOf(val);
                        total1 = Integer.valueOf(val);
                        active = Integer.valueOf(act);
                        deaths = Integer.valueOf(det);
                        recovered = Integer.valueOf(rec);


                        location_card_info.add(new info_card(district_name, MainActivity.spinner_location, active, deaths, recovered, statesum));


                }
        } catch (JSONException e) {

            location_card_info.add(new info_card("state name", MainActivity.spinner_location,1,2,3,4));
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return location_card_info;

    }



}
