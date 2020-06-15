package com.covid19.tracker.interfaces;

import java.util.ArrayList;

public interface OnFetchedListener<T> {

    void fetchingCompleted(ArrayList<T> items);

}
