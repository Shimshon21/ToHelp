package com.example.fifol.tohelp.DeliveriesActivities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.MyOrdersData;
import com.example.fifol.tohelp.Utils.SingletonUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DeliveryAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<MyOrdersData> values;

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
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.delivery_row, parent, false);
            TextView desc = rowView.findViewById(R.id.description);
            TextView address = rowView.findViewById(R.id.address);;
            Button waze = rowView.findViewById(R.id.waze);
            Button details = rowView.findViewById(R.id.details);
       String newProducts = new Gson().toJson(values.get(position).products);
       JSONObject descriptions =new JSONObject();
        try {
          descriptions =  new JSONObject(newProducts);
            System.out.println(descriptions.names().get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  desc.setText(values.get(position).products.get(0));
           // waze.setTag(setAddress(values.get(position).address));
            System.out.println(values.get(position).products.get(0));
            System.out.println(values.get(position));
            address.setText(values.get(position).address);
            return rowView;
        }

    public String setAddress(String text){
        String[] address = text.split("\n");
        return address[2].replace(" ","%20");
    }
}

