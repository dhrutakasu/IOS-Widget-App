package com.ios.widget.Appui.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ios.widget.R;

import androidx.viewpager.widget.PagerAdapter;

public class WalkAdapter extends PagerAdapter {

    private Activity activity;
    private Integer[] imagesArray;

    public WalkAdapter(Activity activity, Integer[] imagesArray) {
        this.activity = activity;
        this.imagesArray = imagesArray;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = activity.getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.item_pager_list, container, false);
        ImageView imageView = viewItem.findViewById(R.id.IvSlider);
        imageView.setImageResource(imagesArray[position]);
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