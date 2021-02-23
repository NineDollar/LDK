package com.sys.ldk.serverset;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sys.ldk.FloatingWindow;
import com.sys.ldk.MainAccessService;
import com.sys.ldk.MainActivity;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.ApiUtil;
import com.sys.ldk.clock.ClockService;
import com.sys.ldk.clock.ServiceUtil;


public class MainService extends Service {
    private Context mcontext;
    private static final String CHANNEL_ID = "10";
    public static CharSequence textTitle = "服务检查";
    public static CharSequence textContent = "检查失败";
    public int notificationId = 123;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mcontext = MainActivity.getMycontext();
        floatingwindow();
        fuzhuservice();
        naozhongserver();
        notification();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * @param
     * @return 无返回
     * @description 开始时钟服务
     * @author Nine_Dollar
     * @time 2020/11/3 1:47
     */
    public void naozhongserver() {
        if (ServiceUtil.isRunning(this, "com.sys.ldk.clock.ClockService")) {
        } else {
            startService(new Intent(mcontext, ClockService.class).putExtra("flag", "MainActivity"));
            Toast.makeText(mcontext, "开启服务", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param
     * @return
     * @description 开启无障碍
     * @author Nine_Dollar
     * @time 2020/11/3 1:50
     */
    public void fuzhuservice() {
        if (!ApiUtil.isAccessibilityServiceOn(mcontext, MainAccessService.class)) {
            User.authority_alerdialog(mcontext);
        }
    }

    /**
     * @param
     * @return
     * @description 启动通知栏客户端
     * @author Nine_Dollar
     * @time 2020/11/4 15:12
     */
    public static void notification() {
        Binding binding = new Binding(MyNotificationType.case1, MyNotificationType.key1, MyNotificationType.message1);
        binding.doBindService();
    }

    public void floatingwindow() {
//        FloatingWindow floatingWindow = new FloatingWindow();
//        floatingWindow.chekPermission();
        FloatingWindow.start_float_windows();
    }
}
