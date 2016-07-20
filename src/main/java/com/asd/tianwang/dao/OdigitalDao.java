package com.asd.tianwang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asd.tianwang.dao.table.Tbodigital;

/**
 * Created by ASD on 2016/7/19.
 */
public class OdigitalDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    public OdigitalDao(Context context){ helper=new DBOpenHelper(context);  }
    /**
     * 添加输入信息
     * */
    public void add(Tbodigital tbodigital){
        db=helper.getWritableDatabase();
        db.execSQL("insert into idigital (id,jsb ,fxb ,csf ,jqf ,zjf ,zpf ,fjf ,fpf ) " +
                "values (?,?,?,?,?,?,?,?,?)",
                new Object[]{
                        tbodigital.getId(),tbodigital.getJsb(),tbodigital.getFxb(),
                        tbodigital.getCsf(),tbodigital.getJqf(),tbodigital.getZjf(),
                        tbodigital.getZpf(), tbodigital.getFjf(),tbodigital.getFpf()
                });
    }
    /**
     * 查找输入信息
     * */
    public Tbodigital find(int id){
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from odigital where id=?",new String[]{String.valueOf(id)});
        if (cursor.moveToNext()){
            return new Tbodigital(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("jsb")),
                    cursor.getInt(cursor.getColumnIndex("fxb")),
                    cursor.getInt(cursor.getColumnIndex("csf")),
                    cursor.getInt(cursor.getColumnIndex("jqf")),
                    cursor.getInt(cursor.getColumnIndex("zjf")),
                    cursor.getInt(cursor.getColumnIndex("zpf")),
                    cursor.getInt(cursor.getColumnIndex("fjf")),
                    cursor.getInt(cursor.getColumnIndex("fpf"))

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
                .rawQuery("select count(id) from odigital", null);// 获取收入信息的记录数
        if (cursor.moveToNext())// 判断Cursor中是否有数据
        {
            return cursor.getInt(0);// 返回总记录数
        }
        return 0;// 如果没有数据，则返回0
    }
}