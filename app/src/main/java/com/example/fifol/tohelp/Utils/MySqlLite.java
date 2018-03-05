package com.example.fifol.tohelp.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cloudant.sync.documentstore.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sh-polak on 30/10/2017.
 */

public class MySqlLite extends SQLiteOpenHelper {
   private static final String dbName="MyDataBase";
    String[] initialStat={"CREATE TABLE  IF NOT EXISTS  products(id INTEGER PRIMARY KEY,ProductId VARCHAR(20),ProductImage Blob,ProductDesc VARCHAR(20),ProductTitle VARCHAR(20),Count INTEGER(3), UNIQUE(ProductId))"};
    public MySqlLite(Context context) {
        super(context,dbName, null, 1);
    }
    private void initDb(SQLiteDatabase db){
        for(String table:initialStat){
            db.execSQL(table);
        }
    }//sql create the data base
    @Override
    public void onCreate(SQLiteDatabase db) {
       initDb(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
