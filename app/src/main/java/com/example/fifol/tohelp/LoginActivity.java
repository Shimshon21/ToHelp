package com.example.fifol.tohelp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.fifol.tohelp.DeliveriesActivities.DeliveryScreen;
import com.example.fifol.tohelp.DeliveriesActivities.MissionsScreen;

import java.io.IOException;


/**
 * Created by shim-polak on 2/26/2018.
 */

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_screen);
        httpWithAsync();
    }

    public void logIn(View view) {
        Intent i = new Intent(this,DeliveryScreen.class);
        startActivity(i);
    }

    public void skipIn(View view) {
        Intent i = new Intent(this,DonorActivity.class);
        startActivity(i);
    }

    public void httpWithAsync(){
               new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                               sendHttp();
                                return null;
                            }
        }.execute();
            }

           //TODO fix the FileNotFoundException that received from the http request
           //Send http request to trigger ibm actions
            public void sendHttp() {
                String url = "https://openwhisk.eu-gb.bluemix.net/api/v1/web/1tamir198_test/default/serverSideSwift.json";
                //api key that i got from blumix EndPoins
                       String apiKey = "530f095a-675e-4e1c-afe0-4b421201e894:0HriiSRoYWohJ4LGOjc5sGAhHvAka1gwASMlhRN8kA5eHgNu8ouogt8BbmXtX21N";
               try {
                      String res = (new HttpRequest(url)).prepare(HttpRequest.Method.POST).withData(apiKey).sendAndReadString();
                       System.out.println(res  + "***********");

                           } catch (IOException exception) {
                        exception.printStackTrace();
                        System.out.println(exception + "   some some some");
                       System.out.println("catched error from response ***********");
                   }
            }
}
