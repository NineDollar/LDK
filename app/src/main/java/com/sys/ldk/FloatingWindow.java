package com.sys.ldk;

import android.accessibilityservice.GestureDescription;
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

import androidx.appcompat.app.AlertDialog;

import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.app.AppThreas;
import com.sys.ldk.app.Startapp;
import com.sys.ldk.app.TaskThread;
import com.sys.ldk.app.function;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.easyfloat.enums.ShowPattern;
import com.sys.ldk.easyfloat.enums.SidePattern;
import com.sys.ldk.easyfloat.permission.PermissionUtils;
import com.sys.ldk.serverset.MyNotificationType;
import com.sys.ldk.xxqg.AutoRead;
import com.sys.ldk.xxqg.AutoVideo;
import com.sys.ldk.xxqg.Autoanswer;
import com.sys.ldk.xxqg.ThreadSleepTime;
import com.sys.ldk.xxqg.XXQG;

import static com.sys.ldk.serverset.Keyguard.context;

public class FloatingWindow {
    private Context mcontext;
    private AppThreas appThreas = new AppThreas();
    private Messenger mService;
    private TaskThread taskTheat = new TaskThread();
    private XXQG xxqg = new XXQG();
    public void chekPermission() {
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
    private void Floating_window() {
        EasyFloat.with(mcontext)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setGravity(Gravity.END, 0, 500)
                .setLayout(R.layout.activity_floatingwindow, View -> {
                    View.findViewById(R.id.btn_readvideo).setOnClickListener(v1 -> readandvideo());
                    View.findViewById(R.id.btn_dati).setOnClickListener(v1 -> dati());
                     View.findViewById(R.id.ivClose).setOnClickListener(v1 -> close());
                   /*View.findViewById(R.id.btn_start).setOnClickListener(v1 -> taskTheat.start());
                    View.findViewById(R.id.zhanting).setOnClickListener(v1 -> taskTheat.zhanting());
                    View.findViewById(R.id.huifu).setOnClickListener(v1 -> taskTheat.huifu());
                    View.findViewById(R.id.tingzhi).setOnClickListener(v1 -> taskTheat.tingzhi());
                    View.findViewById(R.id.btn_test).setOnClickListener(v1 -> taskTheat.test());*/
                })
                .show();
    }

    private void readandvideo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.D("打开学习强国");
                if (!xxqg.startLearning_power(context, "cn.xuexi.android", "com.alibaba.android.rimet.biz.SplashActivity")) {
                    LogUtil.E("打开学习强国失败");
                    return;
                }
                if (!xxqg.isLearning_power()) {
                    LogUtil.E("不在学习强国页面");
                    return;
                }
                AutoRead.auto_read();
                ThreadSleepTime.sleeplog();
                AutoVideo.auto_video();
                ThreadSleepTime.sleeplog();
                Autoanswer.doactivity();
            }
        }).start();
    }

    private void dati() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Autoanswer.startanswer();
            }
        }).start();
    }

    private void close() {
        EasyFloat.dismissAppFloat();
    }

    private void test() {
        LogUtil.I("点击2");

        if (Autoanswer.startanswer()) {
            LogUtil.D("答题完成，开始下一项");
        } else {
            LogUtil.D("答题失败");
        }

    }

    public ServiceConnection mConnection = new ServiceConnection() {
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

    public void doBindService() {
        Intent intent = new Intent(MyApplication.getContext(), Startapp.class);
        mcontext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    // Simulates an L-shaped drag path: 200 pixels right, then 200 pixels down.
    private void doRightThenDownDrag() {
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
