package com.example.fifol.tohelp.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by shim-polak on 3/9/2018.
 */

public  class UserData  {

    public    String role;//user , courier ,secretary,warehouseManager.
   public   String _id;
   public  String _rev;
   public    String name;
   public    String adress;
   public    String phone;
    public   String  password;
    public  Object mission;
   public static UserData currentUser;

   private UserData() {
   }



   private UserData(String _id,String _rev, String name, String password, String telephone, String address, String role) {
      this._id = _id;
      this._rev = _rev;
      this.password = password;
      this.name = name ;
      this.phone = telephone;
      this.adress = address;
      this.role = role;
   }
   private UserData(String _id, String name, String password, String telephone, String address, String role, Object mission) {
      this._id = _id;
      this._rev = _rev;
      this.password = password;
      this.name = name ;
      this.phone = telephone;
      this.adress = address;
      this.role = role;
      this.mission=mission;
   }

   public static void setCurrentUser(UserData current){
     currentUser = current;
   }


   public static UserData getCurrentUser(){
      return currentUser;
   }


   public static void logOut(){
      currentUser = null;
   }

}


