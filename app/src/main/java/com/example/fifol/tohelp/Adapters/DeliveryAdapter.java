package com.example.fifol.tohelp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.example.fifol.tohelp.DeliveriesActivities.MissionDetails;
import com.example.fifol.tohelp.DeliveriesActivities.MissionsScreen;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.CloudentKeys;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import com.example.fifol.tohelp.Utils.SingletonUtil;
import com.example.fifol.tohelp.Utils.UserData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<MyOrdersData> values;

        private UserData courierData = UserData.getCurrentUser();

        private CloudantClient client = CloudentKeys.getClientBuilder();
        private String COURIERS_MISSION = "users";


        public DeliveryAdapter(Context context, List<MyOrdersData> values) {
            super(context,R.layout.delivery_row);
            this.context = context;
            this.values = values;
        }

    @Override
    public int getCount() {return values.size();}

    @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.delivery_row, parent, false);
            TextView desc = rowView.findViewById(R.id.description);
            TextView address = rowView.findViewById(R.id.address);;
            Button confirmOrder = rowView.findViewById(R.id.waze);
            Button details = rowView.findViewById(R.id.details);
            confirmOrder.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View view) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                Map missionOrder = new HashMap();
                                    missionOrder.put("_id",values.get(position)._id);
                                    missionOrder.put("_rev", values.get(position)._rev);
                                courierData.mission = missionOrder;
                                Database db = client.database(COURIERS_MISSION,false);
                                db.update(courierData);
                                return null;
                        }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                Toast.makeText(context,"משימה עודכנה למשימה נוכחית",Toast.LENGTH_LONG).show();
                            }
                        }.execute();

                }
             });
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MissionDetails missionDetails = new MissionDetails();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("missionDetails",  values.get(position));
                    missionDetails.setArguments(bundle);
                    MissionsScreen activity = (MissionsScreen)context;
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.deliveryDetails,missionDetails);
                    transaction.commit();
                }
            });
        desc.setText(getDataFromMap(position));
            address.setText(values.get(position).address);
            return rowView;
        }

        //Get Data from json containing map and return it;
    private String getDataFromMap(int position) {
        String newProducts = new Gson().toJson(values.get(position).products);
        JSONObject descriptions ;
        String itemDesc = "";
        try {
            descriptions =  new JSONObject(newProducts);
            for (int i =0;i<descriptions.length();i++){
                 itemDesc +="  " +descriptions.names().get(i) + "  כמות : " + descriptions.get(descriptions.names().get(i).toString());
                itemDesc= itemDesc.replace("_"," ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemDesc;
    }

    public String setAddress(String text){
        String[] address = text.split("\n");
        return address[2].replace(" ","%20");
    }
}

