package org.olive.pets.PieChart;

import io.realm.RealmObject;

/**
 * Created by seobink on 2017-03-01.
 */
public class RealmFloat extends RealmObject {

    private float floatValue;

    public RealmFloat() {

    }

    public RealmFloat(float floatValue) {
        this.floatValue = floatValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float value) {
        this.floatValue = value;
    }
}
