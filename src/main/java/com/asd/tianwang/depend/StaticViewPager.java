package com.asd.tianwang.depend;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ASD on 2016/6/15.
 */
public class StaticViewPager extends ViewPager {
    public StaticViewPager(Context context){super(context);}
    public StaticViewPager(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){return false;}
    @Override
    public boolean onTouchEvent(MotionEvent ev){
        return false;
    }
   /* @Override
    public void setCurrentItem(int item){
        this.setCurrentItem(item);
    }*/
}
