package com.example.fifol.tohelp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import com.example.fifol.tohelp.DeliveriesActivities.DeliveryScreen;
import com.example.fifol.tohelp.DonatorActivity.DonorActivity;
import com.example.fifol.tohelp.Utils.UserData;
import com.example.fifol.tohelp.WareHouseActivities.GetAllStock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by shim-polak on 2/26/2018.
 */

public class LoginActivity extends Activity {

    EditText name;
    EditText password;
    final String TEXT_API_KEY = "aturedishavingrooletille";
    final String TEXT_API_SECRET = "b48a197d344b364faef1861d74d4385945f4d49c";
    final String DB_USER_NAME = "5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix";
    final String DB_NAME_TEXT = "users";
    final CloudantClient client = ClientBuilder.account(DB_USER_NAME)
            .username(TEXT_API_KEY)
            .password(TEXT_API_SECRET)
            .build();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_screen);
        new GetAllStock().getCompanyaData("materna");
        name = findViewById(R.id.userName);
        password = findViewById(R.id.password);
    }


    public void skipIn(View view) {
        Intent i = new Intent(this,DonorActivity.class);
        startActivity(i);
    }

    //Todo register user.
    public void register(View view){
        System.out.println("testing");
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);

    }


    @SuppressLint("StaticFieldLeak")
    public void logIn(View view) {
        System.out.println("Logged in presssed");
        new AsyncTask<String, Void, UserData>() {
            @Override
            protected UserData doInBackground(String... strings) {
                System.out.println("First  " + strings[0]);
                Database db = client.database(DB_NAME_TEXT, false);
                UserData userData;
                try {
                    JSONObject logData = new JSONObject(), qury = new JSONObject(), qury2 = new JSONObject(), id = new JSONObject(), pass = new JSONObject();
                    qury.put("$eq", strings[0]);
                    qury2.put("$eq", strings[1]);
                    id.put("_id", qury);
                    id.put("password", qury2);
                    logData.put("selector",id );
                    Log.i("LogData",logData.toString());
                    userData = db.findByIndex(String.valueOf(logData), UserData.class).get(0);
                } catch (NoDocumentException | JSONException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    userData = null;
                }
                if (userData != null) {
                    return userData;
                } else return null;
            }
            @Override
            protected void onPostExecute(UserData loggedUser) {
                super.onPostExecute(loggedUser);
                if(loggedUser!=null ){
                    UserData.setCurrentUser(loggedUser);
                    Log.i("Loogen in user" ,loggedUser.name);
                    switch (loggedUser.role){
                        case "user":
                            Intent i = new Intent(LoginActivity.this,DonorActivity.class);
                            startActivity(i);
                            break;
                        case "courier":
                             i = new Intent(LoginActivity.this,DeliveryScreen.class);
                            startActivity(i);
                            break;
                        case "warhouse":
                            i = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(i);
                            break;
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"user doesn't exists", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(name.getText().toString(),password.getText().toString());
    }



   /* //TODO pull the data from cloudant and check if the user exists
    public Boolean checkCredentials(String userName,String userPass){
        if (name.getText().toString().equals(userName) && password.getText().toString().equals(userPass)) {
            return  true;
        }else{
             return false;
        }
    }*/


    @SuppressLint("StaticFieldLeak")
    public  void httpWithAsync(){
               new  AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {sendHttp();return null;
                            }
        }.execute();
            }

           //TODO fix the FileNotFoundException that received from the http request
           //when Sending http request(POST) to the trigger EndPoint ibm actions , i am getting FileNotFoundException
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