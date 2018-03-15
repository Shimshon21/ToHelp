package com.example.fifol.tohelp.DeliveriesActivities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.example.fifol.tohelp.Adapters.DeliveryAdapter;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.CloudentKeys;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import com.example.fifol.tohelp.Utils.UserData;

import java.io.IOException;
import java.util.List;

public class MissionsScreen extends AppCompatActivity {
    final CloudantClient client = CloudentKeys.getClientBuilder();
    String DB_ORDERS = "donaters_delivery_orders";
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_screen);
      listview = findViewById(R.id.listview);
        loadListFromDB();
    }

    //Get all documents from database "donaters_delivery_orders"
    @SuppressLint("StaticFieldLeak")
    private void loadListFromDB() {
        if (UserData.getCurrentUser() != null) {
            new AsyncTask<Void, Void, List<MyOrdersData>>() {
                @Override
                protected List<MyOrdersData> doInBackground(Void... voids) {
                    List<MyOrdersData> allOrders = null;
                    Database db = client.database(DB_ORDERS, false);
                    try {
                        allOrders = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(MyOrdersData.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return allOrders;
                }

                //Set adapter for all orders .
                @Override
                protected void onPostExecute(List<MyOrdersData> allOrders) {
                    super.onPostExecute(allOrders);
                    if (allOrders != null) {
                        final DeliveryAdapter adapter = new DeliveryAdapter(MissionsScreen.this, allOrders);
                        listview.setAdapter(adapter);
                    }
                }
            }.execute();
        }
    }


    public void goBack(View view) {
        finish();
    }
}

