package com.asd.tianwang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.HistoryDao;
import com.asd.tianwang.dao.ResourceDao;
import com.asd.tianwang.dao.WarnDao;
import com.asd.tianwang.dao.table.Tbhistory;
import com.asd.tianwang.dao.table.Tbresource;
import com.asd.tianwang.depend.BaseFragment;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by ASD on 2016/7/20.
 */
public class Fragment4 extends BaseFragment{
    private Button bt_warn,bt_his,bt_makehis;
    private RelativeLayout rl;
    private int mYear, mMonth, mDay;
    private EditText et_warn,et_his,et_makehis;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fram4, null, false);
        init(view);
        bt_warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WarnDao warnDao=new WarnDao(getActivity());
                warnDao.remove(et_warn.getText().toString());
                Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
            }
        });
        bt_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryDao his=new HistoryDao(getActivity());
                his.remove(et_his.getText().toString());
                Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
            }
        });
        bt_makehis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResourceDao resourceDao = new ResourceDao(getActivity());
                HistoryDao historyDao = new HistoryDao(getActivity());
                Random ra = new Random();
                Calendar c = Calendar.getInstance();
                String mins = String.valueOf(c.get(Calendar.MINUTE));
                String hours=String .valueOf(ra.nextInt(23));

                int m = 0;
                while (m < 10) {
                    String sec = String.valueOf(m*5);
                    String a = hours + ":" + mins + ":" + sec;
                    Tbresource tbr = resourceDao.find(ra.nextInt(35));
                    Tbhistory tbh = new Tbhistory(historyDao.getCount(), tbr.getInp(), tbr.getOutp(), tbr.getOpsp(),
                            tbr.getInf(), tbr.getOutf(), tbr.getBackf(),
                            tbr.getOrp(), a, et_makehis.getText().toString());
                    historyDao.add(tbh);
                    // Log.i("id,time", historyDao.getCount() + "," + a[0]);
                    m++;
                }
                Toast.makeText(getActivity(), "新增成功！", Toast.LENGTH_SHORT).show();
            }
        });
        et_warn.setOnClickListener(new View.OnClickListener() {// 为时间文本框设置单击监听事件
            @Override
            public void onClick(View arg0) {
                DatePickerFragment dateDialog = new DatePickerFragment();
                dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
                    @Override
                    public void acceptTime(int a, int b, int c) {
                        mYear = a;
                        mMonth = b;
                        mDay = c;
                        et_warn.setText(new StringBuilder().append(mYear).append("-")
                                .append(mMonth).append("-").append(mDay));
                        //Log.i("found:",mYear+"-"+mMonth+"-"+mDay);
                    }
                });
                dateDialog.show(getFragmentManager(), "dateDialog");
            }
        });

        et_his.setOnClickListener(new View.OnClickListener() {// 为时间文本框设置单击监听事件
            @Override
            public void onClick(View arg0) {
                DatePickerFragment dateDialog = new DatePickerFragment();
                dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
                    @Override
                    public void acceptTime(int a, int b, int c) {
                        mYear = a;
                        mMonth = b;
                        mDay = c;
                        et_his.setText(new StringBuilder().append(mYear).append("-")
                                .append(mMonth).append("-").append(mDay));
                        //Log.i("found:",mYear+"-"+mMonth+"-"+mDay);
                    }
                });
                dateDialog.show(getFragmentManager(), "dateDialog");
            }
        });
        et_makehis.setOnClickListener(new View.OnClickListener() {// 为时间文本框设置单击监听事件
            @Override
            public void onClick(View arg0) {
                DatePickerFragment dateDialog = new DatePickerFragment();
                dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
                    @Override
                    public void acceptTime(int a, int b, int c) {
                        mYear = a;
                        mMonth = b;
                        mDay = c;
                        et_makehis.setText(new StringBuilder().append(mYear).append("-")
                                .append(mMonth).append("-").append(mDay));
                        //Log.i("found:",mYear+"-"+mMonth+"-"+mDay);
                    }
                });
                dateDialog.show(getFragmentManager(), "dateDialog");
            }
        });
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rl.requestFocus();
                return false;
            }
        });
        return view;
    }
    private void init(View view){
        rl=(RelativeLayout)view.findViewById(R.id.rl_100);
        bt_warn=(Button)view.findViewById(R.id.bt_warn);
        bt_his=(Button)view.findViewById(R.id.bt_his);
        bt_makehis=(Button)view.findViewById(R.id.bt_makehis);
        et_makehis=(EditText) view.findViewById(R.id.et_makehis);
        et_warn=(EditText)view.findViewById(R.id.et_warn);
        et_his=(EditText)view.findViewById(R.id.et_his);
    }


}
