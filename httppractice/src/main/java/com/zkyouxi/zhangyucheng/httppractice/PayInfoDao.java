package com.zkyouxi.zhangyucheng.httppractice;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PayInfoDao {

    private MySQLiteHelper mySQLiteHelper;

    public PayInfoDao(Context context) {
        this.mySQLiteHelper = new MySQLiteHelper(context);
        Log.d("PayInfoDao", "构建sqlHelper");

    }

    public void insert(CodeMsg codeMsg) {
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("code", 200);
        contentValues.put("msg", "GET 返回");

        db.insert(MySQLiteHelper.TABLE_NAME, null, contentValues);
    }

    public void delete() {
    }

    @SuppressLint("Range")
    public List<CodeMsg> query() {
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        List<CodeMsg> resultList = new ArrayList<>();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            CodeMsg codeMsg = new CodeMsg();
            codeMsg.setCode(cursor.getString(cursor.getColumnIndex("code")));
            codeMsg.setMsg(cursor.getString(cursor.getColumnIndex("msg")));
            resultList.add(codeMsg);
        }
        return resultList;
    }

    public void update() {
    }
}
