package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<WidgetModel> arrayList;
    private final setClickCategory clickCategory;

    public CategoryAdapter(Context context, ArrayList<WidgetModel> typesArrayList, setClickCategory clickCategory) {
        this.context = context;
        this.arrayList = typesArrayList;
        this.clickCategory = clickCategory;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sub_category_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.IvSmallWidget.setImageResource(arrayList.get(position).getSmall());
        holder.IvMediumWidget.setImageResource(arrayList.get(position).getMedium());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView IvSmallWidget,IvMediumWidget;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            IvSmallWidget = (ImageView) itemView.findViewById(R.id.IvSmallWidget);
            IvMediumWidget = (ImageView) itemView.findViewById(R.id.IvMediumWidget);
        }
    }

    public interface setClickCategory {
        void onClickCategory(int pos);
    }
}
