package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainWidgetListAdapter extends RecyclerView.Adapter<MainWidgetListAdapter.MyViewHolder> {
    private final Context con;
    private final ArrayList<WidgetModel> mList;

    public MainWidgetListAdapter(Context context, ArrayList<WidgetModel> modelArrayList) {
        con = context;
        mList = modelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(con).inflate(R.layout.item_main_widget, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ArrayList<Integer> models = new ArrayList<>();
        models.add(mList.get(position).getSmall());
        models.add(mList.get(position).getMedium());
//        models.add(mList.get(position).getLarge());
        holder.RvChildWidgetList.setLayoutManager(new LinearLayoutManager(con, RecyclerView.HORIZONTAL, false));
        holder.RvChildWidgetList.setAdapter(new ChildWidgetAdapter(con, models,position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView RvChildWidgetList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            RvChildWidgetList = (RecyclerView) itemView.findViewById(R.id.RvChildWidgetList);
        }
    }
}
