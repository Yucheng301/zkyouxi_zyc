package com.zkyouxi.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.Log;


import com.zkyouxi.permission.act.PermissionActivity;
import com.zkyouxi.permission.tip.PermissionTip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PermissionUtil {
    private static final String CODE_ERROR = "requestCode is illegal";
    private static final String NOTNULL = "permissions canot be null";
    private Context context;
    private AbsPermissionListener permissionUtilListener;
    private static List<String> DENIEDDONTASK_REQUEST = new ArrayList<>();
    private static final List<String> FIREST_REQUEST = new ArrayList<>();
    private PermissionUtil instance;
    private RequestStrategy strategy;

    private PermissionUtil(Builder builder) {
        permissionUtilListener = builder.listener;
        this.context = builder.context;
        this.strategy = builder.strategy;
        if (builder.code != -1 && builder.p != null) {
            requestPermissions(builder.code, builder.p);
        }
        if (strategy == null) strategy = RequestStrategy.NORMAL;
    }

    private PermissionUtil() {
    }


    /**
     * 是否6.0以上版本
     *
     * @return 是否大于23 版本
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    /**
     * 兼容性的 权限是否存在的检测
     *
     * @param permission 要检测的权限
     * @return 是都拥有权限结果
     */
    private boolean PermissionGrantedCheck(String permission)//兼容4.4以上权限检测
    {

        boolean result = true;
        if (isOverMarshmallow()) {
            if (getTargetSdk() >= Build.VERSION_CODES.M) {
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    /**
     * 同系统层面的检测 但是只能检测23以上的
     * 这个方法会导致小米手机上出现 两次请求框 一次是 小米rom自带的 一次是系统层面的 具体原因未知
     *
     * @param permission 要检测的权限
     * @return 是否拥有
     */
    private boolean checkPermission(@NonNull String permission) {

        // TODO: 2018/12/5 0005 查找具体原因为什么会导致两次弹窗  猜测是Compat 的方法都会触发小米 爆出 rom自带的 权限请求框 前例为ActivityCompat.requestPermissions(); 会导致2次弹窗
        String op = AppOpsManagerCompat.permissionToOp(permission);
        if (TextUtils.isEmpty(op) || op == null) {
            return false;
        }
        if (AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName()) == AppOpsManagerCompat.MODE_IGNORED) {
            return false;
        }
        // TODO: 2018/12/5 0005 这里这个if警告怎么消除
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    /**
     * 同系统层面 不过这个写法支持的版本更多
     *
     * @param permissionName 要检测的权限
     * @return 权限的结果 检测可以与appopsManager.allowed 对比
     */
    private Boolean checkPermissionops(String permissionName) {
        if (!isOverMarshmallow()) {
            return true;
        }
        try {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            String opsName = AppOpsManager.permissionToOp(permissionName);
            if (opsName == null) {
                return true;
            }
            int opsMode = appOpsManager.checkOpNoThrow(opsName, Binder.getCallingUid(), context.getPackageName());
            return opsMode == AppOpsManager.MODE_ALLOWED || opsMode == 4;
        } catch (Exception ex) {
            return true;
        }

    }

    /**
     * 请求一或多权限
     * 只要进行过一次请求后
     *
     * @param requestCode 请求码 不要小于1
     * @param permissions 权限数 可变
     */
    public void requestPermissions(int requestCode, String... permissions) {
        if (getTargetSdk() < 23) {
            Log.d("UnionSDK", "sdk版本23 以下 手动调用回调");
            checkPermissionAndCallBack(requestCode, permissions).afterRequest(requestCode);
            return;
        }

        String perstr = "";
        for (String name : permissions) {
            perstr += name;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("permit_manage", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int count = sharedPreferences.getInt(perstr, 0);
        switch (strategy) {
            case ONCSPASS:
                if (count >= 1) {
                    checkPermissionAndCallBack(requestCode, permissions).afterRequest(requestCode);
                    return;
                }
                break;
            case NORMAL:
//                if (count == 1) {
//                    requestPermissions(requestCode, Permission.ACCESS_FINE_LOCATION);
//                }
                break;
            case ThirdInTenPASS:
                if (count >= 10) {
                    checkPermissionAndCallBack(requestCode, permissions).afterRequest(requestCode);
                    return;
                }
                if (count % 3 != 0) {
                    checkPermissionAndCallBack(requestCode, permissions).afterRequest(requestCode);
                    return;
                }
                break;
            case ThirdPASS:
                if (count >= 3) {
                    checkPermissionAndCallBack(requestCode, permissions).afterRequest(requestCode);
                    return;
                }
                break;
        }
        editor.putInt(perstr, ++count);
        if (requestCode <= 0) {
            throw new IllegalArgumentException(CODE_ERROR);
        }

        Activity activity = (Activity) context;
        PermissionActivity.setPermissionUtilWeakReference(this);
        Intent intent = new Intent(activity,  PermissionActivity.class);
        intent.putExtra("requestCode", requestCode);
        Bundle bundle = new Bundle();
        bundle.putStringArray("permissions", permissions);
        intent.putExtras(bundle);

//        intent.putExtra("permissions", permissions);
        activity.startActivity(intent);
        for (String p : permissions) {
            if (!FIREST_REQUEST.contains(p)) {
                FIREST_REQUEST.add(p);
            }
        }
        editor.apply();
    }


    /**
     * 将清单文件中的所有权限都请求
     *
     * @param requestCode 请求码 不要小于1
     */
    public void requestPermissions(int requestCode) {
        String[] permissions = (String[]) getManifestPermissions().toArray();
        if (permissions == null) {
            throw new NullPointerException(NOTNULL);
        }
        Activity activity = (Activity) context;

        // TODO: 2018/12/5 0005 单独写一个关于小米6.0以上的危险权限请求 总共有Permission.Group 中的 SMS   这些危险权限无法获取
        try {
            if (isOverMarshmallow()) {
                activity.requestPermissions(permissions, requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }

            for (String p : permissions) {
                if (!FIREST_REQUEST.contains(p)) {
                    FIREST_REQUEST.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * 判断是否是第一次请求
     *
     * @param permission 要检查的权限
     * @return 如果true 那就是第一次请求,
     */
    private Boolean isFirstRequest(String permission) {
        return !FIREST_REQUEST.contains(permission);
    }

    /**
     * 判断是否是被拒绝且不再询问
     *
     * @param permission 要判断的权限
     * @return 结果
     */
    @TargetApi(23)
    private Boolean isDeniedAndDonAsk(String permission) {
        // TODO: 2018/12/5 0005 需要慢慢适配
        //小米7.0以上無法正常請求SMS權限組中的权限
        if (Build.MANUFACTURER.toLowerCase().contains("xiaomi")) {
            if (Permission.READ_SMS.equals(permission) || Permission.SEND_SMS.equals(permission) || Permission.RECEIVE_SMS.equals(permission)
                    || permission.equals(Permission.RECEIVE_WAP_PUSH)
                    || permission.equals(Permission.RECEIVE_MMS)
            ) {
                return false;
            }
        }
        //需要测试几个rom  才能判断6.0以下该怎么操作
        if (!isOverMarshmallow()) {
            return true;
        }
        // TODO: 2018/12/24 0024 这里会导致6.0以下的机器崩溃
        return FIREST_REQUEST.contains(permission) && !((Activity) context).shouldShowRequestPermissionRationale(permission);
    }

    /**
     * 三重检测 兼容的检测权限是否真是的被授权
     *
     * @param p 权限名
     * @return 是否被授权
     * 为了方便使用，在该项目中破坏该函数的封装！
     */
    public Boolean checkPermissionCompatible(final String p) {
        // TODO: 2018/12/5 0005 vivo oppo 特殊的一些权限 比如相机 读写存储 需要通过实测然后捕获异常来拿到
        PermissionCheckPractical permissionCheckPractical = new PermissionCheckPractical(context);

        Boolean result = checkPermissionops(p) && PermissionGrantedCheck(p) && permissionCheckPractical.hasPermission(p);
        if (p.equals(Permission.READ_PHONE_STATE) && checkPermissionops(p) && PermissionGrantedCheck(p) && Build.VERSION.SDK_INT >= 29) {
            result = true;
        }
        return result;
    }

    public void checkPermissionsCompatible(final List<String> p) {

        // 没有同意的权限
//        String[] DENIEDDONTASK_REQUEST_ARRAY = DENIEDDONTASK_REQUEST.toArray(new String[0]);
//        String[] DENIEDDONTASK_REQUEST_ARRAY = checkPermissionAndCallBack()

        if(p.size() == 0 && DENIEDDONTASK_REQUEST.size() == 0)return;

        if (strategy == RequestStrategy.NEVERPASS) {
            if (p.size() == 0) {
                Activity activity = (Activity) context;
                List<String> tempDenied = new ArrayList<>();
                for(String px :DENIEDDONTASK_REQUEST){
                    if(!checkPermissionCompatible(px)){
                        tempDenied.add(px);
                    }
                }
                if(tempDenied.size() != 0) {
                    new PermissionTip().setContext(context)
                            .setListener(new PermissionTip.DialogActionListener() {
                                @Override
                                public void onTouch(int flag) {
                                    List<String> tempDenied = new ArrayList<>();
                                    for(String px :DENIEDDONTASK_REQUEST){
                                        if(!checkPermissionCompatible(px)){
                                            tempDenied.add(px);
                                        }
                                    }
                                    if(tempDenied.size() != 0) {
                                        PermissionSettingPage.start(context, false);
                                    }
                                    requestPermissions(Permission.REREQUESTCODE, DENIEDDONTASK_REQUEST.toArray(new String[0]));
                                }
                            }).show(activity.getFragmentManager(), "zk_permission_tip");
                }

            } else {
                requestPermissions(Permission.REREQUESTCODE, p.toArray(new String[0]));
            }

        }

    }

    /**
     * 先执行 请求权限 之后在回调中 使用此方法
     *
     * @param requestCode onrequestpermissionresult的参数 直接传入即可
     * @param permissions 同上
     */
    @TargetApi(23)
    public PermissionUtil checkPermissionAndCallBack(int requestCode, String... permissions) {
        List<String> permissionGrantedList = new ArrayList<>();
        List<String> permissionDeniedList = new ArrayList<>();
        List<String> permissionDenideDontaskList = new ArrayList<>();

        for (String p : permissions) {
            if (checkPermissionCompatible(p)) {
                permissionUtilListener.onGrantedPermission(requestCode, p);
                permissionGrantedList.add(p);
            } else {
                //发起提示
                if (isOverMarshmallow() && ((Activity) context).shouldShowRequestPermissionRationale(p)) {
                    permissionUtilListener.onShouldShowRequestPermissionRationale(requestCode, p);
                    permissionUtilListener.onDeniedPermission(requestCode, p);
                    permissionDeniedList.add(p);
                } else if (isFirstRequest(p)) {
                    permissionUtilListener.onDeniedPermission(requestCode, p);
                    permissionDeniedList.add(p);
                } else if (isDeniedAndDonAsk(p)) {
                    DENIEDDONTASK_REQUEST.add(p);
                    permissionDenideDontaskList.add(p);
                }
            }
        }
        if (permissionGrantedList.size() == permissions.length) {
            permissionUtilListener.onGrantedAllOfPermission(requestCode);
        } else if (permissionDeniedList.size() == permissions.length) {
            permissionUtilListener.onDeniedAllOfPermission(requestCode, permissionDeniedList);
        } else if (permissionDenideDontaskList.size() > 0) {
            permissionUtilListener.onDeniedPermissionAndDonAsk(permissionDenideDontaskList, requestCode);
        }
        checkPermissionsCompatible(permissionDeniedList);
        return this;
    }

    /**
     * 获取清单文件中的 所有权限请求
     *
     * @return 所有清单文件中需要的请求
     */
    private List<String> getManifestPermissions() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions == null || permissions.length == 0) {
                throw new IllegalStateException("You did not register any permissions in the manifest.xml.");
            }
            return (Arrays.asList(permissions));
        } catch (PackageManager.NameNotFoundException e) {
            throw new AssertionError("Package name cannot be found.");
        }
    }


    public void afterRequest(int code) {
        //不需要动态判断是否赋值 直接执行操作就行
        permissionUtilListener.afterRequestProcess(code);
    }


    public interface SpiceAbsPermissionListener {
        /**
         * 针对特殊rom的 特殊权限的回调 比如小米的 calendar
         */
        void onGranted();

        /**
         * 权限被拒绝的回调
         */
        void onDenied();
    }


    /**
     * 获取当前sdk版本
     *
     * @return 获得目标sdk
     */
    private int getTargetSdk() {
        //默认是19
        Integer targetSdkVersion = 19;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return targetSdkVersion;
    }

    /**
     * 建造者
     */
    public static class Builder {
        private Context context;
        private RequestStrategy strategy;
        private AbsPermissionListener listener;
        private String p[];
        private int code = -1;
        private PermissionUtil permissionUtil;

        public Builder setListener(AbsPermissionListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public Builder request(String... permission) {
            p = permission;
            return this;
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder strategy(RequestStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public PermissionUtil build() {

            if (this.context == null || listener == null) {
//                权限的申请交给 渠道sdk来处理 所以不需要listener！
//                throw new RuntimeException("Material canot be null");
            }
            if (permissionUtil == null)
                permissionUtil = new PermissionUtil(this);
            return permissionUtil;
        }
    }

    public void RequestREAD_SMSPermissionForXiaoMi(Context context, String permission, SpiceAbsPermissionListener listener) {
        // TODO: 2018/12/6 0006 成功喚起小米rom的自帶彈窗  但這樣喚起的彈窗 操作不會自動走OnrequestPermission 必須手動檢測回調
        if (listener != null) {
            if (checkPermission(permission)) {
                listener.onGranted();
            } else {
                listener.onDenied();
            }
        }
    }


}
