package com.example.fifol.tohelp.Utils;

/**
 * Created by shim-polak on 3/14/2018.
 */

public class RegisterUser {
    public  String role = "user";//user , courier ,secretary,warehouseManager.
    public   String _id;
    public   String name;
    public  String lastName;
    public   String adress;
    public   String phone;
    public   String  password;
    public RegisterUser(String _id, String name, String LastName, String adress, String password, String phone){
       this._id = _id;
        this.name = name;
        this.lastName = LastName;
        this.adress = adress;
        this.password = password;
        this.phone = phone;
    }
}
