package com.example.fifol.tohelp.Utils;

import java.util.Map;

/**
 * Created by shim-polak on 3/6/2018.
 */

public class MyOrdersData {
    String _id;
    String doanatorName;
    String address;
    String phone;
    Map<String,Integer> products;


    public MyOrdersData(){}

    public MyOrdersData(String id,String doanatorName,String address,Map<String,Integer> products,String phone){
        this._id = id;
        this.phone = phone;
        this.doanatorName = doanatorName;
        this.address = address;
        this.products = products;
    }
}
