package com.example.fifol.tohelp.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by shim-polak on 3/6/2018.
 */

public class MyOrdersData implements Parcelable {
  public String _id, doanatorName,
            _rev,
          address,
          phone,
          process;
   public Map<String,Integer> products;


    public MyOrdersData(){}

    public MyOrdersData(String id,String _rev,String doanatorName,String address,Map<String,Integer> products,String phone,String process){
        this._id = id;
        this._rev = _rev;
        this.phone = phone;
        this.doanatorName = doanatorName;
        this.address = address;
        this.products = products;
        this.process = process;
    }

    protected MyOrdersData(Parcel in) {
        _id = in.readString();
        doanatorName = in.readString();
        address = in.readString();
        phone = in.readString();
        process = in.readString();
    }

    public static final Creator<MyOrdersData> CREATOR = new Creator<MyOrdersData>() {
        @Override
        public MyOrdersData createFromParcel(Parcel in) {
            return new MyOrdersData(in);
        }

        @Override
        public MyOrdersData[] newArray(int size) {
            return new MyOrdersData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(doanatorName);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(process);
    }
}
