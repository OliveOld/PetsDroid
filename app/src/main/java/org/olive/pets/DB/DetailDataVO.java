package org.olive.pets.DB;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by KMJ on 2017-03-12.
 */

public class DetailDataVO extends RealmObject {

    @PrimaryKey
    private int data_id;
   // @Required
    private String time;
  //  @Required
    private double aceel_X;
  //  @Required
    private double accel_Y;
  //  @Required
    private double accel_Z;
 //   @Required
    private double svm;
 //   @Required
    private double sma;


    public int getDataId() { return data_id; }
    public void setDataId(int data_id) {
        this.data_id = data_id;
    }

    public String getTime() { return time; }
    public void setTime(String time) {
        this.time = time;
    }

    public double getAceelX() { return aceel_X; }
    public void setAceelX(double aceel_X) { this.aceel_X = aceel_X; }

    public double getAccelY() { return accel_Y; }
    public void setAccel_Y(double accel_Y) {
        this.accel_Y = accel_Y;
    }

    public double getAccelZ() {
        return accel_Z;
    }
    public void setAccelZ(double accel_Z) {
        this.accel_Z = accel_Z;
    }
}



