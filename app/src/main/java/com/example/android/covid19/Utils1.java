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
import java.util.HashMap;

/**
 * Utility class with methods to help perform the HTTP request and
 * parse the response.
 */
public final class Utils1 {

    /** Tag for the log messages */
    public static final String LOG_TAG = Utils1.class.getSimpleName();
    static String test;

    static String[] state= {"India","Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
            "Dadra and Nagar Haveli", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Lakshadweep", "Puducherry"} ;


    static HashMap<String, ArrayList<medi_info_card>> state_medi_info=new HashMap<>();

    /**
     * Query the USGS dataset and return an {@link ArrayList <info_card>} object to represent a single earthquake.
     */
    public static ArrayList<medi_info_card> fetchEarthquakeData(String requestUrl) {
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
        ArrayList<medi_info_card> earthquake = extractFeatureFromJson(jsonResponse);
        if(state_medi_info.containsKey("Jharkhand"))
        earthquake=state_medi_info.get("Jharkhand");
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


    private static ArrayList<medi_info_card> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        ArrayList<medi_info_card> result =india_info_api(earthquakeJSON);;
        return result;
    }



    public static ArrayList<medi_info_card> india_info_api(String earthquakeJSON)
    {
        ArrayList<medi_info_card> location_card_info =new ArrayList<medi_info_card>();
        ArrayList<medi_info_card> dist_temp =new ArrayList<medi_info_card>();
        if (TextUtils.isEmpty(earthquakeJSON)) {

            return location_card_info;
        }

        int total1=0;
        try {


            for(int i=0;i<state.length;i++)
            {
                ArrayList<medi_info_card> temp=new ArrayList<medi_info_card>();
                state_medi_info.put(state[i],temp);
            }
            ArrayList<medi_info_card> temp1=new ArrayList<medi_info_card>();
            state_medi_info.put("State Unassigned",temp1);


            JSONObject jsonObject1=new JSONObject(earthquakeJSON);

            JSONObject data = jsonObject1.getJSONObject("data");
            JSONArray medicalColleges_array = data.getJSONArray("medicalColleges");
            for(int i = 0; i < medicalColleges_array.length(); i++)
            {
                JSONObject medi_info_obj = medicalColleges_array.getJSONObject(i);
                String state_name,hospital_name,city,hospital_bed;
                state_name=medi_info_obj.getString("state");
                hospital_bed=medi_info_obj.getString("hospitalBeds");
                city=medi_info_obj.getString("city");
                hospital_name=medi_info_obj.getString("name");
                if(state_name.equals("Jammu & Kashmir"))
                    state_name="Jammu and Kashmir";
                if(state_name.equals("A & N Islands"))
                    state_name="Andaman and Nicobar Islands";

                if(state_medi_info.containsKey(state_name))
                {
                    ArrayList<medi_info_card> temp=new ArrayList<medi_info_card>(state_medi_info.get(state_name));
                    temp.add(new medi_info_card(hospital_name,hospital_bed,city));
//                    Log.i(LOG_TAG,state_name+"  -  "+temp.size());
                    state_medi_info.put(state_name,temp);
                }
                else
                {
                    ArrayList<medi_info_card> temp=new ArrayList<medi_info_card>();
                    temp.add(new medi_info_card(hospital_name,hospital_bed,city));
                    Log.i(LOG_TAG,""+temp.size());
                    state_medi_info.put(state_name,temp);
                }

                Log.i(LOG_TAG,state_name);
            }
        } catch (JSONException e) {

            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return location_card_info;

    }
}
