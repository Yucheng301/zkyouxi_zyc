package com.zkyouxi.zhangyucheng.gamedatapractice;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class MainActivity extends Activity {
    EditText editText;
    Button button;
    private String lanStored;
    public static final String LAN = "address";
    String requestStrSample = "app_id="+12842 +"&ad_id="+1659615+"&login_username="
            +"zycIsSuperCool"+"&login_password="+"zyc1997112"+"&has_tips="+"0"
            +"&os="+"android"+"&extra="+"{}"+"&time="+2020021813+"92a6c1ab-161a-48de-b05c-492f8db2fc96";
    String signSample = MD5Util.string2MD5(requestStrSample);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit_text);
        button = findViewById(R.id.get_button);
        SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
        lanStored = sp.getString(LAN,"");
        button.findViewById(R.id.post_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRequest();
            }
        });
    }

    private void postRequest(){
        String address = editText.getText().toString();
        Log.d("HttpUtil", address);
        try {
            long time1 = System.currentTimeMillis() / 1000;
            int b = (int)time1;
//                    String s = String.valueOf(b);
//                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            String jsonString = "{'app_id':12842, 'ad_id': 1659615, 'login_username':'zycIsSuperCool', 'login_password':'zyc1997112','has_tips': '0', 'time': "+2020021813 +
                    ", 'os':'android', 'extra':{}, 'sign':'"+signSample+"'}";
            JSONObject jsonObject = new JSONObject(jsonString);

            HttpUtil.getInstance().createUrl1(address).createMediaType("Content-Type: application/json; charset=utf-8").setRequestParam(jsonObject).createPostRequest().createCall3().setCallBack(new HttpCallBack() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    TextView lblTitle = (TextView) findViewById(R.id.textView);
                    lblTitle.setText(jsonObject.toString());

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




    @Override
    protected void onStop() {
        SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
        sp.edit().putString(LAN,editText.getText().toString()).apply();
        super.onStop();
    }
}

