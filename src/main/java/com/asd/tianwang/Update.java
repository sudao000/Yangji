package com.asd.tianwang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Sudao on 2016/11/1.
 */
public class Update {
    public static final int ERROR_GET_UPDATEINOF = 0;
    public static final int SUCESS_GET_UPDATEINOF = 1;
    public static final int ERROR_DOWNLOAD_APK = 2;
    public static final int SUCCESS_DOWNLOAD_APK = 3;
    private PackageManager pm;
    private int version;
    private UpdateInfo updateInfo;
    private File apkFile;
    private MyProgressDialog mPb;
    private static final String XMLURL = "http://23.83.240.232:8080/Upd/update.xml";
    private Context mContext;
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;
    private String mSavePath;
    boolean cancelUpdate;
    int progress;
    boolean iscanle;

    public Update(Context con) {
        mContext = con;
        version = getVersion();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0011:
                    mProgress.setProgress(progress);
                    break;
                case 0022:
                    toast("已是最新版本！");
                    break;
                case 0023:
                    int sl = msg.getData().getInt("size");
                    //Log.i("size---------------",sl+"");
                    mPb.setProgress(sl);
                    break;
            /*
             * 获取更新信息错误 , 在断网或者获取信息出现异常执行
             * 提示一下, 之后进入主界面
             */
                case ERROR_GET_UPDATEINOF:
                    toast("获取更新信息失败！");
                    break;
            /*
             * 成功获取更新信息, 一般在成功从网上获取xml文件并解析出来
             * 如果版本号相同, 说明不用更新, 直接进入主界面
             * 如果版本号不同, 需要弹出更新对话框
             */
                case SUCESS_GET_UPDATEINOF:
                    showNoticeDialog();
                    break;
            /*
             * 下载apk文件出现错误, 中途断网 出现异常等情况
             * 提示后进入主界面
             */
                case ERROR_DOWNLOAD_APK:
                    mPb.dismiss();
                    toast("下载更新文件失败！");
                    break;
            /*
             * 成功下载apk文件之后执行的操作
             * 取消进度条对话框, 之后安装apk文件
             */
                case SUCCESS_DOWNLOAD_APK:
                    mPb.dismiss();
                    if (iscanle) {
                        installApk();
                    } else {
                        toast("下载更新失败！");
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public void checkUpdate(int updateMethod) {
        new Thread(new CheckVersionTask(updateMethod)).start();
    }


    //获取当前版本号
    private int getNowVersion(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo("com.asd.tianwang", 0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    //解析version.xml文件
    public HashMap<String, String> xmlParsing(InputStream in) throws Exception {
        HashMap<String, String> hashmap = new HashMap<String, String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(in);
        Element root = document.getDocumentElement();
        NodeList childNode = root.getChildNodes();
        for (int j = 0; j < childNode.getLength(); j++) {
            Node childnode = childNode.item(j);
            if (childnode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childnode;
                if ("version".equals(childElement.getNodeName())) {
                    hashmap.put("version", childElement.getFirstChild().getNodeValue());
                } else if ("name".equals(childElement.getNodeName())) {
                    hashmap.put("name", childElement.getFirstChild().getNodeValue());
                } else if ("url".equals(childElement.getNodeName())) {
                    hashmap.put("url", childElement.getFirstChild().getNodeValue());
                }
            }
        }
        return hashmap;
    }

    //显示软件更新对话框
    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("软件更新！");
        builder.setMessage("检测到有新版本，是否进行更新？");
        builder.setPositiveButton("马上更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        Window window = noticeDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;// 透明度
        window.setAttributes(lp);

        noticeDialog.show();
    }

    //显示下载对话框
    private void showDownloadDialog() {
        iscanle = true;
        //创建进度条对话框
        mPb = new MyProgressDialog(mContext);
        Window window = mPb.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.6f;// 透明度
        window.setAttributes(lp);
        //设置进度条对话框不可回退
        mPb.setCancelable(true);
        //设置进度条对话框样式
        //mPb.setTitle("标题：下载测试");
        mPb.setIcon(android.R.drawable.ic_popup_sync);
        mPb.setIndeterminate(false);
        mPb.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置进度条对话框的信息
        mPb.setMessage("更新下载中...");
        mPb.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                iscanle = false;
                Log.i("onCancel:iscanle---->", iscanle + "");

            }
        });
        //显示进度条对话框
        mPb.show();
        //开启显示进度条对话框线程
        new Thread(new DownloadApkTask()).start();

    }

    //下载APK文件

    /**
     * 在这个线程中主要执行downloadApk方法, 这个方法传入apk路径和进度条对话框
     * 注意 : 下载的前提是sd卡的状态是挂载的
     */
    private final class DownloadApkTask implements Runnable {
        public void run() {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                try {
                    SystemClock.sleep(2000);
                    apkFile = downloadApk(updateInfo.url, mPb);
                    Message msg = new Message();
                    msg.what = SUCCESS_DOWNLOAD_APK;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = ERROR_DOWNLOAD_APK;
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    public File downloadApk(String path, MyProgressDialog pb) throws Exception {
        //创建本地文件对象
        File file = new File(Environment.getExternalStorageDirectory(), updateInfo.getDescription() + ".apk");
        Log.i("文件路径：", Environment.getExternalStorageDirectory().toString());
        //创建HttpURL连接
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        // conn.setRequestProperty("Accept-Encoding", "identity");
        if (conn.getResponseCode() == 200) {
            int max = conn.getContentLength();
            //设置进度条对话框的最大值
            pb.setMax(max);
            Log.i("MAX------------------", max + "");
            int count = 0;
            InputStream is = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while (((len = is.read(buffer)) != -1) & iscanle) {

                fos.write(buffer, 0, len);
                //设置进度条对话框进度
                count = count + len;
                //Log.i("count...........",count+"");
                Message msg = mHandler.obtainMessage();
                msg.getData().putInt("size", count);
                msg.what = 0023;
                mHandler.sendMessage(msg);
            }
            is.close();
            fos.close();
        }
        return file;
    }

    private final class CheckVersionTask implements Runnable {
        int checkMode;

        public CheckVersionTask(int i) {
            checkMode = i;
        }

        public void run() {
            try {
                /*
                 * 获取当前时间, 与onCreate方法中获取的时间进行比较
                 * 如果不足3秒, 在等待够3秒之后在执行下面的操作
                 */
              /*  long temp = System.currentTimeMillis();
                if(temp - time < 3000){
                    SystemClock.sleep(temp - time);
                }*/

                /*
                 * 检查配置文件中的设置, 是否设置了自动更新;
                 * 如果设置了自动更新, 就执行下面的操作,
                 * 如果没有设置自动更新, 就直接进入主界面
                 */
               /* boolean is_auto_update = sp.getBoolean("is_auto_update", true);
                if(!is_auto_update){
                    loadMainUI();
                    return;
                }*/

                /*
                 * 获取更新信息
                 * 如果信息不为null, 向handler发信息SUCESS_GET_UPDATEINOF, 执行后续操作
                 * 如果信息为null, 向handler发信息ERROR_GET_UPDATEINOF, 执行后续操作
                 * 如果出现异常, 向handler发信息ERROR_GET_UPDATEINOF, 执行后续操作
                 */
                updateInfo = getUpdateInfo(XMLURL);
                if (updateInfo != null) {
                    Log.i("是否想等：", updateInfo.getVersion() + ":" + version);
                    if (updateInfo.getVersion() == version) {
                        if (checkMode == 1) {
                            mHandler.sendEmptyMessage(0022);
                        }
                    } else {
                        mHandler.sendEmptyMessage(SUCESS_GET_UPDATEINOF);
                    }
                } else {
                    Message msg = new Message();
                    msg.what = ERROR_GET_UPDATEINOF;
                    mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = ERROR_GET_UPDATEINOF;
                mHandler.sendMessage(msg);
            }
        }
    }

    private UpdateInfo getUpdateInfo(String path) {
        try {
            URL url = new URL(path);    //创建URL对象
            //创建连接对象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置链接超时
            conn.setConnectTimeout(5000);
            //设置获取方式
            conn.setRequestMethod("GET");
            //如果连接成功, 获取输入流
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                //解析输入流中的数据, 返回更新信息
                return parserUpdateInfo(is);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取更新信息
     * ① 创建pull解析器
     * ② 为解析器设置编码格式
     * ③ 获取解析事件
     * ④ 遍历整个xml文件节点, 获取标签元素内容
     */
    private UpdateInfo parserUpdateInfo(InputStream is) {
        try {
            UpdateInfo updateInfo = null;
            //1. 创建pull解析解析器
            XmlPullParser parser = Xml.newPullParser();
            //2. 设置解析编码
            parser.setInput(is, "UTF-8");
            //3. 获取解析器解事件, 如解析到文档开始 , 结尾, 标签等
            int eventType = parser.getEventType();
            //4. 在文档结束前一直解析
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //只解析标签
                    case XmlPullParser.START_TAG:
                        if ("update".equals(parser.getName())) {
                            //当解析到updateInfo标签的时候, 跟标签开始, 创建一个UpdateInfo对象
                            updateInfo = new UpdateInfo();
                        } else if ("version".equals(parser.getName())) {
                            //解析版本号标签
                            int i = Integer.parseInt(parser.nextText());
                            updateInfo.setVersion(i);
                        } else if ("name".equals(parser.getName())) {
                            //解析url标签
                            updateInfo.setDescription(parser.nextText());
                        } else if ("url".equals(parser.getName())) {
                            //解析描述标签
                            updateInfo.setUrl(parser.nextText());
                        }
                        break;
                    default:
                        break;
                }
                //每解析完一个元素, 就将解析标志位下移
                eventType = parser.next();
            }
            Log.i("update", updateInfo.toString());
            is.close();
            return updateInfo;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 安装apk文件流程
     * <p/>
     * a. 设置Action : Intent.ACTION_VIEW.
     * b. 设置数据和类型 : 设置apk文件的uri 和 MIME类型
     * c. 开启安装文件的Activity.
     */
    protected void installApk() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    private int getVersion() {
        try {
            pm = mContext.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public class UpdateInfo {
        private int version; //当前软件版本号
        private String url;     //获取到的软件地址
        private String description; //软件描述

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "UpdateInfo [version=" + version + ", url=" + url
                    + ", description=" + description + "]";
        }
    }

    private void toast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

}
