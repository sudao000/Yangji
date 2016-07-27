package com.asd.tianwang.fragment.fragment2;

import android.app.Fragment;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.HistoryDao;
import com.asd.tianwang.dao.table.Tbhistory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ASD on 2016/7/20.
 */
public class TwoFrag2 extends Fragment {
    boolean isRefesh = true;
    private int mYear, mMonth, mDay;
    private RelativeLayout rl;
    private BarChart mBarchart;
    private BarData mBardata, mBardata2;
    private ArrayList<String> xVals, xVals2;
    private ArrayList<IBarDataSet> dataSet, dataSet2;
    private ArrayList<BarEntry> yValues1, yValues2, yValues3, yValues21, yValues22, yValues23;
    private YAxis leftAxis;
    private Button bt_sure, bt_now;
    private EditText et_date, et_limit;
    private TextView tx;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==0x0112){update();}
            if (isRefesh) {
                mBarchart.setData(mBardata);
                mBarchart.setVisibleXRangeMaximum(18);//设置X轴最多显示数据7个
                mBarchart.moveViewToX(mBardata.getXValCount() - 5);//设置折线移动
            }
            if(msg.what==0x0113&&!isRefesh){
                mBarchart.setData(mBardata2);
                mBarchart.setVisibleXRangeMaximum(18);//设置X轴最多显示数据7个
                mBarchart.moveViewToX(mBardata2.getXValCount() - 5);
            }
            if (!et_limit.getText().toString().equals("")) {
                leftAxis.removeAllLimitLines();
                float lim = Float.parseFloat(et_limit.getText().toString());
                LimitLine yLimitLine = new LimitLine(lim, "最低产水:" + lim);
                yLimitLine.setLineColor(Color.RED);
                yLimitLine.setTextColor(Color.RED);
                yLimitLine.setTextSize(8f);
                leftAxis.addLimitLine(yLimitLine);
            }//限制线设置
            mBarchart.notifyDataSetChanged();
            mBarchart.invalidate();
            String s = msg.arg1 + "";
            tx.setText(s);
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twof2, null, false);
        init(view);
        initbar();
        et_date.setOnClickListener(new View.OnClickListener() {// 为时间文本框设置单击监听事件
            @Override
            public void onClick(View arg0) {
                DatePickerFragment dateDialog = new DatePickerFragment();
                dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
                    @Override
                    public void acceptTime(int a, int b, int c) {
                        mYear = a;
                        mMonth = b;
                        mDay = c;
                        et_date.setText(new StringBuilder().append(mYear).append("-")
                                .append(mMonth).append("-").append(mDay));
                    }
                });
                dateDialog.show(getFragmentManager(), "dateDialog");
            }
        });
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sure();
            }
        });
        bt_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRefesh = true;
                handler.sendEmptyMessage(0x0112);
            }
        });
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rl.requestFocus();
                return false;
            }
        });
        new Thread(new MyThread()).start();
        return view;
    }

    private void sure() {
        yValues21.clear();
        yValues22.clear();
        yValues23.clear();
        xVals2.clear();//每次都清空一下数据，不然会存储很多。
        HistoryDao historyDao = new HistoryDao(getActivity());
        List<Tbhistory> his = historyDao.find(et_date.getText().toString());
        if (his.size() != 0) {
            isRefesh=false;
            for (int i = 0; i < his.size(); i++) {
                yValues21.add(new BarEntry(his.get(i).getInf(), xVals2.size()));
                yValues22.add(new BarEntry(his.get(i).getOutf(), xVals2.size()));
                yValues23.add(new BarEntry(his.get(i).getBackf(), xVals2.size()));
                xVals2.add(his.get(i).getMtime());
                // Log.i("yValues",yValues21.get(i).toString()+","+xVals2.get(i));
            }
        }
        Message msg = handler.obtainMessage();
        msg.what = 0x0113;
        msg.arg1 = xVals2.size();
        handler.sendMessage(msg);

    }

    private void update(){
        final Calendar c = Calendar.getInstance();// 获取当前系统日期
        mYear = c.get(Calendar.YEAR);// 获取年份
        mMonth = c.get(Calendar.MONTH)+1;// 获取月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        et_date.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth).append("-").append(mDay));
    }
    private void init(View view) {
        mBarchart = (BarChart) view.findViewById(R.id.barchart);
        tx = (TextView) view.findViewById(R.id.tx_01);
        bt_sure = (Button) view.findViewById(R.id.bt_sure2);
        bt_now = (Button) view.findViewById(R.id.bt_now2);
        et_date = (EditText) view.findViewById(R.id.et_date2);
        et_limit = (EditText) view.findViewById(R.id.et_limit2);
        rl = (RelativeLayout) view.findViewById(R.id.rl2);
    }

    private void initbar() {
        dataSet = new ArrayList<IBarDataSet>();
        dataSet2 = new ArrayList<IBarDataSet>();
        xVals = new ArrayList<String>();
        xVals2 = new ArrayList<String>();
        yValues1 = new ArrayList<BarEntry>();
        yValues2 = new ArrayList<BarEntry>();
        yValues3 = new ArrayList<BarEntry>();
        yValues21 = new ArrayList<BarEntry>();
        yValues22 = new ArrayList<BarEntry>();
        yValues23 = new ArrayList<BarEntry>();
        dataSet = getBardatas(yValues1, yValues2, yValues3);
        dataSet2 = getBardatas(yValues21, yValues22, yValues23);
        mBardata = new BarData(xVals, dataSet);
        mBardata2 = new BarData(xVals2, dataSet2);

        mBarchart.setDescription("流量:m³/h");
        mBarchart.setDescriptionPosition(230f, 80f);
        mBarchart.setDescriptionTextSize(12);

        mBarchart.animateY(1000);// 动画
        mBarchart.setTouchEnabled(true);// 设置是否可以触摸
        mBarchart.setDragEnabled(true);// 是否可以拖拽
        mBarchart.setScaleEnabled(false);// 是否可以缩放
        mBarchart.setPinchZoom(false);// 集双指缩放
        mBarchart.getAxisRight().setEnabled(false);// 隐藏右边的坐标轴
        // mBarchart.getAxisLeft().setEnabled(false);// 隐藏左边的左边轴
        Legend mLegend = mBarchart.getLegend();
        // mLegend.setForm(Legend.LegendForm.SQUARE);// 设置比例图标示// 设置窗体样式
        mLegend.setFormSize(4f);// 字体
        mLegend.setTextColor(Color.parseColor("#7e7e7e"));// 字体颜色
        XAxis xAxis = mBarchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签位于图标下边。
        xAxis.setDrawGridLines(false);//不画网格线
        xAxis.setSpaceBetweenLabels(2);

        //xAxis.setLabelsToSkip(0);//忽略的标签数
        leftAxis = mBarchart.getAxisLeft();// 获得左侧侧坐标轴
        leftAxis.setAxisMaxValue(12f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(false);
        LimitLine yLimitLine = new LimitLine(5f, "最低产水:5");
        yLimitLine.setLineColor(Color.BLUE);
        yLimitLine.setTextColor(Color.BLACK);
        yLimitLine.setTextSize(8f);
        leftAxis.addLimitLine(yLimitLine);
        mBarchart.setData(mBardata);
        mBarchart.setBackgroundColor(Color.parseColor("#8FB3B373"));
        mBarchart.invalidate();
    }

    private ArrayList<IBarDataSet> getBardatas(ArrayList<BarEntry> yValues1, ArrayList<BarEntry> yValues2,
                                               ArrayList<BarEntry> yValues3) {
        ArrayList<IBarDataSet> dataSet = new ArrayList<>();
        BarDataSet barDataSet1, barDataSet2, barDataSet3;
        barDataSet1 = new BarDataSet(yValues1, "进水流量");
        barDataSet1.setColor(Color.parseColor("#F26077"));
        barDataSet1.setBarShadowColor(Color.parseColor("#01000000"));

        barDataSet2 = new BarDataSet(yValues2, "产水流量");
        barDataSet2.setColor(Color.parseColor("#00C0BF"));
        barDataSet2.setBarShadowColor(Color.parseColor("#01000000"));

        barDataSet3 = new BarDataSet(yValues3, "回水流量");
        barDataSet3.setColor(Color.parseColor("#DEAD26"));
        barDataSet3.setBarShadowColor(Color.parseColor("#01000000"));
        dataSet.add(barDataSet1);
        dataSet.add(barDataSet2);
        dataSet.add(barDataSet3);
        return dataSet;

    }


    class MyThread implements Runnable {
        @Override
        public void run() {
            int m = 0;
            HistoryDao historyDao = new HistoryDao(getActivity());

            while (m < 10) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tbhistory tbh = historyDao.find(m);
                yValues1.add(new BarEntry(tbh.getInf(), xVals.size()));
                yValues2.add(new BarEntry(tbh.getOutf(), xVals.size()));
                yValues3.add(new BarEntry(tbh.getBackf(), xVals.size()));
                xVals.add(tbh.getMtime());
                Log.i("y", yValues1.get(m).toString()+","+yValues2.get(m).toString()+","+yValues3.get(m).toString());
                Message msg = handler.obtainMessage();
                msg.what = 0x0112;
                msg.arg1 = m;
                handler.sendMessage(msg);
                m = xVals.size();
            }
        }
    }
}
