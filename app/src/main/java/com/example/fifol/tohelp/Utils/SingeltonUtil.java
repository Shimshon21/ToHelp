package com.example.fifol.tohelp.Utils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shim-polak on 3/2/2018.
 */

public class SingeltonUtil {
   public static SingeltonUtil singy = new SingeltonUtil();

   private SingeltonUtil(){

    }

    public static SingeltonUtil getSingy(){
       return singy;
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

}
