package com.example.fifol.tohelp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.DocumentConflictException;
import com.example.fifol.tohelp.DonatorActivity.MyProductList;
import com.example.fifol.tohelp.R;
import com.example.fifol.tohelp.Utils.CloudentKeys;
import com.example.fifol.tohelp.Utils.MyProdutsData;
import com.example.fifol.tohelp.Utils.SingletonUtil;

import java.util.List;

/**
 * Created by fifol on 01/03/2018.
 */

    public class MyWarhouseListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private List<MyProdutsData> values;
         CloudantClient client = CloudentKeys.getClientBuilder();
        String ALL_PRODUCTS_DB = "products";

        public MyWarhouseListAdapter(Context context, List<MyProdutsData> values) {
            super(context, R.layout.my_product_row);
            this.context = context;
            this.values = values;
            System.out.println(values);
        }

    @Override
    public int getCount() {
      return  values.size();
    }


    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MyProdutsData item = values.get(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.warhouse_row, parent, false);
            TextView productDesc = convertView.findViewById(R.id.productItem);
            TextView textView = convertView.findViewById(R.id.productNumber);
            EditText itemCount = convertView.findViewById(R.id.productCount);
            productDesc.setText(item._id);
            textView.setText(item.desc +"   "+item.title);
            itemCount.setText(item.count+"");
            itemCount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @SuppressLint("StaticFieldLeak")
                @Override
                //Update Database wwhen change Count.
                public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                    System.out.println(charSequence);
                    if(!charSequence.toString().equals(""))
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            Database db = client.database(ALL_PRODUCTS_DB,false);
                            try {
                            item.count = Integer.parseInt(charSequence.toString());
                                db.update(item);
                            }catch (DocumentConflictException e){
                               e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            return convertView;
        }
       public void refreshData(List<MyProdutsData> myProdutsData){
            this.values = myProdutsData;
            notifyDataSetChanged();
        }
}
