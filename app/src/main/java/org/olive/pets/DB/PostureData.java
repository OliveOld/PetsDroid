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

    public String getDate() { return date; }
    public void setDate(String date) {
        this.date = date;
    }
}
