package com.zkyouxi.zhangyucheng.httppractice;

import org.json.JSONObject;

public interface HttpCallBack {
    void onSuccess(JSONObject jsonObject);//Gson FastJson
    void onFail(String msg);
}