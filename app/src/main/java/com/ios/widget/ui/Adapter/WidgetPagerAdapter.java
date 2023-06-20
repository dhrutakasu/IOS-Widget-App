package com.ios.widget.ui.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ios.widget.R;

import androidx.viewpager.widget.PagerAdapter;

public class WidgetPagerAdapter extends PagerAdapter {

    private Activity activity;
    private Integer[] imagesArray;

    public WidgetPagerAdapter(Activity activity, Integer[] imagesArray) {
        this.activity = activity;
        this.imagesArray = imagesArray;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.item_pager_list, container, false);
        ImageView IvSlider = viewItem.findViewById(R.id.IvSlider);

        IvSlider.setImageResource(imagesArray[position]);
        container.addView(viewItem);

        return viewItem;
    }

    @Override
    public int getCount() {
        return imagesArray.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
