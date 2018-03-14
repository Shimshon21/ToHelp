package com.example.fifol.tohelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText userPssRegister;
    EditText userNameRegster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resigter);
        userPssRegister =findViewById(R.id.passwordRegister);
        userNameRegster =findViewById(R.id.userNameRegister);
    }

    public void registerUser(View view){
        //todo check of the user is not registered , if not write the user to the data base
        if(userPssRegister.getText().length() < 4  ||  userNameRegster.getText().length() < 4 ){
            Toast.makeText(this, "password and username must contain minimum of  4 chars", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"registered",Toast.LENGTH_SHORT).show();
        }
    }

    public void fromRegToLogin(View view){
        finish();
    }

}
