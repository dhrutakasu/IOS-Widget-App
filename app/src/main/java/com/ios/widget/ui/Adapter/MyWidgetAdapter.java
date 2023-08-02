package com.ios.widget.ui.Adapter;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.provider.LargeWidgetProvider;
import com.ios.widget.provider.MediumPhotoWidgetProvider;
import com.ios.widget.provider.MediumWidgetProvider;
import com.ios.widget.provider.SmallWidgetProvider;
import com.ios.widget.utils.Constants;

import java.io.File;
import java.util.ArrayList;

public class MyWidgetAdapter extends RecyclerView.Adapter<MyWidgetAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<WidgetData> myWidgetLists;
    private final MyWidgetAdapter.deleteWidget deleteWidget;
    private final DatabaseHelper helper;

    public MyWidgetAdapter(Context context, ArrayList<WidgetData> myWidgetLists, deleteWidget widget) {
        this.context = context;
        this.myWidgetLists = myWidgetLists;
        this.deleteWidget = widget;
        helper=new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyWidgetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyWidgetAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_widget, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyWidgetAdapter.MyViewHolder holder, int position) {
        System.out.println("_____  _ _ Adp : " + myWidgetLists.get(position).getType() + " ..... : " + myWidgetLists.get(position).getPosition());
        if (Constants.WidgetRemove) {
            holder.IvWidgetRemove.setVisibility(View.VISIBLE);
        } else {
            holder.IvWidgetRemove.setVisibility(View.GONE);
        }
        if (myWidgetLists.get(position).getType() == 0) {
            for (int i = 0; i < Constants.getWidgetLists().size(); i++) {
                if (Constants.getWidgetLists().get(i).getPosition() == myWidgetLists.get(position).getPosition()) {
                    holder.IvMyWidget.setImageResource(Constants.getWidgetLists().get(i).getSmall());
                    if (myWidgetLists.get(position).getPosition() == 23) {
                        System.out.println("+++++ "+FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(helper.getImageList(myWidgetLists.get(position).getNumber()).get(0).getUri())).toString());
                        Bitmap bitmap= BitmapFactory.decodeFile(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(helper.getImageList(myWidgetLists.get(position).getNumber()).get(0).getUri())).toString());
                        holder.IvPhotoWidget.setImageURI(Uri.parse(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(helper.getImageList(myWidgetLists.get(position).getNumber()).get(0).getUri())).toString()));
                    }
                }
            }
        } else if (myWidgetLists.get(position).getType() == 1) {
            for (int i = 0; i < Constants.getWidgetLists().size(); i++) {
                if (Constants.getWidgetLists().get(i).getPosition() == myWidgetLists.get(position).getPosition()) {
                    System.out.println("_____  _ _ CONSSS : " + Constants.getWidgetLists().get(i).getPosition() + " ..... : " + myWidgetLists.get(position).getPosition());
                    holder.IvMyWidget.setImageResource(Constants.getWidgetLists().get(i).getMedium());
                    if (myWidgetLists.get(position).getPosition() == 23) {
                        Bitmap bitmap= BitmapFactory.decodeFile(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(helper.getImageList(myWidgetLists.get(position).getNumber()).get(0).getUri().toString())).toString());
                        holder.IvPhotoWidget.setImageBitmap(bitmap);
                    }
                }
            }
        } else if (myWidgetLists.get(position).getType() == 2) {
            for (int i = 0; i < Constants.getWidgetLists().size(); i++) {
                if (Constants.getWidgetLists().get(i).getPosition() == myWidgetLists.get(position).getPosition()) {
                    holder.IvMyWidget.setImageResource(Constants.getWidgetLists().get(i).getLarge());
                    if (myWidgetLists.get(position).getPosition() == 23) {
                        Bitmap bitmap= BitmapFactory.decodeFile(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(helper.getImageList(myWidgetLists.get(position).getNumber()).get(0).getUri().toString())).toString());
                        holder.IvPhotoWidget.setImageBitmap(bitmap);
                    }
                }
            }
        }
        holder.IvWidgetRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWidget.DeleteWidget(position);
            }
        });
    }

    public interface deleteWidget {
        void DeleteWidget(int pos);
    }

    @Override
    public int getItemCount() {
        return myWidgetLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView IvMyWidget, IvPhotoWidget, IvWidgetRemove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            IvMyWidget = (ImageView) itemView.findViewById(R.id.IvMyWidget);
            IvPhotoWidget = (ImageView) itemView.findViewById(R.id.IvPhotoWidget);
            IvWidgetRemove = (ImageView) itemView.findViewById(R.id.IvWidgetRemove);
        }
    }
}
