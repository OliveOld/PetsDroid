package org.olive.pets;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by KMJ on 2017-03-12.
 */

public class SensorDataVO extends RealmObject {

    @PrimaryKey
    private String time;

    private double accel_X;
    private double accel_Y;
    private double accel_Z;
    private double gyro_X;
    private double gyro_Y;
    private double gyro_Z;
}



