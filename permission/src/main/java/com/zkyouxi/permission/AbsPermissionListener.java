package com.zkyouxi.permission;

import java.util.List;

public abstract class AbsPermissionListener  implements BasePermissionListener  {
    @Override
    public void onShouldShowRequestPermissionRationale(int requestCode, String permission) {

    }

    @Override
    public void onDeniedAllOfPermission(int requestCode, List<String> permissions) {

    }

    @Override
    public void onGrantedAllOfPermission(int requestCode) {

    }

    @Override
    public void onDeniedPermissionAndDonAsk(List<String> permissions, int requestCode) {

    }

    @Override
    public void afterRequestProcess(int requestCode) {

    }

    /**
     * 授权的回调
     * @param requestCode 对应请求的code
     * @param permission 对应的请求名
     */
    public abstract void onGrantedPermission(int requestCode, String permission);



    /**
     * 某个请求被拒绝了的回调
     * @param requestCode 对应请求的code
     * @param permission 对应的请求名
     */
    public abstract void onDeniedPermission(int requestCode, String permission);


}
