package com.example.fifol.tohelp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.DocumentConflictException;
import com.cloudant.sync.documentstore.ConflictException;
import com.example.fifol.tohelp.Utils.CloudentKeys;
import com.example.fifol.tohelp.Utils.RegisterUser;
import com.example.fifol.tohelp.Utils.UserData;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText userPssRegister,userNameRegster,userLastNameRegister,userPhoneRegister,userEmailRegister,userAddressRegister;
    CloudantClient client = CloudentKeys.getClientBuilder();
    String USERS_DB="users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resigter);
        setViews();

    }

    //Find views by id.
    private void setViews() {
        userPssRegister = findViewById(R.id.passwordRegister);
        userNameRegster = findViewById(R.id.nameRegister);
        userLastNameRegister = findViewById(R.id.lastNameRegister);
        userPhoneRegister = findViewById(R.id.phoneRegister);
        userEmailRegister = findViewById(R.id.userEmailRegister);
        userAddressRegister = findViewById(R.id.addressRegister);
    }

    //Add new user to database "users".
    @SuppressLint("StaticFieldLeak")
    public void registerUser(View view){
        if(userPssRegister.getText().toString().length() < 4  ||  userNameRegster.getText().length() < 4 ){
            Toast.makeText(this, "password and username must contain minimum of  4 chars", Toast.LENGTH_LONG).show();
        }else{
            new AsyncTask<Void,Void,Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    boolean successed =true;
                    JSONObject userRegister = new JSONObject();
                    try {
                        RegisterUser  registerUser = new RegisterUser( userEmailRegister.getText().toString().trim()
                                ,userNameRegster.getText().toString()
                                ,userLastNameRegister.getText().toString()
                                ,userAddressRegister.getText().toString()
                                ,userPssRegister.getText().toString()
                                ,userPhoneRegister.getText().toString());
                        Database db = client.database(USERS_DB, false);
                        db.save(registerUser);
                    } catch(DocumentConflictException e){
                        e.printStackTrace();
                        successed =false;
                    }
                    return successed;
                }

                @Override
                protected void onPostExecute(Boolean successed) {
                    super.onPostExecute(successed);
                    if(successed){
                        Toast.makeText(RegisterActivity.this,"registered",Toast.LENGTH_LONG).show();
                        finish();
                    }else    Toast.makeText(RegisterActivity.this,"משתמש כבר קיים במערכת!",Toast.LENGTH_LONG);
                }
            }.execute();
        }
    }

    public void fromRegToLogin(View view){
        finish();
    }

}
