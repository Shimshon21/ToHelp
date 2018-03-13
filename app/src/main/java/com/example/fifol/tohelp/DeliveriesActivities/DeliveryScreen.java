package com.example.fifol.tohelp.DeliveriesActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.UserData;

/**
 * Created by fifol on 06/03/2018.
 */

public class DeliveryScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_screen);
        System.out.println(UserData.getCurrentUser()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }


    public void goToCurrentMission(View view) {
        Intent i = new Intent(this, CurrentMissionScreen.class);
        startActivity(i);
    }

    public void goToMissionScreen(View view) {
        Intent i = new Intent(this, MissionsScreen.class);
        startActivity(i);
    }


    public void logOut(View view) {
        UserData.logOut();
        finish();
    }
}
