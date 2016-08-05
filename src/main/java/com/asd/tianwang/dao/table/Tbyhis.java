package com.asd.tianwang.dao.table;

/**
 * Created by ASD on 2016/8/3.
 */
public class Tbyhis {
    public int id;
    public float con,pre,level;
    public String mtime,mdate;

    public Tbyhis( int id,float con, float pre, float level, String mtime,String mdate ) {
        this.con = con;
        this.id = id;
        this.level = level;
        this.mdate = mdate;
        this.mtime = mtime;
        this.pre = pre;
    }
}
