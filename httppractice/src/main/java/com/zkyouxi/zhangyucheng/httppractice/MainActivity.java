package com.zkyouxi.zhangyucheng.httppractice;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;




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
                    long time1 = System.currentTimeMillis() / 1000;
                    int b = (int)time1;
//                    String s = String.valueOf(b);
//                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    String jsonString = "{'app_id':12875493, 'ad_id': 5312154, 'login_username':'zyc4513547', 'login_password':'zyc1997112','has_tips': '0', 'time': "+b +
                            ", 'os':'android', 'extra':{'imei':'SDAKNSDKGALPWRT'}, 'sign':'asda23234ad4g1lkk2os'}";
                    JSONObject jsonObject = new JSONObject(jsonString);
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("game_id", 12875493);
                    HttpUtil.getInstance().createUrl1(address).createMediaType("Content-Type: application/json; charset=utf-8").setRequestParam(jsonObject).createPostRequest().createCall3().setCallBack(new HttpCallBack() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            TextView lblTitle = (TextView) findViewById(R.id.textView);
                            lblTitle.setText(jsonObject.toString());
//                            PayInfo payInfo1 = new Gson().fromJson(jsonObject.toString(),PayInfo.class);
//                            payInfoDao3.insert(payInfo1);
//                            List<PayInfo> result2 = payInfoDao3.query();
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