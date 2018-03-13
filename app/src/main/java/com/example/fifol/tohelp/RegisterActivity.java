package com.example.fifol.tohelp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

    EditText userNameRegister;
    EditText userPassRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userNameRegister = findViewById(R.id.userNameRegister);
        userPassRegister = findViewById(R.id.passwordRegister);

    }


    public void registerUser(View view){
        //todo check of the user is not registered , if not write the user to the data base
        if(userPassRegister.getText().length() < 4  ||  userNameRegister.getText().length() < 4 ){
            Toast.makeText(this, "password and username must contain minimum of  4 chars", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"registered",Toast.LENGTH_SHORT).show();
        }
    }
    public void backToLogin(View view){
        this.finish();
    }
}
