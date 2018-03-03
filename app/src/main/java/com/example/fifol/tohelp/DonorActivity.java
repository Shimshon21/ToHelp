package com.example.fifol.tohelp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

/**
 * Created by shim-polak on 2/26/2018.
 */

public class DonorActivity extends Activity {
    int CALL_REQUEST=300;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_screen);
    }

    //start DonateProduct activity.
    public void goToDonateProduct(View view) {
        Intent i = new Intent(this, MyProductList.class);
        startActivity(i);
    }

    //start CollectionPoints activity.
    public void goToCollectPoints(View view) {
        Intent i = new Intent(this, CollectPointsActivity.class);
        startActivity(i);
    }

    public void callForMoneyDonation(View view) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 028546212"));
        startActivity(i);
    }

    public void logOut(View view) {
        finish();
    }
}
