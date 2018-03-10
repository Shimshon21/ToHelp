package com.example.fifol.tohelp.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shim-polak on 3/2/2018.
 */

public class SingletonUtil {
   public static SingletonUtil singy = new SingletonUtil();

   private SingletonUtil(){

    }

    public static SingletonUtil getSingy(){
       return singy;
    }

    public String getImageAttachment(MyProdutsData item) {
        try {
            JSONObject jsonStr = new JSONObject(new Gson().toJson(item._attachments));
            String url="https://5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix.cloudant.com/products/"+item._id+"/"+ jsonStr.names().get(0);
            return url;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    //Get all data in table products
    //@return List<Map> the data from table 'products'
    public List<Map> getAllData(SQLiteDatabase db) {
        List<Map> sqlData = new ArrayList<>();
        UserData userData =new UserData();
        Cursor sqlProducts = db.rawQuery("Select * from "+userData.name,null);
        System.out.println(sqlProducts);
            for (sqlProducts.moveToFirst(); !sqlProducts.isAfterLast(); sqlProducts.moveToNext()) {
                Map<String, Object> map = new HashMap<>();
                System.out.println(map);
                map.put(sqlProducts.getColumnName(4), sqlProducts.getString(4));
                map.put(sqlProducts.getColumnName(1), sqlProducts.getString(1));
                map.put(sqlProducts.getColumnName(2), sqlProducts.getString(2));
                map.put(sqlProducts.getColumnName(3), sqlProducts.getString(3));
                map.put(sqlProducts.getColumnName(5), sqlProducts.getInt(5));
                System.out.println(sqlProducts.getColumnName(3) + "" + sqlProducts.getString(3) + " " + sqlProducts.getColumnName(2) + " " + sqlProducts.getString(2) + " " + sqlProducts.getColumnName(1) + "" + sqlProducts.getString(1) + "");
                sqlData.add(map);
            }
            return sqlData;
        }
        }

