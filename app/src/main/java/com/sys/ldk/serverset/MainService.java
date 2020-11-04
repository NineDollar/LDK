package com.sys.ldk.serverset;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sys.ldk.FloatingWindow;
import com.sys.ldk.MainAccessService;
import com.sys.ldk.MainActivity;
import com.sys.ldk.MyApplication;
import com.sys.ldk.R;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.ApiUtil;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.clock.ClockService;
import com.sys.ldk.clock.ServiceUtil;
import com.sys.ldk.easyfloat.EasyFloat;
import com.sys.ldk.easyfloat.enums.ShowPattern;
import com.sys.ldk.easyfloat.enums.SidePattern;
import com.sys.ldk.easyfloat.interfaces.OnInvokeView;
import com.sys.ldk.easyfloat.permission.PermissionUtils;


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
        mcontext = MainActivity.getMcontext();
        FloatingWindow.chekPermission();
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
     * @description 启动通知栏客户端
     * @param
     * @return
     * @author Nine_Dollar
     * @time 2020/11/4 15:12
     */
    public void notification() {
        Binding binding = new Binding(MyNotificationType.case1,MyNotificationType.key1,MyNotificationType.value1);
        binding.doBindService();
    }
}
