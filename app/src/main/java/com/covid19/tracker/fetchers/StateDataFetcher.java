package com.covid19.tracker.fetchers;

import android.os.AsyncTask;

import com.covid19.tracker.constants.ConstantsStates;
import com.covid19.tracker.entitty.ItemData;
import com.covid19.tracker.interfaces.OnFetchedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StateDataFetcher extends AsyncTask<Void, Void, Void> implements ConstantsStates {

    private OnFetchedListener<ItemData> onFetchedListener;
    private ArrayList<ItemData> items = new ArrayList<>();

    public StateDataFetcher(OnFetchedListener<ItemData> onFetchedListener) {
        this.onFetchedListener = onFetchedListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL apiURL = new URL(API_URL_BASE);
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            int n;
            while ((n = bufferedReader.read()) != -1) {
                stringBuilder.append((char) n);
            }

            bufferedReader.close();
            inputStreamReader.close();

            JSONObject main = new JSONObject(stringBuilder.toString());
            JSONArray states = new JSONArray(main.get(STATEWISE_TAG).toString());

            for (int i = 0; i < states.length(); ++i) {

                JSONObject jSONObject = states.getJSONObject(i);
                ItemData itemData = new ItemData();
                itemData.active = jSONObject.get(ACTIVE_TEXT).toString();
                itemData.recovered = jSONObject.get(RECOVERED_TEXT).toString();
                itemData.confirmed = jSONObject.get(CONFIRMED_TEXT).toString();
                itemData.deaths = jSONObject.get(DEATHS_TEXT).toString();
                itemData.state = jSONObject.get(STATENAME).toString();
                itemData.statecode = jSONObject.get(STATECODE).toString();
                itemData.lastUpdated = jSONObject.get(LAST_UPDATED_TEXT).toString();

                if (itemData.statecode.equalsIgnoreCase("tt")){
                    JSONArray array = new JSONArray(main.get("cases_time_series").toString());
                    JSONObject yesterday = array.getJSONObject(array.length() - 1);
                    ItemData _yesterday = new ItemData();

                    _yesterday.confirmed = yesterday.getString("dailyconfirmed");
                    _yesterday.recovered = yesterday.getString("dailyrecovered");
                    _yesterday.deaths = yesterday.getString("dailydeceased");
                    _yesterday.active = "" + (Integer.parseInt(_yesterday.confirmed) - (Integer.parseInt(_yesterday.recovered) + Integer.parseInt(_yesterday.deaths)));

                    if(_yesterday.active.contains("-"))
                        _yesterday.active = "0";

                    itemData.yesterday = _yesterday;

                }

                ItemData delta = new ItemData();
                delta.confirmed = jSONObject.getString(DELTA_CONFIRMED);
                delta.recovered = jSONObject.getString(DELTA_RECOVERED);
                delta.deaths = jSONObject.getString(DELTA_DEATHS);
                delta.active = "" + (Integer.parseInt(delta.confirmed) - (Integer.parseInt(delta.recovered) + Integer.parseInt(delta.deaths)));

                if(delta.active.contains("-"))
                    delta.active = "0";

                itemData.delta = delta;

                items.add(itemData);
            }

        } catch (Exception e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        onFetchedListener.fetchingCompleted(items);
    }

}