package com.asd.tianwang.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.asd.tianwang.MessageEvent;
import com.asd.tianwang.R;
import com.asd.tianwang.depend.BaseFragment;
import com.asd.tianwang.depend.StaticViewPager;
import com.asd.tianwang.depend.TabAdapter;
import com.asd.tianwang.fragment.fragment2.Twof1;
import com.asd.tianwang.fragment.fragment2.Twof2;
import com.asd.tianwang.fragment.fragment2.Twof3;
import com.asd.tianwang.fragment.fragment2.Twof4;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudao on 2016/8/4.
 */
public class Frag2 extends BaseFragment implements View.OnClickListener{
    private RelativeLayout rl;
    private int mYear, mMonth, mDay;
    private EditText et_limit,et_time;
    private Button bt_limit,bt_time,bt_now;
    private TabLayout tabtitle;
    private StaticViewPager vpcontent;
    private TabAdapter fAdapter;
    private List<Fragment> list_fs;
    private List<String> list_title;
    private Twof1 f1;private Twof2 f2;private Twof3 f3;private Twof4 f4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f2, container, false);
        initControls(view);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rl.requestFocus();
                return false;
            }
        });
        return view;
    }

    public void initControls(View view){
        rl=(RelativeLayout)view.findViewById(R.id.rl000);
        et_limit=(EditText)view.findViewById(R.id.et_limit0);
        et_time=(EditText)view.findViewById(R.id.et_time0);
        bt_limit=(Button)view.findViewById(R.id.bt_limit0);
        bt_time=(Button)view.findViewById(R.id.bt_time0);
        bt_now=(Button)view.findViewById(R.id.bt_now2);
        tabtitle=(TabLayout)view.findViewById(R.id.cv_title);
        vpcontent=(StaticViewPager)view.findViewById(R.id.vp_data);
        f1=new Twof1();f2=new Twof2();f3=new Twof3();f4=new Twof4();
        list_fs=new ArrayList<>();
        list_fs.add(f1); list_fs.add(f2); list_fs.add(f3); list_fs.add(f4);
        list_title=new ArrayList<>();
        list_title.add("总表");list_title.add("电导率");
        list_title.add("压力");list_title.add("液位");
        //设置TabLayout模式
        tabtitle.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        fAdapter=new TabAdapter(getChildFragmentManager(),list_fs);
        fAdapter.setTabTitle(list_title);
        vpcontent.setAdapter(fAdapter);//为viewpager添加适配器
        tabtitle.setupWithViewPager(vpcontent);//Tablayout关联viewpager
        bt_limit.setOnClickListener(this);
        bt_time.setOnClickListener(this);
        bt_now.setOnClickListener(this);
        et_time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_limit0:
               limit();
                break;
            case R.id.bt_time0:
                time();
                break;
            case R.id.bt_now2:
                now();
                break;
            case R.id.et_time0:
                ettime();
                break;
        }
    }
    private void limit(){
        MessageEvent me=new MessageEvent();
        me.limit=et_limit.getText().toString();
        me.islimit=true;
        me.istime=false;
        me.isnow=false;
        EventBus.getDefault().post(me);
    }
    private void time(){
        MessageEvent me=new MessageEvent();
        me.time=et_time.getText().toString();
        me.islimit=false;
        me.istime=true;
        me.isnow=false;
        EventBus.getDefault().post(me);
    }
    private void now(){
        MessageEvent me=new MessageEvent();
        me.islimit=false;
        me.istime=false;
        me.isnow=true;
        EventBus.getDefault().post(me);
    }
    private void ettime(){
        DatePickerFragment dateDialog = new DatePickerFragment();
        dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
            @Override
            public void acceptTime(int a, int b, int c) {
                mYear = a;
                mMonth = b;
                mDay = c;
                et_time.setText(new StringBuilder().append(mYear).append("-")
                        .append(mMonth).append("-").append(mDay));
                //Log.i("found:",mYear+"-"+mMonth+"-"+mDay);
            }
        });
        dateDialog.show(getFragmentManager(), "dateDialog");
    }
}
