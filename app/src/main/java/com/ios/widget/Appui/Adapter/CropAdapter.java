package com.ios.widget.Appui.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ios.widget.AppFiles.ImageFile;
import com.ios.widget.R;

import java.io.File;
import java.util.ArrayList;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.MyViewHolder> {
    private final ArrayList<ImageFile> mSelectedList;
    private final Context context;
    private final SetWidgetRemoveCrop setWidgetRemoveCrop;

    public CropAdapter(Context context, ArrayList<ImageFile> mSelectedList, SetWidgetRemoveCrop setWidgetRemoveCrop) {
        this.context = context;
        this.mSelectedList = mSelectedList;
        this.setWidgetRemoveCrop = setWidgetRemoveCrop;
    }

    @NonNull
    @Override
    public CropAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_image_crop_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CropAdapter.MyViewHolder holder, int position) {
        holder.IvImageCropItem.setImageResource(R.drawable.ic_remove);
        ImageFile imageFile = mSelectedList.get(position);
        RequestOptions options = new RequestOptions();
        Bitmap bitmap = BitmapFactory.decodeFile(Uri.fromFile(new File(imageFile.getPath())).getPath());
        Glide.with(context)
                .load(bitmap)
                .error(R.drawable.ic_widget_placeholder_img)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_widget_placeholder_img)
                .transition(withCrossFade())
                .into(holder.IvImageCrop);

        holder.IvImageCropItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setWidgetRemoveCrop.RemoveCropImage(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSelectedList.size();
    }

    public interface SetWidgetRemoveCrop {
        void RemoveCropImage(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView IvImageCrop, IvImageCropItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            IvImageCrop = itemView.findViewById(R.id.IvImageCrop);
            IvImageCropItem = itemView.findViewById(R.id.IvImageCropItem);
        }
    }
}
