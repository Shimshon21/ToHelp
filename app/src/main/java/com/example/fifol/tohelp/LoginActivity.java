package com.example.fifol.tohelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fifol.tohelp.DeliveriesActivities.DeliveryScreen;
import com.example.fifol.tohelp.DonatorActivity.DonorActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by shim-polak on 2/26/2018.
 */

public class LoginActivity extends Activity {
    EditText name;
    EditText password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_screen);
        name = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        httpWithAsync();
    }


    public void skipIn(View view) {
        Intent i = new Intent(this,DonorActivity.class);
        startActivity(i);
    }

    public void logIn(View view) {
        if(checkCredentials() == true){
            Intent i = new Intent(this,DeliveryScreen.class);
            startActivity(i);
        }else if(checkCredentials() == false){
            Toast.makeText(this,"user doesn't exists", Toast.LENGTH_LONG).show();

        }
    }

    public void register(View view){
        if(password.getText().length() < 4  ||  name.getText().length() < 4 ){
            Toast.makeText(this, "password and username must contain minimum of  4 chars", Toast.LENGTH_LONG).show();
        }else{
            
            Toast.makeText(this,"registered",Toast.LENGTH_SHORT).show();
        }
    }


    //TODO pull the data from cloudant and check if the user exists
    public Boolean checkCredentials(){
        if (name.getText().toString().equals("tamir") && password.getText().toString().equals("pass")) {
            return  true;
        }else{
             return false;
        }
    }

    public  void httpWithAsync(){
               new  AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {sendHttp();return null;
                            }
        }.execute();
            }

           //TODO fix the FileNotFoundException that received from the http request
           //Send http request to trigger ibm actions
            public void sendHttp() {
                String url = "https://openwhisk.ng.bluemix.net/api/v1/namespaces/1tamir198_tamirSpace/actions/testing_trigger";
                //api key that i got from blumix EndPoins
                       String apiKey = "530f095a-675e-4e1c-afe0-4b421201e894:0HriiSRoYWohJ4LGOjc5sGAhHvAka1gwASMlhRN8kA5eHgNu8ouogt8BbmXtX21N";
               try {
                   //JSONObject jsonObject = new JSONObject().put("openwhisk.ng.bluemix.net" ,"c1165fd1-f4cf-4a62-8c64-67bf20733413:hdVl64YRzbHBK0n2SkBB928cy2DUO5XB3yDbuXhQ1uHq8Ir0dOEwT0L0bqMeWTTX");
                   String res = (new HttpRequest(url)).prepare(HttpRequest.Method.POST).withData(apiKey).sendAndReadString();
                   //String res = new HttpRequest(url).prepare(HttpRequest.Method.POST).withData(jsonObject.toString()).sendAndReadString();
                       System.out.println( res + "***********");

                           } catch ( IOException exception) {
                        exception.printStackTrace();
                        System.out.println(exception + "   some some some");
                       System.out.println("catched error from response ***********");
                   }
            }
}