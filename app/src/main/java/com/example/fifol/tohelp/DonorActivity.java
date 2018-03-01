package com.example.fifol.tohelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by shim-polak on 2/26/2018.
 */

public class DonorActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_screen);
    }

    //start DonateProduct activity.
    public void goToDonateProduct(View view) {
        Intent i = new Intent(this,MyProductList.class);
        startActivity(i);
    }

    //start CollectionPoints activity.
    public void goToCollectPoints(View view) {
        Intent i = new Intent(this,CollectPointsActivity.class);
        startActivity(i);
    }
}
