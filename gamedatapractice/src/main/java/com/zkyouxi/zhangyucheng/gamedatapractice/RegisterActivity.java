package com.zkyouxi.zhangyucheng.gamedatapractice;

import android.app.Activity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity {
    
    EditText register_username, register_userpassword;
    JSONObject jsonObject, extra;
    ImageView iv_goBack, btn_cancel1, btn_cancel2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        iv_goBack = findViewById(R.id.iv_goBack);
        iv_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button registerButton = findViewById(R.id.btn_register);
        register_username = findViewById(R.id.edt_register_account);
        register_userpassword = findViewById(R.id.edt_register_password);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });
        btn_cancel1 = findViewById(R.id.btn_cancel1);
        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_username.setText("");
            }
        });
        btn_cancel2 = findViewById(R.id.btn_cancel2);
        btn_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_userpassword.setText("");
            }
        });
    }

    public void postRequest(){

        String regisAccount = register_username.getText().toString();
        String regisPassword = register_userpassword.getText().toString();
        String address ="https://userapi.zkyouxi.com/users";
        Log.d("HttpUtil", address);
        try {
            long b = System.currentTimeMillis() / 1000;
            int time = (int)b;
            extra = new JSONObject();
            jsonObject = new JSONObject();
            String sign = getSignature(1659615, 12842, "0",
                    regisPassword, regisAccount, "android", time, "92a6c1ab-161a-48de-b05c-492f8db2fc96");
            Log.d("sign",sign);

            try {
                jsonObject.put("ad_id","1659615");
                jsonObject.put("app_id","12842");
                jsonObject.put("extra",extra);
                jsonObject.put("has_tips","0");
                jsonObject.put("login_password",regisPassword);


                jsonObject.put("login_username",regisAccount);
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
                public void onSuccess(JSONObject jsonObjectCallback) {
                    Log.d("callback",jsonObjectCallback.toString());
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, jsonObjectCallback.toString(),Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onFail(String msg) {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, msg+"系统错误，请联系客服",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }).doRequest4();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getSignature(int ad_id, int app_id,  String has_tips, String login_password,
                                       String login_username, String os, int time, String secret_key) {
        String requestData = "ad_id=" + ad_id +
                "&app_id=" + app_id +
//                "&extra=" + extra +
                "&has_tips=" + has_tips +
                "&login_password=" + login_password + "&login_username=" + login_username + "&os=" + os + "&time=" + time;
        Log.d("Union",requestData);
        String signature = MD5Util.string2MD5(requestData + secret_key);
        Log.d("Union",signature);
        return signature;
    }
}