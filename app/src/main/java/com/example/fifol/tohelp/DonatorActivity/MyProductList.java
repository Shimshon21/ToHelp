package com.example.fifol.tohelp.DonatorActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.fifol.tohelp.Adapters.MyBasketListAdapter;
import com.example.fifol.tohelp.Adapters.MyProductListAdapter;
import com.example.fifol.tohelp.DeliveriesActivities.DelivaryDetailsFrag;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.CloudentKeys;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import com.example.fifol.tohelp.Utils.MyProdutsData;
import com.example.fifol.tohelp.Utils.MySqlLite;
import com.example.fifol.tohelp.Utils.SingletonUtil;
import com.example.fifol.tohelp.Utils.UserData;
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
    UserData currentUser = UserData.getCurrentUser();

    final String DB_NAME_TEXT = "products";


   public final CloudantClient client = CloudentKeys.getClientBuilder();
    SingletonUtil singy = SingletonUtil.getSingy();
    public SQLiteDatabase productsSqliteDb;

    @Override
    @SuppressLint("StaticFieldLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_product_list);
        productsSqliteDb = new MySqlLite(this).getWritableDatabase();
        cameraView = findViewById(R.id.cameraView);
        listview = findViewById(R.id.productListView);
        progressBar = findViewById(R.id.productsLoading);
        final List<Map> productSql = singy.getAllData(productsSqliteDb);

        //Get / Save images  and setting adapter.
            new AsyncTask<Void, Void, List<Map>>() {
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

        //Save images in flyweight list for improve UX.
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
            fragmentTransaction.replace(R.id.productListLay,fragment,null);
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
                    //bug in xiaomi redmi note 4 worth checking.
                    try {
                        askPermission();
                    }catch (RuntimeException e){
                        e.printStackTrace();
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.productListLay,new DelivaryDetailsFrag());
        transaction.commit();
    }

    //Get one document from data by id from bar code scanner.
    @SuppressLint("StaticFieldLeak")
    public void getDataById(final String data){
        new AsyncTask<Void,Void,MyProdutsData>(){
            @Override
            protected MyProdutsData doInBackground(Void... voids) {
                Database db = client.database(DB_NAME_TEXT, false);
                try{
                    dbData = db.find(MyProdutsData.class,data);
                }catch (RuntimeException e){
                    e.printStackTrace();
                    dbData=null;
                }
                if(dbData != null) {
                    setFlyweightAttchImgs(dbData);
                    MyProdutsData item = dbData;
                    return item;
                }return null;
            }

            @Override
            protected void onPostExecute(MyProdutsData results) {
                if(results!=null) {

                    MyProdutsData item = results;
                    setOrUpdateSql(item,currentUser);
                    //INSERT OR REPLACE INTO products (ProductId, ProductImage, ProductDesc,ProductTitle,Count) VALUES ('123','someimagae','awsome','title',(count = '1' Where (SELECT Count FROM products WHERE ProductId = '123')IS NULL +1))
                   List<Map> productSql = singy.getAllData(productsSqliteDb);
                    adapter.swap(productSql);

                }else {
                    new AlertDialog.Builder(MyProductList.this).setTitle("משהו השתבש!:            ").setMessage("מוצר זה אינו קיים במערכת.").show();
                }
            }
        }.execute();
    }

    //Added into user name table values , or update existed table values.
    private void setOrUpdateSql(MyProdutsData item, UserData userData) {
        Cursor cursor = productsSqliteDb.rawQuery("SELECT Count FROM "+userData.name+ " WHERE ProductId = (?)",new String[]{item._id});
        if (cursor.moveToFirst()){
            String insert2 = "INSERT OR REPLACE INTO "+userData.name+"(ProductId, ProductImage, ProductDesc,ProductTitle,Count) VALUES ((?),(?),(?),(?),((SELECT Count FROM products WHERE ProductId = (?))+'1'))";
            String url =singy.getImageAttachment(item);
            productsSqliteDb.execSQL(insert2,new String[]{item._id,url,item.desc,item.title,item._id});
        }else {
            String insert = "INSERT INTO "+userData.name+" (ProductId,ProductImage,ProductDesc,ProductTitle,Count) VALUES ((?),(?),(?),(?),'1')";
            String url =singy.getImageAttachment(item);
            productsSqliteDb.execSQL(insert,new String[]{item._id,url,item.desc,item.title});
        }
        cursor.close();
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
                }
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

}
