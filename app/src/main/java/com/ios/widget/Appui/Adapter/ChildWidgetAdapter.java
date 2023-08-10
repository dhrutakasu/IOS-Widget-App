package com.ios.widget.Appui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ios.widget.R;
import com.ios.widget.Appui.Activity.WidgetItemActivity;
import com.ios.widget.AppUtils.MyAppConstants;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChildWidgetAdapter extends RecyclerView.Adapter<ChildWidgetAdapter.MyViewHolder> {
    private final Context con;
    private final ArrayList<Integer> mList;
    private final int positions;

    public ChildWidgetAdapter(Context context, ArrayList<Integer> modelArrayList, int pos) {
        con = context;
        mList = modelArrayList;
        positions = pos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(con).inflate(R.layout.item_child_widget, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.IvChildWidget.setImageResource(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                con.startActivity(new Intent(con, WidgetItemActivity.class).putExtra(MyAppConstants.ITEM_POSITION,positions));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView IvChildWidget;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            IvChildWidget = (ImageView) itemView.findViewById(R.id.IvChildWidget);
        }
    }
}
