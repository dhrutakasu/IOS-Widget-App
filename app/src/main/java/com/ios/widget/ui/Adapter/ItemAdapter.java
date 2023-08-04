package com.ios.widget.ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;
import com.ios.widget.ui.Activity.SettingActivity;
import com.ios.widget.ui.Activity.ShowItemActivity;
import com.ios.widget.ui.Activity.SplashActivity;
import com.ios.widget.ui.Activity.WidgetItemActivity;
import com.ios.widget.utils.MyAppConstants;
import com.ios.widget.utils.MyAppPref;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<WidgetModel> widgetModelArrayList;
    private final int pos;
    private final ShowItemActivity showItemActivity;

    public ItemAdapter(Context context, ArrayList<WidgetModel> modelArrayList, int tabPos, ShowItemActivity showItemActivity) {
        this.context = context;
        this.pos = tabPos;
        this.widgetModelArrayList = modelArrayList;
        this.showItemActivity = showItemActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_widget, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.IvMediumWidget.setImageResource(widgetModelArrayList.get(position).getMedium());
        holder.IvSmallWidget.setImageResource(widgetModelArrayList.get(position).getSmall());
        holder.IvSmallWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                int itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(showItemActivity, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
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
                int countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                int itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(showItemActivity, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
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
        return widgetModelArrayList.size();
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
