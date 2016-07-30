package com.asd.tianwang.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by ASD on 2016/6/21.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
   public int year;
   public int month;
   public int day;
    public interface OnSetTime {
        public void acceptTime(int a, int b, int c);
    }

    public void setOstm(OnSetTime ostm) {
        this.ostm = ostm;
    }

    private OnSetTime ostm;



     @Override
    public Dialog onCreateDialog(Bundle savedinstanceState){
        final Calendar c= Calendar.getInstance();
        int y=c.get(Calendar.YEAR);
        int m=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);
       // Log.i("onCreateDialog",y+"-"+m+"-"+day);
        return new DatePickerDialog(getActivity(),this,y,m,day);

    }
    @Override
    public void onDateSet(DatePicker view,int year,int monthOfYear,int dayOfMonth){
        this.year=year;
        this.month=monthOfYear+1;
        this.day=dayOfMonth;
       ostm.acceptTime(year,monthOfYear+1,dayOfMonth);
       // Log.i("timeset:","year="+this.year+",month="+this.month+",day="+this.day);

    }




}
