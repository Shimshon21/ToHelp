package com.example.fifol.tohelp.DeliveriesActivities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.DocumentConflictException;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.CloudentKeys;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import com.example.fifol.tohelp.Utils.UserData;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fifol on 06/03/2018.
 */

public class CurrentMissionScreen extends AppCompatActivity {
    TextView donator, donatorAddress, phone, status;
    ListView products;
    UserData courierData = UserData.getCurrentUser();
    CloudantClient client = CloudentKeys.getClientBuilder();
    String ORDERS_DATABASE = "donaters_delivery_orders";
    String COURIER_USERS = "users";
    JSONObject jsonStr;
    MyOrdersData ordersData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curent_mission_screen);
        if (!courierData.mission.equals("no mission")) {
            setViews();
            getCourierCurrentMission();
        } else {
            new AlertDialog.Builder(this).setMessage("אין משימה קיימת !").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).setTitle("אין משימה קיימת !").show();
        }
    }


    //Get the courier mission by mission _id from data base - "donaters_delivery_orders" ;
    @SuppressLint("StaticFieldLeak")
    private void getCourierCurrentMission() {
        new AsyncTask<Void, Void, MyOrdersData>() {

            @Override
            protected MyOrdersData doInBackground(Void... voids) {
                try {
                    jsonStr = new JSONObject(new Gson().toJson(courierData.mission));
                    final Database db = client.database(ORDERS_DATABASE, false);
                    ordersData = db.find(MyOrdersData.class, jsonStr.getString("donatorId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return ordersData;
            }
            @Override
            protected void onPostExecute(MyOrdersData ordersData) {
                super.onPostExecute(ordersData);
                donator.setText(ordersData.doanatorName);
                donatorAddress.setText(ordersData.address);
                phone.setText(ordersData.phone);
                status.setText(ordersData.process);
                setProductsAdapter();
            }
        }.execute();
    }

    //Find views by id.
    private void setViews() {
        donatorAddress = findViewById(R.id.address);
        donator = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        products = findViewById(R.id.products);
        status = findViewById(R.id.status);
    }

    //Set adapter for courier products list to collect.
    public void setProductsAdapter(){
       Map<String,Integer> productsMap = ordersData.products;
        List<String> productsList = new ArrayList<>();
        for(String key :productsMap.keySet()){
            productsList.add(key.replace("_"," ") +"  כמות: " + productsMap.get(key));
        }
       products.setAdapter( new ArrayAdapter<String>(this,R.layout.array_textview_adapter,productsList));
    }

    //Change mission status in databases "donaters_delivery_orders" and "users".
    @SuppressLint("StaticFieldLeak")
    public void changeStatus(View view) {
        final Database db = client.database(ORDERS_DATABASE, false);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                switch (ordersData.process){
                    case "שליח בדרך":
                        ordersData.process = "מוצר נלקח";
                        try {
                            db.update(ordersData);
                        } catch (DocumentConflictException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "מוצר נלקח":
                        //Todo update warhouse count coloumns database.
                        final Database dbUsers = client.database(COURIER_USERS, false);
                        courierData.mission = "no mission" ;
                        dbUsers.update(courierData);
                        db.remove(ordersData);
                        Log.d("initilize", "mission had been restart ");
                        break;
                }
                System.out.println("סטאטוס השתנה בהצלחה!!");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(!courierData.mission.equals("no mission"))
                status.setText(ordersData.process);
                finish();
            }
        }.execute();
    }
    }

