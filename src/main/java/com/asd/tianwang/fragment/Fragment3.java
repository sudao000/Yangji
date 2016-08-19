package com.asd.tianwang.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.Digital;
import com.asd.tianwang.dao.WarnDao;
import com.asd.tianwang.dao.table.Tbwarn;
import com.asd.tianwang.depend.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by ASD on 2016/7/20.
 */
public class Fragment3 extends BaseFragment {
  private RelativeLayout rl;
    private int mYear, mMonth, mDay;
    boolean isnow;
    private Button bt_now;
    private ListView lv;
    private WarnDao warnDao;
    private WarnAdapter warnAdapter1,warnAdapter2;
    private List<Tbwarn> data1,data2;
    private EditText et_time;
    private int hiscount,nowcount;
    private String[] hs,ns;
    private MyEditText myte;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0011) {
                isnow=false;
                data1= warnDao.find(et_time.getText().toString());
                warnAdapter1 = new WarnAdapter(data1,getActivity());
                lv.setAdapter(warnAdapter1);
                 warnAdapter1.notifyDataSetChanged();
                Log.i("0011","dsdsadsad");
            }
            if (msg.what == 0012) {

                 warnAdapter2.notifyDataSetChanged();
            }
        }
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
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onef3, null, false);
        init(view);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rl.requestFocus();
                return false;
            }
        });
        bt_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isnow = true;
                lv.setAdapter(warnAdapter2);
                warnAdapter2.notifyDataSetChanged();
            }
        });
        et_time.setOnClickListener(new View.OnClickListener() {// 为时间文本框设置单击监听事件
            @Override
            public void onClick(View arg0) {
                DatePickerFragment dateDialog = new DatePickerFragment();
                dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
                    @Override
                    public void acceptTime(int a, int b, int c) {
                        mYear = a;
                        mMonth = b;
                        mDay = c;
                        et_time.setText(new StringBuilder().append(mYear).append("-")
                                .append(mMonth).append("-").append(mDay));
                        handler.sendEmptyMessage(0011);
                    }
                });
                dateDialog.show(getFragmentManager(), "dateDialog");
            }
        });
        myte.setOnInputCompleteListener(new MyEditText.OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                Toast.makeText(getActivity(), "编辑完成！", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void init(View view) {
        isnow = true;
        warnDao = new WarnDao(getActivity());
        bt_now = (Button) view.findViewById(R.id.bt_now1);
        lv = (ListView) view.findViewById(R.id.lv_warn);
        et_time = (EditText) view.findViewById(R.id.et_time1);
        rl=(RelativeLayout)view.findViewById(R.id.rl001);
        data2=warnDao.find(Digital.getDate());
        warnAdapter2=new WarnAdapter(data2,getActivity());
        myte=(MyEditText)view.findViewById(R.id.et_myet);
        lv.setAdapter(warnAdapter2);

    }

    @Subscribe
    public void onEvent(Tbwarn tbwarn) {
         warnAdapter2.data.add(tbwarn);
        if(isnow){handler.sendEmptyMessage(0012);}

    }

}