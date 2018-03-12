package com.example.fifol.tohelp.DeliveriesActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.UserData;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;

/**
 * Created by fifol on 06/03/2018.
 */

public class CurrentMissionScreen extends AppCompatActivity {
    UserData courierData =UserData.currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curent_mission_screen);

        System.out.println(courierData.mission);
        try {
            JSONObject jsonStr = new JSONObject(new Gson().toJson(courierData.mission));
            System.out.println(jsonStr);
            //Todo elfi built activity layout use this datas below:
            String name = jsonStr.getString("doanatorName");
            String phone = jsonStr.getString("phone");
            String address = jsonStr.getString("address");
            String email = jsonStr.getString("_id");
           JSONObject jsonObject = (JSONObject) jsonStr.get("products");
            System.out.println(jsonObject.names());
            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                String namee = it.next();
                System.out.println(namee);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    }
