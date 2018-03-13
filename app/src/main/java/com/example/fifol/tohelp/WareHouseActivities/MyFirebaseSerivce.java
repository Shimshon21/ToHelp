package com.example.fifol.tohelp.WareHouseActivities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MyFirebaseSerivce extends FirebaseMessagingService {
    public MyFirebaseSerivce() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            System.out.println("*******************************************");
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("*******************************************");
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            try {
                JSONObject obj = new JSONObject(remoteMessage.getNotification().getBody());
                obj.getString("test");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
