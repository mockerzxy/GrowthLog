package com.example.xueyuanzhang.growthlog.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by xueyuanzhang on 16/7/3.
 */
public class LocalDataBaseHelper extends SQLiteOpenHelper{
    final String CREATE_TABLE_SQL = "create table GrowthLog(_id integer primary key autoincrement, text varchar(200), image varchar(500), time varchar(100), video varchar(200), sound varchar(200))";
    public LocalDataBaseHelper(Context context, String name,int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//       db.execSQL(CREATE_TABLE_SQL);
        System.out.println("------onCreate-------");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       System.out.println("-------onUpdate called-------");
//        db.execSQL("alter table GrowthLog add column sound varchar(200)");
    }
}
