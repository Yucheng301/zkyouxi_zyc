package com.zkyouxi.zhangyucheng.listviewdemo;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

    ListView lv_day;
    Button bt_toSimple;
    private String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        bt_toSimple = findViewById(R.id.bt_toSimpleAdapter);
        lv_day = findViewById(R.id.lv_days);

        //instantiate ArrayAdapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,days);
        lv_day.setAdapter(adapter);
        bt_toSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SimpleAdapterActivity.class);
                startActivity(intent);
            }
        });



    }

}