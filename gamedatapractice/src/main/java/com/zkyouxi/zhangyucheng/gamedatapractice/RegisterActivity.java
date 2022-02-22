package com.zkyouxi.zhangyucheng.gamedatapractice;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity {

    JSONObject jsonObject, extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });
    }

    public void postRequest(){

        String address ="https://userapi.zkyouxi.com/users";
        Log.d("HttpUtil", address);
        try {
            long b = System.currentTimeMillis() / 1000;
            int time = (int)b;
            extra = new JSONObject();
            jsonObject = new JSONObject();
            String sign = getSignature(1659615, 12842, "0",
                    "zyc1997112", "zycIsSuperCool", "android", time, "92a6c1ab-161a-48de-b05c-492f8db2fc96");
            Log.d("sign",sign);

            try {
                jsonObject.put("ad_id","1659615");
                jsonObject.put("app_id","12842");
                jsonObject.put("extra",extra);
                jsonObject.put("has_tips","0");
                jsonObject.put("login_password","zyc1997112");
                jsonObject.put("login_username","zycIsSuperCool");
                jsonObject.put("os","android");
                jsonObject.put("sign", sign);
                jsonObject.put("time", time);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("json", String.valueOf(jsonObject));



            HttpUtil.getInstance().createUrl1(address)
                    .createMediaType("application/json; charset=utf-8")
                    .setRequestParam(jsonObject).createPostRequest().createCall3().setCallBack(new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    TextView lblTitle = (TextView) findViewById(R.id.tv_result);
                    lblTitle.setText(jsonObject.toString());

                }

                @Override
                public void onFail(String msg) {
                    TextView lblTitle = (TextView) findViewById(R.id.tv_result);
                    lblTitle.setText("POST失败了bro");
                }
            }).doRequest4();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getSignature(int ad_id, int app_id,  String has_tips, String login_password, String login_username, String os, int time, String secret_key) {
        String requestData = "ad_id=" + ad_id +
                "&app_id=" + app_id +
//                "&extra="+extra+
                "&has_tips=" + has_tips +
                "&login_password=" + login_password + "&login_username=" + login_username + "&os=" + os + "&time=" + time;
        Log.d("Union",requestData);
        String signature = MD5Util.string2MD5(requestData + secret_key);
        Log.d("Union",signature);
        return signature;
    }
}