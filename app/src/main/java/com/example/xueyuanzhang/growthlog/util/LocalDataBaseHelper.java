package com.example.xueyuanzhang.growthlog.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xueyuanzhang on 16/7/3.
 */
public class LocalDataBaseHelper extends SQLiteOpenHelper{
    final String CREATE_TABLE_SQL = "create table GrowthLog(_id integer primary key autoincrement, text varchar(200), image varchar(500), time varchar(100))";
    public LocalDataBaseHelper(Context context, String name,int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       System.out.println("-------onUpdate called-------");
    }
}
