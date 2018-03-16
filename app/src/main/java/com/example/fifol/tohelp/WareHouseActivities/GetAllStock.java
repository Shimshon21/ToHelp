package com.example.fifol.tohelp.WareHouseActivities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.example.fifol.tohelp.Adapters.MyProductListAdapter;
import com.example.fifol.tohelp.DonatorActivity.MyProductList;
import com.example.fifol.tohelp.Utils.CloudentKeys;
import com.example.fifol.tohelp.Utils.MyProdutsData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by shim-polak on 3/12/2018.
 */

public class GetAllStock {
    CloudantClient client = CloudentKeys.getClientBuilder();
   String ALL_PRODUCTS_DB = "products";
   int getProductCount;
    private List<MyProdutsData> dbListData;

   public void getAllByCatgory() {
       Database db = client.database(ALL_PRODUCTS_DB,false);
   }

    //Get all documents by company type.
    @SuppressLint("StaticFieldLeak")
    public List<MyProdutsData> getCompanyaData(final String companyName){
        System.out.println("get company data");
        new AsyncTask<Void, Void,List<MyProdutsData>>() {
            @Override
            protected List<MyProdutsData> doInBackground(Void... voids) {
                //return databases.toString();
                Database db = client.database(ALL_PRODUCTS_DB, false);
                String myJson=setQuery(companyName);
                List<MyProdutsData> resultsItems = db.findByIndex(myJson, MyProdutsData.class);
                dbListData = resultsItems;
                return resultsItems;
            }

            @Override
            protected void onPostExecute(List<MyProdutsData> resultsItems) {
                System.out.println(resultsItems);
            }
        }.execute();
        return dbListData;
    }
    //Set search by id from database.
    private String  setQuery(String companyName) {
        String myJson="";
        try {
            JSONObject myQuery = new JSONObject();
            JSONObject myQueryField = new JSONObject();
            myQueryField.put("$eq", companyName);
            JSONObject myData = new JSONObject();
            myData.put("company", myQueryField);
            myQuery.put("selector", myData);
            myQuery.put("fields", "[_id,company,title,desc,count]");
            myQuery.put("sort", "[{_id,asc}]");
            Log.e("JSON", "myJson: " + myQuery.toString());
            myJson = myQuery.toString();
        } catch (JSONException e) {

        }
        return myJson;
    }

    @SuppressLint("StaticFieldLeak")
    public void updateCount(int position){
        MyProdutsData myProdutsData = dbListData.get(position);
        myProdutsData.count = myProdutsData.count + getProductCount;
        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }
        }.execute();
    }
}
