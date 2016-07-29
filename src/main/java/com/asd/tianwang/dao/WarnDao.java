package com.asd.tianwang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asd.tianwang.dao.table.Tbwarn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASD on 2016/7/29.
 */
public class WarnDao {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public WarnDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    public void add(Tbwarn tw) {
        db=helper.getWritableDatabase();
        db.execSQL("insert into warn (id,type,mtime,mdate) values (?,?,?,?,?)",
                new Object[]{
                        tw.id,tw.type,tw.mtime,tw.mtime
                });

    }
    public List<Tbwarn> find(String date){
        List<Tbwarn> tbwarns=new ArrayList<Tbwarn>();
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from warn where mdate=?",new String[]{date});
        while (cursor.moveToNext()){
            tbwarns.add( new Tbwarn(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("mtime")),
                    cursor.getString(cursor.getColumnIndex("mdate")))
            );
        }
        return  tbwarns;
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
     * 获取编号
     *
     * @return
     */
    public int getMaxId() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select max(id) from wran", null);// 获取收入信息表中的最大编号
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
        db.execSQL("delete from wran");
    }

}
