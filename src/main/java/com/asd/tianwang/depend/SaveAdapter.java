package com.asd.tianwang.depend;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Sudao on 2016/8/17.
 */
public class SaveAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener{
    private List<Fragment> fragments;//每个Fragment对应一个page
    private FragmentManager fragmentManager;
    private ViewPager viewPager;
    private int currentPagerIndex=0;//当前page的索引（切换之前）
    private OnExtraPageChangeListener onExtraPagerChangeListener;
    //viewpager切换页面时的额外功能接口

    public SaveAdapter(FragmentManager fragmentManager, ViewPager viewPager, List<Fragment> fragments) {
        this.fragmentManager = fragmentManager;
        this.viewPager = viewPager;
        this.fragments = fragments;
        this.viewPager.setAdapter(this);
        this.viewPager.setOnPageChangeListener(this);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView(fragments.get(position).getView());//移除viewpager两边之外的布局
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment=fragments.get(position);
        if(!fragment.isAdded()){
            //如果fragment还没有added
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.add(fragment,fragment.getClass().getSimpleName());
            ft.commit();
            /*
            *在用FragmentTransaction.commit();方法提交FragmentTransaction对象后，会在进程的主线程中
            * 用异步的方式执行。如果要立即执行这个等待操作，就要调用这个方法（只能在主线程中调用）。
            * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的
            * 调用位置。
            * */
            fragmentManager.executePendingTransactions();
            if(fragment.getView().getParent()==null){
                container.addView(fragment.getView());//为viewpager增加布局。
            }
        }
        return fragment.getView();
    }
    /*
    * 当前page索引
    * @return
    * */
    public int getCurrentPagerIndex(){
        return currentPagerIndex;
    }

    public void setOnExtraPagerChangeListener(OnExtraPageChangeListener onExtraPagerChangeListener) {
        this.onExtraPagerChangeListener = onExtraPagerChangeListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(null!=onExtraPagerChangeListener){
            onExtraPagerChangeListener.onExtraPagerScrolled(position,positionOffset,positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        fragments.get(currentPagerIndex).onPause();
        //调用切换前的fragment的onpause();
        if(fragments.get(position).isAdded()){
            fragments.get(position).onResume();
            //调用切换后的Fragment的onResume;
        }
        currentPagerIndex=position;
        if(null!=onExtraPagerChangeListener){
            onExtraPagerChangeListener.onExtraPagerSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(null!=onExtraPagerChangeListener){
            onExtraPagerChangeListener.onExtraPagerScrolStateChanged(state);
        }
    }

//切换页面额外功能的接口
    static class OnExtraPageChangeListener{
        public void onExtraPagerScrolled(int i,float v,int j){}
        public  void  onExtraPagerSelected(int i){}
        public void onExtraPagerScrolStateChanged(int i){}
    }
}
