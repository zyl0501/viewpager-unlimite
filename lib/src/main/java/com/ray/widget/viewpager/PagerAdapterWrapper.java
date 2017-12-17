package com.ray.widget.viewpager;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class PagerAdapterWrapper extends PagerAdapter {

    static final int CACHE_SLIDE = 2;
    static final int INIT_POS = 5;
    private static final int PAGE_COUNT = 11;

    private PagerAdapter mAdapter;
    private Map<Object, Integer> positionCache;
    int circleCount = 0;
    Set<Integer> dirtyPosition;

    PagerAdapterWrapper(PagerAdapter adapter) {
        this.mAdapter = adapter;
        dirtyPosition = new HashSet<>();
        positionCache = new HashMap<>(PAGE_COUNT);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    PagerAdapter getRealAdapter() {
        return mAdapter;
    }

    int getRealPosition(int position) {
        return circleCount * (PAGE_COUNT - CACHE_SLIDE * 2) + position - INIT_POS;
    }

    int getInnerPosition(int position) {
        return position - (circleCount * (PAGE_COUNT - CACHE_SLIDE * 2) - INIT_POS);
    }

    /**
     * 重新计算 circle
     *
     * @param position      想要设置的 position
     * @param innerPosition 内部的 position
     * @return 是否改变了 circle，true 表示 circle已经变化，否则返回 false
     */
    boolean reCalculateCircle(int position, int innerPosition) {
        int newCircleCount = (position - innerPosition + INIT_POS) / (PAGE_COUNT - CACHE_SLIDE * 2);
        boolean hasChanged = circleCount != newCircleCount;
        circleCount = newCircleCount;
        return hasChanged;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = getRealPosition(position);
        Object obj = mAdapter.instantiateItem(container, realPosition);
        positionCache.put(obj, position);
        return obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = getRealPosition(position);
        positionCache.remove(object);
        mAdapter.destroyItem(container, realPosition, object);
    }

    /*
     * Delegate rest of methods directly to the inner adapter.
     */

    @Override
    public void finishUpdate(ViewGroup container) {
        dirtyPosition.clear();
        mAdapter.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return mAdapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        mAdapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return mAdapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        mAdapter.startUpdate(container);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mAdapter.setPrimaryItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mAdapter.getPageTitle(position);
    }

    @Override
    public float getPageWidth(int position) {
        return mAdapter.getPageWidth(position);
    }

    @Override
    public int getItemPosition(Object object) {
        int position = positionCache.get(object);
        return dirtyPosition.contains(position) ? POSITION_NONE : mAdapter.getItemPosition(object);
    }
}