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
import com.example.fifol.tohelp.Utils.MyData;
import com.example.fifol.tohelp.Utils.MySqlLite;
import com.example.fifol.tohelp.Utils.SingeltonUtil;
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
    private MyData dbData;
    private List<MyData> dbListData;
    public ListView listview;
    ProgressBar progressBar;
    MyBasketListAdapter adapter;

    final String TEXT_API_KEY = "aturedishavingrooletille";
    final String TEXT_API_SECRET = "b48a197d344b364faef1861d74d4385945f4d49c";
    final String DB_USER_NAME = "5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix";
    final String DB_NAME_TEXT = "products";

    final CloudantClient client = ClientBuilder.account(DB_USER_NAME).username(TEXT_API_KEY).password(TEXT_API_SECRET).build();
    SingeltonUtil singy = SingeltonUtil.getSingy();
    SQLiteDatabase myDb ;

    @Override
    @SuppressLint("StaticFieldLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_product_list);
        myDb = new MySqlLite(this).getReadableDatabase();
        cameraView = findViewById(R.id.cameraView);
        listview=findViewById(R.id.productListView);
        progressBar=findViewById(R.id.productsLoading);
        final List<Map> productSql = getSqlData();
        //Todo - fix flyweight load images
         adapter = new MyBasketListAdapter(MyProductList.this, productSql);
        listview.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);
          }
        public void  setFlyweight(List<Map> list){
            for (Map key:list){
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
                        fragmentTransaction.commit();
                    }
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
                try{
                    dbData = db.find(MyData.class,data);
                }catch (RuntimeException e){
                    e.printStackTrace();
                    dbData=null;
                }
                if(dbData != null) {
                    setFlyweightAttchImgs(dbData);
                    List<MyData> results = new ArrayList<>();
                    results.add(dbData);
                    return results;
                }return null;
            }
            @Override
            protected void onPostExecute(List<MyData> results) {
                if(results!=null) {
                    MyData item = results.get(0);
                    String insert = "INSERT INTO products (ProductId,ProductImage,ProductDesc,ProductTitle) VALUES ((?),(?),(?),(?)) ";
                    String url =singy.getImageAttachment(item);
                    myDb.execSQL(insert,new String[]{item._id,url,item.desc,item.title});
                   List<Map> productSql = getSqlData();
                    adapter = new MyBasketListAdapter(MyProductList.this, productSql);
                    adapter.notifyDataSetChanged();
                }else {
                    new AlertDialog.Builder(MyProductList.this).setTitle("משהו השתבש!:            ").setMessage("מוצר זה אינו קיים במערכת.").show();
                }
            }
        }.execute();
    }

    private List<Map> getSqlData() {
        List<Map> sqlData=new ArrayList<>();

        Cursor sqlProducts = myDb.rawQuery("Select * from products",null);
        for (sqlProducts.moveToFirst();!sqlProducts.isAfterLast();sqlProducts.moveToNext()){
            Map<String,String> map = new HashMap<>();
             map.put(sqlProducts.getColumnName(4) ,sqlProducts.getString(4));
            map.put(sqlProducts.getColumnName(1) ,sqlProducts.getString(1));
            map.put(sqlProducts.getColumnName(2) ,sqlProducts.getString(2));
            map.put(sqlProducts.getColumnName(3) ,sqlProducts.getString(3));
            System.out.println(sqlProducts.getColumnName(3) +""+sqlProducts.getString(3) +" " +sqlProducts.getColumnName(2)+" " +sqlProducts.getString(2)+" "+sqlProducts.getColumnName(1) +""+sqlProducts.getString(1)+"");
            sqlData.add(map);
        }
        return sqlData;
    }

    //Set search by company name from database.
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

    //If image is not exist save it in flyweightImgs list.
    public void setFlyweightAttchImgs(MyData item){
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
    public List<MyData> getCompanyaData(final String companyName){
        System.out.println("get company data");
        new AsyncTask<Void, Void,List<MyData>>() {
            @Override
            protected List<MyData> doInBackground(Void... voids) {
                //return databases.toString();
                Database db = client.database(DB_NAME_TEXT, false);
                String myJson=setQuery(companyName);
                List<MyData> resultsItems = db.findByIndex(myJson, MyData.class);
                dbListData = resultsItems;
                for (MyData item : resultsItems) {
                    setFlyweightAttchImgs(item);
                    System.out.println("decoding");
                }
                System.out.println("finished");
                return resultsItems;
            }

            @Override
            protected void onPostExecute(List<MyData> resultsItems) {
                MyProductListAdapter adapter = new MyProductListAdapter(MyProductList.this,resultsItems);
               listview.setAdapter(adapter);
               progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
        return dbListData;
    }
}
