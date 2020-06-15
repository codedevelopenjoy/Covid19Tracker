package com.covid19.tracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.covid19.tracker.R;
import com.covid19.tracker.entitty.ItemData;
import com.covid19.tracker.utils.ViewHelper;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterCountries extends RecyclerView.Adapter<RecyclerAdapterCountries.ViewHolder> {

    private Context context;
    private ArrayList<ItemData> countries;

    public RecyclerAdapterCountries(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<ItemData> itemDataArrayList) {
        this.countries = itemDataArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ItemData itemData = countries.get(position);

        String top = itemData.state;
        viewHolder.countryName.setText(top);
        Glide.with(context).load(itemData.flag).into(viewHolder.refreshBtn);
        viewHolder.lastUpdatedText.setText(itemData.lastUpdated);
        viewHolder.table.removeAllViews();

        View viewt = ViewHelper.makeRow(context,
                "Total", itemData.confirmed, itemData.active, itemData.recovered, itemData.deaths);
        viewHolder.table.addView(viewt);

        if (itemData.delta != null) {
            View viewto = ViewHelper.makeRow(context,
                    "Today", itemData.delta.confirmed, itemData.delta.active, itemData.delta.recovered, itemData.delta.deaths);
            viewHolder.table.addView(viewto);
        }
        View viewr = ViewHelper.makeRow(context,
                "Rates", "--", "--", itemData.getRecoveryRate(), itemData.getDeathRate());
        viewHolder.table.addView(viewr);

        View viewm = ViewHelper.makeRowAdditional(context,
                itemData.tests,itemData.critical);
        viewHolder.table.addView(viewm);
    }

    @Override
    public int getItemCount() {
        return countries == null ? 0 : countries.size();
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
