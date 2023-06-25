package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.ui.Activity.TabActivity;
import com.ios.widget.ui.Activity.WidgetItemActivity;
import com.ios.widget.utils.Constants;

import java.util.ArrayList;

public class TypeImageAdapter extends RecyclerView.Adapter<TypeImageAdapter.MyViewHolder> {
    private final Context con;
    private final ArrayList<Integer> mList;

    public TypeImageAdapter(Context context, ArrayList<Integer> modelArrayList) {
        con = context;
        mList = modelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(con).inflate(R.layout.item_pager_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.IvSlider.setImageResource(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(con, WidgetItemActivity.class);
                intent.putExtra(Constants.TabPos, position);
                con.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView IvSlider;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            IvSlider = (ImageView) itemView.findViewById(R.id.IvSlider);
        }
    }
}
