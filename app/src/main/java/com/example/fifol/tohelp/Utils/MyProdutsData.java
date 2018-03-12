package com.example.fifol.tohelp.Utils;
import com.google.gson.internal.LinkedTreeMap;

/**
 * Created by Tamir on 25/02/2018.
 */

public class MyProdutsData {
    public String _id;
    public String _rev;
    public String company;
    public String title;
    public String desc;
    public String count;
    public LinkedTreeMap _attachments ;

    public MyProdutsData(){

    }
    public MyProdutsData(String id,String _rev, String company, String title, String desc ) {
        this._rev =_rev;
        this._id=id;
        this.company = company;
        this.title = title;
        this.desc = desc;
    }

    public MyProdutsData(String id, String company, String title, String desc,String count , LinkedTreeMap _attachments) {
        this._id=id;
        this.company = company;
        this.title = title;
        this.desc = desc;
        this.count = count;
        this._attachments = _attachments;
    }
}
