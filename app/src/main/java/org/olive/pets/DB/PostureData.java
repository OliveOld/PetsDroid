package org.olive.pets.DB;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by KMJ on 2017-04-10.
 */

public class PostureData extends RealmObject {
    @PrimaryKey
    private int data_id;           // 각 데이터가 갖는 기본 id
    private String date;            // 데이터 측정 된 시간 저장
    private double lie_time;
    private double stand_time;
    private double walk_time;
    private double run_time;

    public int getDateId() { return data_id; }
    public void setDateId(int data_id) {
        this.data_id = data_id;
    }

    public int getDataId() { return data_id; }
    public void setDataId(int data_id) {
        this.data_id = data_id;
    }

    public String getDate() { return date; }
    public void setDate(String date) {
        this.date = date;
    }

    public double getLieTime() { return lie_time; }
    public void setLieTime(double lie_time) { this.stand_time = lie_time; }

    public double getStandTime() { return stand_time; }
    public void setStandTime(double stand_time) { this.stand_time = stand_time; }

    public double getWalkTime() { return walk_time; }
    public void setWalkTime(double walk_time) { this.walk_time = walk_time; }

    public double getRunTime() { return run_time; }
    public void setRunTime(double run_time) { this.run_time = run_time; }
}
