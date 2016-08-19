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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

/**
 * Created by ASD on 2016/8/4.
 */
public class Twof2 extends Fragment {
    private LineChart mLinechart;
    private LineData mLinedata, mLinedata2;
    private ArrayList<String> xVals, xVals2;
    private LineDataSet dataSet1, dataSet2;
    private ArrayList<Entry> lineY1, lineY2;
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
                mLinechart.setData(mLinedata2);
                mLinechart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
                mLinechart.moveViewToX(mLinedata2.getXValCount() - 1);//设置折线移动
            }
            if (msg.what == 1002) {
                sure(mevent.time);
                mLinechart.setData(mLinedata);
                mLinechart.fitScreen();
                mLinechart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
                mLinechart.moveViewToX(mLinedata2.getXValCount() - 1);//设置折线移动
            }
            if (msg.what == 1003) {
                leftAxis.removeAllLimitLines();
                float lim = Float.parseFloat(mevent.limit);
                Digital.limit2=lim;
                LimitLine yLimitLine = new LimitLine(lim, "电导率上限:" + lim);
                // yLimitLine.
                yLimitLine.setLineColor(Color.RED);
                yLimitLine.setTextColor(Color.RED);
                yLimitLine.setTextSize(8f);
                leftAxis.addLimitLine(yLimitLine);
            }
            mLinechart.notifyDataSetChanged();
            mLinechart.invalidate();
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
        task.cancel();
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.twof2, null, false);
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
                Toast.makeText(getActivity(), "限制线修改成功！", Toast.LENGTH_SHORT).show();
            }
            if (mevent.istime && !mevent.time.equals("")) {
                isnow = false;
                handler.sendEmptyMessage(1002);
                Toast.makeText(getActivity(), mevent.time + "数据展示！", Toast.LENGTH_SHORT).show();
            }
            if (mevent.isnow) {
                isnow = true;
                handler.sendEmptyMessage(1001);
                Toast.makeText(getActivity(), "实时数据展示！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Subscribe
    public void Event2(Tbyhis tby) {
        lineY2.add(new Entry(tby.con, xVals2.size()));
        xVals2.add(tby.mtime);
        if (isnow) {
            handler.sendEmptyMessage(1001);
        }
    }

    private void init(View view) {
        mLinechart = (LineChart) view.findViewById(R.id.lc_con);
        yhisDao = new YhisDao(getActivity());
        task = new MyTask();
        isnow = true;
    }

    private void initline() {
        xVals = new ArrayList<String>();
        xVals2 = new ArrayList<String>();
        lineY1 = new ArrayList<Entry>();
        lineY2 = new ArrayList<Entry>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSet1 = new LineDataSet(lineY1, "电导率");
        dataSet1.setColor(Color.parseColor("#058E79"));//设置折线颜色
        dataSet1.setLineWidth(1.5f);//设置折线宽度
        dataSet1.setCircleRadius(2f);//设置圆圈大小
        dataSet1.setValueTextSize(8);//设置显示该点值文字大小。
        dataSet1.setCircleColor(Color.parseColor("#F26077"));//设置圆圈颜色
        dataSet2 = new LineDataSet(lineY2, "电导率");
        dataSet2.setColor(Color.parseColor("#00C0BF"));
        dataSet2.setLineWidth(1.5f);//设置折线宽度
        dataSet2.setCircleRadius(2f);
        dataSet2.setValueTextSize(8);
        dataSet2.setCircleColor(Color.parseColor("#00C0BF"));
        mLinedata = new LineData(xVals, dataSet1);
        mLinedata2 = new LineData(xVals2, dataSet2);

        mLinechart.setDescription("电导率：us/cm³");
        mLinechart.setDescriptionPosition(330f, 40f);
        mLinechart.setDescriptionTextSize(12);

        mLinechart.animateY(500);// 动画
        mLinechart.setTouchEnabled(true);// 设置是否可以触摸
        mLinechart.setDragEnabled(true);// 是否可以拖拽
        mLinechart.setScaleEnabled(false);// 是否可以缩放
        mLinechart.setPinchZoom(false);// 集双指缩放
        mLinechart.getAxisRight().setEnabled(false);// 隐藏右边的坐标轴
        Legend mLegend = mLinechart.getLegend();
        mLegend.setForm(Legend.LegendForm.SQUARE);// 设置比例图标示// 设置窗体样式
        // mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);设置比例图位置
        mLegend.setFormSize(4f);// 字体
        mLegend.setTextColor(Color.parseColor("#000000"));// 字体颜色
        XAxis xAxis = mLinechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签位于图标下边。
        xAxis.setDrawGridLines(false);//不画网格线
        xAxis.setSpaceBetweenLabels(2);
        xAxis.setAxisLineColor(Color.BLACK);
        //xAxis.setLabelsToSkip(0);//忽略的标签数
        leftAxis = mLinechart.getAxisLeft();// 获得左侧侧坐标轴
        leftAxis.setAxisMaxValue(25f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisLineColor(Color.BLACK);

        LimitLine yLimitLine = new LimitLine(10f, "电导率上限：10.0");
        yLimitLine.setLineColor(Color.RED);
        yLimitLine.setTextColor(Color.RED);
        yLimitLine.setTextSize(8f);
        leftAxis.addLimitLine(yLimitLine);
        //Digital.timer.scheduleAtFixedRate(task, 3000, 3000);
        getdata(Digital.getDate());
        mLinechart.setData(mLinedata2);
        mLinechart.setViewPortOffsets(60, 50, 80, 110);
        /*setViewPortOffsets(float left, float top, float right, float bottom) : 设置当前视图的偏移量（实际图表窗口的两侧偏移量）。 设置这个，将阻止图表自动计算它的偏移量。
使用 resetViewPortOffsets() 撤消此设置。 */
        mLinechart.setBackgroundColor(Color.parseColor("#BE4C7BA4"));
        mLinechart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
        mLinechart.moveViewToX(mLinedata.getXValCount() - 1);//设置折线移动

    }

    private void sure(String s) {

        lineY1.clear();
        xVals.clear();
        List<Tbyhis> tbh = yhisDao.find(s);
        if (tbh.size() != 0) {
            for (int i = 0; i < tbh.size(); i++) {
                lineY1.add(new Entry(tbh.get(i).con, xVals.size()));
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
                lineY2.add(new Entry(tbh.get(i).con, xVals2.size()));
                xVals2.add(tbh.get(i).mtime);
            }
        }
    }

    private class MyTask extends TimerTask {

        @Override
        public void run() {
            if (isnow) {
                Random r = new Random();
                Tbyhis thy = yhisDao.find(r.nextInt(14));
                lineY2.add(new Entry(thy.con, xVals2.size()));
                xVals2.add(thy.mtime);
                handler.sendEmptyMessage(1001);
                Log.i("run2", thy.mtime);
            }
        }
    }
}
