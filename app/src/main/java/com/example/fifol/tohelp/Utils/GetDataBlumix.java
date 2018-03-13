package com.example.fifol.tohelp.Utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shim-polak on 3/1/2018.
 */

//Class to get data by various ways such as id or company name.
    //Not used yet.
public class GetDataBlumix extends AsyncTask<Void,Void,Void> {
    private static Map<String, Bitmap> flyweightImgs = new HashMap();
    private MyProdutsData dbData;
    private List<MyProdutsData> dbListData;
    final String TEXT_API_KEY = "aturedishavingrooletille";
    final String TEXT_API_SECRET = "b48a197d344b364faef1861d74d4385945f4d49c";
    final String DB_USER_NAME = "5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix";
    final String DB_NAME_TEXT = "products";
    final CloudantClient client = ClientBuilder.account(DB_USER_NAME)
            .username(TEXT_API_KEY)
            .password(TEXT_API_SECRET)
            .build();

    //
    public void setImage(String id, ImageView imageView){
       MyProdutsData item = getDataById(id);
        String url =getImageAttachment(item);
        Bitmap imageBit = flyweightImgs.get(url);
        if(imageBit!= null) {
            imageView.setImageBitmap(flyweightImgs.get(url));
        }else
        Log.w("Image load error", "setImage:product doeas not exist in the data", null);
    }

    private String getImageAttachment(MyProdutsData item) {
        try {
           JSONObject jsonStr = new JSONObject(new Gson().toJson(item._attachments));
          String url="https://5163dd96-e2e4-42f6-8956-24a8ba1360ab-bluemix.cloudant.com/products/"+item._id+"/"+ jsonStr.names().get(0);
          return url;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }





    //Get one document from data by id.
    @SuppressLint("StaticFieldLeak")
    public MyProdutsData getDataById(final String data){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Database db = client.database(DB_NAME_TEXT, false);
                System.out.println(db.find(MyProdutsData.class,data)._id);
                 dbData = db.find(MyProdutsData.class,data);
                setFlyweightImgs(dbData);
                return null;
            }
        }.execute();
        System.out.println("data returned !!!"+dbData._id);
        return dbData;
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

    //*****End reading values from DB*****//
    //Save images if not exist in flyweightImgs to improve load time.
    public void setFlyweightImgs(MyProdutsData item){
            try {
            String url = getImageAttachment(item);
            if (flyweightImgs.get(url)== null){
                flyweightImgs.put(url, BitmapFactory.decodeStream(new URL(url).openStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String convert(InputStream inputStream, Charset charset) throws IOException {
        return IOUtils.toString(inputStream, charset);
    }

    //Get all documents by company type.
    @SuppressLint("StaticFieldLeak")
    public List<MyProdutsData> getCompanyaData(final String companyName){
        System.out.println("get company data");
        return dbListData;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //return databases.toString();
        String companyName="materna";
        Database db = client.database(DB_NAME_TEXT, false);
        String myJson=setQuery(companyName);
        List<MyProdutsData> test = db.findByIndex(myJson, MyProdutsData.class);
        dbListData = test;
        for (MyProdutsData item : test) {
            setFlyweightImgs(item);
            System.out.println("decoding");
        }
        System.out.println("finished");
        return null;
    }
}


