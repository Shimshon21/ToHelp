package com.example.fifol.tohelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

    public class CollectPointsAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public CollectPointsAdapter(Context context, String[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.collect_points_row, parent, false);
            TextView textView = rowView.findViewById(R.id.lable);
            ImageView waze_btn = rowView.findViewById(R.id.waze_nav_btn);
            textView.setText(values[position]);
            waze_btn.setTag(setAddress(values[position]));
            return rowView;
        }

        public String setAddress(String text){
            String[] address = text.split("\n");
            return address[2].replace(" ","%20");
        }

    }

