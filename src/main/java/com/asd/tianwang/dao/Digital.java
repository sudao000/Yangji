package com.asd.tianwang.dao;

/**
 * Created by ASD on 2016/7/19.
 */
public class Digital {
  public static int in,out,an;
  public static boolean isrun;
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
