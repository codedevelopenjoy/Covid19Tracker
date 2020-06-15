package com.covid19.tracker.fetchers;

import android.os.AsyncTask;
import com.covid19.tracker.constants.ConstantsCountries;
import com.covid19.tracker.entitty.ItemData;
import com.covid19.tracker.interfaces.OnCountriesFetchedListener;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class CountriesDataFetcher extends AsyncTask<Void, Void, Void> implements ConstantsCountries {

    private ArrayList<ItemData> itemDataArrayList = new ArrayList<>();
    private OnCountriesFetchedListener onCountriesFetchedListener;

    public CountriesDataFetcher(OnCountriesFetchedListener onCountriesFetchedListener) {
        this.onCountriesFetchedListener = onCountriesFetchedListener;
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

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject _country = jsonArray.getJSONObject(i);

                ItemData itemData = new ItemData();
                itemData.active = _country.getInt(ACTIVE_TAG) + "";
                itemData.recovered = _country.getInt(RECOVERED_TAG) + "";
                itemData.confirmed = _country.getInt(CASES_TAG) + "";
                itemData.deaths = _country.getInt(DEATHS_TAG) + "";
                itemData.lastUpdated = new Date(_country.getLong(UPDATED_TAG)).toLocaleString();
                itemData.state = _country.getString(COUNTRY_TAG);
                itemData.flag = _country.getJSONObject(COUNTRY_INFO_TAG).getString(FLAG_TAG);
                itemData.critical = _country.getInt(CRITICAL_TAG) + "";
                itemData.tests = _country.getInt(TESTS_TAG) + "";

                ItemData delta = new ItemData();
                delta.recovered = _country.getInt(TODAY_RECOVERED_TAG) + "";
                delta.confirmed = _country.getInt(TODAY_CASES_TAG) + "";
                delta.deaths = _country.getInt(TODAY_DEATHS_TAG) + "";
                delta.active = Integer.parseInt(delta.confirmed) - (Integer.parseInt(delta.recovered) + Integer.parseInt(delta.deaths)) + "";

                itemData.delta = delta;

                itemDataArrayList.add(itemData);
            }

        } catch (Exception ex) {

        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        onCountriesFetchedListener.onCountriesFetched(itemDataArrayList);
    }
}
