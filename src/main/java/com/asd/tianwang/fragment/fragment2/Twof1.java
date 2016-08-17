package com.asd.tianwang.fragment.fragment2;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.asd.tianwang.MessageEvent;
import com.asd.tianwang.R;
import com.asd.tianwang.dao.Digital;
import com.asd.tianwang.dao.YhisDao;
import com.asd.tianwang.dao.table.Tbyhis;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASD on 2016/8/4.
 */
public class Twof1 extends Fragment {
    private ListView lv;
    private LvAdapter lvAdapter,lvAdapter2;
    private List<Tbyhis> listData = new ArrayList<>();
    boolean isnow;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0012){
                lvAdapter.notifyDataSetChanged();
            }
            if(msg.what==0011){
                lvAdapter2.notifyDataSetChanged();
            }
        }
        //接收并显示信息在接收窗口
    };
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(MessageEvent event) {

        if(event.istime&&!event.time.equals("")) {
            isnow=false;
            YhisDao yhisDao = new YhisDao(getActivity());
            listData = yhisDao.find(event.time);
            lvAdapter2 = new LvAdapter(listData, getActivity());
            lv.setAdapter(lvAdapter2);
            handler.sendEmptyMessage(0011);

        }
        if(event.isnow){
            isnow=true;
            lv.setAdapter(lvAdapter);
            lvAdapter.notifyDataSetChanged();
        }
    }
    @Subscribe
    public  void Event1(Tbyhis tby){
        /*YhisDao yhisDao = new YhisDao(getActivity());
        lvAdapter.data=yhisDao.find(getDate());*/
        lvAdapter.data.add(tby);
       if(isnow){ handler.sendEmptyMessage(0012);}
        Log.i("Event1","4445");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.twof1, null, false);
        init(view);
        return view;
    }

    private void init(View view) {
        lv = (ListView) view.findViewById(R.id.lvdata);
        isnow=true;
        getdata(Digital.getDate());
        lvAdapter2 = new LvAdapter(listData, getActivity());
        lv.setAdapter(lvAdapter);
        Log.i("init","11111111111");
    }

    private void getdata(String s) {
        YhisDao yhisDao = new YhisDao(getActivity());
        listData = yhisDao.find(s);
        lvAdapter = new LvAdapter(listData, getActivity());
    }



}
