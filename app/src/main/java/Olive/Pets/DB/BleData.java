package Olive.Pets.DB;

import com.punchthrough.bean.sdk.Bean;

import java.util.HashMap;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by KMJ on 2017-04-10.
 */

public class BleData
        extends RealmObject
{
    private static final String TAG = "Device";

    static HashMap<String, Bean> beans;
    @PrimaryKey
    private String BT_Address; // BT device MAC address
    private String BT_name; // device BT name
    private int rssi; // signal strength at scan
    private boolean isSelected; // selected by user
}
