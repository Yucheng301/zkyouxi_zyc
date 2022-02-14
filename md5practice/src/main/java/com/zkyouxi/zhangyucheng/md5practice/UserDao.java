package com.zkyouxi.zhangyucheng.md5practice;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class UserDao extends BaseDao<User>{

    public UserDao(Context context)
    {
        super(context, new MySQLiteHelper(context));
    }

    @Override
    public List<String> getColumns() {
        List<String> list = new ArrayList<>();
        list.add("username");
        list.add("password");
        return list;
    }


}
