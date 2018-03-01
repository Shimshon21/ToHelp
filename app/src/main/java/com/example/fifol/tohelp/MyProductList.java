package com.example.fifol.tohelp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by fifol on 01/03/2018.
 */

public class MyProductList extends AppCompatActivity{

    public static final int PREMISSION_REQUEST = 200;
    SurfaceView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_product_list);
        cameraView = findViewById(R.id.cameraView);
        final ListView listview = findViewById(R.id.productListView);

        String[] values = new String[]{
                "מרכז קהילתי נאות שושנים.\nסיגלון 12, חולון.\nטלפון: 03-5503977",
                "מרכז קהילתי ומרכז הספורט בן גוריון.\nקרסל 6, חולון.\nטלפון: 03-5528490",
                "מרכז קהילתי נווה ארזים.\nישעיהו 16, חולון.\nטלפון: 03-5506772",
                "מרכז קהילתי וולפסון.\nצבי ש\"ץ 29, חולון.\nטלפון: 03-6519181",
                "מרכז קהילתי לזרוס.\nסנהדרין 27, חולון.\nטלפון: 03-5030068",
                "מרכז קהילתי נאות רחל.\nחצרים 2, חולון.\nטלפון: 03-5035499",
                "מרכז קהילתי קליין.\nפילדפיה 16, חולון.\nטלפון: 03-5038083",
                "מרכז קהילתי תורני.\nפילדפיה 5, חולון.\nטלפון: 03-5015529",
                "מרכז פסגות.\nסרלין 21, חולון.\nטלפון: 03-6530300",
                "מרכז חנקין.\nחנקין 109, חולון.\nטלפון: 03-5590021",
                "רעים מרכז למחול ותנועת הגוף.\nהופיין 44, חולון.\nטלפון: 03-5035299",
                "מרכז שטיינברג החדש -\nבמה למוסיקה חיה.\nגבעת התחמושת 21, חולון.\nטלפון: 03-5500012"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final MyProductListAdapter adapter = new MyProductListAdapter(this, values);
        listview.setAdapter(adapter);

    }



    //Ask user for permission to use the camera .
    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PREMISSION_REQUEST);
        }else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = new ScanBarCode();
            fragmentTransaction.replace(R.id.shimshon,fragment,null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PREMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment fragment = new ScanBarCode();
                    fragmentTransaction.add(fragment,null);
                    fragmentTransaction.commit();

                } else {
                    Toast.makeText(this,"הסורק לא יעבור בלי הרשאה למצלמה", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void goBackClick(View view) {
        Intent i = new Intent(this,DonorActivity.class);
        startActivity(i);
    }

    public void barcodeClick(View view) {
        askPermission();
    }

    public void sendClick(View view) {
        Intent i = new Intent(this,DonorActivity.class);
        startActivity(i);
    }
}
