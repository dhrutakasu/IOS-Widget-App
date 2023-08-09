package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.ios.widget.Model.WidgetImages;
import com.ios.widget.R;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {
    private final Context context;
    private final List<WidgetImages> photoLists;
    private final setPhotoWidget widget;

    public PhotoAdapter(Context context, List<WidgetImages> photoLists, setPhotoWidget widget) {
        this.context = context;
        this.photoLists = photoLists;
        this.widget = widget;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (position != 0) {
            if (photoLists.get(position).getUri().contains("content://")) {
                ((RequestBuilder) Glide.with(context)
                        .load(Uri.parse(String.valueOf(photoLists.get(position).getUri())))
                        .centerCrop())
                        .into(holder.IvPhoto);
            } else if (photoLists.get(position).getUri().contains(context.getPackageName())) {
                File file = new File(photoLists.get(position).getUri());
                ((RequestBuilder) Glide.with(context)
                        .load(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file))
                        .centerCrop())
                        .into(holder.IvPhoto);
            } else {
                File file = new File(photoLists.get(position).getUri());
                ((RequestBuilder) Glide.with(context)
                        .load(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file))
                        .centerCrop())
                        .into(holder.IvPhoto);
            }
            holder.IvPhotoUpload.setVisibility(View.GONE);
        } else {
            holder.IvPhotoUpload.setImageResource(R.drawable.ic_upload);
            holder.IvPhotoRemove.setVisibility(View.GONE);
            holder.LlPhoto.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                widget.setPhotoWidget(position);
            }
        });
        holder.IvPhotoRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                widget.RemovePhotoWidget((position - 1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoLists.size();
    }

    public interface setPhotoWidget {
        void setPhotoWidget(int pos);

        void RemovePhotoWidget(int pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView IvPhoto, IvPhotoUpload, IvPhotoRemove;
        private final LinearLayout LlPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            IvPhoto = (ImageView) itemView.findViewById(R.id.IvPhoto);
            LlPhoto = (LinearLayout) itemView.findViewById(R.id.LlPhoto);
            IvPhotoUpload = (ImageView) itemView.findViewById(R.id.IvPhotoUpload);
            IvPhotoRemove = (ImageView) itemView.findViewById(R.id.IvPhotoRemove);
        }
    }
}
