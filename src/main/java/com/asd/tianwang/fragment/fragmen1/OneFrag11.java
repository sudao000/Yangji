package com.asd.tianwang.fragment.fragmen1;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.Digital;
import com.asd.tianwang.dao.table.Tbyhis;
import com.asd.tianwang.depend.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Sudao on 2016/8/15.
 */
public class OneFrag11 extends BaseFragment {
    private TextView tv_state, tv_con, tv_pre, tv_level;
    private Button bt_start, bt_stop;
    private MyHandler handler;
    private Tbyhis yhis;
    int i = 0;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0011) {
                if (Digital.sbang == 0) {
                    tv_state.setText("停机");
                    tv_state.setTextColor(Color.RED);
                } else {
                    tv_state.setText("运行中");
                    tv_state.setTextColor(Color.GREEN);
                }
                if(yhis!=null){
                tv_con.setText(yhis.con + "");
                tv_pre.setText(yhis.pre + "");
                tv_level.setText(yhis.level + "");}
            }

        }
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.onef11, null, false);
        init(view);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Digital.stsa = 2;
                Digital.sbang =1;
                handler.sendEmptyMessage(0011);

            }
        });
        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Digital.stsa = 1;
                Digital.sbang =0;
                handler.sendEmptyMessage(0011);
            }
        });
        return view;
    }

    private void init(View view) {
        tv_state = (TextView) view.findViewById(R.id.tv_state);
        tv_con = (TextView) view.findViewById(R.id.tv_con);
        tv_pre = (TextView) view.findViewById(R.id.tv_pre);
        tv_level = (TextView) view.findViewById(R.id.tv_level);
        bt_start = (Button) view.findViewById(R.id.bt_start);
        bt_stop = (Button) view.findViewById(R.id.bt_stop);
        handler = new MyHandler();
    }

    @Subscribe
    public void onEvent(Tbyhis tbyhis) {
        yhis = tbyhis;
        handler.sendEmptyMessage(0011);
    }
}
