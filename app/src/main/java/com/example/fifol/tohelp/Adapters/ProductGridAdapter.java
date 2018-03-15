package com.example.fifol.tohelp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fifol.tohelp.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by shim-polak on 2/18/2018.
 */
//Not used !!!!!!!!!!!!
public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.RecyclerHolder>  {
    Context context;
    JSONArray dataArr;
    String company;


    //Get list and context.
    public ProductGridAdapter(Context context, JSONArray txt) {
        this.context = context;
        this.dataArr = txt;
    }

    //On view creation.
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutRoot;
        layoutRoot = LayoutInflater.from(context).inflate(R.layout.product_image, parent, false);
        final RecyclerHolder recyclerHolder = new RecyclerHolder(layoutRoot);
        return recyclerHolder;
    }



    //Config views attributes.
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        try {
            holder.layout.setTag(dataArr.getJSONObject(position).getString("מספר קטלוגי"));
            holder.textViewType.setText(dataArr.getJSONObject(position).getString("סוג"));
            if(dataArr.getJSONObject(position).getString("שלב") == null)
                holder.textViewAge.setText(dataArr.getJSONObject(position).getString("גיל"));
            else
                holder.textViewAge.setText("שלב: "+dataArr.getJSONObject(position).getString("שלב"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //How many time to show.
    @Override
    public int getItemCount() {
        return dataArr.length();
    }


    class RecyclerHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView textViewType,textViewAge;

        public RecyclerHolder(View itemView) {
            super(itemView);
            this.textViewType = itemView.findViewById(R.id.typeTxt);
            this.textViewAge = itemView.findViewById(R.id.ageOrLevelTxt);
            this.layout = itemView.findViewById(R.id.imageLayout);
        }
    }
    public void swap(JSONArray data){
        if(data == null || data.length()==0)
            return;
        else{
            this.dataArr = data;
        }
        notifyDataSetChanged();
    }
}
