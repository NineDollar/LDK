package com.sys.ldk;

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.easyfloat.enums.ShowPattern;
import com.sys.ldk.easyfloat.enums.SidePattern;
import com.sys.ldk.easyfloat.interfaces.OnInvokeView;
import com.sys.ldk.easyfloat.permission.PermissionUtils;
import com.sys.ldk.serverset.Binding;
import com.sys.ldk.serverset.MessengerService;
import com.sys.ldk.serverset.MyNotificationType;

public class FloatingWindow {
    private static Context mcontext;

    public static void chekPermission() {
        mcontext = MainActivity.getMcontext();
        if (!PermissionUtils.checkPermission(mcontext)) {
            AlertDialog alertDialog = new AlertDialog.Builder(mcontext)
                    .setTitle("提示")
                    .setMessage("使用浮窗功能，需要您授权悬浮窗权限。")
                    .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Floating_window();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
            alertDialog.show();
        } else {
            Floating_window();
        }
    }

    //    悬浮窗
    private static void Floating_window() {
        EasyFloat.with(mcontext)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setGravity(Gravity.END, 0, 700)
                .setLayout(R.layout.activity_floatingwindow,
                        new OnInvokeView() {
                            @Override
                            public void invoke(View View) {
                                View.findViewById(R.id.btn_floatwindow).setOnClickListener(v1 -> test5());
                            }
                        })
                .show();
    }

    private static Boolean flag = true;

    private static void test5() {
        LogUtil.I("点击");

    }

}
