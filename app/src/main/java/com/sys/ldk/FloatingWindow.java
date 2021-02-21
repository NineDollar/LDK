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
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.app.AppThreas;
import com.sys.ldk.app.Startapp;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.easyfloat.enums.ShowPattern;
import com.sys.ldk.easyfloat.enums.SidePattern;
import com.sys.ldk.easyfloat.permission.PermissionUtils;
import com.sys.ldk.serverset.MyNotificationType;
import com.sys.ldk.xxqg.Autoanswer;
import com.sys.ldk.xxqg.XXQG;
import com.sys.ldk.xxqg.XxqgFuntion;

public class FloatingWindow {

    private Context mcontext;
    private AppThreas appThreas = new AppThreas();
    private Messenger mService;
    private XXQG xxqg = new XXQG();
    private ImageView imageView;
    private Button button1;

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
//                            Floating_window_1();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
            alertDialog.show();
        } else {
            Floating_window();
//            Floating_window_1();
        }
    }

    //    悬浮窗
    private void Floating_window() {
        EasyFloat.with(mcontext)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setGravity(Gravity.END, 0, 800)
                .setLayout(R.layout.activity_floatingwindow, View -> {
                    View.findViewById(R.id.start).setOnClickListener(v1 -> readandvideo());
                    View.findViewById(R.id.btn_dati).setOnClickListener(v1 -> dati());
                    View.findViewById(R.id.ivClose).setOnClickListener(v1 -> close());
                 /*   View.findViewById(R.id.alltext).setOnClickListener(v1 -> getfallinfo());
                    View.findViewById(R.id.btn_test1).setOnClickListener(v1 -> test1());*/
                    button1 = View.findViewById(R.id.btn_test_3);
                    button1.setOnClickListener(v -> test3());
                    imageView = View.findViewById(R.id.im);
                    imageView.setOnClickListener(v -> im());
                    /*View.findViewById(R.id.btn_start).setOnClickListener(v1 -> DG_Thread.start());
                    View.findViewById(R.id.zhanting).setOnClickListener(v1 -> DG_Thread.zhanting());
                    View.findViewById(R.id.huifu).setOnClickListener(v1 -> DG_Thread.huifu());
                    View.findViewById(R.id.tingzhi).setOnClickListener(v1 -> DG_Thread.stop());*/
//                    View.findViewById(R.id.btn_test).setOnClickListener(v1 -> test());
                })
                .show();
    }

    private void im() {
        imageView.setImageResource(R.drawable.jinri);
    }

    private void test3() {
        button1.setText("2");
    }

    /*private void Floating_window_1() {
        EasyFloat.with(mcontext)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setGravity(Gravity.END, 0, 500)
                .setLayout(R.layout.float1, View -> {
                    imageView = View.findViewById(R.id.icon);
                    imageView.setOnClickListener(v -> Floating_window());
                })
                .show();
    }*/

    private void test2() {
        Autoanswer.doactivity();

    }

    private void test1() {
        XxqgFuntion.back();
    }

    private void getfallinfo() {
        User.getallInfo();
    }

    private void readandvideo() {
        Thread dg_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (XXQG.openxxqj(mcontext)) {
                    AcessibilityApi.performAction(AcessibilityApi.ActionType.POWER);
                }
            }
        });
        dg_thread.start();
    }


    private void dati() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Autoanswer.doactivity()) {
                    AcessibilityApi.performAction(AcessibilityApi.ActionType.POWER);
                }
            }
        }).start();
    }

    private void close() {
        EasyFloat.dismissAppFloat();
    }

    private void test() {
        Autoanswer.doactivity();
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
