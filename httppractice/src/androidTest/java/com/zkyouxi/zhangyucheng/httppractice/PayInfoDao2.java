package com.zkyouxi.zhangyucheng.httppractice;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class PayInfoDao2 extends BaseDao<CodeMsg> {

    public PayInfoDao2(Context context, MySQLiteHelper mySQLiteHelper) {
        super(context, mySQLiteHelper);
    }

    @Override
    public List<String> getColumns() {
        List<String> cols = new ArrayList<>();
        cols.add("code");
        cols.add("msg");
        return cols;
    }
}
