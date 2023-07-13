package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.utils.Constants;

import java.util.ArrayList;

public class MyWidgetAdapter extends RecyclerView.Adapter<MyWidgetAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<WidgetData> myWidgetLists;

    public MyWidgetAdapter(Context context, ArrayList<WidgetData> myWidgetLists) {
        this.context = context;
        this.myWidgetLists = myWidgetLists;
    }

    @NonNull
    @Override
    public MyWidgetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyWidgetAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_widget, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyWidgetAdapter.MyViewHolder holder, int position) {
        if (myWidgetLists.get(position).getType() == 0) {
            holder.IvMyWidget.setImageResource(Constants.getWidgetLists().get(myWidgetLists.get(position).getPosition()).getSmall());
        } else if (myWidgetLists.get(position).getType() == 1) {
            holder.IvMyWidget.setImageResource(Constants.getWidgetLists().get(myWidgetLists.get(position).getPosition()).getMedium());
        } else if (myWidgetLists.get(position).getType() == 2) {
            holder.IvMyWidget.setImageResource(Constants.getWidgetLists().get(myWidgetLists.get(position).getPosition()).getLarge());
        }
    }

    @Override
    public int getItemCount() {
        return myWidgetLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView IvMyWidget;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            IvMyWidget = (ImageView) itemView.findViewById(R.id.IvMyWidget);
        }
    }
}
