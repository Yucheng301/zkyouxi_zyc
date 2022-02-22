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


import android.content.Context;
import android.os.Build;
import android.widget.Toast;


import java.util.UUID;

import github.gzuliyujiang.oaid.DeviceID;
import github.gzuliyujiang.oaid.DeviceIdentifier;
import github.gzuliyujiang.oaid.IGetter;

public class UniqueIdUtil {
    private static String uniqueID = null;
    private static String oaid = null;

    public static String getUniqueID(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            IMEIUtil imeiUtil = new IMEIUtil();
            uniqueID = imeiUtil.getIMEI(context);
            if(uniqueID != null){
                return "IMEI: " + uniqueID;
            }
            MacUtil macUtil = new MacUtil();
            uniqueID = macUtil.getMac(context);
            if (uniqueID!=null){
                return "MAC: " + uniqueID;
            }
            obtainDeviceId(context);
            if(oaid != null){
                return "OAID: " + oaid;
            }
        }
        return "UUID: " + UUID.randomUUID().toString().replace("-","");

    }

    private static void obtainDeviceId(Context context) {

        DeviceIdentifier.getOAID(context);
        DeviceID.getOAID(context, new IGetter() {
            @Override
            public void onOAIDGetComplete(String result) {
                oaid = result;
            }

            @Override
            public void onOAIDGetError(Exception error) {
                Toast.makeText(context, "error " + error, Toast.LENGTH_LONG).show();
            }
        });
//        final StringBuilder builder = new StringBuilder();
//        builder.append("IMEI: ");
//        // 获取设备唯一标识，只支持Android 10之前的系统，需要READ_PHONE_STATE权限，可能为空
//        String imei = DeviceIdentifier.getIMEI(context);
//        if (TextUtils.isEmpty(imei)) {
//            builder.append("DID/IMEI/MEID获取失败");
//        } else {
//            builder.append(imei);
//        }
//        builder.append("\n");
//        builder.append("AndroidID: ");
//        // 获取安卓ID，可能为空
//        String androidID = DeviceIdentifier.getAndroidID(context);
//        if (TextUtils.isEmpty(androidID)) {
//            builder.append("AndroidID获取失败");
//        } else {
//            builder.append(androidID);
//        }
//        builder.append("\n");
//        builder.append("WidevineID: ");
//        // 获取数字版权管理ID，可能为空
//        String widevineID = DeviceIdentifier.getWidevineID();
//        if (TextUtils.isEmpty(widevineID)) {
//            builder.append("WidevineID获取失败");
//        } else {
//            builder.append(widevineID);
//        }
//        builder.append("\n");
//        builder.append("PseudoID: ");
//        // 获取伪造ID，根据硬件信息生成，不会为空，有大概率会重复
//        builder.append(DeviceIdentifier.getPseudoID());
//        builder.append("\n");
//        builder.append("GUID: ");
//        // 获取GUID，随机生成，不会为空
//        builder.append(DeviceIdentifier.getGUID(context));
//        builder.append("\n");
//        // 是否支持OAID/AAID
//        builder.append("supported: ").append(DeviceID.supportedOAID(context));
//        builder.append("\n");
//        builder.append("OAID: ");
//        // 获取OAID，同步调用，第一次可能为空
//        builder.append(DeviceIdentifier.getOAID(context));
//        builder.append("\n");
//        // 获取OAID/AAID，异步回调
//        oaid = DeviceIdentifier.getOAID(context);
//        DeviceID.getOAID(context, new IGetter() {
//            @Override
//            public void onOAIDGetComplete(String result) {
////                 不同厂商的OAID/AAID格式是不一样的，可进行MD5、SHA1之类的哈希运算统一
//                builder.append("OAID/AAID: ").append(result);
//                oaid = builder.toString();
//            }
//
//            @Override
//            public void onOAIDGetError(Exception error) {
//                // 获取OAID/AAID失败
//                builder.append("OAID/AAID: ").append(error);
//                oaid = builder.toString();
//            }
//        });



    }


}
