package com.covid19.tracker.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.covid19.tracker.R;

public class ViewHelper {

    public static View makeRow(Context context,String...values){
        View view = LayoutInflater.from(context).inflate(R.layout.item_row,null,false);
        ((TextView) view.findViewById(R.id.time)).setText(values[0]);
        ((TextView) view.findViewById(R.id.confirmed)).setText(values[1]);
        ((TextView) view.findViewById(R.id.active)).setText(values[2]);
        ((TextView) view.findViewById(R.id.recovered)).setText(values[3]);
        ((TextView) view.findViewById(R.id.deaths)).setText(values[4]);
        return view;
    }

    public static View makeRowAdditional(Context context,String...values){
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_additional_data,null,false);
        ((TextView) view.findViewById(R.id.tests)).setText("Tests : "+values[0]);
        ((TextView) view.findViewById(R.id.critical)).setText("Critical : "+values[1]);
        return view;
    }

}
