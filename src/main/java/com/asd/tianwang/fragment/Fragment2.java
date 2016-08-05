package com.asd.tianwang.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.Digital;
import com.asd.tianwang.dao.HistoryDao;
import com.asd.tianwang.dao.ResourceDao;
import com.asd.tianwang.dao.table.Tbhistory;
import com.asd.tianwang.dao.table.Tbresource;
import com.asd.tianwang.depend.BaseFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ASD on 2016/7/20.
 */
public class Fragment2 extends BaseFragment {
    private int mYear, mMonth, mDay;
    boolean isRefesh = true;
    private BarChart mBarchart;
    private BarData mBardata, mBardata2;
    private ArrayList<IBarDataSet> dataSet, dataSet2;
    private ArrayList<BarEntry> barY1, barY2, barY3, barY21, barY22, barY23;
    private YAxis yleftAxis;
    private RelativeLayout rl;
    private Button bt_sure, bt_now, bt_change;
    private LineChart mLinechart;
    private LineData mLinedata, mLinedata2;
    private ArrayList<String> xVals, xVals2,barX1,barX2;
    private ArrayList<ILineDataSet> dataSets, dataSets2;
    private ArrayList<Entry> lineY1, lineY2, lineY3, lineY21, lineY22, lineY23;
    private YAxis leftAxis;
    private TextView tx;
    private EditText date, linelimit,barlimit;
    Thread datanow;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x0102) {
                update();
            }
            if (isRefesh) {
                mLinechart.setData(mLinedata);
                mLinechart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
                mLinechart.moveViewToX(mLinedata.getXValCount() - 1);//设置折线移动
                mBarchart.setData(mBardata);
                mBarchart.setVisibleXRangeMaximum(18);//设置X轴最多显示数据7个
                mBarchart.moveViewToX(mBardata.getXValCount() - 5);//设置折线移动
            }
            if (msg.what == 0x0103 && !isRefesh) {
                mLinechart.setData(mLinedata2);
                mLinechart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
                mLinechart.moveViewToX(mLinedata.getXValCount() - 1);
                mBarchart.setData(mBardata2);
                mBarchart.setVisibleXRangeMaximum(18);//设置X轴最多显示数据7个
                mBarchart.moveViewToX(mBardata2.getXValCount() - 5);
            }
            if (!linelimit.getText().toString().equals("")) {
                leftAxis.removeAllLimitLines();
                float lim = Float.parseFloat(linelimit.getText().toString());
                LimitLine yLimitLine = new LimitLine(lim, "进水压力上限:" + lim);
                Digital.limit1=lim;

                // yLimitLine.
                yLimitLine.setLineColor(Color.RED);
                yLimitLine.setTextColor(Color.RED);
                yLimitLine.setTextSize(8f);
                leftAxis.addLimitLine(yLimitLine);
            }//压力限制线设置
            if (!barlimit.getText().toString().equals("")) {
                yleftAxis.removeAllLimitLines();
                float lim = Float.parseFloat(barlimit.getText().toString());
                Digital.limit1=lim;

                LimitLine yLimitLine = new LimitLine(lim, "最低产水:" + lim);
                yLimitLine.setLineColor(Color.BLUE);
                yLimitLine.setTextColor(Color.BLUE);
                yLimitLine.setTextSize(8f);
                yleftAxis.addLimitLine(yLimitLine);
            }//流量限制线设置
            mLinechart.notifyDataSetChanged();
            mLinechart.invalidate();
            mBarchart.notifyDataSetChanged();
            mBarchart.invalidate();
            String s = msg.arg1 + "";
            tx.setText(s);
        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fram2, null, false);
        init(view);
        date.setOnClickListener(new View.OnClickListener() {// 为时间文本框设置单击监听事件
            @Override
            public void onClick(View arg0) {
                DatePickerFragment dateDialog = new DatePickerFragment();
                dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
                    @Override
                    public void acceptTime(int a, int b, int c) {
                        mYear = a;
                        mMonth = b;
                        mDay = c;
                        date.setText(new StringBuilder().append(mYear).append("-")
                                .append(mMonth).append("-").append(mDay));
                        //Log.i("found:",mYear+"-"+mMonth+"-"+mDay);
                    }
                });
                dateDialog.show(getFragmentManager(), "dateDialog");
            }
        });
        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
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
                handler.sendEmptyMessage(0x0102);
            }
        });
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rl.requestFocus();
                return false;
            }
        }); //设置RelativeLayout可获取焦点，用于点击屏幕其他区域使文本编辑框失去焦点。*/
        //datanow.start();
        update();
        return view;
    }

    public void change() {
        if (bt_change.getText().toString().equals("流量")) {
            mLinechart.setVisibility(View.INVISIBLE);
            mBarchart.setVisibility(View.VISIBLE);
            bt_change.setText("压力");
        } else {
            mBarchart.setVisibility(View.INVISIBLE);
            mLinechart.setVisibility(View.VISIBLE);
            bt_change.setText("流量");
        }
    }

    private void update() {
        final Calendar c = Calendar.getInstance();// 获取当前系统日期
        mYear = c.get(Calendar.YEAR);// 获取年份
        mMonth = c.get(Calendar.MONTH) + 1;// 获取月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        date.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth).append("-").append(mDay));
    }

    private void sure() {
        Digital.ischange=true;
        if(!date.getText().toString().equals(""))
        { lineY21.clear();
        lineY22.clear();
        lineY23.clear();
        barY21.clear();
        barY22.clear();
        barY23.clear();
        xVals2.clear();
        barX2.clear();}
        //每次都清空一下数据，不然会存储很多。
        HistoryDao historyDao = new HistoryDao(getActivity());
        List<Tbhistory> his = historyDao.find(date.getText().toString());
        if (his.size() != 0) {
            isRefesh = false;
            for (int i = 0; i < his.size(); i++) {
                lineY21.add(new Entry(his.get(i).getInp(), xVals2.size()));
                lineY22.add(new Entry(his.get(i).getOutp(), xVals2.size()));
                lineY23.add(new Entry(his.get(i).getOpsp(), xVals2.size()));
                barY21.add(new BarEntry(his.get(i).getInf(), barX2.size()));
                barY22.add(new BarEntry(his.get(i).getOutf(), barX2.size()));
                barY23.add(new BarEntry(his.get(i).getBackf(), barX2.size()));
                xVals2.add(his.get(i).getMtime());
                barX2.add(his.get(i).getMtime());
            }

        }
        Message msg = handler.obtainMessage();
        msg.what = 0x0103;
        msg.arg1 = xVals2.size();
        handler.sendMessage(msg);
    }

    private void init(View view) {
        mBarchart = (BarChart) view.findViewById(R.id.barchart);
        mLinechart = (LineChart) view.findViewById(R.id.linechart);
        tx = (TextView) view.findViewById(R.id.tx_02);
        bt_now = (Button) view.findViewById(R.id.bt_now2);
        bt_sure = (Button) view.findViewById(R.id.bt_qd);
        bt_change = (Button) view.findViewById(R.id.bt_change);
        linelimit = (EditText) view.findViewById(R.id.et_linelimit);
        barlimit=(EditText) view.findViewById(R.id.et_barlimit);
        date = (EditText) view.findViewById(R.id.et_date);
        rl = (RelativeLayout) view.findViewById(R.id.rl01);
        initbar();
        initline();
        mBarchart.setVisibility(View.INVISIBLE);
    }

    private void initline() {
        datanow = new Thread(new MyThread());
        dataSets = new ArrayList<ILineDataSet>();
        dataSets2 = new ArrayList<ILineDataSet>();
        xVals = new ArrayList<String>();
        xVals2 = new ArrayList<String>();
        lineY1 = new ArrayList<Entry>();
        lineY2 = new ArrayList<Entry>();
        lineY3 = new ArrayList<Entry>();
        lineY21 = new ArrayList<Entry>();
        lineY22 = new ArrayList<Entry>();
        lineY23 = new ArrayList<Entry>();
        dataSets = getDatasets(lineY1, lineY2, lineY3);
        dataSets2 = getDatasets(lineY21, lineY22, lineY23);
        mLinedata = new LineData(xVals, dataSets);
        mLinedata2 = new LineData(xVals2, dataSets2);


        mLinechart.setDescription("压力：0.01MPa");
        mLinechart.setDescriptionPosition(320f, 80f);
        mLinechart.setDescriptionTextSize(12);

        mLinechart.animateY(500);// 动画
        mLinechart.setTouchEnabled(true);// 设置是否可以触摸
        mLinechart.setDragEnabled(true);// 是否可以拖拽
        mLinechart.setScaleEnabled(false);// 是否可以缩放
        mLinechart.setPinchZoom(false);// 集双指缩放
        mLinechart.getAxisRight().setEnabled(false);// 隐藏右边的坐标轴
        // mBarchart.getAxisLeft().setEnabled(false);// 隐藏左边的左边轴
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
        leftAxis.setAxisMaxValue(9f);
        leftAxis.setAxisMinValue(2f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisLineColor(Color.BLACK);

        LimitLine yLimitLine = new LimitLine(6.8f, "进水压力上限：6.8");
        // yLimitLine.
        yLimitLine.setLineColor(Color.RED);
        yLimitLine.setTextColor(Color.RED);
        yLimitLine.setTextSize(8f);
        leftAxis.addLimitLine(yLimitLine);
        mLinechart.setData(mLinedata);
        mLinechart.setBackgroundColor(Color.parseColor("#BE4C7BA4"));

    }

    private void initbar() {
        dataSet = new ArrayList<IBarDataSet>();
        dataSet2 = new ArrayList<IBarDataSet>();
        barX1 = new ArrayList<String>();
        barX2 = new ArrayList<String>();
        barY1 = new ArrayList<BarEntry>();
        barY2 = new ArrayList<BarEntry>();
        barY3 = new ArrayList<BarEntry>();
        barY21 = new ArrayList<BarEntry>();
        barY22 = new ArrayList<BarEntry>();
        barY23 = new ArrayList<BarEntry>();
        dataSet = getBardatas(barY1, barY2, barY3);
        dataSet2 = getBardatas(barY21, barY22, barY23);
        mBardata = new BarData(barX1, dataSet);
        mBardata2 = new BarData(barX2, dataSet2);

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
        xAxis.setAxisLineColor(Color.BLACK);

        //xAxis.setLabelsToSkip(0);//忽略的标签数
        yleftAxis = mBarchart.getAxisLeft();// 获得左侧侧坐标轴
        yleftAxis.setAxisMaxValue(12f);
        yleftAxis.setAxisMinValue(0f);
        yleftAxis.setDrawGridLines(false);
        yleftAxis.setAxisLineColor(Color.BLACK);
        LimitLine yLimitLine = new LimitLine(5f, "最低产水:5");
        yLimitLine.setLineColor(Color.BLUE);
        yLimitLine.setTextColor(Color.BLACK);
        yLimitLine.setTextSize(8f);
        yleftAxis.addLimitLine(yLimitLine);
        mBarchart.setData(mBardata);
        mBarchart.setBackgroundColor(Color.parseColor("#8FB3B373"));
        mBarchart.invalidate();
    }


    class MyThread implements Runnable {
        @Override
        public void run() {
            int m = 0;
            ResourceDao resourceDao = new ResourceDao(getActivity());
            while (true) {

                if (Digital.out==0) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String a[]=Digital.getTime();
                    Tbresource tbresource = resourceDao.find(Digital.an);
                    lineY1.add(new Entry(tbresource.getInp(), xVals.size()));
                    lineY2.add(new Entry(tbresource.getOutp(), xVals.size()));
                    lineY3.add(new Entry(tbresource.getOpsp(), xVals.size()));
                    barY1.add(new BarEntry(tbresource.getInf(), barX1.size()));
                    barY2.add(new BarEntry(tbresource.getOutf(), barX1.size()));
                    barY3.add(new BarEntry(tbresource.getBackf(), barX1.size()));
                    xVals.add(a[0]);
                    barX1.add(a[0]);
                    Message msg = handler.obtainMessage();
                    msg.what = 0x0104;
                    msg.arg1 = m;
                    handler.sendMessage(msg);
                    m = xVals.size();
                    //一开始用m++,但切换到其他页面在切换回来 m值会乱跳不知为何，目前这样解决了
                    //m值乱跳问题，但切回来后线程休眠时间仍会错乱，不是3秒，会少于3秒原因未知。
                }
            }
        }
    }

    private ArrayList<ILineDataSet> getDatasets(ArrayList<Entry> yValues1,
                                                ArrayList<Entry> yValues2, ArrayList<Entry> yValues3) {
        LineDataSet dataSet1, dataSet2, dataSet3;
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSet1 = new LineDataSet(yValues1, "进水压力");
        dataSet1.setColor(Color.parseColor("#F26077"));//设置折线颜色
        dataSet1.setLineWidth(1.5f);//设置折线宽度
        dataSet1.setCircleRadius(2f);//设置圆圈大小
        dataSet1.setValueTextSize(8);//设置显示该点值文字大小。
        dataSet1.setCircleColor(Color.parseColor("#F26077"));//设置圆圈颜色
        dataSet2 = new LineDataSet(yValues2, "产水压力");
        dataSet2.setColor(Color.parseColor("#00C0BF"));
        dataSet2.setLineWidth(1.5f);//设置折线宽度
        dataSet2.setCircleRadius(2f);
        dataSet2.setValueTextSize(8);
        dataSet2.setCircleColor(Color.parseColor("#00C0BF"));
        dataSet3 = new LineDataSet(yValues3, "浓水压力");
        dataSet3.setColor(Color.parseColor("#DEAD26"));
        dataSet3.setLineWidth(1.5f);//设置折线宽度
        dataSet3.setCircleRadius(2f);
        dataSet3.setValueTextSize(8);
        dataSet3.setCircleColor(Color.parseColor("#DEAD26"));

        dataSets.add(dataSet1);
        dataSets.add(dataSet2);
        dataSets.add(dataSet3);
        return dataSets;
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


}
