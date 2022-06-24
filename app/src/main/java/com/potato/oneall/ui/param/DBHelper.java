package com.potato.oneall.ui.param;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库连接搭建
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
        super(context,"param",null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table param(id integer primary key autoincrement,name varchar(50),http varchar(50),firstload char)");
        //创建自定义网址数据库
        db.execSQL("create table time(id integer primary key autoincrement,time varchar(50))");
        //创建基础变量数据库
        db.execSQL("create table msg(id integer primary key autoincrement,msg varchar(65534))");
        db.execSQL("create table user(id integer primary key autoincrement,user varchar(50),sex varchar(5),telephone int)");

        db.execSQL("insert into param values(1,'自定义网址','https://www.test.com',1)");
        db.execSQL("insert into time values(1,'2022年1月1日 星期一')");
        db.execSQL("insert into msg values(1,'在此处写上你的备忘录')");
        db.execSQL("insert into user values(1,'User','未知','')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
