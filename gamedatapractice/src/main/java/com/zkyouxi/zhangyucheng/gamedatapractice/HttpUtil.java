package com.zkyouxi.zhangyucheng.gamedatapractice;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import zkyouxi.ohhtp3.Call;
import zkyouxi.ohhtp3.Callback;
import zkyouxi.ohhtp3.MediaType;
import zkyouxi.ohhtp3.OkHttpClient;
import zkyouxi.ohhtp3.Request;
import zkyouxi.ohhtp3.RequestBody;
import zkyouxi.ohhtp3.Response;


public class HttpUtil {

    private OkHttpClient okHttpClient;
    private volatile static HttpUtil httpUtil;
    private Request request;
    private String mUrl;
    private Call mCall;

    private HttpCallBack mCallBack;
    private RequestBody mRequestBody;
    private String mediaTypeStr = "application/json; charset=utf-8";

    private HttpUtil() {

    }

    public HttpUtil setCallBack(HttpCallBack CallBack) {
        mCallBack = CallBack;
        return httpUtil;
    }

    private void setMediaType(String mediaType) {
        this.mediaTypeStr = mediaType;
    }

    private void setCall(Call Call) {
        mCall = Call;
    }

    private void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    private void setRequest(Request request) {
        this.request = request;
    }

    public synchronized static HttpUtil getInstance() {
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

    //        String url ="";
    public HttpUtil createUrl1(String url) throws Exception {
        if (httpUtil == null) {
            throw new Exception("请不要在 getInstance 之前调用 seturl");
        }
        httpUtil.setmUrl(url);
        return this;
    }


    public HttpUtil createGetRequest2() throws Exception {
        if (mUrl == null) {
            throw new Exception("请不要在 setUrl 之前调用 createGetRequest");
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mUrl)
                .get()
                .build();

        httpUtil.setRequest(request);
        return this;
    }

    public HttpUtil createMediaType(String mediaType) throws Exception {
        this.setMediaType(mediaType);
        return this;
    }

    public HttpUtil createCall3() throws Exception {
        if (request == null) {
            throw new Exception("请不要在 createGetRequest 之前调用 createCall");
        }
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        final Call Call = okHttpClient.newCall(request);
        httpUtil.setCall(Call);
        return this;
    }

    /**
     * 获取轮播图接口
     * GET请求
     */
    public void doRequest4() {

        //6.异步请求网络enqueue(Callback)
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call Call, IOException e) {
                Log.d("HttpUtil", e.toString());
                if (mCallBack != null) {
                    mCallBack.onFail(e.toString());
                }
            }

            @Override
            public void onResponse(Call Call, Response response) throws IOException {
                Log.d("HttpUtil", "onResponse");
                String json = Objects.requireNonNull(response.body()).string();// json real content
                Log.d("HttpUtil", json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (mCallBack != null) {
                        mCallBack.onSuccess(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * post请求
     */


    public HttpUtil setRequestParam(JSONObject jsonObject){
        MediaType mType = null;
        if(mediaTypeStr != null){
            mType = MediaType.parse(mediaTypeStr);
        }mRequestBody = RequestBody.create(mType,jsonObject.toString());
        return this;
    }


    public HttpUtil createPostRequest() throws Exception {
        if (mUrl == null) {
            throw new Exception("请不要在 setUrl 之前调用 createPostRequest");
        }
        if (mRequestBody == null) {
            throw new Exception("请不要在 setRequestBody 之前调用 createPostRequest");
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mUrl)
                .post(mRequestBody)
                .build();

        httpUtil.setRequest(request);
        return this;
    }
}
