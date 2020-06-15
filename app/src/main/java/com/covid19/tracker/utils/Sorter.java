package com.covid19.tracker.utils;

import com.covid19.tracker.entitty.ItemData;
import java.util.Comparator;

public class Sorter{

    public static final int BY_ACTIVE = 1;
    public static final int BY_CONFIRMED = 2;
    public static final int BY_RECOVERED = 3;
    public static final int BY_DEATHS = 4;

    private static int aorder=1,corder=1,rorder=1,dorder=1;

    public Comparator<ItemData> active,confirmed,recovered,deaths;

    public Sorter(int type){
        switch (type){
            case BY_ACTIVE:
                active = new Comparator<ItemData>() {
                    @Override
                    public int compare(ItemData itemData, ItemData t1) {
                        return (Integer.parseInt(itemData.active) - Integer.parseInt(t1.active))*aorder;
                    }
                };
                aorder *= -1;
                break;
            case BY_CONFIRMED:
                confirmed = new Comparator<ItemData>() {
                    @Override
                    public int compare(ItemData itemData, ItemData t1) {
                        return (Integer.parseInt(itemData.confirmed) - Integer.parseInt(t1.confirmed))*corder
                        ;
                    }
                };
                corder *= -1;
                break;
            case BY_RECOVERED:
                recovered = new Comparator<ItemData>() {
                    @Override
                    public int compare(ItemData itemData, ItemData t1) {
                        return (Integer.parseInt(itemData.recovered) - Integer.parseInt(t1.recovered))*rorder;
                    }
                };
                rorder *= -1;
                break;
            case BY_DEATHS:
                deaths = new Comparator<ItemData>() {
                    @Override
                    public int compare(ItemData itemData, ItemData t1) {
                        return (Integer.parseInt(itemData.deaths) - Integer.parseInt(t1.deaths))*dorder;
                    }
                };
                dorder *= -1;
                break;
            default:
                break;
        }

    }

}
