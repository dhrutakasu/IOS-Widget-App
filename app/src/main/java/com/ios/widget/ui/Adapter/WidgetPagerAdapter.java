package com.ios.widget.ui.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ios.widget.Model.WidgetModel;
import com.ios.widget.R;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

public class WidgetPagerAdapter extends PagerAdapter {

    private Activity activity;
    private int pos;
    private ArrayList<WidgetModel> imagesArray;

    public WidgetPagerAdapter(Activity activity, ArrayList<WidgetModel> imagesArray, int i) {
        this.activity = activity;
        this.imagesArray = imagesArray;
        this.pos = i;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.item_pager_list, container, false);
        ImageView IvSlider = viewItem.findViewById(R.id.IvSlider);

        if (pos == 0) {
            IvSlider.setImageResource(imagesArray.get(position).getSmall());
        } else if (pos == 1) {
            IvSlider.setImageResource(imagesArray.get(position).getMedium());
        } else {
            IvSlider.setImageResource(imagesArray.get(position).getLarge());
        }
        container.addView(viewItem);

        return viewItem;
    }

    @Override
    public int getCount() {
        return imagesArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setchange(int position) {
        pos = position;
        notifyDataSetChanged();
    }
}
