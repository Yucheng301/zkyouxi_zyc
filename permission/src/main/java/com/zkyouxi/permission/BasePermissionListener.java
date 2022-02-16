package com.zkyouxi.permission;

import java.util.List;

public interface BasePermissionListener {

    /**
     * 需要弹出解释框的回调
     * @param requestCode 对应请求的code
     * @param permission  对应的请求名
     */
    void onShouldShowRequestPermissionRationale(int requestCode, String permission);

    /**
     * 所有请求都被授权了的回调
     * @param requestCode 对应请求的code
     */
    void onGrantedAllOfPermission(int requestCode);

    /**
     *  所有请求都被拒绝了的回调
     * @param requestCode 对应请求的code
     * @param permissions 对应的请求名
     */
    void onDeniedAllOfPermission(int requestCode, List<String> permissions);


    /**
     * 某个请求被拒绝且不再询问的回调
     * @param permissions 对应的请求名
     * @param requestCode 对应请求的code
     */
    void onDeniedPermissionAndDonAsk(List<String> permissions, int requestCode);

    void afterRequestProcess(int requestCode);

}
