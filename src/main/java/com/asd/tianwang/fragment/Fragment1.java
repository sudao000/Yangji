package com.asd.tianwang.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asd.tianwang.R;
import com.asd.tianwang.depend.BaseFragment;
import com.asd.tianwang.depend.TabAdapter;
import com.asd.tianwang.fragment.fragmen1.OneFrag1;
import com.asd.tianwang.fragment.fragmen1.OneFrag2;
import com.asd.tianwang.fragment.fragmen1.OneFrag3;
import com.asd.tianwang.fragment.fragmen1.OneFrag4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASD on 2016/7/20.
 */
public class Fragment1 extends BaseFragment{
    private TabLayout tabtitle;
    private ViewPager vpcontent;
    private TabAdapter fAdapter;
    private List<Fragment> list_fs;
    private List<String> list_title;
    private OneFrag1 f1;private OneFrag2 f2;private OneFrag3 f3;private OneFrag4 f4;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fram1, container, false);
        initControls(view);
        return view;
    }
    //初始化控件
    public void initControls(View view){
        tabtitle=(TabLayout)view.findViewById(R.id.tabtitle);
        vpcontent=(ViewPager)view.findViewById(R.id.vp_content);
        f1=new OneFrag1();f2=new OneFrag2();f3=new OneFrag3();f4=new OneFrag4();
        list_fs=new ArrayList<>();
        list_fs.add(f1); list_fs.add(f2); list_fs.add(f3); list_fs.add(f4);
        list_title=new ArrayList<>();
        list_title.add("运行状况");list_title.add("输入输出");
        list_title.add("报警信息");list_title.add("备用");
        //设置TabLayout模式
        tabtitle.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        fAdapter=new TabAdapter(getChildFragmentManager(),list_fs);
        fAdapter.setTabTitle(list_title);
        vpcontent.setAdapter(fAdapter);//为viewpager添加适配器
        tabtitle.setupWithViewPager(vpcontent);//Tablayout关联viewpager
    }
}
