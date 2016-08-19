package com.asd.tianwang.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Sudao on 2016/8/19.
 * 可检测文本输入完成的编辑框，前提是其父控件或同级控件可获取焦点，不然编辑完不会失去焦点。
 */
public class MyEditText extends EditText{
    private OnInputCompleteListener onInputCompleteListener;
    public MyEditText(Context context){
        super(context);
    }
    public MyEditText(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public MyEditText(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }
    @SuppressLint("NewApi")
    public MyEditText(Context context,AttributeSet attrs,int defStyleAttr,int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(!focused){
            onInputCompleteListener.onInputComplete();
        }
    }

    public void setOnInputCompleteListener(OnInputCompleteListener onInputCompleteListener) {
        this.onInputCompleteListener = onInputCompleteListener;
    }

    interface OnInputCompleteListener{
        void onInputComplete();
    }
}
