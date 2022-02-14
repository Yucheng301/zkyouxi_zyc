package com.zkyouxi.zhangyucheng.md5practice;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends Activity {

    EditText et_user;
    EditText et_pwd;
    Button btn_regis;
    private UserDao userDao;
    private String username;
    private String pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
        userDao = new UserDao(this);
    }

    public void initView(){
        et_user = findViewById(R.id.username_et);
        et_pwd = findViewById(R.id.password_et);
        btn_regis = findViewById(R.id.regis_btn);
        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_user.getText().toString();
                pwd = et_pwd.getText().toString();
                register();
            }
        })
    ;}


    public void register(){
        String encryptedPwd = MD5Util.string2MD5(pwd);
        User user1 = new User(username,encryptedPwd);
        userDao.insert(user1);
        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
    }
}