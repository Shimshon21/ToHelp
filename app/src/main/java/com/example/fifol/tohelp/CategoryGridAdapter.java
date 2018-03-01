package com.example.fifol.tohelp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by shim-polak on 2/18/2018.
 */

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.RecyclerHolder>  {
    Context context;
    JSONArray jsonArrTxt;
    int level = 0;

    //Get list and context.
    public CategoryGridAdapter(Context context, JSONArray txt) {
        this.context = context;
        this.jsonArrTxt = txt;
    }

    //On view creation.
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutRoot;
        layoutRoot = LayoutInflater.from(context).inflate(R.layout.categoty_cell, parent, false);
        final RecyclerHolder recyclerHolder = new RecyclerHolder(layoutRoot);
        return recyclerHolder;
    }



    //Config views attributes.
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        try {
            holder.textView.setText(jsonArrTxt.getString(position));
            holder.layout.setTag(jsonArrTxt.getString(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //How many time to show.
    @Override
    public int getItemCount() {
        return jsonArrTxt.length();
    }


    class RecyclerHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        TextView textView;

        public RecyclerHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.txt);
            this.layout = itemView.findViewById(R.id.imageLayout);
        }
    }

    public void swap(JSONArray datas,int level) {
        if(datas == null || datas.length()==0)
            return;
        if (jsonArrTxt != null && jsonArrTxt.length()>0) {
            jsonArrTxt = datas;
            this.level = level;
        }
        notifyDataSetChanged();
    }
}
