package com.ios.widget.ui.Adapter;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ios.widget.R;
import com.ios.widget.provider.BetteryBroadcastReceiver;
import com.ios.widget.ui.Activity.PhotoWidgetActivity;
import com.ios.widget.ui.Activity.ShowItemActivity;
import com.ios.widget.ui.Activity.WidgetItemActivity;
import com.ios.widget.utils.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

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
