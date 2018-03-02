package com.example.fifol.tohelp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.example.fifol.tohelp.Utils.MyData;
import com.example.fifol.tohelp.Utils.SingeltonUtil;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fifol on 01/03/2018.
 */

public class MyProductList extends AppCompatActivity{

    public static final int PREMISSION_REQUEST = 200;
    SurfaceView cameraView;
    Barcode barcode;
    public static Map<String, Bitmap> flyweightImgs = new HashMap();
    private MyData dbData;
    private List<MyData> dbListData;
    final String TEXT_API_KEY = "aturedishavingrooletille";
    final String TEXT_API_SECRET = "b48a197d344b364faef1861d74d4385945f4d49c";
    final String DB_USER_NAME = "5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix";
    final String DB_NAME_TEXT = "products";
    final CloudantClient client = ClientBuilder.account(DB_USER_NAME).username(TEXT_API_KEY).password(TEXT_API_SECRET).build();
    SingeltonUtil singy = SingeltonUtil.getSingy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_product_list);
        cameraView = findViewById(R.id.cameraView);
        getDataById("7290013083678");


       /* String[] values = new String[]{
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
        }*/



    }






    //Ask user for permission to use the camera .
    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PREMISSION_REQUEST);
        }else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = new ScanBarCode();
            fragmentTransaction.replace(R.id.barCodeScannerLay,fragment,null);
            fragmentTransaction.commit();
        }
    }

    //Get the bar code from the fragment ScanBarCode
    public void getBarCode(Barcode barcode){
        if(barcode!= null)
        this.barcode=barcode;
       getDataById(barcode.displayValue);
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

    //Get one document from data by id.
    @SuppressLint("StaticFieldLeak")
    public void getDataById(final String data){
        new AsyncTask<Void,Void,List<MyData>>(){
            @Override
            protected List<MyData> doInBackground(Void... voids) {
                Database db = client.database(DB_NAME_TEXT, false);
                System.out.println(db.find(MyData.class,data)._id);
                dbData = db.find(MyData.class,data);
                setFlyweightImgs(dbData);
                List<MyData> results = new ArrayList<>();
                results.add(dbData);
                return results;
            }
            @Override
            protected void onPostExecute(List<MyData> results) {
                final ListView listview = findViewById(R.id.productListView);
                final MyProductListAdapter adapter = new MyProductListAdapter(MyProductList.this,results);
                listview.setAdapter(adapter);
                System.out.println("Adapter set with reuslt " +results.get(0)._id);
            }
        }.execute();
    }



    //Set search by id from database.
    private String  setQuery(String companyName) {
        String myJson="";
        try {
            // A Java type that can be serialized to JSON
            JSONObject myQuery = new JSONObject();
            //get the query conditions
            JSONObject myQueryField = new JSONObject();
            //Here put the search by condtioin.
            myQueryField.put("$eq", companyName);
            JSONObject myData = new JSONObject();
            myData.put("company", myQueryField);
            myQuery.put("selector", myData);
            //get the fields
            //Here put the field u want to be returend
            myQuery.put("fields", "[_id,company,title,desc,_attachments]");
            myQuery.put("sort", "[{_id,asc}]");
            Log.e("JSON", "myJson: " + myQuery.toString());
            myJson = myQuery.toString();
        } catch (JSONException e) {

        }
        return myJson;
    }

    public void setFlyweightImgs(MyData item){
        try {
            String url = singy.getImageAttachment(item);
            if (flyweightImgs.get(url)== null){
                flyweightImgs.put(url, BitmapFactory.decodeStream(new URL(url).openStream()));
                System.out.println("Image decoded !!!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getImageAttachment(MyData item) {
        try {
            JSONObject jsonStr = new JSONObject(new Gson().toJson(item._attachments));
            String url="https://5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix.cloudant.com/products/"+item._id+"/"+ jsonStr.names().get(0);
            return url;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Get all documents by company type.
    @SuppressLint("StaticFieldLeak")
    public List<MyData> getCompanyaData(final String companyName){
        System.out.println("get company data");
        new AsyncTask<Void, Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //return databases.toString();
                Database db = client.database(DB_NAME_TEXT, false);
                String myJson=setQuery(companyName);
                List<MyData> test = db.findByIndex(myJson, MyData.class);
                dbListData = test;
                for (MyData item : test) {
                    setFlyweightImgs(item);
                    System.out.println("decoding");
                }
                System.out.println("finished");
                return null;
            }
        }.execute();
        return dbListData;
    }
}
