package com.asd.tianwang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asd.tianwang.dao.table.Tbset;

/**
 * Created by ASD on 2016/7/29.
 */
public class SetDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public SetDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    public void change(Tbset ts) {
        db = helper.getWritableDatabase();
        db.execSQL("update setting set cycle=?,qx_time=?,qy_time=?,fx_time=? where id=0",
                new Object[]{
                        ts.cycle, ts.qx, ts.qiy, ts.fx
                });
    }
    public void add(Tbset ts){
        db = helper.getWritableDatabase();
        db.execSQL(
                "insert into setting (id,cycle,qx_time,qy_time,fx_time) values (?,?,?,?,?)",
                new Object[]{
                       0,ts.cycle,ts.qx,ts.qiy,ts.fx
                });
    }

    public Tbset find() {
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select cycle,qx_time,qy_time,fx_time from setting where id=?",
                new String[]{String.valueOf(0)});// 根据编号查找收入信息，并存储到Cursor类中

        if (cursor.moveToNext())// 遍历查找到的收入信息
        {
            Tbset tb = new Tbset(
            // 将遍历到的收入信息存储到Tb_inaccount类中
             cursor.getInt(cursor.getColumnIndex("cycle")),
             cursor.getInt(cursor.getColumnIndex("qx_time")),
             cursor.getInt(cursor.getColumnIndex("qy_time")),
             cursor.getInt(cursor.getColumnIndex("fx_time")));
            return tb;
        }
        return null;// 如果没有信息，则返回null
    }
}
