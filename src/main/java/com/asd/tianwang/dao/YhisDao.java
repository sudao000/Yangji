package com.asd.tianwang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asd.tianwang.dao.table.Tbyhis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASD on 2016/8/3.
 */
public class YhisDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public YhisDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    public void add(Tbyhis tbyhis) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into yhis (id,con,pre,level,mtime,mdate) " +
                        "values (?,?,?,?,?,?)",
                new Object[]{
                        tbyhis.id,tbyhis.con,tbyhis.pre,tbyhis.level,tbyhis.mtime,tbyhis.mdate
                });
    }
    public  List<Tbyhis> findAll(){
        List<Tbyhis> tbyhises=new ArrayList<Tbyhis>();
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from yhis",null);
        while (cursor.moveToNext()){
            tbyhises.add( new Tbyhis(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getFloat(cursor.getColumnIndex("con")),
                    cursor.getFloat(cursor.getColumnIndex("pre")),
                    cursor.getFloat(cursor.getColumnIndex("level")),
                    cursor.getString(cursor.getColumnIndex("mtime")),
                    cursor.getString(cursor.getColumnIndex("mdate")))
            );

        }
        return  tbyhises;
    }
    public List<Tbyhis> find(String date){
        List<Tbyhis> tbyhises=new ArrayList<Tbyhis>();
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from yhis where mdate=?",new String[]{date});
        while (cursor.moveToNext()){
            tbyhises.add( new Tbyhis(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getFloat(cursor.getColumnIndex("con")),
                    cursor.getFloat(cursor.getColumnIndex("pre")),
                    cursor.getFloat(cursor.getColumnIndex("level")),
                    cursor.getString(cursor.getColumnIndex("mtime")),
                    cursor.getString(cursor.getColumnIndex("mdate")))
            );
        }
        return  tbyhises;
    }
    public Tbyhis find(int id){
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from yhis where id=?",new String[]{String.valueOf(id)});
        if (cursor.moveToNext()){
            return new Tbyhis(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getFloat(cursor.getColumnIndex("con")),
                    cursor.getFloat(cursor.getColumnIndex("pre")),
                    cursor.getFloat(cursor.getColumnIndex("level")),
                    cursor.getString(cursor.getColumnIndex("mtime")),
                    cursor.getString(cursor.getColumnIndex("mdate"))
            );
        }
        return null;
    }
    public int getCount() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db
                .rawQuery("select count(id) from yhis", null);// 获取收入信息的记录数
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
        Cursor cursor = db.rawQuery("select max(id) from yhis", null);// 获取收入信息表中的最大编号
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
        db.execSQL("delete from yhis");
    }
    public void remove(String date){
        db = helper.getWritableDatabase();
        db.execSQL("delete  from yhis where mdate=?",new String[]{date});
    }

}
