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
import android.widget.Toast;

import com.asd.tianwang.MessageEvent;
import com.asd.tianwang.R;
import com.asd.tianwang.dao.Digital;
import com.asd.tianwang.dao.YhisDao;
import com.asd.tianwang.dao.table.Tbyhis;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by ASD on 2016/8/4.
 */
public class Twof4 extends Fragment {
    private BarChart mbarChart;
    private BarData mbardada, mbarData2;
    private ArrayList<String> xVals, xVals2;
    private BarDataSet dataSet1, dataSet2;
    private ArrayList<BarEntry> lineY1, lineY2;
    private YAxis leftAxis;
    private YhisDao yhisDao;
    private MyTask task = null;
    private MessageEvent mevent;
    private boolean isnow;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001 && isnow) {
                //实时图表
                mbarChart.setData(mbarData2);
                mbarChart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
                mbarChart.moveViewToX(mbarData2.getXValCount() - 4);//设置折线移动
            }
            if (msg.what == 1002) {
                sure(mevent.time);
                mbarChart.setData(mbardada);
                mbarChart.fitScreen();
                /*
                * 使用setVisibleXRangeMaximum之前调用fitscreen重设所有拖动和缩放，
                * 不然setVisibleXRangeMaximum
                * 执行有误，显示个数混乱，不按设定值来，有时很多，有时很少。
                * */
                mbarChart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
                mbarChart.moveViewToX(mbarData2.getXValCount() - 4);//设置折线移动
            }
            if (msg.what == 1003) {
                leftAxis.removeAllLimitLines();
                float lim = Float.parseFloat(mevent.limit);
                LimitLine yLimitLine = new LimitLine(lim, "液位上限:" + lim);
                // yLimitLine.
                yLimitLine.setLineColor(Color.RED);
                yLimitLine.setTextColor(Color.RED);
                yLimitLine.setTextSize(8f);
                leftAxis.addLimitLine(yLimitLine);
                Log.i("f4", "1003");
            }
            mbarChart.notifyDataSetChanged();
            mbarChart.invalidate();
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
        task.cancel();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.twof4, null, false);
        init(view);
        initline();
        return view;
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        mevent = event;

        if (getUserVisibleHint()) {
            if (mevent.islimit && !mevent.limit.equals("")) {
                handler.sendEmptyMessage(1003);
                Log.i("f4", "limit");
                Toast.makeText(getActivity(), "限制线修改成功！", Toast.LENGTH_SHORT).show();

            }
            if (mevent.istime && !mevent.time.equals("")) {
                isnow = false;
                handler.sendEmptyMessage(1002);
            }
            if (mevent.isnow) {
                isnow = true;
                handler.sendEmptyMessage(1001);
            }
        }
    }

    @Subscribe
    public void Event2(Tbyhis tby) {
        if (isnow) {
            lineY2.clear();
            xVals2.clear();
            List<Tbyhis> tbh = yhisDao.find("2016-8-17");
            if (tbh.size() != 0) {
                for (int i = 0; i < tbh.size(); i++) {
                    lineY2.add(new BarEntry(tbh.get(i).level, xVals2.size()));
                    xVals2.add(tbh.get(i).mtime);
                }
            }
            handler.sendEmptyMessage(1001);
        }
    }

    private void init(View view) {
        mbarChart = (BarChart) view.findViewById(R.id.bc_level);
        yhisDao = new YhisDao(getActivity());
        task = new MyTask();
        isnow = true;
    }

    private void initline() {
        xVals = new ArrayList<String>();
        xVals2 = new ArrayList<String>();
        lineY1 = new ArrayList<BarEntry>();
        lineY2 = new ArrayList<BarEntry>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSet1 = new BarDataSet(lineY1, "液位");
        dataSet1.setColor(Color.parseColor("#058E79"));//设置折线颜色
        dataSet1.setValueTextSize(8);//设置显示该点值文字大小。
        dataSet2 = new BarDataSet(lineY2, "液位");
        dataSet2.setColor(Color.parseColor("#00C0BF"));
        dataSet2.setValueTextSize(8);
        mbardada = new BarData(xVals, dataSet1);
        mbarData2 = new BarData(xVals2, dataSet2);

        mbarChart.setDescription("液位：cm");
        mbarChart.setDescriptionPosition(230f, 40f);
        mbarChart.setDescriptionTextSize(12);

        mbarChart.animateY(500);// 动画
        mbarChart.setTouchEnabled(true);// 设置是否可以触摸
        mbarChart.setDragEnabled(true);// 是否可以拖拽
        mbarChart.setScaleEnabled(false);// 是否可以缩放
        mbarChart.setPinchZoom(false);// 集双指缩放
        mbarChart.getAxisRight().setEnabled(false);// 隐藏右边的坐标轴
        // mBarchart.getAxisLeft().setEnabled(false);// 隐藏左边的左边轴
        Legend mLegend = mbarChart.getLegend();
        mLegend.setForm(Legend.LegendForm.SQUARE);// 设置比例图标示// 设置窗体样式
        // mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);设置比例图位置
        mLegend.setFormSize(4f);// 字体
        mLegend.setTextColor(Color.parseColor("#000000"));// 字体颜色
        XAxis xAxis = mbarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签位于图标下边。
        xAxis.setDrawGridLines(false);//不画网格线
        xAxis.setSpaceBetweenLabels(2);
        xAxis.setAxisLineColor(Color.BLACK);
        //xAxis.setLabelsToSkip(0);//忽略的标签数
        leftAxis = mbarChart.getAxisLeft();// 获得左侧侧坐标轴
        leftAxis.setAxisMaxValue(25f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisLineColor(Color.BLACK);

        LimitLine yLimitLine = new LimitLine(5f, "液位下限：5");
        yLimitLine.setLineColor(Color.RED);
        yLimitLine.setTextColor(Color.RED);
        yLimitLine.setTextSize(8f);
        leftAxis.addLimitLine(yLimitLine);
        // Digital.timer.scheduleAtFixedRate(task, 3000, 3000);
        getdata(Digital.getDate());
        mbarChart.setData(mbarData2);
        mbarChart.setViewPortOffsets(60, 50, 80, 110);
        mbarChart.setBackgroundColor(Color.parseColor("#BE4C7BA4"));
        mbarChart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
        mbarChart.moveViewToX(mbardada.getXValCount() - 1);//设置折线移动

    }

    private void sure(String s) {

        lineY1.clear();
        xVals.clear();
        List<Tbyhis> tbh = yhisDao.find(s);
        if (tbh.size() != 0) {
            for (int i = 0; i < tbh.size(); i++) {
                lineY1.add(new BarEntry(tbh.get(i).level, xVals.size()));
                xVals.add(tbh.get(i).mtime);
            }
        }
    }

    private void getdata(String s) {

        lineY2.clear();
        xVals2.clear();
        List<Tbyhis> tbh = yhisDao.find(s);
        if (tbh.size() != 0) {
            for (int i = 0; i < tbh.size(); i++) {
                lineY2.add(new BarEntry(tbh.get(i).level, xVals2.size()));
                xVals2.add(tbh.get(i).mtime);
            }
        }
    }

    private class MyTask extends TimerTask {

        @Override
        public void run() {
            if (isnow) {
               /* Random r = new Random();
                Tbyhis thy = yhisDao.find(r.nextInt(14));
                lineY2.add(new BarEntry(thy.level, xVals2.size()));
                xVals2.add(thy.mtime);*/

                lineY2.clear();
                xVals2.clear();
                List<Tbyhis> tbh = yhisDao.find("2016-8-17");
                if (tbh.size() != 0) {
                    for (int i = 0; i < tbh.size(); i++) {
                        lineY2.add(new BarEntry(tbh.get(i).level, xVals2.size()));
                        xVals2.add(tbh.get(i).mtime);
                    }
                }
                handler.sendEmptyMessage(1001);
                Log.i("run3", "运行");
            }
        }
    }
}
