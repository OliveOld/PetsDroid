package olive.Pets.DB;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by KMJ on 2017-04-10.
 */

public class PostureData
        extends RealmObject
{
    @PrimaryKey
    private String date;            // 데이터 측정 된 시간 저장
    private int Unknown;
    private int Lie;
    private int LieSide;
    private int LieBack;
    private int Sit;
    private int Stand;
    private int Walk;
    private int Run;

    /*
    public int getDateId() { return data_id; }
    public void setDateId(int data_id) {
        this.data_id = data_id;
    }
    */
    public String getDate() { return date; }
    public void setDate(String date) {
        this.date = date;
    }

    public int getUnknown() { return Unknown; }
    public void setUnknown(int Unknown) { this.Unknown = Unknown; }

    public int getLie() { return Lie; }
    public void setLie(int Lie) { this.Lie = Lie; }

    public int getLieSide() { return LieSide; }
    public void setLieSide(int LieSide) { this.LieSide = LieSide; }

    public int getSit() { return Sit; }
    public void setSit(int Sit) { this.Sit = Sit; }

    public int getLieBacke() { return LieBack; }
    public void setLieBack(int LieBack) { this.LieBack = LieBack; }

    public int getStand() { return Stand; }
    public void setStand(int Stand) { this.Stand = Stand; }

    public int getWalk() { return Walk; }
    public void setWalk(int Walk) { this.Walk = Walk; }

    public int getRun() { return Run; }
    public void setRun(int Run) { this.Run = Run; }
}
