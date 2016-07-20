package com.asd.tianwang.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASD on 2016/6/13.
 */
public class DBOpenHelper extends SQLiteOpenHelper{
    private static final int VERSION=1;//定义数据库版本号
    private static final String DBNAME="machine.db";//定义数据库名
    public DBOpenHelper(Context context){         //定义构造函数
        super(context,DBNAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){   //创建数据库
        db.execSQL("create table resource(id integer primary key,inp decimal(2,1),outp decimal(2,1)," +
                "opsp decimal(2,1),inf decimal(3,1),outf decimal(2,1),backf decimal(2,1),orp int)");//创建数据源表
        db.execSQL("create table history(id integer primary key,inp decimal(2,1),outp decimal(2,1)," +
                "opsp decimal(2,1),inf decimal(3,1),outf decimal(2,1),backf decimal(2,1),orp int," +
                "mtime varchar(20),mdate varchar(20))");  //创建数据存储表
        db.execSQL("create table odigital(id integer primary key,jsb bit,fxb bit,csf bit," +
                "jqf bit,zjf bit,zpf bit,fjf bit,fpf bit)");   //创建输出表
        db.execSQL("create table idigital(id integer primary key,inl bit,inh bit,outl bit," +
                "outh bit)");//创建输入表
    }
    @Override   //覆写基类的onUpgrade方法，以便数据库版本更新
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){}
}
