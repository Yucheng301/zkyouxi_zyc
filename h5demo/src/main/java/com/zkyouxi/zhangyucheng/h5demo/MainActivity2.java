package com.zkyouxi.zhangyucheng.h5demo;


import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;

import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends Activity {
    WebView webView;
    private TextView textView;
    private Button back;
    JSONObject json= null;
    JSONObject backJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        webView=findViewById(R.id.webview);
        textView=findViewById(R.id.text);
        back=findViewById(R.id.back);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://hgm.test.ikeyik.com/sy_sdktest/");

        webView.addJavascriptInterface(this,"ZKNWKT");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:getAllInfo('"+backJson+"')");
            }
        });
    }

    // @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @JavascriptInterface
    public void postMessage(String jsondata) throws JSONException {
        textView.setText(jsondata);



        //   webView.evaluateJavascript("javascript:customizeTest()", new ValueCallback<String>() {
        //  @Override
        //  public void onReceiveValue(String value) {


        json = new JSONObject(jsondata);
        String name=json.getString("name");
        String data=json.getString("data");



        if (name.equals("a")){
            Log.d("MainActivity","初始化");
            backJson=new JSONObject();
            backJson.put("level","1");




            webView.loadUrl("javascript:getAllInfo('"+backJson+"')");
            //textView.setText("初始化++++");
            Toast.makeText(MainActivity2.this,"正在准备初始化===",Toast.LENGTH_LONG);

            //Log.d("MainActivity","回调成功");
            // textView.setText("回调成功");

        }else if(name.equals("b")){
            Log.d("MainActivity","关闭游戏");
            // textView.setText("关闭游戏");
            Toast.makeText(MainActivity2.this,"关闭游戏",Toast.LENGTH_LONG);
            finish();
        }
        return;


    }
    // });





    // getName(name);

}





