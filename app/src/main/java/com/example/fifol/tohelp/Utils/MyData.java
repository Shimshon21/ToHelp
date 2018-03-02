package com.example.fifol.tohelp.Utils;
import com.google.gson.internal.LinkedTreeMap;

/**
 * Created by Tamir on 25/02/2018.
 */

public class MyData {
    public String _id;
    public String company;
    public String title;
    public String desc;
    public LinkedTreeMap _attachments ;

    public MyData(){}

    public MyData(String id, String company, String title, int myAge, String desc, LinkedTreeMap _attachments) {
        this._id=id;
        this.company = company;
        this.title = title;
        this.desc = desc;
        this._attachments = _attachments;
    }
}
