package com.covid19.tracker.fetchers;

import android.os.AsyncTask;

import com.covid19.tracker.constants.ConstantsDistrict;
import com.covid19.tracker.entitty.ItemData;
import com.covid19.tracker.interfaces.OnFetchedListener;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class DistrictDataFetcher extends AsyncTask<Void,Void,Void> implements ConstantsDistrict {

    private ArrayList<ItemData> arrayList = new ArrayList<>();
    private OnFetchedListener<ItemData> fetchedListener;
    private String stateName;

    public DistrictDataFetcher(OnFetchedListener<ItemData> fetchedListener, String stateName) {
        this.fetchedListener = fetchedListener;
        this.stateName = stateName;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(API_URL_BASE);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            int n;
            while ((n=bufferedReader.read()) != -1) {
                stringBuilder.append((char)n);
            }

            bufferedReader.close();
            inputStreamReader.close();

            JSONObject jSONObject = new JSONObject(stringBuilder.toString())
                    .getJSONObject(stateName).getJSONObject(DISTRICT_DATA);
            Iterator<String> iterator = jSONObject.keys();
            while (iterator.hasNext()) {
                String districtName = iterator.next();
                ItemData itemData = new ItemData();
                itemData.state = districtName;
                itemData.confirmed = jSONObject.getJSONObject(districtName).get(CONFIRMED_TEXT).toString();
                itemData.active = jSONObject.getJSONObject(districtName).get(ACTIVE_TEXT).toString();
                itemData.recovered = jSONObject.getJSONObject(districtName).get(RECOVERED_TEXT).toString();
                itemData.deaths = jSONObject.getJSONObject(districtName).get(DEATHS_TEXT).toString();
                arrayList.add(itemData);
            }
        }catch (Exception ex){

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        fetchedListener.fetchingCompleted(arrayList);
    }
}
