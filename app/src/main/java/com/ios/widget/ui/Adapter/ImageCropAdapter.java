package com.ios.widget.ui.Adapter;

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
import com.ios.widget.Files.ImageFile;
import com.ios.widget.R;

import java.io.File;
import java.util.ArrayList;

public class ImageCropAdapter extends RecyclerView.Adapter<ImageCropAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<ImageFile> mselect;
    private final CropImage cropImage;

    public ImageCropAdapter(Context context, ArrayList<ImageFile> mSelectedList, CropImage cropImage) {
        this.context = context;
        this.mselect = mSelectedList;
        this.cropImage = cropImage;
    }

    @NonNull
    @Override
    public ImageCropAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_image_crop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageCropAdapter.MyViewHolder holder, int position) {
        ImageFile imageFile = mselect.get(position);
        RequestOptions options = new RequestOptions();
        System.out.println("---- - - path 77: "+imageFile.getPath());
        Bitmap bitmap= BitmapFactory.decodeFile(Uri.fromFile(new File(imageFile.getPath())).getPath());
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
                ImageFile imageFile = mselect.get(position);
                cropImage.setCropImage(imageFile, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mselect.size();
    }

    public interface CropImage {
        void setCropImage(@NonNull ImageFile imageFile, int position);
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
