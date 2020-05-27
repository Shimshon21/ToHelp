package com.example.fifol.tohelp.DonatorActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cloudant.sync.internal.sqlite.SQLDatabase;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.MySqlLite;
import com.example.fifol.tohelp.Utils.UserData;

/**
 * Created by shim-polak on 2/26/2018.
 */

public class DonorActivity extends Activity {

    UserData currentUser = UserData.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_screen);
        if (currentUser != null) {
            SQLiteDatabase db = new MySqlLite(this).getWritableDatabase();
            db.execSQL("CREATE TABLE  IF NOT EXISTS  " + currentUser.name + "(id INTEGER PRIMARY KEY,ProductId VARCHAR(20),ProductImage Blob,ProductDesc VARCHAR(20),ProductTitle VARCHAR(20),Count INTEGER(3), UNIQUE(ProductId))");
        }
    }

    //Start DonateProduct activity.
    public void goToDonateProduct(View view) {
        if(UserData.getCurrentUser()!=null) {
            Intent i = new Intent(this, MyProductList.class);
            startActivity(i);
        }else{
            new AlertDialog.Builder(this).setTitle("אינך מחובר").setMessage("כדי להשמתמש בפיצר זה הינך צריך להתחבר במסך הראשי עם שם וסיסמא.").show();
        }
    }
    //Start about us activity
    public void goToAboutUs(View view) {
        Intent i = new Intent(this, AboutUs.class);
        startActivity(i);
    }

    //Start CollectionPoints activity.
    public void goToCollectPoints(View view) {
        Intent i = new Intent(this, CollectPointsActivity.class);
        startActivity(i);
    }

    //Start dial number intent for call donation.
    public void callForMoneyDonation(View view) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 028546212"));
        startActivity(i);
    }

    //Logout user .
    public void logOut(View view) {
         UserData.logOut();
        finish();
    }

    public void options(View view){
        Toast.makeText(this,"פיצר זה אינו זמין כרגע",Toast.LENGTH_SHORT).show();
    }

    public void stock(View view){
        Toast.makeText(this,"פיצר זה אינו זמין כרגע",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     UserData.logOut();
    }
}
