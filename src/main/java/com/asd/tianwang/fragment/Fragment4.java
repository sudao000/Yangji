package com.asd.tianwang.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.Toast;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.HistoryDao;
import com.asd.tianwang.dao.ResourceDao;
import com.asd.tianwang.dao.WarnDao;
import com.asd.tianwang.dao.table.Tbhistory;
import com.asd.tianwang.dao.table.Tbresource;
import com.asd.tianwang.depend.BaseFragment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ASD on 2016/7/20.
 */
public class Fragment4 extends BaseFragment implements View.OnClickListener {
    private Button bt_warn, bt_his, bt_makehis, bt_send, bt_con;
    private RelativeLayout rl;
    private int mYear, mMonth, mDay;
    private EditText et_warn, et_his, et_makehis, et_send, et_ip, et_port;
    private TextView tv_act;
    private String tag = "MainActivity";
    InputStream in;
    PrintWriter printWriter = null;
    Socket mSocket = null;
    public boolean isConnected = false;
    private MyHandler myHandler;
    Thread receiverThread;
    private Timer timer = null;
    private MyTask task = null;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==3){
                bt_con.setText("断开");
            }
            if (msg.what == 2) {
                //timer.scheduleAtFixedRate(task, 3000, 3000);
            }
            receiverData(msg.what);
            if (msg.what == 1) {
                String result = msg.getData().get("msg").toString();
                tv_act.setText(result);
                Log.i("result", result);
            }
            //接收并显示信息在接收窗口
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fram4, null, false);
        init(view);

        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rl.requestFocus();
                return false;
            }
        });
        return view;
    }

    private void init(View view) {
        rl = (RelativeLayout) view.findViewById(R.id.rl_100);
        bt_warn = (Button) view.findViewById(R.id.bt_warn);
        bt_his = (Button) view.findViewById(R.id.bt_his);
        bt_makehis = (Button) view.findViewById(R.id.bt_makehis);
        bt_con = (Button) view.findViewById(R.id.bt_con);
        bt_send = (Button) view.findViewById(R.id.bt_send);
        et_makehis = (EditText) view.findViewById(R.id.et_makehis);
        et_warn = (EditText) view.findViewById(R.id.et_warn);
        et_his = (EditText) view.findViewById(R.id.et_his);
        et_send = (EditText) view.findViewById(R.id.et_send);
        et_ip = (EditText) view.findViewById(R.id.et_ip);
        et_port = (EditText) view.findViewById(R.id.et_port);
        tv_act = (TextView) view.findViewById(R.id.tv_accept);
        myHandler = new MyHandler();
        bt_warn.setOnClickListener(this);
        bt_his.setOnClickListener(this);
        bt_makehis.setOnClickListener(this);
        bt_con.setOnClickListener(this);
        bt_send.setOnClickListener(this);
        et_warn.setOnClickListener(this);
        et_his.setOnClickListener(this);
        et_makehis.setOnClickListener(this);
        timer = new Timer();
        task = new MyTask();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_warn:
                WarnDao warnDao = new WarnDao(getActivity());
                warnDao.remove(et_warn.getText().toString());
                Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_his:
                HistoryDao his = new HistoryDao(getActivity());
                his.remove(et_his.getText().toString());
                Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
            case R.id.bt_makehis:
                makehis();
                break;
            case R.id.et_warn:
                setDtae(et_warn);
                break;
            case R.id.et_his:
                setDtae(et_his);
                break;
            case R.id.et_makehis:
                setDtae(et_makehis);
                break;
            case R.id.bt_con:
                //点击接收按钮 启动连接
                connectThread();
                break;
            case R.id.bt_send:
                sendData();
                break;
        }
    }

    private void makehis() {
        ResourceDao resourceDao = new ResourceDao(getActivity());
        HistoryDao historyDao = new HistoryDao(getActivity());
        Random ra = new Random();
        Calendar c = Calendar.getInstance();
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        String hours = String.valueOf(ra.nextInt(23));

        int m = 0;
        while (m < 10) {
            String sec = String.valueOf(m * 5);
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

    private void setDtae(final EditText et) {
        DatePickerFragment dateDialog = new DatePickerFragment();
        dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
            @Override
            public void acceptTime(int a, int b, int c) {
                mYear = a;
                mMonth = b;
                mDay = c;
                et.setText(new StringBuilder().append(mYear).append("-")
                        .append(mMonth).append("-").append(mDay));
                //Log.i("found:",mYear+"-"+mMonth+"-"+mDay);
            }
        });
        dateDialog.show(getFragmentManager(), "dateDialog");
    }

    public String readFromInputStream(InputStream in) {
        int count = 0;
        byte[] inDatas = null;
        try {
            while (count == 0) {
                count = in.available();//为了得到服务端回馈数据的确切长度
                // 返回此输入流下一个方法调用可以不受阻塞地从此输入流读取（或跳过）的估计字节数。
            }
            inDatas = new byte[count];
            in.read(inDatas);
            return new String(inDatas, "gbk");//使用gb2312方式对接收的字符流数据编码
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void receiverData(int flag) {

        if (flag == 2) {
            // mTask = new ReceiverTask();
            receiverThread = new Thread(new MyReceiverRunnable());
            receiverThread.start();

            Log.i(tag, "--->>socket 连接成功!");
            bt_con.setText("断开");

            isConnected = true;
            // mTask.execute(null);
        }

    }

    private class MyReceiverRunnable implements Runnable {
        public void run() {
            while (true) {
                //Log.i(tag, "---->>client receive....");
               //if(mSocket.isConnected()&&bt_con.getText().equals("连接")){myHandler.sendEmptyMessage(3);}
                if (isConnected) {
                    if (mSocket != null && mSocket.isConnected()) {
                        String result = readFromInputStream(in);
                        try {
                            if (!result.equals("")) {
                                Message msg = new Message();
                                msg.what = 1;
                                Bundle data = new Bundle();
                                data.putString("msg", result);
                                msg.setData(data);
                                myHandler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            Log.e(tag, "--->>read failure!" + e.toString());
                        }
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendData() {

        // sendThread.start();
        try {
            String context = et_send.getText().toString()+"\n";
            if (printWriter == null || context == null) {
                if (printWriter == null) {
                    showInfo("连接失败!");
                    return;
                }
                if (context == null) {
                    showInfo("连接失败!");
                    return;
                }
            }
            printWriter.print(context);
            printWriter.flush();
            Log.i(tag, "--->> client send data!");
        } catch (Exception e) {
            Log.e(tag, "--->> send failure!" + e.toString());
        }
    }

    public void connectThread() {
        if (!isConnected) {//没连接时，启动线程进行连接
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Looper.prepare();
                    Log.i(tag, "---->> connect/close server!");

                    connectServer(et_ip.getText().toString(), et_port.getText()
                            .toString());
                    isConnected = true;
                }
            }).start();
        } else {//连接好后，关闭线程，设置按钮显示为连接。
            try {
                if (mSocket != null) {
                    mSocket.close();
                    mSocket = null;
                    Log.i(tag, "--->>取消server.");
                    // receiverThread.interrupt();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bt_con.setText("连接");
            isConnected = false;
        }
    }

    private void connectServer(String ip, String port) {
        try {
            Log.e(tag, "--->>start connect  server !" + ip + "," + port);

            mSocket = new Socket(ip, Integer.parseInt(port));
            Log.e(tag, "--->>end connect  server!");
            OutputStream outputStream = mSocket.getOutputStream();
            printWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(outputStream,
                            Charset.forName("gb2312"))));
            in = mSocket.getInputStream();
            myHandler.sendEmptyMessage(2);
            showInfo("连接成功!");
        } catch (Exception e) {
            isConnected = false;
            showInfo("连接失败！");
            Log.e(tag, "exception:" + e.toString());
        }
    }

    private void showInfo(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private class MyTask extends TimerTask {

        @Override
        public void run() {

            if (printWriter != null) {
                printWriter.write("测试定时");
                printWriter.flush();
            }
        }
    }
}
