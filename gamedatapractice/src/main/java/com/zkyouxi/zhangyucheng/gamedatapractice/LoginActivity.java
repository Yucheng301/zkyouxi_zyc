package com.zkyouxi.zhangyucheng.gamedatapractice;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zkyouxi.zhangyucheng.uniqueid.UniqueIdUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });
    }
    public void postRequest(){
        JSONObject jsonObject,extra;
        String address ="https://userapi.zkyouxi.com/authorize?_action=login_by_account";

        try {
            String uniqueID = UniqueIdUtil.getUniqueID(LoginActivity.this);
            long b = System.currentTimeMillis() / 1000;
            int time = (int)b;
            extra = new JSONObject();
            jsonObject = new JSONObject();
            String sign = getSignature(1659615, 12842,
                    "zyc1997112", "zycIsSuperCool", "base", time, "92a6c1ab-161a-48de-b05c-492f8db2fc96");
            Log.d("signature",sign);

            extra.put("imei",uniqueID);

            try {
                jsonObject.put("ad_id","1659615");
                jsonObject.put("app_id","12842");
                jsonObject.put("sign", sign);
                jsonObject.put("login_password","zyc1997112");
                jsonObject.put("login_username","zycIsSuperCool");
                jsonObject.put("scope","base");
                jsonObject.put("extra", extra);
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
                    Log.d("loginCallback", jsonObject.toString());

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

    private static String getSignature(int ad_id, int app_id, String login_password, String login_username, String scope, int time, String secret_key) {
        String requestData =
                "ad_id=" + ad_id +
                "&app_id=" + app_id +
                "&login_password=" + login_password +
                "&login_username=" + login_username +
                "&scope=" + scope +
                "&time=" + time;
        Log.d("Union",requestData);
        String signature = MD5Util.string2MD5(requestData + secret_key);
        Log.d("Union",signature);
        return signature;
    }

}