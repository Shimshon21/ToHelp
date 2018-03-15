package com.example.fifol.tohelp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fifol.tohelp.DonatorActivity.MyProductList;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.MyProdutsData;
import com.example.fifol.tohelp.Utils.SingletonUtil;

import java.util.List;

/**
 * Created by fifol on 01/03/2018.
 */

    public class MyProductListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<MyProdutsData> values;
    SingletonUtil singy = SingletonUtil.getSingy();

        public MyProductListAdapter(Context context, List<MyProdutsData> values) {
            super(context, R.layout.my_product_row);
            this.context = context;
            this.values = values;
            System.out.println(values);
        }

    @Override
    public int getCount() {
      return   values.size();
    }


    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyProdutsData item = values.get(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.my_product_row, parent, false);
            }
            TextView productDesc = convertView.findViewById(R.id.productDesc);
            TextView textView = convertView.findViewById(R.id.productTitle);
            textView.setText(item.title);
            productDesc.setText(item.desc);
            ImageView imageView = convertView.findViewById(R.id.productImg);
            String url = singy.getImageAttachment(item);
            imageView.setImageBitmap( MyProductList.flyweightImgs.get(url));
            return convertView;
        }
}
