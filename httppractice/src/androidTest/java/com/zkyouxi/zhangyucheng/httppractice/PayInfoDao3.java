package com.zkyouxi.zhangyucheng.httppractice;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class PayInfoDao3 extends BaseDao<PayInfo> {


    public PayInfoDao3(Context context) {
        super(context, new MySQLiteHelper(context));
    }

    @Override
    public List<String> getColumns() {
        List<String> cols = new ArrayList<>();
        cols.add("out_trade_no");
        cols.add("total_charge");
        cols.add("access_token");
        cols.add("async_callback_url");
        cols.add("product_id");
        cols.add("product_name");
        cols.add("product_amount");
        cols.add("product_desc");
        cols.add("rate");
        cols.add("role_id");
        cols.add("role_name");
        cols.add("server_id");
        cols.add("extend");
        cols.add("union_extend");
        cols.add("currency_code");
        cols.add("pay_type");
        cols.add("order_no");
        return cols;
    }

}
