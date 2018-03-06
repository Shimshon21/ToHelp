package com.example.fifol.tohelp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.sync.internal.mazha.DocumentConflictException;
import com.example.fifol.tohelp.Adapters.MyBasketListAdapter;
import com.example.fifol.tohelp.Adapters.MyProductListAdapter;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import com.example.fifol.tohelp.Utils.MyProdutsData;
import com.example.fifol.tohelp.Utils.MySqlLite;
import com.example.fifol.tohelp.Utils.SingletonUtil;
import com.google.android.gms.vision.barcode.Barcode;

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
    private MyProdutsData dbData;
    private List<MyProdutsData> dbListData;
    public ListView listview;
    ProgressBar progressBar;
    MyBasketListAdapter adapter;

    final String TEXT_API_KEY = "aturedishavingrooletille";
    final String TEXT_API_SECRET = "b48a197d344b364faef1861d74d4385945f4d49c";
    final String DB_USER_NAME = "5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix";
    final String DB_NAME_TEXT = "products";
    final String DB_NAME_ORDERS="donaters_delivery_orders";

    final CloudantClient client = ClientBuilder.account(DB_USER_NAME).username(TEXT_API_KEY).password(TEXT_API_SECRET).build();
    SingletonUtil singy = SingletonUtil.getSingy();
    SQLiteDatabase productsSqliteDb;

    @Override
    @SuppressLint("StaticFieldLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_product_list);
        productsSqliteDb = new MySqlLite(this).getReadableDatabase();
        cameraView = findViewById(R.id.cameraView);
        listview=findViewById(R.id.productListView);
        progressBar=findViewById(R.id.productsLoading);
        final List<Map> productSql = singy.getAllData(productsSqliteDb);
        new AsyncTask<Void,Void,List<Map>>(){
            @Override
            protected List<Map> doInBackground(Void... avoid) {
                setFlyweight(productSql);
                return null;
            }
            @Override
            protected void onPostExecute(List<Map> aVoid) {
                adapter = new MyBasketListAdapter(MyProductList.this, productSql);
                listview.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
          }
        public void  setFlyweight(List<Map> list){
            System.out.println(list);
            for (Map key:list){
                System.out.println("decoid" + key);
                if(flyweightImgs.get(key.get("ProductImage"))==null){
                    String url = key.get("ProductImage").toString();
                    try {
                        flyweightImgs.put(url, BitmapFactory.decodeStream(new URL(url).openStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

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
                    if(!isFinishing()) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment fragment = new ScanBarCode();
                        fragmentTransaction.add(fragment, null);
                        try {
                            //Todo - fix commit problem first time ask premmision crushes of bundleInstane etc...
                            fragmentTransaction.commit();
                        }catch (IllegalStateException e){
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(this,"הסורק לא יעבור בלי הרשאה למצלמה", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void goBackClick(View view) {
        finish();
    }

    public void barcodeClick(View view) {
        askPermission();
    }

    public void sendProductOrder(View view) {
       elfiTransaction();
    }
    private void elfiTransaction() {
        //todo - Fragment for products ElFi!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Map <String,Object> outMap = new HashMap<>();
       List<Map> elfiMap = singy.getAllData(productsSqliteDb);
       for (Map<String,Object> nrmMap:elfiMap){

       }
        writeDB();
    }

    //*****Write into DB*****//
    @SuppressLint("StaticFieldLeak")
    private void writeDB(){
        if(productsSqliteDb.rawQuery("Select * from products",null).moveToFirst()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //return databases.toString();
                    Database db = client.database(DB_NAME_ORDERS, false);
                    Map<String, Integer> products = new HashMap<>();
                    List<Map> ownedProducts = singy.getAllData(productsSqliteDb);
                    for (Map<String, Object> myMap : ownedProducts) {
                        System.out.println(myMap);
                        products.put(myMap.get("ProductTitle") + "_" + myMap.get("ProductDesc"), Integer.parseInt(myMap.get("Count").toString()));
                    }
                    // A Java type that can be serialized to JSON
                    //write document in to db
                    MyOrdersData myOrdersData = new MyOrdersData( "Shim@gmail.com","shimshon", "margalit303", products,"telephone");
                    //MyProdutsData myProdutsData = new MyProdutsData(  );
                    //todo fix existed document bug.
                    try {
                        db.save(myOrdersData);
                    }catch (com.cloudant.client.org.lightcouch.DocumentConflictException e){
                        e.printStackTrace();
                        Toast.makeText(MyProductList.this,"מצטערים מוצר זה נקלט במערכת",Toast.LENGTH_LONG).show();
                    }
                    Log.e("TAG", "doInBackground: cloudant data was saved.... ");
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    System.out.println("****************pushed data successfully***************************");
                    new AlertDialog.Builder(MyProductList.this).setTitle("תרומה התבצאה:").setMessage("תרומתך נקלטה במערכת אנחנו נשלח הודעה ששליח יגיע אליך.").show();
                }
            }.execute();
            finish();
        }else Toast.makeText(this,"אנא הוסף מוצר לרשימה.",Toast.LENGTH_SHORT).show();
    }

    //Get one document from data by id.
    @SuppressLint("StaticFieldLeak")
    public void getDataById(final String data){
        new AsyncTask<Void,Void,List<MyProdutsData>>(){
            @Override
            protected List<MyProdutsData> doInBackground(Void... voids) {
                Database db = client.database(DB_NAME_TEXT, false);
                try{
                    dbData = db.find(MyProdutsData.class,data);
                }catch (RuntimeException e){
                    e.printStackTrace();
                    dbData=null;
                }
                if(dbData != null) {
                    setFlyweightAttchImgs(dbData);
                    List<MyProdutsData> results = new ArrayList<>();
                    results.add(dbData);
                    return results;
                }return null;
            }
            @Override
            protected void onPostExecute(List<MyProdutsData> results) {
                //Todo - increase count coloumn by one if the product exist else added to data.
                if(results!=null) {
                    MyProdutsData item = results.get(0);
                    Cursor cursor = productsSqliteDb.rawQuery("SELECT Count FROM products WHERE ProductId = (?)",new String[]{item._id});
                    if (cursor.moveToFirst()){
                        System.out.println("Added by one");
                        String insert2 = "INSERT OR REPLACE INTO products (ProductId, ProductImage, ProductDesc,ProductTitle,Count) VALUES ((?),(?),(?),(?),((SELECT Count FROM products WHERE ProductId = (?))+'1'))";
                        String url =singy.getImageAttachment(item);
                        productsSqliteDb.execSQL(insert2,new String[]{item._id,url,item.desc,item.title,item._id});
                    }else {
                        String insert = "INSERT INTO products (ProductId,ProductImage,ProductDesc,ProductTitle,Count) VALUES ((?),(?),(?),(?),'1')";
                        String url =singy.getImageAttachment(item);
                        productsSqliteDb.execSQL(insert,new String[]{item._id,url,item.desc,item.title});
                    }
                    //INSERT OR REPLACE INTO products (ProductId, ProductImage, ProductDesc,ProductTitle,Count) VALUES ('123','someimagae','awsome','title',(count = '1' Where (SELECT Count FROM products WHERE ProductId = '123')IS NULL +1))
                   List<Map> productSql = singy.getAllData(productsSqliteDb);
                    adapter.swap(productSql);
                }else {
                    new AlertDialog.Builder(MyProductList.this).setTitle("משהו השתבש!:            ").setMessage("מוצר זה אינו קיים במערכת.").show();
                }
            }
        }.execute();
    }

    //Set search by company name from database.
    private String  setQuery(String companyName) {
        String myJson="";
        try {
            JSONObject myQuery = new JSONObject();
            JSONObject myQueryField = new JSONObject();
            myQueryField.put("$eq", companyName);
            JSONObject myData = new JSONObject();
            myData.put("company", myQueryField);
            myQuery.put("selector", myData);
            myQuery.put("fields", "[_id,company,title,desc,_attachments]");
            myQuery.put("sort", "[{_id,asc}]");
            Log.e("JSON", "myJson: " + myQuery.toString());
            myJson = myQuery.toString();
        } catch (JSONException e) {

        }
        return myJson;
    }

    //If image is not exist save it in flyweightImgs list.
    public void setFlyweightAttchImgs(MyProdutsData item){
        try {
            String url = singy.getImageAttachment(item);
            if (flyweightImgs.get(url)== null){
                flyweightImgs.put(url, BitmapFactory.decodeStream(new URL(url).openStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Get all documents by company type.
    @SuppressLint("StaticFieldLeak")
    public List<MyProdutsData> getCompanyaData(final String companyName){
        System.out.println("get company data");
        new AsyncTask<Void, Void,List<MyProdutsData>>() {
            @Override
            protected List<MyProdutsData> doInBackground(Void... voids) {
                //return databases.toString();
                Database db = client.database(DB_NAME_TEXT, false);
                String myJson=setQuery(companyName);
                List<MyProdutsData> resultsItems = db.findByIndex(myJson, MyProdutsData.class);
                dbListData = resultsItems;
                for (MyProdutsData item : resultsItems) {
                    setFlyweightAttchImgs(item);
                    System.out.println("decoding");
                }
                System.out.println("finished");
                return resultsItems;
            }

            @Override
            protected void onPostExecute(List<MyProdutsData> resultsItems) {
                MyProductListAdapter adapter = new MyProductListAdapter(MyProductList.this,resultsItems);
               listview.setAdapter(adapter);
               progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
        return dbListData;
    }
    public  void getAdapter(List<Map> data){
       adapter.swap(data);
    }
}
