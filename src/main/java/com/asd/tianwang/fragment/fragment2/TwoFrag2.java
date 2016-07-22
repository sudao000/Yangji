package com.asd.tianwang.fragment.fragment2;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by ASD on 2016/7/20.
 */
public class TwoFrag2 extends Fragment {
    private BarChart mBarchart;
    private BarData mBardata;
    private ArrayList<String> xVals;
    private ArrayList<IBarDataSet> dataSet;
    private ArrayList<BarEntry> yValues1, yValues2, yValues3;
    private BarDataSet barDataSet1, barDataSet2, barDataSet3;
    private TextView tx;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x0101) {
                mBarchart.setVisibleXRangeMaximum(18);//设置X轴最多显示数据5个
                mBarchart.moveViewToX(mBardata.getXValCount() - 5);//设置折线移动
                mBarchart.notifyDataSetChanged();
                mBarchart.invalidate();
                String s=msg.arg1+"";
                tx.setText(s);
                Log.i("mbchart", "执行handler");
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twof2, null, false);
        mBarchart = (BarChart) view.findViewById(R.id.barchart);
        tx=(TextView)view.findViewById(R.id.tx_01);
        initbar();
        new Thread(new MyThread()).start();
        return view;
    }


    private void initbar() {
        dataSet = new ArrayList<IBarDataSet>();
        xVals = new ArrayList<String>();
        yValues1 = new ArrayList<BarEntry>();
        yValues2 = new ArrayList<BarEntry>();
        yValues3 = new ArrayList<BarEntry>();

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
        mBardata = new BarData(xVals, dataSet);


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
        YAxis leftAxis = mBarchart.getAxisLeft();// 获得左侧侧坐标轴
        leftAxis.setAxisMaxValue(12f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(false);
        LimitLine yLimitLine = new LimitLine(5f,"最低产水流量:5");
        yLimitLine.setLineColor(Color.BLUE);
        yLimitLine.setTextColor(Color.BLACK);
        yLimitLine.setTextSize(8f);
        leftAxis.addLimitLine(yLimitLine);
        mBarchart.setData(mBardata);
        mBarchart.setBackgroundColor(Color.parseColor("#8FB3B373"));
        mBarchart.invalidate();
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
                Log.i("m", m + "");
                Message msg=handler.obtainMessage();
                msg.what=0x0101;
                msg.arg1=m;
                handler.sendMessage(msg);
                m=xVals.size();
            }
        }
    }
}
