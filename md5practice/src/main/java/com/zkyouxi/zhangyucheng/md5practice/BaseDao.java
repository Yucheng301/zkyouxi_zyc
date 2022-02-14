package com.zkyouxi.zhangyucheng.md5practice;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T> {

    private MySQLiteHelper mySQLiteHelper;

    public BaseDao(Context context, MySQLiteHelper mySQLiteHelper) {
        this.mySQLiteHelper = mySQLiteHelper;

    }

    public void insert(T obj) {
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        List<String> columns = getColumns();
        if (columns != null && !columns.isEmpty()) {
            for (String item : columns) {
                Object value = getColumnValue(obj, item);
                if (value != null) {
                    contentValues.put(item, String.valueOf(value));
                }
            }
            db.insert(MySQLiteHelper.TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    /**
     * 查询方法
     *
     * @return
     */
    @SuppressLint("Range")
    public List<T> query() {
       return queryWhere(null);
    }

    @SuppressLint("Range")
    public List<T> queryWhere(String where) {
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        List<T> resultList = new ArrayList<>();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_NAME, null, where, null, null, null, null);
        while (cursor.moveToNext()) {
            List<String> columns = getColumns();
            //创建泛型实例
            T obj = getInstanceOfT();
            if (columns != null && !columns.isEmpty()) {
                for (String item : columns) {
                    setColumnValue(obj, item, cursor.getString(cursor.getColumnIndex(item)));
                }
            }
            resultList.add(obj);
        }
        db.close();
        return resultList;
    }

    public void update() {
    }

    public void delete() {
    }

    /**
     * 获取需要存储到数据库的列名
     *
     * @return
     */
    public abstract List<String> getColumns();

    /**
     * 创建泛型实例
     *
     * @return
     */
    private T getInstanceOfT() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
        try {
//            PayInfo A=new PayInfo();
//            A=PayInfo.class.newInstance();
            return type.newInstance();
        } catch (Exception e) {
            // Oops, no default constructor
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取泛型实例的变量值
     *
     * @param obj
     * @param column
     * @return
     */
    private Object getColumnValue(T obj, String column) {
        try {
            Class curClass = obj.getClass();
            Field curField = curClass.getDeclaredField(column);//得到移动标示这个字段
            curField.setAccessible(true); //设置些属性是可以访问的
            return curField.get(obj);
        } catch (NoSuchFieldException ex) {
            ex.fillInStackTrace();
            return null;
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return null;
        }
    }

    /**
     * 设置泛型实例的变量值
     *
     * @param obj
     * @param column
     * @param value
     */
    private void setColumnValue(T obj, String column, Object value) {
        try {
            Class curClass = obj.getClass();
            Field curField = curClass.getDeclaredField(column);//得到移动标示这个字段
            curField.setAccessible(true); //设置些属性是可以访问的
            if (curField.getType().getName().equals(String.class.getName())) {
                curField.set(obj, value);
            } else if (curField.getType().getName().equals(Integer.class.getName()) || curField.getType().getName().equals("int")) {
                if (value != null) {
                    curField.setInt(obj, Integer.parseInt(String.valueOf(value)));
                }
            } else if (curField.getType().getName().equals(Long.class.getName()) || curField.getType().getName().equals("long")) {
                if (value != null) {
                    curField.setLong(obj, Integer.parseInt(String.valueOf(value)));
                }
            } else if (curField.getType().getName().equals(Boolean.class.getName()) || curField.getType().getName().equals("boolean")) {
                if (value != null) {
                    curField.setLong(obj, Integer.parseInt(String.valueOf(value)));
                }
            }
        } catch (NoSuchFieldException ex) {
            ex.fillInStackTrace();
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }


}
