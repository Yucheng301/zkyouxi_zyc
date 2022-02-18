package com.zkyouxi.zhangyucheng.uniqueid;


import android.Manifest;
import android.app.Activity;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import github.gzuliyujiang.oaid.DeviceID;
import github.gzuliyujiang.oaid.DeviceIdentifier;
import github.gzuliyujiang.oaid.IGetter;


public class MainActivity extends Activity {
    boolean permission;
    private static int REQUEST_READ_PHONE_STATE = 9527;
    TextView tv_result;
    String oaid;
    String imei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        Button imeiButton = findViewById(R.id.btn_IMEI);
         tv_result = findViewById(R.id.tv_result);
        Button oaidButton = findViewById(R.id.btn_OAID);
        imeiButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
        oaidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOaid();
            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getIMEI(){
         imei = IMEIUtil.getIMEI(MainActivity.this);
        if(imei != null){
            tv_result.setText("IMEI: " + imei);
            copy("imei",imei);
            Toast.makeText(MainActivity.this, "已自动复制imei到粘贴版", Toast.LENGTH_SHORT).show();
        }else{
            tv_result.setText("当前设备安卓版本无法获取IMEI");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            permission = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            getIMEI();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getIMEI();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                tv_result.setText("请开启权限");
                //这里表示申请权限后被用户拒绝了
            } else {
                tv_result.setText("permission is not granted after requested！");
            }
        }

    }

    private void getOaid() {
        // 获取OAID，同步调用，第一次可能为空
         oaid = DeviceIdentifier.getOAID(this);
        if (oaid != null){
            tv_result.setText("oaid: " + oaid);
            copy("oaid","oaid: " + oaid);
            Toast.makeText(MainActivity.this, "已自动复制oaid到粘贴版", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取OAID/AAID，异步回调
        DeviceID.getOAID(this, new IGetter() {
            @Override
            public void onOAIDGetComplete(String result) {
                oaid = result;
                tv_result.append("oaid: " + oaid);
                copy("oaid","oaid: " + oaid);
                Toast.makeText(MainActivity.this, "已自动复制oaid到粘贴版", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOAIDGetError(Exception error) {
                // 获取OAID/AAID失败
               tv_result.append("oaid获取失败： " + error);
            }
        });
    }

    private void copy(String label, String text){
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText("text to clip");
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);
        }
    }

}