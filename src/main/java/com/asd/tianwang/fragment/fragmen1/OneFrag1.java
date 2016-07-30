package com.asd.tianwang.fragment.fragmen1;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.Digital;
import com.asd.tianwang.dao.ResourceDao;
import com.asd.tianwang.dao.WarnDao;
import com.asd.tianwang.dao.table.Tbresource;

import java.math.BigDecimal;

/**
 * Created by ASD on 2016/7/20.
 */
public class OneFrag1 extends Fragment {
    private ResourceDao resourceDao;
    private Button contral;
    private TextView tv_inp, tv_outp, tv_opsp, tv_inf, tv_outf, tv_backf, tv_status;
    private ImageView iv_in, iv_out, iv_jsb, iv_fxb, iv_csf, iv_jqf, iv_zjf, iv_zpf, iv_fjf, iv_fpf;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1100) {
                   show_level(Digital.in);
                   show_out(Digital.out);
                    show_analog();
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.onef1, null, false);
        init(view);
        contral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContral(view);
            }
        });
        new Thread(new MyThread()).start();
        return view;
    }

    private void init(View view) {
        resourceDao = new ResourceDao(getActivity());
        contral = (Button) view.findViewById(R.id.bt_cto);
        tv_inp = (TextView) view.findViewById(R.id.tv_inp);
        tv_outp = (TextView) view.findViewById(R.id.tv_outp);
        tv_opsp = (TextView) view.findViewById(R.id.tv_opsp);
        tv_inf = (TextView) view.findViewById(R.id.tv_inf);
        tv_outf = (TextView) view.findViewById(R.id.tv_outf);
        tv_backf = (TextView) view.findViewById(R.id.tv_backf);
        tv_status = (TextView) view.findViewById(R.id.tv_status1);
        iv_in = (ImageView) view.findViewById(R.id.iv_inpum);
        iv_out = (ImageView) view.findViewById(R.id.iv_outpum);
        iv_jsb = (ImageView) view.findViewById(R.id.iv_jsb);
        iv_fxb = (ImageView) view.findViewById(R.id.iv_fxb);
        iv_csf = (ImageView) view.findViewById(R.id.iv_csf);
        iv_jqf = (ImageView) view.findViewById(R.id.iv_jqf);
        iv_zjf = (ImageView) view.findViewById(R.id.iv_zjf);
        iv_zpf = (ImageView) view.findViewById(R.id.iv_zpf);
        iv_fjf = (ImageView) view.findViewById(R.id.iv_fjf);
        iv_fpf = (ImageView) view.findViewById(R.id.iv_fpf);

    }

    public void showContral(View view) {
        Controlmenu cmenu = new Controlmenu();
        cmenu.setMgetdata(new Controlmenu.Getdata() {
            @Override
            public void sendData(String id) {

            }
        });
        cmenu.show(getChildFragmentManager(), "操作面板");

    }

    public void show_level(int i) {
        if (Digital.idigital[i][0] == 0) {
            iv_in.getDrawable().setLevel(2);
        } else if (Digital.idigital[i][1] == 0) {
            iv_in.getDrawable().setLevel(5);
        } else {
            iv_in.getDrawable().setLevel(8);
        }
        if (Digital.idigital[i][2] == 0) {
            iv_out.getDrawable().setLevel(2);
        } else if (Digital.idigital[i][3] == 0) {
            iv_out.getDrawable().setLevel(5);
        } else {
            iv_out.getDrawable().setLevel(8);
        }

    }
    public void show_out(int i){
        switch (i){
            case 0:
                tv_status.setText("制水");
                break;
            case 1:
                tv_status.setText("清洗—气洗");
                break;
            case 2:
                tv_status.setText("清洗—气液混合洗");
                break;
            case 3:
                tv_status.setText("清洗—反洗");
                break;
            default:
                tv_status.setText("停机");
        }
        ImageView []outs={ iv_jsb, iv_fxb, iv_csf,
                iv_jqf, iv_zjf, iv_zpf, iv_fjf, iv_fpf};
        for(int j=0;j<8;j++){
            setimState(Digital.odigital[i][j],outs[j]);
        }
    }
    public void show_analog(){
        if(Digital.out==0){
            Tbresource ts=resourceDao.find(Digital.an);
            tv_inp.setText(changeF(ts.getInp())+"");
            tv_outp.setText(changeF(ts.getOutp())+"");
            tv_opsp.setText(changeF(ts.getOpsp())+"");
            tv_inf.setText(changeF(ts.getInf())+"");
            tv_outf.setText(changeF(ts.getOutf())+"");
            tv_backf.setText(changeF(ts.getBackf())+"");

        }
        else {tv_inp.setText(0.0+"");
            tv_outp.setText(0.0+"");
            tv_opsp.setText(0.0+"");
            tv_inf.setText(0.0+"");
            tv_outf.setText(0.0+"");
            tv_backf.setText(0.0+"");

        }
    }
    private void setimState(int i, ImageView im) {
        if (i == 1) {
            im.setImageResource(R.drawable.greencircle);
        } else {
            im.setImageResource(R.drawable.redcircle);
        }
    }
    private float changeF(float f){
        //把float转换为一位小数。
        BigDecimal b=new BigDecimal(f);
        float f1=b.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
        return  f1;
    }
    class MyThread implements Runnable {
        @Override
        public void run() {
            WarnDao warnDao=new WarnDao(getActivity());
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage(0x1100);
                handler.sendMessage(msg);

            }
        }
    }


}

