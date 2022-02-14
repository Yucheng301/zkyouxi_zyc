package com.zkyouxi.zhangyucheng.h5demo;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LittleGameDemo extends Activity {
    LinearLayout mLayout;
    WebView mWebView;
    TextView tvShowmsg;
    Button button1;
    JSONObject postJsonObject;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.little_game_demo_activity);
        initView();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("http://hgm.test.ikeyik.com/sy_sdktest/");
        mWebView.addJavascriptInterface(this, "ZKNWKT");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:getAllInfo('"+postJsonObject+"')");
            }
        });


    }

    private void initView() {
        tvShowmsg = findViewById(R.id.tv_showmsg);
        button1 = (Button) findViewById(R.id.btn1_1);
        mLayout = (LinearLayout) findViewById(R.id.container_webView);
        mWebView = new WebView(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        mLayout.addView(mWebView);

    }


    public JSONObject postMessage1(JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString("name");
        JSONObject charactor = new JSONObject();
        if (name.equals("1")) {
            charactor.put("level", 1);
            Toast.makeText(LittleGameDemo.this,charactor.toString(),Toast.LENGTH_SHORT).show();
            return charactor;
        } else if (name.equals("2")) {
            Toast.makeText(LittleGameDemo.this,charactor.toString()+", 关闭webview",Toast.LENGTH_SHORT).show();
            destroyWebView();
            return null;
        }
        return charactor;
    }

    @JavascriptInterface
    public void postMessage(String jsobject) throws JSONException {
        tvShowmsg.setText(jsobject);
        JSONObject receivedJsonObject = new JSONObject(jsobject);
        postJsonObject = postMessage1(receivedJsonObject);
    }

    @Override
    protected void onDestroy() {
        destroyWebView();
        super.onDestroy();
    }

    private void destroyWebView(){
            mLayout.removeAllViews();
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.freeMemory();
            mWebView.pauseTimers();
            mWebView = null;
        }
    }
}