package com.covid19.tracker.fetchers;

import android.os.AsyncTask;

import com.covid19.tracker.constants.ConstantsTimeSeries;
import com.covid19.tracker.entitty.ItemData;
import com.covid19.tracker.interfaces.OnTimeSeriesFetchedListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class TimeSeriesFetcher extends AsyncTask<Void, Void, Void>
        implements ConstantsTimeSeries {

    private OnTimeSeriesFetchedListener onFetchedListener;
    private HashMap<String, HashMap<String, ItemData>> items = new HashMap<>();
    private String stateCode;

    public TimeSeriesFetcher(OnTimeSeriesFetchedListener onFetchedListener, String stateCode) {
        this.onFetchedListener = onFetchedListener;
        this.stateCode = stateCode;
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
            JSONObject state = main.getJSONObject(stateCode.toUpperCase());

            for (Iterator<String> it = state.keys(); it.hasNext(); ) {
                String date = it.next();
                if (!state.getJSONObject(date).has(DELTA_TEXT)) {
                    HashMap<String, ItemData> map = new HashMap<>();
                    map.put(DELTA_TEXT, new ItemData());
                    items.put(date, map);
                    continue;
                }
                JSONObject delta = state.getJSONObject(date).getJSONObject(DELTA_TEXT);
                ItemData itemData = new ItemData();
                if (delta.has(CONFIRMED_TEXT)) {
                    itemData.confirmed = delta.get(CONFIRMED_TEXT).toString();
                }
                if (delta.has(RECOVERED_TEXT)) {
                    itemData.recovered = delta.get(RECOVERED_TEXT).toString();
                }
                if (delta.has(DEATHS_TEXT)) {
                    itemData.deaths = delta.get(DEATHS_TEXT).toString();
                }
                HashMap<String, ItemData> map = new HashMap<>();
                map.put(DELTA_TEXT, itemData);
                items.put(date, map);
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


