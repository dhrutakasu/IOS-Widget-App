package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ios.widget.Files.Directory;
import com.ios.widget.Files.ImageFile;
import com.ios.widget.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class FolderListAdapter extends BaseAdapter<Directory, FolderListAdapter.FolderListViewHolder> {
    private FolderListListener mListener;
    private int selectedPosition = 0;

    public FolderListAdapter(Context ctx, ArrayList<Directory> list) {
        super(ctx, list);
    }

    @Override
    public FolderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_folder_list,
                parent, false);
        return new FolderListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FolderListViewHolder holder, int position) {
        if (position == 0) {
            if (mList.get(1).getFiles().size() > 0) {
                try {
                    final ImageFile file = (ImageFile) mList.get(1).getFiles().get(0);
                    RequestOptions options = new RequestOptions();
                    Glide.with(mContext)
                            .load(file.getPath())
                            .apply(options.centerCrop())
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(holder.iv_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (mList.get(position).getFiles().size() > 0) {
                try {
                    final ImageFile file = (ImageFile) mList.get(position).getFiles().get(0);
                    RequestOptions options = new RequestOptions();
                    Glide.with(mContext)
                            .load(file.getPath())
                            .apply(options.centerCrop())
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(holder.iv_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (selectedPosition == position) {
            holder.mTvTitle.setTextColor(mContext.getResources().getColor(R.color.widget_black));
        } else {
            holder.mTvTitle.setTextColor(mContext.getResources().getColor(R.color.widget_white));
        }

        holder.mTvTitle.setText(mList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = holder.getAdapterPosition();
                if (mListener != null) {
                    mListener.onFolderListClick(mList.get(holder.getAdapterPosition()));
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(FolderListListener listener) {
        this.mListener = listener;
    }

    public interface FolderListListener {
        void onFolderListClick(Directory directory);
    }

    class FolderListViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle;
        private ImageView iv_image;

        public FolderListViewHolder(View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.TvFolderTitle);
            iv_image = itemView.findViewById(R.id.IvImage);
        }
    }
}
