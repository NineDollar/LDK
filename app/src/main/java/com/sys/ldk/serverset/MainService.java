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
        FloatingWindow.chekPermission(mcontext);
        fuzhuservice();
        naozhongserver();
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


    public void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

//           跳转页面
            Intent intent = new Intent(mcontext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(mcontext, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(mcontext, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.user_ic_launcher_round)
                    .setContentTitle(textTitle)
                    .setContentText(textContent)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
                    .setContentIntent(pendingIntent)
//                    只响一次声音
//                    .setOnlyAlertOnce(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

//            显示通知
            NotificationManagerCompat Manager = NotificationManagerCompat.from(this);
            Manager.notify(notificationId, builder.build());
        }
    }
}
