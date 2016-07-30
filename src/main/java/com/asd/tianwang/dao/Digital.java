package com.asd.tianwang.dao;

import java.util.Calendar;

/**
 * Created by ASD on 2016/7/19.
 */
public class Digital {
  public static int in,out,an;
  public static boolean isrun,ischange;
  public static float limit1,limit2;
  public static String[] getTime() {
    String[] a = new String[2];
    Calendar c = Calendar.getInstance();
    String year = String.valueOf(c.get(Calendar.YEAR));
    String month = String.valueOf(c.get(Calendar.MONTH) + 1);
    String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
    String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
    String mins = String.valueOf(c.get(Calendar.MINUTE));
    String sec = String.valueOf(c.get(Calendar.SECOND));
    a[0] = hour + ":" + mins + ":" + sec;
    a[1] = year + "-" + month + "-" + day;
    return a;
  }
  public static  int idigital[][]={
            {0,0,0,0},
            {1,0,0,0},
            {1,1,0,0},
            {0,0,1,0},
            {1,0,1,0},
            {1,1,1,0},
            {0,0,1,1},
            {1,0,1,1},
            {1,1,1,1}
    };
    public static int odigital[][]={
            {1,0,1,0,0,0,0,0},
            {0,0,0,1,0,1,0,0},
            {0,1,0,1,1,1,0,0},
            {0,1,0,0,0,0,1,1},
            {0,1,0,0,1,1,0,0},
            {0,0,0,0,0,0,0,0}
    };
}
