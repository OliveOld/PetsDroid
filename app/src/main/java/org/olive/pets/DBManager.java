package org.olive.pets;

/**
 * Created by seobink on 2017-02-08.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "/mnt/sdcard/" + name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE dog_profile (_id INTEGER PRIMARY KEY AUTOINCREMENT, dog_name TEXT, dog_age INTEGER, dog_sex TEXT, dog_photo TEXT);";
        // 새로운 테이블 생성
        /* 이름은 DATA, 자동으로 값이 증가하는 _id 정수형 기본키*/
        db.execSQL(sql);
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "drop table if exists dog_profile";
        db.execSQL(sql);

        onCreate(db); // 테이블을 지웠으므로 다시 테이블을 만들어주는 과정
    }

}
