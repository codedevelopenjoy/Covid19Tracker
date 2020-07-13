package com.covid19.tracker.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.covid19.tracker.R;
import com.covid19.tracker.adapter.RecyclerAdapterDistricts;
import com.covid19.tracker.entitty.ItemData;
import com.covid19.tracker.fetchers.DistrictDataFetcher;
import com.covid19.tracker.interfaces.OnFetchedListener;
import com.covid19.tracker.utils.MiscDialogs;
import com.covid19.tracker.utils.Sorter;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class DistrictsActivity extends AppCompatActivity
        implements OnFetchedListener<ItemData>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String STATE_TAG = "state";

    private RecyclerView recyclerView;
    private RecyclerAdapterDistricts recyclerAdapterDistricts;
    private ProgressDialog progressDialog;
    private ArrayList<ItemData> items;
    private ArrayList<ItemData> tempList = new ArrayList<>();
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_districts);

        state = getIntent().getStringExtra(STATE_TAG);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(state);
        }

        recyclerView = findViewById(R.id.recyclerView);

        findViewById(R.id.confirmSort).setOnClickListener(this);
        findViewById(R.id.activeSort).setOnClickListener(this);
        findViewById(R.id.recoveredSort).setOnClickListener(this);
        findViewById(R.id.deathsSort).setOnClickListener(this);
        ((SwipeRefreshLayout) findViewById(R.id.swipeLayout)).setOnRefreshListener(this);

        TextView note = findViewById(R.id.topTV);
        note.setSelected(true);
        note.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showInstructionsDialog();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerAdapterDistricts = new RecyclerAdapterDistricts(this);
        recyclerView.setAdapter(recyclerAdapterDistricts);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DistrictDataFetcher stateDataFetcher = new DistrictDataFetcher(this, state);
        stateDataFetcher.execute();

    }

    public void showInstructionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instructions");
        builder.setMessage("1. Long press on your Home screen, you will see some options at the bottom.\n\n2. Select WIDGETS from the options.\n\n3. Search for C O V I D-19  Tracker and long press to place it on your Home screen.\n\n4. Refresh the Widget by pressing the refresh button.\n\n5. Done \n").setNegativeButton((CharSequence) "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int n) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void fetchingCompleted(ArrayList<ItemData> items) {
        this.items = items;
        recyclerAdapterDistricts.setList(items);
        if (items.size() == 0)
            findViewById(R.id.oopstext).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.oopstext).setVisibility(View.GONE);
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View view) {

        if (items == null)
            return;

        if (view.getId() == R.id.activeSort) {
            Collections.sort(items, new Sorter(Sorter.BY_ACTIVE).active);
        } else if (view.getId() == R.id.confirmSort) {
            Collections.sort(items, new Sorter(Sorter.BY_CONFIRMED).confirmed);
        } else if (view.getId() == R.id.recoveredSort) {
            Collections.sort(items, new Sorter(Sorter.BY_RECOVERED).recovered);
        } else if (view.getId() == R.id.deathsSort) {
            Collections.sort(items, new Sorter(Sorter.BY_DEATHS).deaths);
        }
        recyclerAdapterDistricts.setList(items);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_countries_districts_timeseries, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            public boolean onQueryTextChange(String string2) {
                tempList.clear();
                for (ItemData fa : items) {
                    if(fa.state.toLowerCase().contains(string2.toLowerCase()))
                        tempList.add(fa);
                }
                recyclerAdapterDistricts.setList(tempList);
                return false;
            }

            public boolean onQueryTextSubmit(String string2) {
                searchView.clearFocus();
                recyclerAdapterDistricts.setList(items);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.sources){
            MiscDialogs.showSources(this);
        }
        else if(item.getItemId() == R.id.developers){
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

        DistrictDataFetcher stateDataFetcher = new DistrictDataFetcher(this, state);
        stateDataFetcher.execute();

        ((SwipeRefreshLayout) findViewById(R.id.swipeLayout)).setRefreshing(false);
    }

}
