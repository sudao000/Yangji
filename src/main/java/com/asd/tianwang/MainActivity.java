package com.asd.tianwang;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.WindowManager;

import com.asd.tianwang.dao.Digital;
import com.asd.tianwang.dao.HistoryDao;
import com.asd.tianwang.dao.ResourceDao;
import com.asd.tianwang.dao.SetDao;
import com.asd.tianwang.dao.WarnDao;
import com.asd.tianwang.dao.table.Tbhistory;
import com.asd.tianwang.dao.table.Tbresource;
import com.asd.tianwang.dao.table.Tbset;
import com.asd.tianwang.dao.table.Tbwarn;
import com.asd.tianwang.depend.BaseFragment;
import com.asd.tianwang.depend.IconPagerAdapter;
import com.asd.tianwang.depend.IconTabPageIndicator;
import com.asd.tianwang.depend.StaticViewPager;
import com.asd.tianwang.fragment.Frag2;
import com.asd.tianwang.fragment.Fragment3;
import com.asd.tianwang.fragment.Fragment4;
import com.asd.tianwang.fragment.fragmen1.OneFrag11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    private StaticViewPager mViewPager;
    private IconTabPageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        initdatabase();
        initViews();
        //new Thread(new RunThread()).start();
        //new Thread(new InThread()).start();
    }

    private void initViews() {
        mViewPager = (StaticViewPager) findViewById(R.id.view_pager);
        mIndicator = (IconTabPageIndicator) findViewById(R.id.indicator);
        List<BaseFragment> fragments = initFragments();
        FragmentAdapter adapter = new FragmentAdapter(fragments, getFragmentManager());
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
        Digital.limit1 = 6;
        Digital.limit2 = 5;
    }

    private List<BaseFragment> initFragments() {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();

        OneFrag11 userFragment = new OneFrag11();
        userFragment.setTitle("运行状态");
        userFragment.setIconId(R.drawable.tab_run);
        fragments.add(userFragment);

        Frag2 noteFragment = new Frag2();
        noteFragment.setTitle("数据记录");
        noteFragment.setIconId(R.drawable.tab_data);
        fragments.add(noteFragment);

        Fragment3 contactFragment = new Fragment3();
        contactFragment.setTitle("参数设置");
        contactFragment.setIconId(R.drawable.tab_setting);
        fragments.add(contactFragment);

        Fragment4 recordFragment = new Fragment4();
        recordFragment.setTitle("菜单");
        recordFragment.setIconId(R.drawable.tab_menu);
        fragments.add(recordFragment);

        return fragments;
    }

    class FragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        private List<BaseFragment> mFragments;

        public FragmentAdapter(List<BaseFragment> fragments, FragmentManager fm) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getIconResId(int index) {
            return mFragments.get(index).getIconId();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }
    }

    public void initdatabase() {
        ResourceDao redao = new ResourceDao(MainActivity.this);
        SetDao setDao = new SetDao(MainActivity.this);
        if (redao.getCount() < 2) {
            float inp = 4.0f, outp = 3.1f, opsp = 2.8f, inf = 10.5f, outf = 9.0f, backf = 1.2f;
            int orp = -12;
            List<Tbresource> lire = new ArrayList<Tbresource>();
            for (int i = 0; i < 36; i++) {
                lire.add(new Tbresource(i, inp, outp, opsp, inf, outf, backf, orp));
                redao.add(lire.get(i));
                inp = inp + 0.1f;
                outp = outp + 0.1f;
                opsp = opsp + 0.1f;
                outf = outf - 0.1f;
                backf = backf + 0.1f;
                orp = orp - 3;
            }

        }
        /*Tbset tbset = new Tbset(20, 10, 10, 10);
        setDao.add(tbset);*/
    }


    public class RunThread implements Runnable {
        @Override
        public void run() {
            SetDao sd = new SetDao(MainActivity.this);
            while (true) {
                if (Digital.isrun) {
                    setDo(0);
                    Tbset ts = sd.find();
                    try {
                        Thread.sleep(ts.cycle * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setDo(1);
                    try {
                        Thread.sleep(ts.qx * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setDo(2);
                    try {
                        Thread.sleep(ts.qiy * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setDo(3);
                    try {
                        Thread.sleep(ts.fx * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setDo(0);

                } else {
                    Digital.out = 5;
                }
            }
        }
    }

    public class InThread implements Runnable {

        @Override
        public void run() {
            ResourceDao resourceDao = new ResourceDao(MainActivity.this);
            HistoryDao historyDao = new HistoryDao(MainActivity.this);
            WarnDao warnDao = new WarnDao(MainActivity.this);
            Random r = new Random();
            updatewarn(historyDao, warnDao);
            while (true) {
                if (Digital.out == 0) {
                    String a[] = Digital.getTime();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Digital.in = r.nextInt(8);
                    Digital.an = r.nextInt(35);
                    Tbresource tbr = resourceDao.find(Digital.an);
                    Tbhistory tbh = new Tbhistory(historyDao.getCount(), tbr.getInp(), tbr.getOutp(), tbr.getOpsp(),
                            tbr.getInf(), tbr.getOutf(), tbr.getBackf(), tbr.getOrp(), a[0], a[1]);
                    historyDao.add(tbh);
                    if (Digital.ischange) {
                        updatewarn(historyDao, warnDao);
                    }
                    if (tbr.getInp() > Digital.limit1) {
                        Tbwarn tbwarn = new Tbwarn(warnDao.getCount(), 0, a[0], a[1]);
                        warnDao.add(tbwarn);
                    }
                    if (tbr.getOutf() < Digital.limit2) {
                        Tbwarn tbwarn = new Tbwarn(warnDao.getCount(), 1, a[0], a[1]);
                        warnDao.add(tbwarn);
                    }
                }
            }
        }
    }

    public void setDo(int i) {
        if (Digital.isrun) {
            Digital.out = i;
        } else Digital.out = 5;
    }

    public void updatewarn(HistoryDao h, WarnDao w) {
        w.deleteAll();
        List<Tbhistory> listh = new ArrayList<>();
        listh = h.findAll();
        int length = listh.size();
       /* Log.i("Digital.limit1",Digital.limit1+"");
        Log.i("Digital.limit2",Digital.limit2+"");
        Log.i("Digital.ischange0",Digital.ischange+"");*/
        Digital.ischange = false;
        for (int i = 0; i < length; i++) {
            Tbhistory tbh = listh.get(i);
            if (tbh.getInp() > Digital.limit1) {
                Tbwarn tbwarn = new Tbwarn(w.getCount(), 0, tbh.getMtime(), tbh.getMdate());
                w.add(tbwarn);

            }
            if (tbh.getOutf() < Digital.limit2) {
                Tbwarn tbwarn = new Tbwarn(w.getCount(), 1, tbh.getMtime(), tbh.getMdate());
                w.add(tbwarn);

            }
        }
    }
}
