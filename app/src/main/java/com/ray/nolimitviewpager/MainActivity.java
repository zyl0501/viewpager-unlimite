package com.ray.nolimitviewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.widget.viewpager.NoLimitViewPager;

/**
 * @author zyl
 */
public class MainActivity extends AppCompatActivity {

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final NoLimitViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 13;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.d("raytest", "instantiateItem: " + position);
                TextView textView = new TextView(MainActivity.this);
                textView.setText(String.valueOf(position));
                textView.setTextSize(200);
                container.addView(textView);
                return textView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("raytest", "onPageScrolled  position: " + position + ", positionOffset: " + positionOffset + ", positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("raytest", "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("raytest", "onPageScrollStateChanged: " + state);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("raytest", "onPageScrolled2  position: " + position + ", positionOffset: " + positionOffset + ", positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("raytest", "onPageSelected2: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("raytest", "onPageScrollStateChanged2: " + state);
            }
        });
        Runnable r = new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(i++);
                viewPager.postDelayed(this, 1500);
            }
        };
        viewPager.postDelayed(r,1500);
        viewPager.setCurrentItem(-7);
    }
}
