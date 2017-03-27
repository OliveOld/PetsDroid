package org.olive.pets.DB;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by KMJ on 2017-03-19.
 */

public class DateDataVO extends RealmObject {

    @PrimaryKey
    private int date_id;
    @Required
    private String date;
    private RealmList<DetailDataVO> detail_data;

    public int getDateId() { return date_id; }
    public void setDateId(int date_id) {
        this.date_id = date_id;
    }

    public String getDate() { return date; }
    public void setDate(String date) {
        this.date = date;
    }
}
