package com.example.fifol.tohelp.DeliveriesActivities;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.DocumentConflictException;
import com.example.fifol.tohelp.DonatorActivity.MyProductList;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import com.example.fifol.tohelp.Utils.MySqlLite;
import com.example.fifol.tohelp.Utils.UserData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.fifol.tohelp.Utils.SingletonUtil.singy;

/**
 * Created by shim-polak on 3/8/2018.
 */

public class DelivaryDetailsFrag extends Fragment {

    Button confirmDetails,closeFrag;
    EditText name,address,phone,email ;
    SQLiteDatabase productsSqliteDb ;
    final String DB_NAME_ORDERS="donaters_delivery_orders";
    UserData currentUser =  UserData.getCurrentUser();
    ListView productslist;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        productsSqliteDb = new MySqlLite(getContext()).getWritableDatabase();
        return inflater.inflate(R.layout.confirm_delivery_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        closeConfamationFrag();
        prodBforeConform();
        setViewsTexts();
        confirmDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            addDonationRequest();
            }
        });
    }

    //set text for local EditText Views.
    private void setViewsTexts() {
        name.setText(currentUser.name);
        email.setText(currentUser._id);
        phone.setText(currentUser.phone);
        address.setText(currentUser.adress);
    }

    //Set list of products text before conformation.
    private void prodBforeConform() {
        List<Map> allSqlData = singy.getAllData(productsSqliteDb);
        String [] productArray = new String[allSqlData.size()];
        for (Map nrmMap:allSqlData){
            productArray[allSqlData.indexOf(nrmMap)] = nrmMap.get("Count") + " כמות "  + nrmMap.get("ProductDesc") + ""+nrmMap.get("ProductTitle");
        }
        productslist.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.array_textview_adapter,productArray));
    }

    //Set views connections.
    private void findViews(View view) {
        closeFrag = view.findViewById(R.id.closeFrag);
        confirmDetails=view.findViewById(R.id.confirmBtn);
        name = view.findViewById(R.id.input_name);
        address = view.findViewById(R.id.input_address);
        email = view.findViewById(R.id.input_email);
        phone =view.findViewById(R.id.input_password);
        productslist = view.findViewById(R.id.products);
    }

    private void closeConfamationFrag() {
        closeFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //*****Write into DB*****//
    @SuppressLint("StaticFieldLeak")
    private void addDonationRequest(){
        if(productsSqliteDb.rawQuery("Select * from "+currentUser.name,null).moveToFirst()) {
           MyProductList myProductList = (MyProductList)getActivity();
          final CloudantClient client = myProductList.client ;
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    final Database db = client.database(DB_NAME_ORDERS, false);
                    Map<String, Integer> products = new HashMap<>();
                    List<Map> ownedProducts = singy.getAllData(productsSqliteDb);
                    for (Map myMap : ownedProducts) {
                        products.put(myMap.get("ProductTitle") + "_" + myMap.get("ProductDesc"), Integer.parseInt(myMap.get("Count").toString()));
                    }
                    final MyOrdersData myOrdersData = new MyOrdersData( currentUser._id,currentUser.name, currentUser.adress, products,currentUser.phone,"system");
                    try {
                        db.save(myOrdersData);
                    }catch (DocumentConflictException e){
                        e.printStackTrace();
                        System.out.println("request alrdy assigned.");
                        return false;
                    }
                    Log.e("TAG", "doInBackground: cloudant data was saved.... ");
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean aVoid) {
                    System.out.println("****************pushed data successfully***************************");
                    if(aVoid) {
                        new AlertDialog.Builder(getContext()).setTitle("תרומה התבצעה:").setMessage("תרומתך נקלטה במערכת אנחנו נשלח הודעה ששליח יגיע אליך.").show();
                    }else {
                        new AlertDialog.Builder(getContext()).setTitle("תרומתך נרשמה כבר למערכת").setMessage("לא ניתן לעדכן תרומה, לברורים התקשר ל-0565498415").show();
                    }
                    closeFragment();
                }
            }.execute();

        }else Toast.makeText(getContext(),"אנא הוסף מוצר לרשימה.",Toast.LENGTH_SHORT).show();
    }


    private void closeFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(DelivaryDetailsFrag.this);
        fragmentTransaction.commit();
    }


}
