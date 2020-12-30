package com.sys.ldk;

import android.accessibilityservice.GestureDescription;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Path;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.apptype.Startapp;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.easyfloat.enums.ShowPattern;
import com.sys.ldk.easyfloat.enums.SidePattern;
import com.sys.ldk.easyfloat.interfaces.OnInvokeView;
import com.sys.ldk.easyfloat.permission.PermissionUtils;
import com.sys.ldk.sdcard.SaveLog;
import com.sys.ldk.serverset.MyNotificationType;
import com.sys.ldk.weixin.WXDK;
import com.sys.ldk.xxqg.Autoanswer;

import java.util.List;

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
                                View.findViewById(R.id.btn_floatwindow).setOnClickListener(v1 -> test());
                            }
                        })
                .show();
    }
    private static Messenger mService;

    private static void test() {
        LogUtil.I("点击2");

        if (Autoanswer.startanswer()) {
            LogUtil.D("答题完成，开始下一项");
        } else {
            LogUtil.D("答题失败");
        }

//        WXDK.startdgsx();
//        doRightThenDownDrag();
       /* List<AccessibilityNodeInfo> list = AcessibilityApi.getAllNode(null, null);
        for (AccessibilityNodeInfo a:list
             ) {
            LogUtil.I(""+a+"\n");
        }*/
//        doBindService();
    }

    public static ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ClockService", "连接成功");
            mService = new Messenger(service);
            Message message = Message.obtain(null, MyNotificationType.case3);
            Bundle bundle = new Bundle();
            bundle.putString("app", "test");
            message.setData(bundle);
            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ClockService", "连接失败");
        }
    };

    public static void doBindService() {
        Intent intent = new Intent(MyApplication.getContext(), Startapp.class);
        mcontext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    // Simulates an L-shaped drag path: 200 pixels right, then 200 pixels down.
    private static void doRightThenDownDrag() {
        Path dragRightPath = new Path();
        dragRightPath.moveTo(200, 200);
        dragRightPath.lineTo(400, 200);
        long dragRightDuration = 500L; // 0.5 second

        // The starting point of the second path must match
        // the ending point of the first path.
        Path dragDownPath = new Path();
        dragDownPath.moveTo(400, 200);
        dragDownPath.lineTo(400, 400);
        long dragDownDuration = 500L;
        GestureDescription.StrokeDescription rightThenDownDrag =
                new GestureDescription.StrokeDescription(dragRightPath, 0L,
                        dragRightDuration, true);
        rightThenDownDrag.continueStroke(dragDownPath, dragRightDuration,
                dragDownDuration, false);
    }

}
