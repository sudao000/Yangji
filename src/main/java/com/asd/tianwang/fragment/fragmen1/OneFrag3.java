package com.asd.tianwang.fragment.fragmen1;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.Digital;
import com.asd.tianwang.dao.WarnDao;
import com.asd.tianwang.dao.table.Tbwarn;
import com.asd.tianwang.fragment.DatePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ASD on 2016/7/20.
 */
public class OneFrag3 extends Fragment {
    Parcelable state;
    private int mYear, mMonth, mDay;
    boolean isnow;
    private Button bt_now, bt_sure;
    private ListView lv;
    private WarnDao warnDao;
    private ArrayAdapter<String> arrayAdapter;
    private EditText et_time;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1200) {

                lv.setAdapter(arrayAdapter);
                lv.setOnScrollListener(mScrolllitener);
                lv.onRestoreInstanceState(state);//记录滑动位置，防止每次重设adapter引起的lv回到顶部。
                arrayAdapter.notifyDataSetChanged();
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onef3, null, false);
        init(view);
        bt_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isnow = true;
                getData(getDate());
                Message msg = handler.obtainMessage(0x1200);
                handler.sendMessage(msg);
            }
        });
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isnow = false;
                getData(et_time.getText().toString());
                Message msg = handler.obtainMessage(0x1200);
                handler.sendMessage(msg);
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
                    }
                });
                dateDialog.show(getFragmentManager(), "dateDialog");
            }
        });
        //new Thread(new InThread()).start();
        return view;
    }

    public void init(View view) {
        isnow = true;
        warnDao = new WarnDao(getActivity());
        bt_now = (Button) view.findViewById(R.id.bt_now1);
        lv = (ListView) view.findViewById(R.id.lv_warn);
        et_time = (EditText) view.findViewById(R.id.et_time1);
        state=lv.onSaveInstanceState();


    }

    public class InThread implements Runnable {

        @Override
        public void run() {

            while (true) {

                if (Digital.an == 0&&Digital.isrun) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isnow) {
                        getData(getDate());
                        Message msg = handler.obtainMessage(0x1200);
                        handler.sendMessage(msg);
                    }
                   // Log.i("0000","执行run");
                }
            }
        }
    }

    public String getDate() {
        String a;
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        a = year + "-" + month + "-" + day;
        return a;
    }

    public void getData(String s) {
        List<Tbwarn> listw = new ArrayList<>();
        listw = warnDao.find(s);
        String[] strInfos = new String[listw.size()];
        for (int i = 0; i < strInfos.length; i++) {
            if (listw.get(i).type == 0) {
                strInfos[i] = i + "  " + "进水压力过高" + "   " + listw.get(i).mtime +
                        "   " + listw.get(i).mdate;
            } else {
                strInfos[i] = i + "  " + "产水流量过低" + "   " + listw.get(i).mtime +
                        "   " + listw.get(i).mdate;
            }
        }
        arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, strInfos);
    }
    public AbsListView.OnScrollListener mScrolllitener=new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            state=lv.onSaveInstanceState();
            switch (scrollState){
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    break;
                default:
                    break;
            }
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };
}
