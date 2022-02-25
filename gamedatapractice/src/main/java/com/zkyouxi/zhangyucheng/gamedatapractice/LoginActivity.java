package com.zkyouxi.zhangyucheng.gamedatapractice;

import android.app.Activity;
import android.content.pm.ActivityInfo;
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

public class LoginActivity extends Activity {

//    在android里通过xml布局文件寻找某个控件时，我们通常做法是通过R文件来指向
//    如： findById(R,id.xx);
//    但其实还有另外一种方法来获取控件 》》反射
//    如 ：
//    name : id的命名
//    findById(context.getResource().getIdentifier(name,"id",context.getPackageName));
//    如此类推：
//    获取string类型
//    name : string的命名
//    findById(context.getResource().getIdentifier(name,"string",context.getPackageName));

    EditText login_username, login_password;
    ImageView btn_goback, btn_cancel1, btn_cancel2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Button loginButton = findViewById(R.id.btn_login);
        login_username = findViewById(R.id.edt_login_account);
        login_password = findViewById(R.id.edt_login_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });
        btn_goback = findViewById(R.id.iv_goBack);
        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_cancel1 = findViewById(R.id.btn_cancel1);
        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_username.setText("");
            }
        });
        btn_cancel2 = findViewById(R.id.btn_cancel2);
        btn_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_password.setText("");
            }
        });
    }



    public void postRequest(){
        String postUsername = login_username.getText().toString();
        String postPassword = login_password.getText().toString();
        JSONObject jsonObject,extra;
        String address ="https://userapi.zkyouxi.com/authorize?_action=login_by_account";

        try {
            String uniqueID = UniqueIdUtil.getUniqueID(LoginActivity.this);
            long b = System.currentTimeMillis() / 1000;
            int time = (int)b;
            extra = new JSONObject();
            jsonObject = new JSONObject();
            String sign = getSignature(1659615, 12842,
                    postPassword, postUsername, "base", time, "92a6c1ab-161a-48de-b05c-492f8db2fc96");
            Log.d("signature",sign);

            extra.put("imei",uniqueID);

            try {
                jsonObject.put("ad_id","1659615");
                jsonObject.put("app_id","12842");
                jsonObject.put("sign", sign);
                jsonObject.put("login_password",postPassword);
                jsonObject.put("login_username",postUsername);
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
                public void onSuccess(JSONObject jsonObjectCallback) {
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this, jsonObjectCallback.toString(),Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }

                @Override
                public void onFail(String msg) {
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this, msg+"系统错误，请联系客服",Toast.LENGTH_SHORT).show();
                    Looper.loop();
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