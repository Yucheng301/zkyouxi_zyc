package com.zkyouxi.zhangyucheng.md5practice;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.util.List;

public class MainActivity extends Activity {

    EditText et_user;
    EditText et_pwd;
    Button btn_regis;
    Button btn_login;
    private String username;
    private String password;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDao = new UserDao(this);
        initView();

    }

    public void initView() {
        et_user = findViewById(R.id.username_et);
        et_pwd = findViewById(R.id.password_et);
        btn_regis = findViewById(R.id.register_btn);
        btn_login = findViewById(R.id.login_btn);
        username = et_user.getText().toString();
        password = et_pwd.getText().toString();

        //跳转到注册activity
        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_user.getText().toString();
                password = et_pwd.getText().toString();
                if (username.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                login();
            }
        });
    }

    public void login() {
        List<User> result = userDao.queryWhere("username = '" + username + "'");
        Log.d("Login", result.toString());
        String encryptedPassword = MD5Util.string2MD5(password);
        if(encryptedPassword.equals(result.get(0).getPassword())){
            Toast.makeText(this,"login successful!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"login failed!",Toast.LENGTH_LONG).show();
        }


    }

}