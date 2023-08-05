package com.ios.widget.ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.material.tabs.TabLayout;
import com.ios.widget.Ads.MyAppAd_Banner;
import com.ios.widget.Ads.MyAppAd_Interstitial;
import com.ios.widget.R;
import com.ios.widget.ui.Adapter.WalkAdapter;
import com.ios.widget.utils.MyAppPref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class WalkthroughActivity extends AppCompatActivity {

    private ViewPager PagerWalk;
    private TextView TvWalkTitle, TvWalkSubTitle;
    private Context context;

    private Integer[] images = {R.drawable.ic_bg_1, R.drawable.ic_bg_2, R.drawable.ic_bg_3};
    private TabLayout TabWalk;
    private ImageView IvStart,IvNext;
    private LinearLayout LlNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.GRAY);
        }
        setContentView(R.layout.activity_walkthrough);

        initViews();
        initListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        PagerWalk = (ViewPager) findViewById(R.id.PagerWalk);
        TvWalkTitle = (TextView) findViewById(R.id.TvWalkTitle);
        TvWalkSubTitle = (TextView) findViewById(R.id.TvWalkSubTitle);
        IvStart = (ImageView) findViewById(R.id.IvStart);
        IvNext = (ImageView) findViewById(R.id.IvNext);
        LlNext = (LinearLayout) findViewById(R.id.LlNext);
        TabWalk = (TabLayout) findViewById(R.id.TabWalk);
    }

    private void initListeners() {
        IvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
                int itemClick = SplashActivity.click++;
                if (itemClick % countExtra == 0) {
                    MyAppAd_Interstitial.getInstance().showInter(WalkthroughActivity.this, new MyAppAd_Interstitial.MyCallback() {
                        @Override
                        public void callbackCall() {
                            if (PagerWalk.getCurrentItem() == 0) {
                                PagerWalk.setCurrentItem(1);
                                LlNext.setVisibility(View.VISIBLE);
                                IvStart.setVisibility(View.GONE);
                            } else if (PagerWalk.getCurrentItem() == 1) {
                                PagerWalk.setCurrentItem(2);
                                LlNext.setVisibility(View.GONE);
                                IvStart.setVisibility(View.VISIBLE);
                            } else {
                                LlNext.setVisibility(View.GONE);
                                IvStart.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    if (PagerWalk.getCurrentItem() == 0) {
                        PagerWalk.setCurrentItem(1);
                        LlNext.setVisibility(View.VISIBLE);
                        IvStart.setVisibility(View.GONE);
                    } else if (PagerWalk.getCurrentItem() == 1) {
                        PagerWalk.setCurrentItem(2);
                        LlNext.setVisibility(View.GONE);
                        IvStart.setVisibility(View.VISIBLE);
                    } else {
                        LlNext.setVisibility(View.GONE);
                        IvStart.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        IvStart.setOnClickListener(view -> {
            int countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
            int itemClick = SplashActivity.click++;
            if (itemClick % countExtra == 0) {
                MyAppAd_Interstitial.getInstance().showInter(WalkthroughActivity.this, new MyAppAd_Interstitial.MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (PagerWalk.getCurrentItem() == 2) {
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            } else {
                if (PagerWalk.getCurrentItem() == 2) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });
    }

    private void initActions() {
        MyAppAd_Banner.getInstance().showBanner(this, AdSize.LARGE_BANNER, (RelativeLayout) findViewById(R.id.RlBannerAdView), (RelativeLayout) findViewById(R.id.RlBannerAd));

        WalkAdapter adapter = new WalkAdapter(this, images);
        PagerWalk.setAdapter(adapter);
        TvWalkTitle.setText(getResources().getString(R.string.str_walk_title_1));
        TvWalkSubTitle.setText(getResources().getString(R.string.str_walk_sub_title_1));
        TabWalk.setupWithViewPager(PagerWalk, true);

        LlNext.setVisibility(View.VISIBLE);
        IvStart.setVisibility(View.GONE);

        PagerWalk.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                changeText(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void changeText(int i) {
        if (i == 0) {
            TvWalkTitle.setText(getResources().getString(R.string.str_walk_title_1));
            TvWalkSubTitle.setText(getResources().getString(R.string.str_walk_sub_title_1));
            LlNext.setVisibility(View.VISIBLE);
            IvStart.setVisibility(View.GONE);
        } else if (i == 1) {
            TvWalkTitle.setText(getResources().getString(R.string.str_walk_title_2));
            TvWalkSubTitle.setText(getResources().getString(R.string.str_walk_sub_title_2));

            LlNext.setVisibility(View.VISIBLE);
            IvStart.setVisibility(View.GONE);
        } else if (i == 2) {
            TvWalkTitle.setText(getResources().getString(R.string.str_walk_title_3));
            TvWalkSubTitle.setText(getResources().getString(R.string.str_walk_sub_title_3));

            LlNext.setVisibility(View.GONE);
            IvStart.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        int countExtra = new MyAppPref(context).getInt(MyAppPref.AD_COUNTER, 0);
        int itemClick = SplashActivity.click++;
        if (itemClick % countExtra == 0) {
            MyAppAd_Interstitial.getInstance().showInter(WalkthroughActivity.this, new MyAppAd_Interstitial.MyCallback() {
                @Override
                public void callbackCall() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }
}