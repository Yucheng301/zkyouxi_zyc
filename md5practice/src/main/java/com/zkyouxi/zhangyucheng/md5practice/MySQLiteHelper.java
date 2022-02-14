package com.zkyouxi.zhangyucheng.md5practice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MySQLiteHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "USER_INFO";//数据库名称
    public final static String TABLE_NAME = "login_credential";//表名
    private final static int VERSION = 1;//当前版本

    String sql="create table if not exists " + TABLE_NAME + "(id integer primary key autoincrement,"+"username text,"+"password text)";
    //参数分别是：上下文，数据库名，游标工厂，数据库版本
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //数据库第一次创建时调用的函数
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("MySQLiteHelper","onCreate");
        db.execSQL(sql);
    }

    //数据库版本版本号增加时调用的函数
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("hc", "onUpgrade: 版本更新了！");
    }

}