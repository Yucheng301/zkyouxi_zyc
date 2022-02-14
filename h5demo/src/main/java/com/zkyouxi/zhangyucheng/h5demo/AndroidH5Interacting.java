package com.zkyouxi.zhangyucheng.h5demo;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class AndroidH5Interacting extends Activity {
    WebView mWebView;
    Button button1;
    Button button2;
    TextView tvShowmsg;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_h5_interacting_activity);

        mWebView = (WebView) findViewById(R.id.wv);

        WebSettings webSettings = mWebView.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/javascript.html");
        mWebView.addJavascriptInterface(this, "android");

        initButton1();
        initButton2();
        tvShowmsg = findViewById(R.id.tv_showmsg);

    }

    private void initButton1() {
        button1 = (Button) findViewById(R.id.btn1_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:javacalljs()");
            }
        });
    }


    private void initButton2() {
        String name = "'Android'";
        button2 = findViewById(R.id.btn1_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:javacalljswith(" + name + ")");
            }
        });
    }

    @JavascriptInterface
    public void jsCallAndroid() {
        tvShowmsg.setText("你好哇");
    }

    @JavascriptInterface
    public void jsCallAndroidArgs(String args) {
        tvShowmsg.setText("你好哇，" + args);
    }
}