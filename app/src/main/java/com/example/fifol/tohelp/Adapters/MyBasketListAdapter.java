package com.example.fifol.tohelp.Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fifol.tohelp.MyProductList;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.MyData;
import com.example.fifol.tohelp.Utils.MySqlLite;
import com.example.fifol.tohelp.Utils.SingeltonUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by fifol on 01/03/2018.
 */

    public class MyBasketListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<Map> values;
    SingeltonUtil singy = SingeltonUtil.getSingy();
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


    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Map<String,String> item = values.get(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.my_product_row, parent, false);
                System.out.println("first only");
            }
            TextView productDesc = convertView.findViewById(R.id.productDesc);
            TextView textView = convertView.findViewById(R.id.productInfo);
            textView.setText(item.get("ProductTitle"));
            productDesc.setText(item.get("ProductDesc"));
            ImageView imageView = convertView.findViewById(R.id.productImg);
            String url = item.get("ProductImage");
        System.out.println(url);
            imageView.setImageBitmap( MyProductList.flyweightImgs.get(url));
            Button button = convertView.findViewById(R.id.removeItemBtn);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                //Todo fix remove item from sql data.
                public void onClick(View view) {
                   String insert = " DELETE FROM products WHERE ProductId =(?)";
                    db.execSQL(insert, new String[]{item.get("ProductId")});
                    System.out.println(item.get("ProductId"));
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
}
