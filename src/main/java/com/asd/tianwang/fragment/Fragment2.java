package com.asd.tianwang.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asd.tianwang.R;
import com.asd.tianwang.depend.BaseFragment;
import com.asd.tianwang.depend.TabAdapter;
import com.asd.tianwang.fragment.fragment2.TwoFrag1;
import com.asd.tianwang.fragment.fragment2.TwoFrag2;
import com.asd.tianwang.fragment.fragment2.TwoFrag3;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ASD on 2016/7/20.
 */
public class Fragment2 extends BaseFragment {
    private TabLayout tabtitle;
    private ViewPager vpcontent;
    private TabAdapter fAdapter;
    private List<Fragment> list_fs;
    private List<String> list_title;
    private TwoFrag1 f1;
    private TwoFrag2 f2;
    private TwoFrag3 f3;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fram2, null, false);
        initControls(view);
        return view;
    }

    public void initControls(View view) {
        tabtitle = (TabLayout) view.findViewById(R.id.twotitle);
        vpcontent = (ViewPager) view.findViewById(R.id.twovp);
        f1 = new TwoFrag1();
        f2 = new TwoFrag2();
        f3 = new TwoFrag3();
        list_fs = new ArrayList<>();
        list_fs.add(f1);
        list_fs.add(f2);
        list_fs.add(f3);
        list_title = new ArrayList<>();
        list_title.add("压力");
        list_title.add("流量");
        list_title.add("备用");
        //设置TabLayout模式
        tabtitle.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        fAdapter = new TabAdapter(getChildFragmentManager(), list_fs);
        fAdapter.setTabTitle(list_title);
        vpcontent.setAdapter(fAdapter);//为viewpager添加适配器
        tabtitle.setupWithViewPager(vpcontent);//Tablayout关联viewpager
    }

}
