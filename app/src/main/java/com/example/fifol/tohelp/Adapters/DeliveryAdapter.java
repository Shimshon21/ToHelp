package com.example.fifol.tohelp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.CourierData;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import com.example.fifol.tohelp.Utils.SingletonUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<MyOrdersData> values;
    CourierData courierData = new CourierData();

        public DeliveryAdapter(Context context, List<MyOrdersData> values) {
            super(context,R.layout.delivery_row);
            this.context = context;
            this.values = values;
        }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.delivery_row, parent, false);
            TextView desc = rowView.findViewById(R.id.description);
            TextView address = rowView.findViewById(R.id.address);;
            Button waze = rowView.findViewById(R.id.waze);
            Button details = rowView.findViewById(R.id.details);
            //Todo add elfi waze integration.
            waze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(courierData.mission);
                    if(courierData.mission == null) {
                        courierData.mission = values.get(position);
                        System.out.println(courierData.mission);
                    } else Toast.makeText(context,"משימה כבר קיימת !",Toast.LENGTH_LONG).show();
                }
            });

        System.out.println(getDataFromMap(position));
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
                System.out.println(descriptions.names().get(i) +":"+descriptions.get(descriptions.names().get(i).toString()));
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

