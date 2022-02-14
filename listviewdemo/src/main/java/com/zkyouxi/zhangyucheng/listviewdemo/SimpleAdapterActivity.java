package com.zkyouxi.zhangyucheng.listviewdemo;



import android.app.Activity;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAdapterActivity extends Activity {
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_adapter);
        listView = findViewById(R.id.lv_simple);
        initData();
        simpleAdapter = new SimpleAdapter(SimpleAdapterActivity.this,data,R.layout.simple_adapter_1,new String[]{"image","name","conversation"},new int[]{R.id.iv_img,R.id.tv_name,R.id.tv_conversation});
        listView.setAdapter(simpleAdapter);
    }

    private void initData(){
        Map map1 = new HashMap();
        map1.put("image",R.drawable.img);
        map1.put("name","狮子");
        map1.put("conversation","今夜，勾栏听曲？的反馈给介绍了当今法国喀什酱豆腐士大夫立刻后就很少可能 但是萌生了看过你函数slkejrsd/nh地方。，漫画");
        Map map2 = new HashMap();
        map2.put("image",R.drawable.th);
        map2.put("name","michel");
        map2.put("conversation","你好哇");
        data.add(map1);
        data.add(map2);


    }
}