package com.covid19.tracker.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.covid19.tracker.R;
import com.covid19.tracker.entitty.ItemData;
import com.covid19.tracker.fetchers.StateDataFetcher;
import com.covid19.tracker.fetchers.TimeSeriesFetcher;
import com.covid19.tracker.interfaces.OnTimeSeriesFetchedListener;
import com.covid19.tracker.utils.MiscDialogs;
import com.covid19.tracker.utils.ViewHelper;
import java.util.ArrayList;
import java.util.HashMap;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class TimeSeriesActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, OnTimeSeriesFetchedListener {

    private ProgressDialog progressDialog;
    private HashMap<String,HashMap<String,ItemData>> items;
    private ArrayList<String> names;
    private String[] stateCodes;
    private int index = 0;
    private int stateSelected = 0;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_series);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("TimeSeries Data");
        }

        items = new HashMap<>();
        ArrayList<ItemData> list = (ArrayList<ItemData>) getIntent().getSerializableExtra(StatesActivity.STATES_LIST_IDENTIFIER);
        if(list == null) {
            Toast.makeText(this, "Error While Fetching Try Again Later", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        stateCodes = new String[list.size()];
        names = new ArrayList<>();
        for (ItemData a : list) {
            if(a.statecode.equalsIgnoreCase("TT"))
                continue;
            stateCodes[index++] = a.statecode;
            names.add(a.state);
        }


        spinner = findViewById(R.id.stateSpinner);spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                progressDialog = new ProgressDialog(TimeSeriesActivity.this);
                progressDialog.setMessage("Fetching Details...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                stateSelected = i;
                TimeSeriesFetcher timeSeriesFetcher = new TimeSeriesFetcher(TimeSeriesActivity.this,stateCodes[i]);
                timeSeriesFetcher.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ((SwipeRefreshLayout) findViewById(R.id.swipeLayout)).setOnRefreshListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_countries_districts_timeseries, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sources) {
            MiscDialogs.showSources(this);
        } else if (item.getItemId() == R.id.developers) {
            MiscDialogs.showDevelopers(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        TimeSeriesFetcher timeSeriesFetcher = new TimeSeriesFetcher(this,stateCodes[stateSelected]);
        timeSeriesFetcher.execute();

        ((SwipeRefreshLayout) findViewById(R.id.swipeLayout)).setRefreshing(false);
    }

    @Override
    public void fetchingCompleted(HashMap<String, HashMap<String, ItemData>> items) {
        this.items = items;

        View view = LayoutInflater.from(this).inflate(R.layout.item_data,null,false);
        view.findViewById(R.id.countrytext).setVisibility(View.GONE);
        view.findViewById(R.id.lastUpdatedText).setVisibility(View.GONE);
        view.findViewById(R.id.refreshBtn).setVisibility(View.GONE);
        LinearLayout table = view.findViewById(R.id.table);
        table.removeAllViews();

        for(String date : items.keySet()){
            ItemData itemData = items.get(date).get("delta");
            if(itemData == null)
                continue;
            View viewr = ViewHelper.makeRow(this,
                    date, itemData.confirmed, itemData.active, itemData.recovered, itemData.deaths);

            TextView _date = viewr.findViewById(R.id.time);
            _date.setSingleLine(true);
            _date.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            _date.setSelected(true);

            table.addView(viewr);
        }

        ((LinearLayout)findViewById(R.id.linearLayout)).removeAllViews();
        ((LinearLayout)findViewById(R.id.linearLayout)).addView(view);

        progressDialog.dismiss();
    }
}
