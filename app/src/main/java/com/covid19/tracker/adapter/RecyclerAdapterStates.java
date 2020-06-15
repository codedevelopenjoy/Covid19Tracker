package com.covid19.tracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.covid19.tracker.R;
import com.covid19.tracker.activity.DistrictsActivity;
import com.covid19.tracker.constants.ConstantsStates;
import com.covid19.tracker.entitty.ItemData;
import com.covid19.tracker.utils.ViewHelper;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterStates extends RecyclerView.Adapter<RecyclerAdapterStates.ViewHolder> implements ConstantsStates {

    private Context context;
    private ArrayList<ItemData> states;

    public RecyclerAdapterStates(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<ItemData> arrayList) {
        this.states = arrayList;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int n) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_data, viewGroup, false));
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {

        ItemData itemData = states.get(n);

        String top = itemData.state + " (" + itemData.statecode + ")";
        viewHolder.countryName.setText(top);
        viewHolder.refreshBtn.setVisibility(View.GONE);
        viewHolder.lastUpdatedText.setText(itemData.lastUpdated);
        viewHolder.table.removeAllViews();

        View view = LayoutInflater.from(context).inflate(R.layout.item_row, null, false);
        ((TextView) view.findViewById(R.id.time)).setText("Total");
        ((TextView) view.findViewById(R.id.confirmed)).setText(itemData.confirmed);
        ((TextView) view.findViewById(R.id.active)).setText(itemData.active);
        ((TextView) view.findViewById(R.id.recovered)).setText(itemData.recovered);
        ((TextView) view.findViewById(R.id.deaths)).setText(itemData.deaths);
        viewHolder.table.addView(view);

        if (itemData.delta != null) {
            View viewt = LayoutInflater.from(context).inflate(R.layout.item_row, null, false);
            ((TextView) viewt.findViewById(R.id.time)).setText("Today");
            ((TextView) viewt.findViewById(R.id.confirmed)).setText(itemData.delta.confirmed);
            ((TextView) viewt.findViewById(R.id.active)).setText(itemData.delta.active);
            ((TextView) viewt.findViewById(R.id.recovered)).setText(itemData.delta.recovered);
            ((TextView) viewt.findViewById(R.id.deaths)).setText(itemData.delta.deaths);
            viewHolder.table.addView(viewt);
        }

        if (itemData.yesterday != null) {
            View viewy = LayoutInflater.from(context).inflate(R.layout.item_row, null, false);
            ((TextView) viewy.findViewById(R.id.time)).setText("Yest...");
            ((TextView) viewy.findViewById(R.id.confirmed)).setText(itemData.yesterday.confirmed);
            ((TextView) viewy.findViewById(R.id.active)).setText(itemData.yesterday.active);
            ((TextView) viewy.findViewById(R.id.recovered)).setText(itemData.yesterday.recovered);
            ((TextView) viewy.findViewById(R.id.deaths)).setText(itemData.yesterday.deaths);
            viewHolder.table.addView(viewy);
        }

        View viewr = ViewHelper.makeRow(context,
                "Rates", "--", "--", itemData.getRecoveryRate(), itemData.getDeathRate());
        viewHolder.table.addView(viewr);

        if(!itemData.statecode.equalsIgnoreCase("tt"))
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, DistrictsActivity.class);
                intent.putExtra(DistrictsActivity.STATE_TAG, states.get(n).state);
                context.startActivity(intent);
            }
        });

    }

    public int getItemCount() {
        return states == null ? 0 : states.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName, lastUpdatedText;
        ImageView refreshBtn;
        LinearLayout layout, table;

        ViewHolder(View view) {
            super(view);
            this.countryName = view.findViewById(R.id.countrytext);
            this.layout = view.findViewById(R.id.mainLayout);
            this.lastUpdatedText = view.findViewById(R.id.lastUpdatedText);
            this.refreshBtn = view.findViewById(R.id.refreshBtn);
            this.table = view.findViewById(R.id.table);
        }
    }

}

