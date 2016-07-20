package com.asd.tianwang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asd.tianwang.dao.table.Tbidigital;

/**
 * Created by ASD on 2016/7/19.
 */
public class IdigitalDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    public IdigitalDao(Context context){ helper=new DBOpenHelper(context);  }
    /**
     * 添加输入信息
     * */
    public void add(Tbidigital tbidigital){
        db=helper.getWritableDatabase();
        db.execSQL("insert into idigital (id,inl,inh,outl,outh) values (?,?,?,?,?)",
                new Object[]{
                      tbidigital.getId(),tbidigital.getInl(),tbidigital.getInh(),
                        tbidigital.getOutl(), tbidigital.getOuth()
                });
    }
    /**
     * 查找输入信息
     * */
    public Tbidigital find(int id){
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from idigital where id=?",new String[]{String.valueOf(id)});
        if (cursor.moveToNext()){
            return new Tbidigital(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("inl")),
                    cursor.getInt(cursor.getColumnIndex("inh")),
                    cursor.getInt(cursor.getColumnIndex("outl")),
                    cursor.getInt(cursor.getColumnIndex("outh"))
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
                .rawQuery("select count(id) from idigital", null);// 获取收入信息的记录数
        if (cursor.moveToNext())// 判断Cursor中是否有数据
        {
            return cursor.getInt(0);// 返回总记录数
        }
        return 0;// 如果没有数据，则返回0
    }
}
