package com.asd.tianwang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asd.tianwang.dao.table.Tbresource;

/**
 * Created by ASD on 2016/7/19.
 */
public class ResourceDao {
   private DBOpenHelper helper;
    private SQLiteDatabase db;
    public ResourceDao(Context context){ helper=new DBOpenHelper(context);  }
    /**
     * 添加数据信息
     * */
    public void add(Tbresource tbresource){
        db=helper.getWritableDatabase();
        db.execSQL("insert into resource (id,inp,outp,opsp,inf,outf,backf,orp) values (?,?,?,?,?,?,?,?)",
                new Object[]{
                       tbresource.getId(),tbresource.getInp(),tbresource.getOutp(),tbresource.getOpsp()
                        ,tbresource.getInf(),tbresource.getOutf(),tbresource.getBackf(),tbresource.getOrp()
                });
    }
    /**
     * 查找数据信息
     * */
    public Tbresource find(int id){
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from resource where id=?",new String[]{String.valueOf(id)});
        if (cursor.moveToNext()){
            return new Tbresource(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getFloat(cursor.getColumnIndex("inp")),
                    cursor.getFloat(cursor.getColumnIndex("outp")),
                    cursor.getFloat(cursor.getColumnIndex("opsp")),
                    cursor.getFloat(cursor.getColumnIndex("inf")),
                    cursor.getFloat(cursor.getColumnIndex("outf")),
                    cursor.getFloat(cursor.getColumnIndex("backf")),
                    cursor.getInt(cursor.getColumnIndex("orp"))
                    );
        }
        return null;
    }
    /**
     * 获取总记录数
     *
     */
    public int getCount() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db
                .rawQuery("select count(id) from resource", null);// 获取收入信息的记录数
        if (cursor.moveToNext())// 判断Cursor中是否有数据
        {
            return cursor.getInt(0);// 返回总记录数
        }
        return 0;// 如果没有数据，则返回0
    }

}
