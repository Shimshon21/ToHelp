package com.example.fifol.tohelp.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sh-polak on 30/10/2017.
 */

public class MySqlLite extends SQLiteOpenHelper {
    //Todo test the sql if recreate itself when user changed.
   private static final String dbName="MyDataBase";
   UserData currentUser = UserData.getCurrentUser();
    String[] initialStat={"CREATE TABLE  IF NOT EXISTS  "+ currentUser.name+"(id INTEGER PRIMARY KEY,ProductId VARCHAR(20),ProductImage Blob,ProductDesc VARCHAR(20),ProductTitle VARCHAR(20),Count INTEGER(3), UNIQUE(ProductId))"};
    public MySqlLite(Context context) {
        super(context,dbName, null, 1);
    }
    private void initDb(SQLiteDatabase db){
        for(String table:initialStat){
            Log.i("Data base Created","the data base was created successfully");
            db.execSQL(table);
        }
    }//sql create the data base
    @Override
    public void onCreate(SQLiteDatabase db) {
       initDb(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("new data base created");
        db.execSQL("CREATE TABLE  IF NOT EXISTS  "+ currentUser.name+"(id INTEGER PRIMARY KEY,ProductId VARCHAR(20),ProductImage Blob,ProductDesc VARCHAR(20),ProductTitle VARCHAR(20),Count INTEGER(3), UNIQUE(ProductId))");

    }
}
