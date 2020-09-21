package com.iot.user.ui.view.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.LinkedHashMap;

/*****
 * 解决ViewPager与NestedScrollView的冲突，不显示高度等
 *不同的子fragment高度不同需要动态加载高度
 * 1.子fragment传入viewPager
 * 2.子fragment中viewPager.setObjectForPosition(view,0);
 * 3。刷新高度viewPager.resetHeight(selectTablayout);
 * **/
public class UnitHomeViewPager extends ViewPager {

    private int current;
    private int height = 0;
    /**
     * 保存position与对于的View
     */
    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<Integer, View>();

    private boolean scrollble = true;

    public UnitHomeViewPager(Context context) {
        super(context);
    }

    public UnitHomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mChildrenViews.size() > current) {
            View child = mChildrenViews.get(current);
            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            height = child.getMeasuredHeight();
        }

        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int current) { this.current = current; if (mChildrenViews.size() > current) { ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) getLayoutParams(); if (layoutParams == null) { layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height); } else { layoutParams.height = height; } setLayoutParams(layoutParams); } }
    /**
     * 保存position与对应的View
     */
    public void setObjectForPosition(View view, int position)
    {
        mChildrenViews.put(position, view);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }


    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

}

