/*
 * Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.zkyouxi.zhangyucheng.uniqueid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;




import java.lang.reflect.Method;

public class IMEIUtil {


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getIMEI(Context context) {
        String currentImei = null;
        //优先获取IMEI(即使是电信卡)  不行的话就获取MEID
        currentImei = getImeiOrMeid(context, 0);
        if(currentImei != null){
            return currentImei;
        }else{
            currentImei = getDeviceId(context);
            if(currentImei != null){
                return currentImei;
            }
        }return null;
    }


    /**
     * 获取 Imei/Meid    优先获取IMEI(即使是电信卡)  不行的话就获取MEID
     * <p>
     * 如果装有CDMA制式的SIM卡(电信卡) ，在Android 8 以下 只能获取MEID ,无法获取到该卡槽的IMEI
     * 8及以上可以通过 #imei 方法获取IMEI  通过 #deviceId 方法获取的是MEID
     *
     * @param context
     * @param slotId  slotId为卡槽Id，它的值为 0、1；
     * @return
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getImeiOrMeid(Context context, int slotId) {
        String imei = null;

        //Android 6.0 以后需要获取动态权限  检查权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return imei;
        }

        try {
            TelephonyManager manager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// android 8 即以后建议用getImei 方法获取 不会获取到MEID
                    Method method = manager.getClass().getMethod("getImei", int.class);
                    imei = (String) method.invoke(manager, slotId);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //5.0的系统如果想获取MEID/IMEI1/IMEI2  ----framework层提供了两个属性值“ril.cdma.meid"和“ril.gsm.imei"获取
                    imei = getSystemPropertyByReflect("ril.gsm.imei");
                    //如果获取不到 就调用 getDeviceId 方法获取

                } else {//5.0以下获取imei/meid只能通过 getDeviceId  方法去取
                }
            }
        } catch (Exception e) {
        }

        if (TextUtils.isEmpty(imei)) {
            imei = getDeviceId(context, slotId);
        }
        return imei;
    }






    private static String getSystemPropertyByReflect(String key) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method getMethod = clz.getMethod("get", String.class, String.class);
            return (String) getMethod.invoke(clz, key, null);
        } catch (Exception e) {/**/}
        return null;
    }

    /**
     * 获取 IMEI/MEID
     *
     * @param context 上下文
     * @return 获取到的值 或者 空串""
     */
    public static String getDeviceId(Context context) {
        String imei = null;
        //Android 6.0 以后需要获取动态权限  检查权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return imei;
        }

        // 1. 尝试通过系统api获取imei
        imei = getDeviceIdFromSystemApi(context);
        if (TextUtils.isEmpty(imei)) {
            imei = getDeviceIdByReflect(context);
        }
        return imei;
    }

    /**
     * 获取 IMEI/MEID
     *
     * @param context 上下文
     * @param slotId  slotId为卡槽Id，它的值为 0、1；
     * @return 获取到的值 或者 空串""
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getDeviceId(Context context, int slotId) {
        String imei = null;
        // 1. 尝试通过系统api获取imei
        imei = getDeviceIdFromSystemApi(context, slotId);
        if (TextUtils.isEmpty(imei)) {
            imei = getDeviceIdByReflect(context, slotId);
        }
        return imei;
    }

    /**
     * 调用系统接口获取 IMEI/MEID
     * <p>
     * Android 6.0之后如果用户不允许通过 {@link Manifest.permission#READ_PHONE_STATE} 权限的话，
     * 那么是没办法通过系统api进行获取 IMEI/MEID 的，但是可以通过{@linkplain #getDeviceIdByReflect(Context)} 反射}绕过权限进行获取
     *
     * @param context 上下文
     * @return 获取到的值 或者 空串""
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getDeviceIdFromSystemApi(Context context, int slotId) {
        String imei = null;
        try {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                imei = telephonyManager.getDeviceId(slotId);
            }
        } catch (Throwable e) {
        }
        return imei;
    }

    public static String getDeviceIdFromSystemApi(Context context) {
        String imei = null;
        try {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                imei = telephonyManager.getDeviceId();
            }
        } catch (Throwable e) {
        }
        return imei;
    }


    /**
     * 反射获取 IMEI/MEID
     * <p>
     * Android 6.0之后如果用户不允许通过 {@link Manifest.permission#READ_PHONE_STATE} 权限的话，
     * 那么是没办法通过系统api进行获取 IMEI/MEID 的，但是可以通过这个反射来尝试绕过权限进行获取
     *
     * @param context 上下文
     * @return 获取到的值 或者 空串""
     */
    public static String getDeviceIdByReflect(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= 21) {
                Method simMethod = TelephonyManager.class.getDeclaredMethod("getDefaultSim");
                Object sim = simMethod.invoke(tm);
                Method method = TelephonyManager.class.getDeclaredMethod("getDeviceId", int.class);
                return method.invoke(tm, sim).toString();
            } else {
                Class<?> clazz = Class.forName("com.android.internal.telephony.IPhoneSubInfo");
                Method subInfoMethod = TelephonyManager.class.getDeclaredMethod("getSubscriberInfo");
                subInfoMethod.setAccessible(true);
                Object subInfo = subInfoMethod.invoke(tm);
                Method method = clazz.getDeclaredMethod("getDeviceId");
                return method.invoke(subInfo).toString();
            }
        } catch (Throwable e) {

        }
        return null;
    }

    /**
     * 反射获取 deviceId
     *
     * @param context
     * @param slotId  slotId为卡槽Id，它的值为 0、1；
     * @return
     */
    public static String getDeviceIdByReflect(Context context, int slotId) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            Method method = tm.getClass().getMethod("getDeviceId", int.class);
            return method.invoke(tm, slotId).toString();
        } catch (Throwable e) {
        }
        return null;
    }
}
