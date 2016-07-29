package com.asd.tianwang.dao.table;

/**
 * Created by ASD on 2016/7/29.
 */
public class Tbwarn {
    public int id,type;
    public String mtime,mdate;

    public Tbwarn(int id, int type, String mtime, String mdate) {
        this.id = id;
        this.type = type;
        this.mtime = mtime;
        this.mdate = mdate;
    }
}
