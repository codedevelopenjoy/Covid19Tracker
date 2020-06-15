package com.covid19.tracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    private static final String SHARED_PREFS_NAME = "tracker";
    private static final String STATE_CODE_TAG = "statecode";

    public static void saveStateCode(Context context,String code){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE).edit();
        editor.putString(STATE_CODE_TAG,code);
        editor.apply();
    }

    public static String getStateCode(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(STATE_CODE_TAG,"tt");
    }

}
