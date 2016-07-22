package com.asd.tianwang.fragment.fragmen1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.asd.tianwang.R;

/**
 * Created by ASD on 2016/7/21.
 */
public class Controlmenu extends DialogFragment {
    private Getdata mgetdata;
    private Context mContext;
    private String mStr;
    public interface Getdata {
        public void sendData(String id);
    }

    public void setMgetdata(Getdata mgetdata) {
        this.mgetdata = mgetdata;
    }

    @NonNull
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        AlertDialog.Builder builder=new AlertDialog.Builder((getActivity()));
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.control_menu,null);
        Button button=(Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mgetdata.sendData("点击了启动");
                dismiss();
            }
        });
        builder.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("返回",null);
        return builder.create();
    }

}
