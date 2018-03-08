package com.example.fifol.tohelp.Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fifol.tohelp.MyProductList;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.MySqlLite;
import com.example.fifol.tohelp.Utils.SingletonUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by fifol on 01/03/2018.
 */

    public class MyBasketListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private List<Map> values;
        SingletonUtil singy = SingletonUtil.getSingy();
        SQLiteDatabase db ;

        public MyBasketListAdapter(Context context, List<Map> values) {
            super(context, R.layout.my_product_row);
            this.context = context;
            this.values = values;
            System.out.println(values);
            db= new MySqlLite(context).getReadableDatabase();
        }

    @Override
    public int getCount() {
      return   values.size();
    }


    @NonNull
    @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            final Map item = values.get(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.my_product_row, parent, false);
                System.out.println("first only");
            }
            setViewsText(convertView,item);
            setListners(convertView,item);
            return convertView;
        }

        //Configure buttons and listners.
    private void setListners(View convertView, final Map<String,Object>item) {
        Button removeItem = convertView.findViewById(R.id.removeItemBtn);
        Button plusItem = convertView.findViewById(R.id.addItemBtn);
        //Add  column 'Count' by one.
        plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sum = Integer.parseInt(item.get("Count").toString())+1;
                String insert = " UPDATE products SET Count=(?) WHERE ProductId =(?)";
                db.execSQL(insert, new Object[]{sum,item.get("ProductId")});
                List<Map> sqlData=singy.getAllData(db);
                values.clear();
                values.addAll(sqlData);
                MyBasketListAdapter.this.notifyDataSetChanged();
        }});

        //Decrease column 'Count' by one.
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseCount(item);
                List<Map> sqlData=singy.getAllData(db);
                values.clear();
                values.addAll(sqlData);
                MyBasketListAdapter.this.notifyDataSetChanged();
            }
        });
    }

    //Set product texts(description,title,count) and image.
    private void setViewsText(View convertView, Map<String,Object>item) {
        TextView productDesc = convertView.findViewById(R.id.productDesc);
        TextView productTitle = convertView.findViewById(R.id.productTitle);
        TextView productCount = convertView.findViewById(R.id.productNumber);
        productCount.setText(item.get("Count").toString());
        productTitle.setText(item.get("ProductTitle").toString());
        productDesc.setText(item.get("ProductDesc").toString());
        ImageView productImage = convertView.findViewById(R.id.productImg);
        String url = item.get("ProductImage").toString();
        productImage.setImageBitmap( MyProductList.flyweightImgs.get(url));
    }

    //Decrease products in basket by one if below 1 remove it.
    private void decreaseCount(Map<String,Object> item) {
        int sum = Integer.parseInt(item.get("Count").toString())-1;
        String insert = "";
        if(sum>0) {
            insert = " UPDATE products SET Count=(?) WHERE ProductId =(?)";
            db.execSQL(insert, new Object[]{sum,item.get("ProductId")});
        } else {
            insert = "DELETE FROM products WHERE ProductId =(?)";
            db.execSQL(insert, new Object[]{item.get("ProductId")});
        }
    }

    //Update the value and refresh the list.
    public void swap(List<Map> data){
        if(data == null || data.size()==0)
            return;
        else{
            this.values = data;
        }
        notifyDataSetChanged();
    }
}
