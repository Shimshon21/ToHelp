package com.example.fifol.tohelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


/**
 * Created by shim-polak on 2/26/2018.
 */

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_screen);
    }

    public void logIn(View view) {
        Intent i = new Intent(this,DonorActivity.class);
        startActivity(i);
    }

    public void skipIn(View view) {
        Intent i = new Intent(this,DonorActivity.class);
        startActivity(i);
    }
}
