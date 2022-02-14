package com.zkyouxi.zhangyucheng.httppractice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;


public class MainActivity extends Activity {

//    private PayInfoDao payInfoDao;
//    private PayInfoDao2 payInfoDao2;

    public static final String LAN = "address";
    private PayInfoDao3 payInfoDao3;
    private String lanStored;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit_text);
        payInfoDao3 = new PayInfoDao3(this);


        SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
        lanStored = sp.getString(LAN,"");
        editText.setText(lanStored);

        findViewById(R.id.get_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = editText.getText().toString();
                Log.d("HttpUtil", address);
                try {
                    HttpUtil.getInstance().createUrl1("http://" + address + "/get").createGetRequest2().createCall3().setCallBack(new HttpCallBack() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            TextView lblTitle = (TextView) findViewById(R.id.textView);
                            lblTitle.setText(jsonObject.toString());
                        }

                        @Override
                        public void onFail(String msg) {
                            TextView lblTitle = (TextView) findViewById(R.id.textView);
                            lblTitle.setText("GET失败了bro");
                        }
                    }).doRequest4();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.post_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = editText.getText().toString();
                Log.d("HttpUtil", address);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("order_no", "akdlsfndsankfm;dlkaf");
                    HttpUtil.getInstance().createUrl1("http://" + address + "/post").createMediaType("application/json; charset=utf-8").setRequestParam(jsonObject).createPostRequest().createCall3().setCallBack(new HttpCallBack() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            TextView lblTitle = (TextView) findViewById(R.id.textView);
                            lblTitle.setText(jsonObject.toString());
                            PayInfo payInfo1 = new Gson().fromJson(jsonObject.toString(),PayInfo.class);
                            payInfoDao3.insert(payInfo1);
                            List<PayInfo> result2 = payInfoDao3.query();
                        }

                        @Override
                        public void onFail(String msg) {
                            TextView lblTitle = (TextView) findViewById(R.id.textView);
                            lblTitle.setText("POST失败了bro");

                        }
                    }).doRequest4();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onStop() {
        SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
        sp.edit().putString(LAN,editText.getText().toString()).apply();
        super.onStop();
    }
}