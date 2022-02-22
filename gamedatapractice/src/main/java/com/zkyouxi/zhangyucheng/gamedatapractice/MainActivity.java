package com.zkyouxi.zhangyucheng.gamedatapractice;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class MainActivity extends Activity {

    Button btn_register, btn_login;

    /*SharePreference
    public static final String LAN = "address";
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //构造
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_register = findViewById(R.id.login_button);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        btn_register = findViewById(R.id.register_button);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_register = findViewById(R.id.secondaryVerificationActivity_button);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondaryVerificationActivity.class);
                startActivity(intent);
            }
        });
    }


//    //SharePreference
//    @Override
//    protected void onStop() {
//        SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
//        sp.edit().putString(LAN,editText.getText().toString()).apply();
//        super.onStop();
//    }
//


}

