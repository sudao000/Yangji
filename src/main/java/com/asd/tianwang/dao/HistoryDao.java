package com.asd.tianwang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asd.tianwang.dao.table.Tbhistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASD on 2016/7/19.
 */
public class HistoryDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    public HistoryDao(Context context){ helper=new DBOpenHelper(context);  }
    /**
     * 添加数据信息
     * */
    public void add(Tbhistory tbhistory){
        db=helper.getWritableDatabase();
        db.execSQL("insert into history (id,inp,outp,opsp,inf,outf,backf,orp,mtime,mdate) " +
                "values (?,?,?,?,?,?,?,?,?,?)",
                new Object[]{
                        tbhistory.getId(),tbhistory.getInp(),tbhistory.getOutp(),tbhistory.getOpsp()
                        ,tbhistory.getInf(),tbhistory.getOutf(),tbhistory.getBackf(),tbhistory.getOrp()
                        ,tbhistory.getMtime(),tbhistory.getMdate()
                });
    }
    /**
     * 查找数据信息
     * */
    public List<Tbhistory> find(String date){
        List<Tbhistory> tbhistories=new ArrayList<Tbhistory>();
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from history where mdate=?",new String[]{date});
        while (cursor.moveToNext()){
            tbhistories.add( new Tbhistory(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getFloat(cursor.getColumnIndex("inp")),
                    cursor.getFloat(cursor.getColumnIndex("outp")),
                    cursor.getFloat(cursor.getColumnIndex("opsp")),
                    cursor.getFloat(cursor.getColumnIndex("inf")),
                    cursor.getFloat(cursor.getColumnIndex("outf")),
                    cursor.getFloat(cursor.getColumnIndex("backf")),
                    cursor.getInt(cursor.getColumnIndex("orp")),
                    cursor.getString(cursor.getColumnIndex("mtime")),
                    cursor.getString(cursor.getColumnIndex("mdate")))

            );

        }
      return  tbhistories;
    }
    public Tbhistory find(int id){
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from history where id=?",new String[]{String.valueOf(id)});
        if (cursor.moveToNext()){
            return new Tbhistory(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getFloat(cursor.getColumnIndex("inp")),
                    cursor.getFloat(cursor.getColumnIndex("outp")),
                    cursor.getFloat(cursor.getColumnIndex("opsp")),
                    cursor.getFloat(cursor.getColumnIndex("inf")),
                    cursor.getFloat(cursor.getColumnIndex("outf")),
                    cursor.getFloat(cursor.getColumnIndex("backf")),
                    cursor.getInt(cursor.getColumnIndex("orp")),
                    cursor.getString(cursor.getColumnIndex("mtime")),
                    cursor.getString(cursor.getColumnIndex("mdate"))
            );
        }
        return null;
    }
    /**
     * 获取总记录数
     *
     * @return
     */
    public int getCount() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db
                .rawQuery("select count(id) from history", null);// 获取收入信息的记录数
        if (cursor.moveToNext())// 判断Cursor中是否有数据
        {
            return cursor.getInt(0);// 返回总记录数
        }
        return 0;// 如果没有数据，则返回0
    }

    /**
     * 获取收入最大编号
     *
     * @return
     */
    public int getMaxId() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select max(id) from history", null);// 获取收入信息表中的最大编号
        while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
            return cursor.getInt(0);// 获取访问到的数据，即最大编号
        }
        return 0;// 如果没有数据，则返回0
    }
    /**
     * 删除所有数据
     **/
    public void deleteAll(){
        db = helper.getWritableDatabase();
        db.execSQL("delete from history");
    }

}
