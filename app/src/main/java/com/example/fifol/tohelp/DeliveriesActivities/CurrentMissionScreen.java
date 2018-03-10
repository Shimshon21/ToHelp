package com.example.fifol.tohelp.DeliveriesActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.CourierData;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fifol on 06/03/2018.
 */

public class CurrentMissionScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curent_mission_screen);
        if(CourierData.mission!=null) {
            MyOrdersData myOrdersData = CourierData.mission;
            String name = myOrdersData.doanatorName;
            String address = myOrdersData.address;
            String phone = myOrdersData.phone;
            System.out.println(myOrdersData.products);
            try {
                JSONObject product = new JSONObject( new Gson().toJson(myOrdersData.products));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}