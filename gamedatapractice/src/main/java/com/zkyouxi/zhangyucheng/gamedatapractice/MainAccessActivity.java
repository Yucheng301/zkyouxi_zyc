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

import com.zkyouxi.zhangyucheng.uniqueid.UniqueIdUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class MainAccessActivity extends Activity {
    EditText et_username, et_userpassword;
    JSONObject extra;
    ImageView iv_goBack, btn_cancel1, btn_cancel2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_access);

        iv_goBack = findViewById(R.id.iv_goBack);
        iv_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button registerButton = findViewById(R.id.btn_register);
        et_username = findViewById(R.id.edt_register_account);
        et_userpassword = findViewById(R.id.edt_register_password);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        btn_cancel1 = findViewById(R.id.btn_cancel1);
        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_username.setText("");
            }
        });
        btn_cancel2 = findViewById(R.id.btn_cancel2);
        btn_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_userpassword.setText("");
            }
        });
    }


    public void register() {
        String username = et_username.getText().toString();
        String userpassword = et_userpassword.getText().toString();

        String address = "https://userapi.zkyouxi.com/users";
        Log.d("HttpUtil", address);
        try {
            long b = System.currentTimeMillis() / 1000;
            int time = (int) b;
            extra = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            String sign = getRegisterSignature(1659615, 12842, "0",
                    userpassword, username, "android", time, "92a6c1ab-161a-48de-b05c-492f8db2fc96");
            Log.d("sign", sign);

            try {
                jsonObject.put("ad_id", "1659615");
                jsonObject.put("app_id", "12842");
                jsonObject.put("extra", extra);
                jsonObject.put("has_tips", "0");
                jsonObject.put("login_password", userpassword);


                jsonObject.put("login_username", username);
                jsonObject.put("os", "android");
                jsonObject.put("sign", sign);
                jsonObject.put("time", time);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("json", String.valueOf(jsonObject));

            HttpUtil.getInstance()
                    .createUrl1(address)
                    .createMediaType("application/json; charset=utf-8")
                    .setRequestParam(jsonObject)
                    .createPostRequest()
                    .createCall3()
                    .setCallBack(new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject jsonObjectCallback) {
                    Log.d("callback", jsonObjectCallback.toString());
                    Looper.prepare();
                    Toast.makeText(MainAccessActivity.this, jsonObjectCallback.toString(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    login();
                }

                @Override
                public void onFail(String msg) {
                    Looper.prepare();
                    Toast.makeText(MainAccessActivity.this, msg + "系统错误，请联系客服", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }).doRequest4();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login() {
        String username = et_username.getText().toString();
        String userpassword = et_userpassword.getText().toString();
        String uniqueID = UniqueIdUtil.getUniqueID(MainAccessActivity.this);
        long b = System.currentTimeMillis() / 1000;
        int time = (int) b;
        extra = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        String address = "https://userapi.zkyouxi.com/authorize?_action=login_by_account";
        String sign = getLoginSignature(1659615, 12842,
                userpassword, username, "base", time, "92a6c1ab-161a-48de-b05c-492f8db2fc96");
        Log.d("signature", sign);


        try {
            extra.put("imei", uniqueID);
            jsonObject.put("ad_id", "1659615");
            jsonObject.put("app_id", "12842");
            jsonObject.put("sign", sign);
            jsonObject.put("login_password", userpassword);
            jsonObject.put("login_username", username);
            jsonObject.put("scope", "base");
            jsonObject.put("extra", extra);
            jsonObject.put("time", time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json", String.valueOf(jsonObject));
        try {
            HttpUtil.getInstance()
                    .createUrl1(address)
                    .createMediaType("application/json; charset=utf-8")
                    .setRequestParam(jsonObject)
                    .createPostRequest()
                    .createCall3()
                    .setCallBack(new HttpCallBack() {
                        @Override
                        public void onSuccess(JSONObject jsonObjectCallback) {
                            Looper.prepare();
                            Toast.makeText(MainAccessActivity.this, jsonObjectCallback.toString(),Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onFail(String msg) {
                            Looper.prepare();
                            Toast.makeText(MainAccessActivity.this, msg+"系统错误，请联系客服",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getRegisterSignature(int ad_id, int app_id, String has_tips, String login_password,
                                               String login_username, String os, int time, String secret_key) {
        String requestData = "ad_id=" + ad_id +
                "&app_id=" + app_id +
//                "&extra=" + extra +
                "&has_tips=" + has_tips +
                "&login_password=" + login_password + "&login_username=" + login_username + "&os=" + os + "&time=" + time;
        Log.d("Union", requestData);
        String signature = MD5Util.string2MD5(requestData + secret_key);
        Log.d("Union", signature);
        return signature;
    }

    private static String getLoginSignature(int ad_id, int app_id, String login_password, String login_username, String scope, int time, String secret_key) {
        String requestData =
                "ad_id=" + ad_id +
                        "&app_id=" + app_id +
                        "&login_password=" + login_password +
                        "&login_username=" + login_username +
                        "&scope=" + scope +
                        "&time=" + time;
        Log.d("Union", requestData);
        String signature = MD5Util.string2MD5(requestData + secret_key);
        Log.d("Union", signature);
        return signature;
    }
}