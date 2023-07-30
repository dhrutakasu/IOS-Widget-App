package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ios.widget.Files.ImageFile;
import com.ios.widget.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.ios.widget.utils.Constants.mSelectedList;

public class ImagePickAdapter extends BaseAdapter<ImageFile, ImagePickAdapter.ImagePickViewHolder> {
    public ImagePickAdapter(Context ctx) {
        this(ctx, new ArrayList<ImageFile>());
    }


    public ImagePickAdapter(Context ctx, ArrayList<ImageFile> list) {
        super(ctx, list);
    }


    @Override
    public ImagePickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_image_pick, parent, false);
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if (params != null) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            params.height = width / 4 - 10;
        }
        return new ImagePickViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImagePickViewHolder holder, int position) {
        CharSequence charSequence;
        int i;
        ImageFile file;
        file = mList.get(position);
        if (file.imageCount == 0) {
            charSequence = "";
            i = 0;
        } else {
            charSequence = String.format("%02d", new Object[]{Integer.valueOf(file.imageCount)});
            i = mContext.getResources().getColor(R.color.orange);
        }
        for (ImageFile imageFile : mSelectedList) {
            if (imageFile.equals(file)) {
               file.setSelected(true);
            }
                int index = mList.indexOf(imageFile);
                if (index != -1) {
            }
        }
        if (mList.get(position).isSelected()) {
            holder.mShadow.setVisibility(View.VISIBLE);
            holder.iv_thumbnailChecked.setImageResource(R.drawable.ic_checked);
        } else {
            holder.mShadow.setVisibility(View.INVISIBLE);
            holder.iv_thumbnailChecked.setImageResource(R.drawable.ic_checked_not);
        }
        RequestOptions options = new RequestOptions();
        Glide.with(mContext)
                .load(file.getPath())
                .error(R.drawable.placeholder_img)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.placeholder_img)
                .apply(options.centerCrop())
                .transition(withCrossFade())
                .into(holder.mIvThumbnail);

        holder.mIvThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = holder.getAdapterPosition();
                if (mSelectedList.size() != 10) {
                    if (holder.mShadow.getVisibility() == View.VISIBLE) {
                        holder.mShadow.setVisibility(View.INVISIBLE);
                        mList.get(position).setSelected(false);

                        holder.iv_thumbnailChecked.setImageResource(R.drawable.ic_checked_not);
                    } else {
                        holder.mShadow.setVisibility(View.VISIBLE);
                        mList.get(position).setSelected(true);
                        holder.iv_thumbnailChecked.setImageResource(R.drawable.ic_checked);
                    }
                }
                if (mListener != null) {
                    mListener.OnSelectStateChanged(true, mList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ImagePickViewHolder extends RecyclerView.ViewHolder {
        //        private final TextView tv_folder_title;
        private ImageView mIvThumbnail;
        private ImageView iv_thumbnailChecked;
        private View mShadow;

        public ImagePickViewHolder(View itemView) {
            super(itemView);
            mIvThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            mShadow = itemView.findViewById(R.id.shadow);
            iv_thumbnailChecked = itemView.findViewById(R.id.iv_thumbnailChecked);
//            tv_folder_title = itemView.findViewById(R.id.tv_folder_title);
        }
    }

}
