package com.asd.tianwang.fragment.fragmen1;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.Digital;

import java.util.Random;

/**
 * Created by ASD on 2016/7/20.
 */
public class OneFrag2 extends Fragment {
    private TextView tx_status;
    private ImageView im_inl, im_inh, im_outl, im_outh, im_jsb, im_fxb, im_csf,
            im_jqf, im_zjf, im_zpf, im_fjf, im_fpf;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1000) {
               setState(msg.arg2,tx_status);
               ImageView []ins={im_inl, im_inh, im_outl, im_outh};
                ImageView []outs={ im_jsb, im_fxb, im_csf,
                        im_jqf, im_zjf, im_zpf, im_fjf, im_fpf};
                for(int i=0;i<4;i++){
                    setimState(Digital.idigital[msg.arg1][i],ins[i]);
                }
                for(int i=0;i<8;i++){
                    setimState(Digital.odigital[msg.arg2][i],outs[i]);
                }
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onef2, null, false);
        init(view);
        new Thread(new MyThread()).start();
        return view;
    }

    private void init(View view) {
        tx_status = (TextView) view.findViewById(R.id.tx_status);
        im_inl = (ImageView) view.findViewById(R.id.im_inl);
        im_inh = (ImageView) view.findViewById(R.id.im_inh);
        im_outl = (ImageView) view.findViewById(R.id.im_outl);
        im_outh = (ImageView) view.findViewById(R.id.im_outh);
        im_jsb = (ImageView) view.findViewById(R.id.im_jsb);
        im_fxb = (ImageView) view.findViewById(R.id.im_fxb);
        im_csf = (ImageView) view.findViewById(R.id.im_csf);
        im_jqf = (ImageView) view.findViewById(R.id.im_jqf);
        im_zjf = (ImageView) view.findViewById(R.id.im_zjf);
        im_zpf = (ImageView) view.findViewById(R.id.im_zpf);
        im_fjf = (ImageView) view.findViewById(R.id.im_fjf);
        im_fpf = (ImageView) view.findViewById(R.id.im_fpf);
    }

    private void setimState(int i, ImageView im) {
        if (i == 1) {
            im.setImageResource(R.drawable.greencircle);
        } else {
            im.setImageResource(R.drawable.redcircle);
        }
    }

    private void setState(int i, TextView tx) {
        switch (i) {
            case 0:
                tx.setText("制水");
                break;
            case 1:
                tx.setText("气洗");
                break;
            case 2:
                tx.setText("气液混合洗");
                break;
            case 3:
                tx.setText("反洗");
                break;
            case 4:
                tx.setText("正洗");
                break;
            default:
                tx.setText("停机");

        }
    }

    class MyThread implements Runnable {
        @Override
        public void run() {
            Random r = new Random();
            int i, j;
            while (true) {
                i = r.nextInt(8);
                j = r.nextInt(5);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = 0x1000;
                msg.arg1 = i;
                msg.arg2 = j;
                handler.sendMessage(msg);

            }
        }
    }

}
