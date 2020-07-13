package com.covid19.tracker.interfaces;

import com.covid19.tracker.entitty.ItemData;
import java.util.HashMap;

public interface OnTimeSeriesFetchedListener {

    void fetchingCompleted(HashMap<String,HashMap<String, ItemData>> items);

}
