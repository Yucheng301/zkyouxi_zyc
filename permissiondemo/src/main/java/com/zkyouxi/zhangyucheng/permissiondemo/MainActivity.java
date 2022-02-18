package com.zkyouxi.zhangyucheng.permissiondemo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zkyouxi.permission.AbsPermissionListener;
import com.zkyouxi.permission.Permission;
import com.zkyouxi.permission.PermissionListener;
import com.zkyouxi.permission.PermissionUtil;
import com.zkyouxi.permission.RequestStrategy;


public class MainActivity extends Activity {

    private static final int CAMERA_PIC_REQUEST = 100;
    TextView tv_result;
    Button btn_getPermission;
    PermissionUtil permissionUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }



    private void initView(){
        tv_result = findViewById(R.id.tv_result);
        btn_getPermission = findViewById(R.id.btn_getPermission);
        btn_getPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
    }


    private void openCamera(){
        permissionUtil = new PermissionUtil
                .Builder()
                .setListener(new AbsPermissionListener() {
                    @Override
                    public void onGrantedPermission(int requestCode, String permission) {
                        Intent cameraIntent  = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivity(cameraIntent);
                    }

                    @Override
                    public void onDeniedPermission(int requestCode, String permission) {
                        Toast.makeText(MainActivity.this, "camera permission has been denied", Toast.LENGTH_SHORT).show();
                    }
                })
                .with(MainActivity.this)
                .request(Permission.CAMERA).code(CAMERA_PIC_REQUEST)
                .strategy(RequestStrategy.ThirdInTenPASS).build();

//        permissionUtil.requestPermissions(1);
    }

//    private void getPermission() {
//
//    }
}