package com.asd.tianwang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asd.tianwang.R;
import com.asd.tianwang.dao.SetDao;
import com.asd.tianwang.dao.table.Tbset;
import com.asd.tianwang.depend.BaseFragment;

/**
 * Created by ASD on 2016/7/20.
 */
public class Fragment3 extends BaseFragment {
    private RelativeLayout rl;
    private SetDao sd;
    private Button bt_mr, bt_qd;
    private EditText et_cycle, et_qx, et_qy, et_fx;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fram3, null, false);
        init(view);
        bt_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mr();
            }
        });
        bt_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qd();
            }
        });
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rl.requestFocus();
                return false;
            }
        });
        return view;
    }

    public void init(View view) {
        sd = new SetDao(getActivity());
        rl=(RelativeLayout)view.findViewById(R.id.rl_003);
        bt_mr = (Button) view.findViewById(R.id.bt_mr);
        bt_qd = (Button) view.findViewById(R.id.bt_qd);
        et_cycle = (EditText) view.findViewById(R.id.et_cycle);
        et_qx = (EditText) view.findViewById(R.id.et_qx);
        et_qy = (EditText) view.findViewById(R.id.et_qy);
        et_fx = (EditText) view.findViewById(R.id.et_fx);
    }

    public void mr() {
        et_cycle.setText(20+"");
        et_qx.setText(10+"");
        et_qy.setText(10+"");
        et_fx.setText(10+"");
        Tbset tbset=new Tbset(20,10,10,10);
        sd.change(tbset);
        Toast.makeText(getActivity(), "恢复默认设置成功！", Toast.LENGTH_SHORT).show();
    }
    public void qd(){
        String s="";
        int a=Integer.parseInt(et_cycle.getText().toString());
        int b=Integer.parseInt(et_qx.getText().toString());
        int c=Integer.parseInt(et_qy.getText().toString());
        int d=Integer.parseInt(et_fx.getText().toString());
        Tbset tbset=new Tbset(a,b,c,d);
        sd.change(tbset);
        Toast.makeText(getActivity(), "设置成功！", Toast.LENGTH_SHORT).show();
        Tbset test=sd.find();
        Log.i("tset:",test.cycle+","+test.qx+","+test.qiy+","+test.fx);
    }
}
