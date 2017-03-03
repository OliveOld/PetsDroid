package com.example.realm.base;

import android.graphics.Color;

import charting.data.Entry;
import charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

import io.realm.RealmObject;
import io.realm.RealmResults;


/**
 * Created by seobink on 2017-02-28.
 */

public abstract class RealmBarLineScatterCandleBubbleDataSet<T extends RealmObject, S extends Entry> extends RealmBaseDataSet<T, S> implements IBarLineScatterCandleBubbleDataSet<S> {

    /** default highlight color */
    protected int mHighLightColor = Color.rgb(255, 187, 115);

    public RealmBarLineScatterCandleBubbleDataSet(RealmResults<T> results, String yValuesField) {
        super(results, yValuesField);
    }

    /**
     * Constructor that takes the realm RealmResults, sorts & stores them.
     *
     * @param results
     * @param xValuesField
     * @param yValuesField
     */
    public RealmBarLineScatterCandleBubbleDataSet(RealmResults<T> results, String xValuesField, String yValuesField) {
        super(results, xValuesField, yValuesField);
    }

    /**
     * Sets the color that is used for drawing the highlight indicators. Dont
     * forget to resolve the color using getResources().getColor(...) or
     * Color.rgb(...).
     *
     * @param color
     */
    public void setHighLightColor(int color) {
        mHighLightColor = color;
    }

    @Override
    public int getHighLightColor() {
        return mHighLightColor;
    }
}
