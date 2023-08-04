package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ios.widget.R;

import java.util.ArrayList;

public class TypeImageAdapter extends RecyclerView.Adapter<TypeImageAdapter.MyViewHolder> {
    private final Context con;
    private final ArrayList<Integer> mList;
    private final setClickListener listner;

    public TypeImageAdapter(Context context, ArrayList<Integer> modelArrayList, setClickListener clickListener) {
        con = context;
        mList = modelArrayList;
        listner = clickListener;
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
                listner.ClickListener(position);
            }
        });
    }

    public interface setClickListener {
        void ClickListener(int position);
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
