package com.zkyouxi.permission.act;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.zkyouxi.permission.Permission;
import com.zkyouxi.permission.PermissionUtil;

import java.lang.ref.WeakReference;

import static com.zkyouxi.permission.PermissionUtil.isOverMarshmallow;

public class PermissionActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int code = getIntent().getIntExtra("requestCode", 9527);
//        String[] permissions = intent.getBundleExtra("bundle").getStringArray("permissions");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            requestPermission(code, bundle.getStringArray("permissions"));
    }

    private static WeakReference<PermissionUtil> permissionUtilWeakReference = null;

    public static void setPermissionUtilWeakReference(PermissionUtil permissionUtil) {
        permissionUtilWeakReference = new WeakReference<>(permissionUtil);
    }

    private void requestPermission(int requestCode, String... permissions) {

        if (isOverMarshmallow()) {
            requestPermissions(permissions, requestCode);
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


            if (permissionUtilWeakReference != null && permissionUtilWeakReference.get() != null) {
                permissionUtilWeakReference.get()
                        .checkPermissionAndCallBack(requestCode, permissions)
                        .afterRequest(requestCode);

            } else {

            }

        finish();
    }
}
