package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ios.widget.Model.WidgetData;
import com.ios.widget.R;
import com.ios.widget.helper.DatabaseHelper;
import com.ios.widget.utils.MyAppConstants;

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
        if (MyAppConstants.WidgetRemove) {
            holder.IvWidgetRemove.setVisibility(View.VISIBLE);
        } else {
            holder.IvWidgetRemove.setVisibility(View.GONE);
        }
        if (myWidgetLists.get(position).getType() == 0) {
            for (int i = 0; i < MyAppConstants.getWidgetLists().size(); i++) {
                if (MyAppConstants.getWidgetLists().get(i).getPosition() == myWidgetLists.get(position).getPosition()) {
                    holder.IvMyWidget.setImageResource(MyAppConstants.getWidgetLists().get(i).getSmall());
                    if (myWidgetLists.get(position).getPosition() == 23) {
                        holder.IvPhotoWidget.setImageURI(Uri.parse(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(helper.getImageList(myWidgetLists.get(position).getNumber()).get(helper.getImageList(myWidgetLists.get(position).getNumber()).size()-1).getUri())).toString()));
                    }else {
                        holder.IvPhotoWidget.setVisibility(View.GONE);
                    }
                }
            }
        } else if (myWidgetLists.get(position).getType() == 1) {
            for (int i = 0; i < MyAppConstants.getWidgetLists().size(); i++) {
                if (MyAppConstants.getWidgetLists().get(i).getPosition() == myWidgetLists.get(position).getPosition()) {
                    System.out.println("_____  _ _ CONSSS : " + MyAppConstants.getWidgetLists().get(i).getPosition() + " ..... : " + myWidgetLists.get(position).getPosition());
                    holder.IvMyWidget.setImageResource(MyAppConstants.getWidgetLists().get(i).getMedium());
                    if (myWidgetLists.get(position).getPosition() == 23) {
                        holder.IvPhotoWidget.setImageURI(Uri.parse(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(helper.getImageList(myWidgetLists.get(position).getNumber()).get(helper.getImageList(myWidgetLists.get(position).getNumber()).size()-1).getUri())).toString()));
                    }else {
                        holder.IvPhotoWidget.setVisibility(View.GONE);
                    }
                }
            }
        } else if (myWidgetLists.get(position).getType() == 2) {
            for (int i = 0; i < MyAppConstants.getWidgetLists().size(); i++) {
                if (MyAppConstants.getWidgetLists().get(i).getPosition() == myWidgetLists.get(position).getPosition()) {
                    holder.IvMyWidget.setImageResource(MyAppConstants.getWidgetLists().get(i).getLarge());
                    if (myWidgetLists.get(position).getPosition() == 23) {
                        holder.IvPhotoWidget.setImageURI(Uri.parse(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(helper.getImageList(myWidgetLists.get(position).getNumber()).get(helper.getImageList(myWidgetLists.get(position).getNumber()).size()-1).getUri())).toString()));
                    }else {
                        holder.IvPhotoWidget.setVisibility(View.GONE);
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
