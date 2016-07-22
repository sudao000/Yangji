package com.asd.tianwang.fragment.fragmen1;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asd.tianwang.R;

/**
 * Created by ASD on 2016/7/20.
 */
public class Frag1 extends Fragment{
    private TextView txac;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.lfra1, null, false);
        Button contral=(Button)view.findViewById(R.id.bt_cto);
        txac=(TextView)view.findViewById(R.id.tx_acc);
        contral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContral(view);
            }
        });
        return view;
    }
    public void showContral(View view){
        Controlmenu cmenu=new Controlmenu();
        cmenu.setMgetdata(new Controlmenu.Getdata() {
            @Override
            public void sendData(String id) {
                txac.setText(id);
            }
        });
        cmenu.show(getChildFragmentManager(),"操作面板");


    }
}
