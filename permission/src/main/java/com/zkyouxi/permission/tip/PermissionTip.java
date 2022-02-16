package com.zkyouxi.permission.tip;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;




/**
 * 提示权限需要同意才能进入游戏的弹框
 * Created by apson on 2019/9/3.
 */

public class PermissionTip extends android.app.DialogFragment {
    private DialogActionListener listener;
    private Context context;
    public PermissionTip setListener(DialogActionListener listener) {
        this.listener = listener;
        return this;
    }

    public PermissionTip setContext(Context context) {
        this.context = context;
        return this;
    }

    private int getRes(String name,String flag){
        return context.getResources().getIdentifier(name, flag, context.getPackageName());
    }

   
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        View view = inflater.inflate(getRes("zk_permission_tip","layout" ),container,false);
        Button btn = view.findViewById(getRes("zk_ysdk_permission_tip_btn_id","id"));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener!=null){
                    listener.onTouch(0);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if(window == null)return;
        WindowManager windowManager = window.getWindowManager();
        WindowManager.LayoutParams params = window.getAttributes();
//        params.gravity = Gravity.BOTTOM;
        if(params.width<params.height) {
            params.width = windowManager.getDefaultDisplay().getWidth() / 5 * 4;
            params.height = windowManager.getDefaultDisplay().getHeight() / 6;
        }else {
            params.width = windowManager.getDefaultDisplay().getWidth() /3;
            params.height = windowManager.getDefaultDisplay().getHeight() / 3;
        }
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }



    public interface DialogActionListener{
        void onTouch(int flag);
    }
}