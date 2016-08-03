package com.asd.tianwang;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by ASD on 2016/8/1.
 */
public class DataService extends Service {
    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    InputStream in;
    PrintWriter printWriter = null;
    BufferedReader reader;

    Socket mSocket = null;
    public boolean isConnected = false;
    Thread receiverThread;
    private String tag = "DataService";

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String result;
    public String ip, port, send;
    private MainActivity activity;
    private final IBinder binder = new DataBinder();

    public class DataBinder extends Binder {
        DataService getService() {
            return DataService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //接收线程
    private class MyReceiverRunnable implements Runnable {

        public void run() {

            while (true) {

                Log.i(tag, "---->>client receive....");
                if (isConnected) {
                    if (mSocket != null && mSocket.isConnected()) {
                        String result = readFromInputStream(in);
                        try {
                            if (!result.equals("")) {
                                setResult(result);
                            }
                        } catch (Exception e) {
                            Log.e(tag, "--->>read failure!" + e.toString());
                        }
                    }
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //获取服务器数据。
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
            return new String(inDatas, "gb2312");//使用gb2312方式对接收的字符流数据编码
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 连接到服务器才会触发接收事件。
     */
    public void receiverData(int flag) {

        if (flag == 2) {
            // mTask = new ReceiverTask();
            receiverThread = new Thread(new MyReceiverRunnable());
            receiverThread.start();
            Log.i(tag, "--->>socket 连接成功!");
            isConnected = true;
            // mTask.execute(null);
        }
    }

    /**
     * 发送数据线程.
     */
    public void sendData() {

        // sendThread.start();
        try {
            if (printWriter == null || send == null) {

                if (printWriter == null) {
                    showInfo("连接失败!");
                    return;
                }
                if (send == null) {
                    showInfo("连接失败!");
                    return;
                }
            }
            printWriter.print(send);
            printWriter.flush();
            Log.i(tag, "--->> client send data!");
        } catch (Exception e) {
            Log.e(tag, "--->> send failure!" + e.toString());
        }
    }

    /**
     * 启动连接线程.
     */
    public void connectThread() {
        if (!isConnected&&!ip.equals("")&&!port.equals("")) {//没连接时，启动线程进行连接
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Looper.prepare();
                    Log.i(tag, "---->> connect/close server!");
                    connectServer(ip, port);
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
            isConnected = false;
        }
    }

    // 连接服务器.(网络调试助手的服务器端编码方式:gb2312)
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
            showInfo("连接成功!");
        } catch (Exception e) {
            isConnected = false;
            showInfo("连接失败！");
            Log.e(tag, "exception:" + e.toString());
        }

    }

    private void showInfo(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

    }
}
