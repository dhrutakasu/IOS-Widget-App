package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ios.widget.R;
import com.ios.widget.ui.Activity.TabActivity;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {
    private final ArrayList<String> arrayList;
    private final Context context;

    public DataAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pager_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyViewHolder holder, int position) {
//holder.TvSlider.setText(arrayList.get(position).toString());
        holder.IvSlider.setImageResource(R.drawable.btn_trendy);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //        private final TextView TvSlider;
        private final ImageView IvSlider;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            TvSlider=(TextView) itemView.findViewById(R.id.TvSlider);
            IvSlider = (ImageView) itemView.findViewById(R.id.IvSlider);
        }
    }
}
