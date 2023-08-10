package com.ios.widget.Appui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ios.widget.AppAdv.MyAppAd_Interstitial;
import com.ios.widget.ImageModel.AppWidgetModel;
import com.ios.widget.R;
import com.ios.widget.Appui.Activity.ShowItemActivity;
import com.ios.widget.Appui.Activity.SplashActivity;
import com.ios.widget.Appui.Activity.WidgetItemActivity;
import com.ios.widget.AppUtils.MyAppConstants;
import com.ios.widget.AppUtils.MyAppPref;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<AppWidgetModel> appWidgetModelArrayList;
    private final int pos;
    private final ShowItemActivity showItemActivity;

    public ItemAdapter(Context context, ArrayList<AppWidgetModel> modelArrayList, int tabPos, ShowItemActivity showItemActivity) {
        this.context = context;
        this.pos = tabPos;
        this.appWidgetModelArrayList = modelArrayList;
        this.showItemActivity = showItemActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_widget, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.IvMediumWidget.setImageResource(appWidgetModelArrayList.get(position).getMedium());
        holder.IvSmallWidget.setImageResource(appWidgetModelArrayList.get(position).getSmall());
        holder.IvSmallWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                int itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(showItemActivity, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
                            Intent intent = new Intent(context, WidgetItemActivity.class);
                            intent.putExtra(MyAppConstants.ITEM_POSITION, 0);
                            intent.putExtra(MyAppConstants.WIDGET_ITEM_POSITION, position);
                            intent.putExtra(MyAppConstants.TabPos, pos);
                            context.startActivity(intent);
                        }
                    });
                } else {
                    Intent intent = new Intent(context, WidgetItemActivity.class);
                    intent.putExtra(MyAppConstants.ITEM_POSITION, 0);
                    intent.putExtra(MyAppConstants.WIDGET_ITEM_POSITION, position);
                    intent.putExtra(MyAppConstants.TabPos, pos);
                    context.startActivity(intent);
                }
            }
        });
        holder.IvMediumWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countExtra = new MyAppPref(context).getInt(MyAppPref.APP_AD_COUNTER, 0);
                int itemClick = SplashActivity.click++;
                if (MyAppConstants.isConnectingToInternet(context) && itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(showItemActivity, new MyAppAd_Interstitial.MyAppCallback() {
                        @Override
                        public void AppCallback() {
                            Intent intent = new Intent(context, WidgetItemActivity.class);
                            intent.putExtra(MyAppConstants.ITEM_POSITION, 1);
                            intent.putExtra(MyAppConstants.WIDGET_ITEM_POSITION, position);
                            intent.putExtra(MyAppConstants.TabPos, pos);
                            context.startActivity(intent);
                        }
                    });
                } else {
                    Intent intent = new Intent(context, WidgetItemActivity.class);
                    intent.putExtra(MyAppConstants.ITEM_POSITION, 1);
                    intent.putExtra(MyAppConstants.WIDGET_ITEM_POSITION, position);
                    intent.putExtra(MyAppConstants.TabPos, pos);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appWidgetModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView IvMediumWidget, IvSmallWidget;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            IvMediumWidget = (ImageView) itemView.findViewById(R.id.IvMediumWidget);
            IvSmallWidget = (ImageView) itemView.findViewById(R.id.IvSmallWidget);
        }
    }
}
