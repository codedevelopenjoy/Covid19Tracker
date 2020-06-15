package com.covid19.tracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.covid19.tracker.constants.ConstantsStates;
import com.covid19.tracker.R;
import com.covid19.tracker.entitty.ItemData;
import com.covid19.tracker.utils.ViewHelper;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterDistricts extends RecyclerView.Adapter<RecyclerAdapterDistricts.ViewHolder> implements ConstantsStates {

    private Context context;
    private ArrayList<ItemData> districts;

    public RecyclerAdapterDistricts(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<ItemData> arrayList) {
        this.districts = arrayList;
        notifyDataSetChanged();
    }

    public RecyclerAdapterDistricts.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int n) {
        return new RecyclerAdapterDistricts.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_data, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerAdapterDistricts.ViewHolder viewHolder, final int n) {

        ItemData itemData = districts.get(n);
        viewHolder.countryName.setText(districts.get(n).state);
        viewHolder.lastUpdatedText.setVisibility(View.GONE);
        viewHolder.refreshBtn.setVisibility(View.GONE);
        viewHolder.table.removeAllViews();

        View view = LayoutInflater.from(context).inflate(R.layout.item_row,null,false);
        ((TextView) view.findViewById(R.id.confirmed)).setText(itemData.confirmed);
        ((TextView) view.findViewById(R.id.active)).setText(itemData.active);
        ((TextView) view.findViewById(R.id.recovered)).setText(itemData.recovered);
        ((TextView) view.findViewById(R.id.deaths)).setText(itemData.deaths);
        ((TextView) view.findViewById(R.id.time)).setText("Total");

        viewHolder.table.addView(view);

        View viewr = ViewHelper.makeRow(context,
                "Rates", "--", "--", itemData.getRecoveryRate(), itemData.getDeathRate());
        viewHolder.table.addView(viewr);

    }

    public int getItemCount() {
        return districts == null ? 0 : districts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName, lastUpdatedText;
        ImageView refreshBtn;
        LinearLayout table;

        ViewHolder(View view) {
            super(view);
            this.countryName = view.findViewById(R.id.countrytext);
            this.lastUpdatedText = view.findViewById(R.id.lastUpdatedText);
            this.refreshBtn = view.findViewById(R.id.refreshBtn);
            this.table = view.findViewById(R.id.table);
        }
    }

}

