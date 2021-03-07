package com.sys.ldk;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.util.ApiUtil;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.dg.Autoanswer;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.easyfloat.anim.AppFloatDefaultAnimator;
import com.sys.ldk.easyfloat.enums.ShowPattern;
import com.sys.ldk.easyfloat.enums.SidePattern;
import com.sys.ldk.easyfloat.interfaces.OnFloatCallbacks;
import com.sys.ldk.easyfloat.permission.PermissionUtils;
import com.sys.ldk.serverset.MyNotificationType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static android.content.Context.MODE_PRIVATE;

public class FloatingWindow {
    private static int clickNum = 0;
    @SuppressLint("StaticFieldLeak")
    public static Context mcontext;
    @SuppressLint("StaticFieldLeak")
    public static ImageView runimage;
    private static ConstraintLayout constraintLayout;
    private static AnimationDrawable drawable;
    @SuppressLint("StaticFieldLeak")
    private static Button btn_start;
    @SuppressLint("StaticFieldLeak")
    private static Button btn_stop;

    public static void start_float_windows() {
        mcontext = MainActivity.getMycontext();
        if (!PermissionUtils.checkPermission(mcontext)) {
            AlertDialog alertDialog = new AlertDialog.Builder(mcontext)
                    .setTitle("提示")
                    .setMessage("使用浮窗功能，需要您授权悬浮窗权限。")
                    .setPositiveButton("去开启", (dialog, which) -> {
                        Floating_windows_1();
                    })
                    .setNegativeButton("取消", null)
                    .create();
            alertDialog.show();
        } else {
            Floating_windows_1();
//            test_float();
        }
    }

    private static void Floating_windows_1() {
        EasyFloat.with(mcontext)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setTag("1")
                .setGravity(Gravity.END, 0, 500)
                .setLayout(R.layout.float1, View -> {
                    runimage = View.findViewById(R.id.icon);

                    runimage.setOnClickListener(v -> {
                        clickNum++;
                        runimage.postDelayed(() -> {
                            if (clickNum == 1) {
                                LogUtil.V("单击");
                                lick_runimage();
                            } else if (clickNum == 2) {
                                LogUtil.V("双击");
                                mcontext.startActivity(new Intent(mcontext, MainActivity.class));
                            }
                            clickNum = 0;
                        }, 300);
                    });

                    runimage.setOnLongClickListener(v -> {
                        EasyFloat.hideAppFloat("1");
                        Toast.makeText(mcontext, "隐藏悬浮窗", Toast.LENGTH_SHORT).show();
                        return true;
                    });
                })
                .registerCallbacks(new OnFloatCallbacks() {
                    @Override
                    public void createdResult(boolean isCreated, @Nullable String msg, @Nullable View view) {
                        LogUtil.V("创建第1个悬浮窗");
                    }

                    @Override
                    public void show(@NotNull View view) {
                        LogUtil.V("显示第1个悬浮窗");
                    }

                    @Override
                    public void hide(@NotNull View view) {
                        LogUtil.V("隐藏第1个悬浮窗");
                    }

                    @Override
                    public void dismiss() {
                        LogUtil.V("关闭第1个悬浮窗");
                    }

                    @Override
                    public void touchEvent(@NotNull View view, @NotNull MotionEvent event) {
//                        LogUtil.I("触摸第1个悬浮窗");
                    }

                    @Override
                    public void drag(@NotNull View view, @NotNull MotionEvent event) {
//                        LogUtil.I("拖动第1个悬浮窗");
                    }

                    @Override
                    public void dragEnd(@NotNull View view) {
                        LogUtil.V("第1个悬浮窗拖动结束");
                    }
                })
                .show();
    }

    public static void xuan_zhuan() {
        LogUtil.D("旋转");
//        回主线程
        runimage.post(() -> {
            runimage.setBackgroundResource(R.drawable.run_xml);
            drawable = (AnimationDrawable) runimage.getBackground();

            LogUtil.D("旋转开始");
            if (drawable != null) {
                drawable.start();
            }
        });
    }

    private static void lick_runimage() {
        LogUtil.V("单击");
        if (EasyFloat.appFloatIsShow("2")) {
            EasyFloat.hideAppFloat("2");
        } else {
            EasyFloat.showAppFloat("2");
            if (!EasyFloat.appFloatIsShow("2")) {
                Floating_windows_2();
            }
        }
    }

    //    悬浮窗
    private static void Floating_windows_2() {
        EasyFloat.with(mcontext)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setGravity(Gravity.END, 0, 800)
                .setAppFloatAnimator(new AppFloatDefaultAnimator())
                .setTag("2")
                .setLayout(R.layout.activity_floatingwindow, View -> {
                    constraintLayout = View.findViewById(R.id.parent_float_windows);

                    btn_start = View.findViewById(R.id.start);
                    btn_start.setOnClickListener(v1 -> start());

                    View.findViewById(R.id.btn_dati).setOnClickListener(v1 -> dati());

                    btn_stop = View.findViewById(R.id.btn_stop);
                    btn_stop.setOnClickListener(v1 -> stop());

                    View.findViewById(R.id.ivClose).setOnClickListener(v1 -> hide());
                })
                .registerCallbacks(new OnFloatCallbacks() {
                    @Override
                    public void createdResult(boolean isCreated, @Nullable String msg, @Nullable View view) {
                        LogUtil.V("创建第2个悬浮窗");
                        EasyFloat.hideAppFloat("1");
                    }

                    @Override
                    public void show(@NotNull View view) {
                        LogUtil.V("显示第2个悬浮窗");
                        EasyFloat.hideAppFloat("1");
                    }

                    @Override
                    public void hide(@NotNull View view) {
                        LogUtil.V("隐藏第2个悬浮窗");
                        EasyFloat.showAppFloat("1");
                    }

                    @Override
                    public void dismiss() {
                        LogUtil.V("关闭第2个悬浮窗");
                    }

                    @Override
                    public void touchEvent(@NotNull View view, @NotNull MotionEvent event) {
//                        LogUtil.I("触摸第2个悬浮窗");
                    }

                    @Override
                    public void drag(@NotNull View view, @NotNull MotionEvent event) {
//                        LogUtil.I("拖动第2个悬浮窗");
                        constraintLayout.setBackgroundResource(R.drawable.corners);
                    }

                    @Override
                    public void dragEnd(@NotNull View view) {
                        LogUtil.V("第2个悬浮窗拖动结束");
                        int[] location = new int[2];
//                        返回x，y坐标
                        view.getLocationOnScreen(location);
                        constraintLayout.setBackgroundResource(location[0] > 3 ? R.drawable.corners_left : R.drawable.corners_right);
                    }
                })
                .show();
    }

    private static void start() {
        if (!ApiUtil.isAccessibilityServiceOn(mcontext, MainAccessService.class)) {
            Toast.makeText(mcontext, "请开启辅助服务", Toast.LENGTH_SHORT).show();
            return;
        }
        init();
    }

    @SuppressLint("ResourceAsColor")
    private static void init() {
        LogUtil.I("---" + DG_Thread.get_modeThread());
        switch (DG_Thread.get_modeThread()) {
            case DG_Thread.runing:
                btn_start.setText("恢复");
                btn_start.setTextColor(mcontext.getResources().getColor(R.color.violet, null));
                btn_stop.setEnabled(true);
                btn_stop.setTextColor(mcontext.getResources().getColor(R.color.colorAccent, null));
                DG_Thread.zhanting();
                break;
            case DG_Thread.zan_ting:
                btn_start.setText("暂停");
                btn_start.setTextColor(mcontext.getResources().getColor(R.color.colorAccent, null));
                btn_stop.setEnabled(true);
                btn_stop.setTextColor(mcontext.getResources().getColor(R.color.colorAccent, null));
                DG_Thread.huifu();
                EasyFloat.hideAppFloat("2");
                break;

            case DG_Thread.no_run:
            case "未就绪":
                btn_start.setText("暂停");
                btn_start.setTextColor(mcontext.getResources().getColor(R.color.colorAccent, null));
                btn_stop.setEnabled(true);
                btn_stop.setTextColor(mcontext.getResources().getColor(R.color.colorAccent, null));

                EasyFloat.hideAppFloat("2");
                EasyFloat.showAppFloat("1");

                DG_Thread.start();
                break;
        }
    }


    private static void dati() {
        hide();
        new Thread(Autoanswer::startanswer).start();
    }

    public static void stop() {
//        加载悬浮窗
        AcessibilityApi.AutoKeyBoard();
        Toast.makeText(mcontext, "正在停止", Toast.LENGTH_SHORT).show();
        Floating_windows_3();
        DG_Thread.stop();
        new Thread(new Runnable() {
            int i = 20;

            @Override
            public void run() {
                while (i-- > 0) {
                    LogUtil.W("关闭第 " + i + " 次");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (DG_Thread.get_modeThread().equals(DG_Thread.no_run)) {
                        LogUtil.D("线程成功停止");
                        Message msg = Message.obtain();
                        msg.what = 50;   //标志消息的标志
                        handler1.sendMessage(msg);
                        break;
                    }

                    DG_Thread.stop();
                }
                if (i <= 0) {
                    LogUtil.W("线程关闭失败");
                }
            }
        }).start();
    }

    //    UI线程
    public static Handler handler1 = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 50:
                    LogUtil.D("更新UI");
                    btn_start.setText("开始学习");
                    btn_stop.setTextColor(mcontext.getResources().getColor(R.color.font_common_1, null));
                    btn_stop.setEnabled(false);

                    if (EasyFloat.appFloatIsShow("3")) {
                        LogUtil.I("关闭加载悬浮窗");
                        EasyFloat.dismissAppFloat("3");
                    }
                    image_stop();
                    Toast.makeText(mcontext, "停止", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private static void hide() {
        /* Floating_windows_1();*/
        EasyFloat.hideAppFloat("2");
    }

    public static ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ClockService", "连接成功");
            Messenger mService = new Messenger(service);
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

    private static void test_float() {
        EasyFloat.with(mcontext)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setGravity(Gravity.END, 0, 200)
                .setAppFloatAnimator(new AppFloatDefaultAnimator())
                .setTag("4")
                .setLayout(R.layout.test3, View -> {
                    View.findViewById(R.id.btn_test1).setOnClickListener(v ->
                            Autoanswer.doactivity()
                    );

                    View.findViewById(R.id.btn_test2).setOnClickListener(v ->
                            test2());

                    View.findViewById(R.id.btn_test3).setOnClickListener(v ->
                            DG_Thread.huifu());

                    View.findViewById(R.id.btn_test4).setOnClickListener(v ->
                            DG_Thread.stop());
                })
                .show();
    }

    private static void test2() {
        String m = "0";
        SharedPreferences sp = mcontext.getSharedPreferences("data", MODE_PRIVATE);
        m = sp.getString("password","");
        LogUtil.V("local_password: " + m);
    }


    private static void Floating_windows_3() {
        EasyFloat.with(mcontext)
                .setSidePattern(SidePattern.DEFAULT)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setGravity(Gravity.END, -290, 650)
                .setAppFloatAnimator(new AppFloatDefaultAnimator())
                .setTag("3")
                .setDragEnable(false)
                .setLayout(R.layout.float_progressbar, View -> {
                    ProgressBar progressBar = (ProgressBar) View.findViewById(R.id.progressbar);
                    progressBar.setVisibility(android.view.View.VISIBLE);
                })
                .show();
    }

    public static void image_run() {
        xuan_zhuan();
    }

    public static void image_hui_fu() {
        drawable.start();
    }

    public static void image_zan_ting() {
        drawable.stop();
    }

    public static void image_stop() {
//        回主线程
        runimage.setBackgroundResource(R.drawable.stop);
    }
}
