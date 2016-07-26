package com.asd.tianwang.fragment.fragment2;

import android.app.Fragment;
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
import com.asd.tianwang.dao.HistoryDao;
import com.asd.tianwang.dao.table.Tbhistory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ASD on 2016/7/20.
 */
public class TwoFrag1 extends Fragment {
    private int mYear, mMonth, mDay;
    boolean isRefesh = true;
    private RelativeLayout rl;
    private Button bt_sure, bt_now;
    private LineChart mLinechart;
    private LineData mLinedata, mLinedata2;
    private ArrayList<String> xVals, xVals2;
    private ArrayList<ILineDataSet> dataSets, dataSets2;
    private ArrayList<Entry> yValues1, yValues2, yValues3, yValues21, yValues22, yValues23;
    private YAxis leftAxis;
    private TextView tx;
    private EditText date, limit,txtest;
    Thread datanow;
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
          if(msg.what==0x0102){update();}
            if (isRefesh) {
                mLinechart.setData(mLinedata);
                mLinechart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
                mLinechart.moveViewToX(mLinedata.getXValCount() - 1);//设置折线移动
            }
            if(msg.what==0x0103&&!isRefesh){
                  mLinechart.setData(mLinedata2);
                mLinechart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
               // mLinechart.moveViewToX(mLinedata.getXValCount() - 1);
                }
            if (!limit.getText().toString().equals("")) {
                    leftAxis.removeAllLimitLines();
                    float lim = Float.parseFloat(limit.getText().toString());
                    LimitLine yLimitLine = new LimitLine(lim, "进水压力上限:" + lim);
                    // yLimitLine.
                    yLimitLine.setLineColor(Color.RED);
                    yLimitLine.setTextColor(Color.RED);
                    yLimitLine.setTextSize(8f);
                    leftAxis.addLimitLine(yLimitLine);
                }//限制线设置
            mLinechart.notifyDataSetChanged();
            mLinechart.invalidate();
            String s = msg.arg1 + "";
            tx.setText(s);
           // Log.i("mbchart", "执行handler2");

        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twof1, null, false);
        initline(view);
        date.setOnClickListener(new View.OnClickListener() {// 为时间文本框设置单击监听事件
            @Override
            public void onClick(View arg0) {
                DatePickerFragment dateDialog = new DatePickerFragment();
                dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
                    @Override
                    public void acceptTime(int a, int b, int c) {
                        mYear=a;
                        mMonth=b;
                        mDay=c;
                        date.setText(new StringBuilder().append(mYear).append("-")
                                .append(mMonth).append("-").append(mDay));
                        //Log.i("found:",mYear+"-"+mMonth+"-"+mDay);
                    }
                });
                dateDialog.show(getFragmentManager(),"dateDialog");
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
                handler2.sendEmptyMessage(0x0102);
            }
        });
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rl.requestFocus();
                return false;
            }
        }); //设置RelativeLayout可获取焦点，用于点击屏幕其他区域使文本编辑框失去焦点。*/
        datanow.start();
        update();
        return view;
    }

    private void update(){
        final Calendar c = Calendar.getInstance();// 获取当前系统日期
        mYear = c.get(Calendar.YEAR);// 获取年份
        mMonth = c.get(Calendar.MONTH)+1;// 获取月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        date.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth).append("-").append(mDay));
    }
    private void sure() {
        yValues21.clear();
        yValues22.clear();
        yValues23.clear();
        xVals2.clear();//每次都清空一下数据，不然会存储很多。
        HistoryDao historyDao = new HistoryDao(getActivity());
        List<Tbhistory> his = historyDao.find(date.getText().toString());
        if (his.size() != 0) {
            isRefesh=false;
            for (int i = 0; i < his.size(); i++) {
                yValues21.add(new Entry(his.get(i).getInp(), xVals2.size()));
                yValues22.add(new Entry(his.get(i).getOutp(), xVals2.size()));
                yValues23.add(new Entry(his.get(i).getOpsp(), xVals2.size()));
                xVals2.add(his.get(i).getMtime());
               // Log.i("yValues",yValues21.get(i).toString()+","+xVals2.get(i));
            }
            Message msg = handler2.obtainMessage();
            msg.what = 0x0103;
            msg.arg1 = xVals2.size();
            handler2.sendMessage(msg);
        }
    }

    private void initline(View view) {
        datanow = new Thread(new MyThread());
        mLinechart = (LineChart) view.findViewById(R.id.linechart);
        tx = (TextView) view.findViewById(R.id.tx_02);
        date = (EditText) view.findViewById(R.id.et_date);
        txtest = (EditText) view.findViewById(R.id.et_test);
        bt_sure = (Button) view.findViewById(R.id.bt_sure);
        limit = (EditText) view.findViewById(R.id.et_limit);
        rl = (RelativeLayout) view.findViewById(R.id.rl01);
        bt_now = (Button) view.findViewById(R.id.bt_now);

        dataSets = new ArrayList<ILineDataSet>();
        dataSets2 = new ArrayList<ILineDataSet>();
        xVals = new ArrayList<String>();
        xVals2 = new ArrayList<String>();
        yValues1 = new ArrayList<Entry>();
        yValues2 = new ArrayList<Entry>();
        yValues3 = new ArrayList<Entry>();
        yValues21 = new ArrayList<Entry>();
        yValues22 = new ArrayList<Entry>();
        yValues23 = new ArrayList<Entry>();
        dataSets = getDatasets(yValues1, yValues2, yValues3);
        dataSets2 = getDatasets(yValues21, yValues22, yValues23);
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
        //xAxis.setLabelsToSkip(0);//忽略的标签数
        leftAxis = mLinechart.getAxisLeft();// 获得左侧侧坐标轴
        leftAxis.setAxisMaxValue(9f);
        leftAxis.setAxisMinValue(2f);
        leftAxis.setDrawGridLines(false);

        LimitLine yLimitLine = new LimitLine(6.8f, "进水压力上限：6.8");
        // yLimitLine.
        yLimitLine.setLineColor(Color.RED);
        yLimitLine.setTextColor(Color.RED);
        yLimitLine.setTextSize(8f);
        leftAxis.addLimitLine(yLimitLine);
        mLinechart.setData(mLinedata);
        mLinechart.setBackgroundColor(Color.parseColor("#BE4C7BA4"));

    }


    class MyThread implements Runnable {
        @Override
        public void run() {
            int m = 0;
            HistoryDao historyDao = new HistoryDao(getActivity());
            while (m < 16) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Tbhistory tbh = historyDao.find(m);
                yValues1.add(new Entry(tbh.getInp(), xVals.size()));
                yValues2.add(new Entry(tbh.getOutp(), xVals.size()));
                yValues3.add(new Entry(tbh.getOpsp(), xVals.size()));
                xVals.add(tbh.getMtime());
                //Log.i("m", m + "");
                Message msg = handler2.obtainMessage();
                msg.what = 0x0104;
                msg.arg1 = m;
                handler2.sendMessage(msg);
                m = xVals.size();//一开始用m++,但切换到其他页面在切换回来 m值会乱跳不知为何，目前这样解决了
                //m值乱跳问题，但切回来后线程休眠时间仍会错乱，不是5秒，会少于5秒原因未知。
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

}
