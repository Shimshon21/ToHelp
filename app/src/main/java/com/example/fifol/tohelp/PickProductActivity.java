package com.example.fifol.tohelp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.android.gms.vision.barcode.Barcode;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by fifol on 22/02/2018.
 */

public class PickProductActivity extends AppCompatActivity {
    public static final int PREMISSION_REQUEST = 200;
    final String TEXT_API_KEY = "aturedishavingrooletille";
    final String TEXT_API_SECRET = "b48a197d344b364faef1861d74d4385945f4d49c";
    final String DB_USER_NAME = "5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix";
    final String DB_NAME_TEXT = "tamal_db";
    FrameLayout layout;
    final CloudantClient client = ClientBuilder.account(DB_USER_NAME)
            .username(TEXT_API_KEY)
            .password(TEXT_API_SECRET)
            .build();

    Button scannerBtn;
    String resultBarCode;

    RecyclerView recyclerView,recyclerView2;
    JSONObject mainJOb;
    String company = "מטרנה",type = "חלבי";
    CategoryGridAdapter categoryGridAdapter;
    ProductGridAdapter productGridAdapterB;

    public static  final int REQUEST_CODE=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_product_screen);
        loadJson();
        recyclerView = (findViewById(R.id.categoty));
        recyclerView2 = (findViewById(R.id.mainGrid));
        scannerBtn = findViewById(R.id.scanBtn);

        GridLayoutManager layoutGrid = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutGrid2 = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutGrid);
        recyclerView2.setLayoutManager(layoutGrid2);

        //readDb();
        setAdapter();

    }

    //Configure grid list adapter.
    public void setAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        categoryGridAdapter = new CategoryGridAdapter(this,getCompanies());
        productGridAdapterB = new ProductGridAdapter(this,getTypes("מטרנה"));
            recyclerView.setAdapter(categoryGridAdapter);
        recyclerView2.setAdapter(productGridAdapterB);
    }

    public JSONArray getCompanies(){
        JSONArray companies = null;
        try {
            companies = mainJOb.getJSONObject("חברה").names();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return companies;
    }

    public JSONArray getTypes(String company){
        JSONArray data = null;
        try {
            data = mainJOb.getJSONObject("חברה").getJSONArray(company);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public JSONArray getProducts(String company, String type){
        JSONArray images = new JSONArray();
        int index = 0;
        JSONArray typeJS = new JSONArray();
        try {

            typeJS = mainJOb.getJSONObject("חברה").getJSONObject(company).getJSONObject(type).names();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return typeJS;

    }

    public void loadJson() {
        try {
            mainJOb = new JSONObject(loadJSONFromAsset(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.tamal_db);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public void categoryClick(View view) {
        String ttl = view.getTag().toString();
        this.company = ttl;
        productGridAdapterB.swap(getTypes(ttl));
    }

    public void typeClick(View view) {
        String ttl = view.getTag().toString();
        getItemData(ttl);
        FragmentTransaction  transaction = getSupportFragmentManager().beginTransaction();
        //transaction.add(R.id.productItem,new ProductItem());
        transaction.commit();
        System.out.println(ttl);
    }

    private void getItemData(String ttl) {
        /*try {
            JSONArray main = mainJOb.getJSONObject("חברה").names();
            for(int i=0;i<main.length();i++){
                JSONArray company = mainJOb.getJSONArray(main.getString(i));
                for(int j=0;j<company.length();j++){
                    System.out.println(company.getJSONObject(j).getString("מספר קטלוגי"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        System.out.println("on construction");
    }

    //Get number of product by scanbar.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this,"Analyze...",Toast.LENGTH_LONG).show();
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){

            if(data!=null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                Toast.makeText(this,"Done"+barcode.displayValue,Toast.LENGTH_LONG).show();
                getData(barcode);

            }
        }
    }

    public void getData(Barcode barcode){
        this.resultBarCode = barcode.displayValue;
    }

    public void goToScanBarCode(View view) {
        askPremmison();
    }

    //Get data from database tamal_DB.
    @SuppressLint("StaticFieldLeak")
    private void readDb() {
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                //return databases.toString();
                Database db = client.database(DB_NAME_TEXT, false);
                JSONObject jsonData=new JSONObject();
                try {
                    JSONObject stringJson=new JSONObject(convert(db.find("products"), Charset.forName("UTF-8")));
                    System.out.println(stringJson.getJSONObject("חברה").get("מטרנה"));
                    jsonData = stringJson;
                } catch (IOException|JSONException e) {
                    e.printStackTrace();
                }
                return jsonData;
            }
            @Override
            protected void onPostExecute(JSONObject jsonData) {
                System.out.println("****************pulled data successfully "+ jsonData + "***************************");
                mainJOb = jsonData;
                setAdapter();
            }
        }.execute();
    }
    public String convert(InputStream inputStream, Charset charset) throws IOException {
        return IOUtils.toString(inputStream, charset);
    }

    public void goBack(View view) {
        finish();
    }

    //Ask user for premission to use the camera .
    private void askPremmison() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PREMISSION_REQUEST);
        }else {
            //Intent intent = new Intent(this, ScanBarCode.class);
            //startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PREMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Intent intent = new Intent(this, ScanBarCode.class);
                    //startActivityForResult(intent, REQUEST_CODE);

                } else {
                    Toast.makeText(this,"הסורק לא יעבור בלי הרשאה למצלמה", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
