package com.example.fifol.tohelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fifol on 01/03/2018.
 */

    public class MyProductListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public MyProductListAdapter(Context context, String[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.my_product_row, parent, false);
            TextView textView = rowView.findViewById(R.id.productInfo);
            textView.setText(values[position]);
            ImageView imageView = rowView.findViewById(R.id.productImg);
            return rowView;
        }
}
