package com.covid19.tracker.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.covid19.tracker.R;
import com.covid19.tracker.activity.SplashActivity;
import com.covid19.tracker.activity.StatesActivity;
import com.covid19.tracker.entitty.ItemData;
import com.covid19.tracker.fetchers.StateDataFetcher;
import com.covid19.tracker.interfaces.OnFetchedListener;
import com.covid19.tracker.utils.SharedPrefsHelper;
import java.util.ArrayList;

public class TrackerWidget extends AppWidgetProvider {

    private static final String ACTION_MY_BUTTON = "REFRESHED_CLICKED";
    private static final String ACTION_LAYOUT = "LAYOUT_CLICKED";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_data);

            //REFRESH CLICKED ACTION
            Intent intent = new Intent(context, getClass());
            intent.setAction(ACTION_MY_BUTTON);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.refreshBtn, pendingIntent);

            //LAYOUT CLICKED ACTION
            Intent intent1 = new Intent(context, getClass());
            intent1.setAction(ACTION_LAYOUT);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 1, intent1, 0);
            views.setOnClickPendingIntent(R.id.clickedLayout, pendingIntent1);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ACTION_MY_BUTTON)) {

            final String CODE = SharedPrefsHelper.getStateCode(context);

            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_data);

            Toast.makeText(context, "Refreshing Data  Please Wait...", Toast.LENGTH_SHORT).show();

            StateDataFetcher stateDataFetcher = new StateDataFetcher(new OnFetchedListener<ItemData>() {
                @Override
                public void fetchingCompleted(ArrayList<ItemData> items) {
                    ItemData total = null;
                    for (ItemData item : items) {
                        if (item.statecode.trim().equalsIgnoreCase(CODE)) {
                            total = item;
                            break;
                        }
                    }
                    if (total != null) {

                        views.setTextViewText(R.id.countrytext, total.state.toUpperCase());
                        views.setTextViewText(R.id.lastUpdatedText,"Updated : "+total.lastUpdated);

                        //REMOVE ALL ROWS
                        views.removeAllViews(R.id.table);

                        //ADD TOTAL ROW
                        RemoteViews rview = new RemoteViews(context.getPackageName(), R.layout.item_row);
                        views.addView(R.id.table,rview);
                        rview.setTextViewText(R.id.time,"Total");
                        rview.setTextViewText(R.id.confirmed, total.confirmed);
                        rview.setTextViewText(R.id.active, total.active);
                        rview.setTextViewText(R.id.recovered, total.recovered);
                        rview.setTextViewText(R.id.deaths, total.deaths);

                        if(total.delta!=null) {
                            //ADD TODAY DELTA ROW
                            RemoteViews rtdview = new RemoteViews(context.getPackageName(), R.layout.item_row);
                            views.addView(R.id.table, rtdview);
                            rtdview.setTextViewText(R.id.time, "Today");
                            rtdview.setTextViewText(R.id.confirmed, total.delta.confirmed);
                            rtdview.setTextViewText(R.id.active, total.delta.active);
                            rtdview.setTextViewText(R.id.recovered, total.delta.recovered);
                            rtdview.setTextViewText(R.id.deaths, total.delta.deaths);
                        }

                        if(total.yesterday!=null) {
                            //ADD YESTERDAY DELTA ROW
                            RemoteViews rtdview = new RemoteViews(context.getPackageName(), R.layout.item_row);
                            views.addView(R.id.table, rtdview);
                            rtdview.setTextViewText(R.id.time, "Yest...");
                            rtdview.setTextViewText(R.id.confirmed, total.yesterday.confirmed);
                            rtdview.setTextViewText(R.id.active, total.yesterday.active);
                            rtdview.setTextViewText(R.id.recovered, total.yesterday.recovered);
                            rtdview.setTextViewText(R.id.deaths, total.yesterday.deaths);
                        }

                        //ADD RATES ROW
                        RemoteViews rrview = new RemoteViews(context.getPackageName(), R.layout.item_row);
                        views.addView(R.id.table, rrview);
                        rrview.setTextViewText(R.id.time, "Rates");
                        rrview.setTextViewText(R.id.confirmed, "--");
                        rrview.setTextViewText(R.id.active, "--");
                        rrview.setTextViewText(R.id.recovered, total.getRecoveryRate());
                        rrview.setTextViewText(R.id.deaths, total.getDeathRate());

                    }

                    Toast.makeText(context, "Data Refreshed", Toast.LENGTH_SHORT).show();

                    //UPDATING TO WIDGET
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    ComponentName componentName = new ComponentName(context, TrackerWidget.class);
                    appWidgetManager.updateAppWidget(componentName, views);
                }
            });

            stateDataFetcher.execute();
        }
        else if(intent.getAction()!=null && intent.getAction().equals(ACTION_LAYOUT)){
            Intent intent1 = new Intent(context, SplashActivity.class);
            intent1.putExtra("TIME",1000);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
        else
            super.onReceive(context, intent);
    }

}