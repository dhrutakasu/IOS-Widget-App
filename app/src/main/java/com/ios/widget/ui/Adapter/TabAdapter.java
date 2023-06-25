package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ios.widget.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<Integer> arrayList;
    private final setClickCategory clickCategory;

    public TabAdapter(Context context, ArrayList<Integer> typesArrayList,setClickCategory clickCategory) {
        this.context = context;
        this.arrayList = typesArrayList;
        this.clickCategory = clickCategory;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.IvSlider.setImageResource(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView IvSlider;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            IvSlider = (ImageView) itemView.findViewById(R.id.IvSlider);
        }
    }

    public interface setClickCategory {
        void onClickCategory(int pos);
    }
}
