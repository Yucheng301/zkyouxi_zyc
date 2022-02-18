package com.zkyouxi.zhangyucheng.gamedatapractice;

import org.json.JSONObject;

public interface HttpCallBack {
    void onSuccess(JSONObject jsonObject);//Gson FastJson
    void onFail(String msg);
}