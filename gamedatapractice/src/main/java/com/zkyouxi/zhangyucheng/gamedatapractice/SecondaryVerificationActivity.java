package com.zkyouxi.zhangyucheng.gamedatapractice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

public class SecondaryVerificationActivity extends Activity {

    //{"state":1,"code":"200","data":{"user_id":262657189,"authorize_code":"339a9d31-cf31-4517-8ea8-01dd47784a77"}}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_verification);

        Button postButton = findViewById(R.id.btn_secondaryVerification);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });
    }

    public void postRequest() {
        JSONObject jsonObject;
        String address = "https://userapi.zkyouxi.com/token";

        try {

            long b = System.currentTimeMillis() / 1000;
            int time = (int) b;

            jsonObject = new JSONObject();
            String sign = getSignature(12842, "46ceacbd-7a10-4755-93c4-37b543b83d22", "base", time, "68933485-b422-47f5-90f6-ab714cd205d4");
            Log.d("signature", sign);


            try {
                jsonObject.put("app_id", "12842");
                jsonObject.put("authorize_code", "46ceacbd-7a10-4755-93c4-37b543b83d22");
                jsonObject.put("scope", "base");
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

    private static String getSignature(int app_id, String authorize_code, String scope, int time, String secret_key) {
        String requestData =
                "app_id=" + app_id +
                        "&authorize_code=" + authorize_code +
                        "&scope=" + scope +
                        "&time=" + time;
        Log.d("Union", requestData);
        String signature = MD5Util.string2MD5(requestData + secret_key);
        Log.d("Union", signature);
        return signature;
    }
}