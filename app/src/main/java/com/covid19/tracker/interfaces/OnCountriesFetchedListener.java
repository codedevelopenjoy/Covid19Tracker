package com.covid19.tracker.interfaces;

import com.covid19.tracker.entitty.ItemData;
import java.util.ArrayList;

public interface OnCountriesFetchedListener {

    void onCountriesFetched(ArrayList<ItemData> items);

}
