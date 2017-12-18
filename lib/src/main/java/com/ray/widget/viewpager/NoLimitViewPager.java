package com.ray.widget.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * @author zyl
 */
public class NoLimitViewPager extends ViewPager {

    private PagerAdapterWrapper mAdapter;

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = new PagerAdapterWrapper(adapter);
        super.setAdapter(mAdapter);
        super.setCurrentItem(PagerAdapterWrapper.INIT_POS, false);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : null;
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.getRealPosition(super.getCurrentItem()) : 0;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        boolean hasCircleChanged = mAdapter.reCalculateCircle(item, super.getCurrentItem());
        int innerPos = mAdapter.getInnerPosition(item);
        int posOffset = innerPos - super.getCurrentItem();
        int offscreenLimit = getOffscreenPageLimit();
        if (hasCircleChanged && Math.abs(posOffset) <= offscreenLimit) {
            int p1 = innerPos - Math.abs(offscreenLimit);
            int p2 = innerPos + Math.abs(offscreenLimit);
            for (; p1 <= p2; p1++) {
                mAdapter.dirtyPosition.add(p1);
            }
            mAdapter.notifyDataSetChanged();
            super.setCurrentItem(innerPos, smoothScroll);
        } else {
            super.setCurrentItem(innerPos, smoothScroll);
        }
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        super.setOnPageChangeListener(new OnPagerChangeListenerWrap(listener));
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(new OnPagerChangeListenerWrap(listener));
    }

    public NoLimitViewPager(Context context) {
        super(context);
        init();
    }

    public NoLimitViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOffscreenPageLimit(PagerAdapterWrapper.CACHE_SLIDE);
        super.addOnPageChangeListener(new OnPagerChangeListenerWrap(null));
    }

    private class OnPagerChangeListenerWrap implements OnPageChangeListener {
        OnPageChangeListener mListener;
        private int mSelectRealPosition = -1;
        private int mSelectPosition = -1;


        OnPagerChangeListenerWrap(OnPageChangeListener listener) {
            this.mListener = listener;
        }

        @Override
        public void onPageSelected(int position) {
            int realPosition = mAdapter.getRealPosition(position);
            mSelectPosition = position;
            if (mSelectRealPosition != realPosition) {
                mSelectRealPosition = realPosition;
                if (mListener != null) {
                    mListener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.getRealPosition(position);
            }
            if (mListener != null) {
                mListener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE && mAdapter != null) {
                if (mSelectPosition <= PagerAdapterWrapper.CACHE_SLIDE - 1) {
                    mAdapter.circleCount--;
                    int off = PagerAdapterWrapper.CACHE_SLIDE - mSelectPosition;
                    NoLimitViewPager.super.setCurrentItem(mAdapter.getCount() - (PagerAdapterWrapper.CACHE_SLIDE + off), false);
                } else if (mSelectPosition >= mAdapter.getCount() - PagerAdapterWrapper.CACHE_SLIDE) {
                    mAdapter.circleCount++;
                    int off = mSelectPosition - (mAdapter.getCount() - PagerAdapterWrapper.CACHE_SLIDE);
                    NoLimitViewPager.super.setCurrentItem(PagerAdapterWrapper.CACHE_SLIDE + off, false);
                }
            }
            if (mListener != null) {
                mListener.onPageScrollStateChanged(state);
            }
        }
    }
}
