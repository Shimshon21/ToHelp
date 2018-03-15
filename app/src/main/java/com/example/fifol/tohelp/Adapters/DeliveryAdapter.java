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
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.example.fifol.tohelp.DeliveriesActivities.MissionDetailsFrag;
import com.example.fifol.tohelp.DeliveriesActivities.MissionsScreen;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.CloudentKeys;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import com.example.fifol.tohelp.Utils.UserData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<MyOrdersData> values;

        private UserData courierData = UserData.getCurrentUser();

        private CloudantClient client = CloudentKeys.getClientBuilder();
        private String COURIERS_MISSION = "users";
        private String ORDERS_DATABASE = "donaters_delivery_orders";


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
            final Button confirmOrder = rowView.findViewById(R.id.waze);
            Button details = rowView.findViewById(R.id.details);
            if(values.get(position).process.equals("נקלט במערכת")) {
                takeMission(confirmOrder,position);
            }else {
                confirmOrder.setText(" משימה בטיפול");
            }
            showMissionDetails(details,position);
             desc.setText(getDataFromMap(position));
            address.setText(values.get(position).address);
            return rowView;
        }

        //Show mission details in fragment 'DelivaryDetailsFrag'.
    private void showMissionDetails(Button details, final int position) {
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MissionDetailsFrag missionDetailsFrag = new MissionDetailsFrag();
                Bundle bundle = new Bundle();
                bundle.putParcelable("missionDetailsFrag",  values.get(position));
                missionDetailsFrag.setArguments(bundle);
                MissionsScreen activity = (MissionsScreen)context;
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.deliveryDetails, missionDetailsFrag);
                transaction.commit();
            }
        });
    }

    //Set on click for confirmOrder Button ,if clicked set order as taken.
    private void takeMission(final Button confirmOrder,final int position) {
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                if (courierData.mission.equals("no mission")) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Map missionOrder = new HashMap();
                            missionOrder.put("donatorId", values.get(position)._id);
                            missionOrder.put("rev", values.get(position)._rev);
                            courierData.mission = missionOrder;
                            Database db = client.database(COURIERS_MISSION, false);
                            Database missionDb = client.database(ORDERS_DATABASE, false);
                            values.get(position).process = "שליח בדרך";
                            missionDb.update(values.get(position));
                            db.update(courierData);
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Toast.makeText(context, "משימה עודכנה למשימה נוכחית", Toast.LENGTH_LONG).show();
                            confirmOrder.setText(" משימה בטיפול");
                        }
                    }.execute();
                } else {
                    Toast.makeText(context, "משימה כבר קיימת", Toast.LENGTH_LONG).show();
                }
            }
        });
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


}

