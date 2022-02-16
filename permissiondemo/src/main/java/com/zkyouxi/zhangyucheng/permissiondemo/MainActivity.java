package com.zkyouxi.zhangyucheng.permissiondemo;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zkyouxi.permission.Permission;
import com.zkyouxi.permission.PermissionListener;
import com.zkyouxi.permission.PermissionUtil;
import com.zkyouxi.permission.RequestStrategy;


public class MainActivity extends Activity {
    TextView tv_result;
    Button btn_getPermission;
    PermissionListener testPermissionListener1;
    PermissionUtil permissionUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getPermissionListener();
//        getPermission();
    }

    private void getPermissionListener() {
        testPermissionListener1 = new PermissionListener() {
            @Override
            public void onDeniedPermission(int requestCode, String permission) {
                super.onDeniedPermission(requestCode, permission);
                Toast.makeText(MainActivity.this, permission+" 权限被拒绝",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onGrantedPermission(int requestCode, String permission) {
                super.onGrantedPermission(1, "MediaStore.ACTION_IMAGE_CAPTURE");
                Toast.makeText(MainActivity.this, permission+" 权限通过",Toast.LENGTH_SHORT).show();
            }
        };
    }


    private void initView(){
        tv_result = findViewById(R.id.tv_result);
        btn_getPermission = findViewById(R.id.btn_getPermission);
        btn_getPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamara();
            }
        });
    }


    private void openCamara(){
        permissionUtil = new PermissionUtil
                .Builder()
                .setListener(testPermissionListener1)
                .with(MainActivity.this)
                .request("MediaStore.ACTION_IMAGE_CAPTURE").code(1)
                .strategy(RequestStrategy.NORMAL).build();

//        permissionUtil.requestPermissions(1);
    }

//    private void getPermission() {
//
//    }
}