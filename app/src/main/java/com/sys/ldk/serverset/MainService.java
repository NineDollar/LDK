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
import com.sys.ldk.MainConfig;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.ApiUtil;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.clock.ClockService;
import com.sys.ldk.clock.ServiceUtil;
import com.sys.ldk.http.Http;
import com.sys.ldk.http.JsonHelper;
import com.sys.ldk.http.JsonString;

import org.json.JSONObject;


public class MainService extends Service {
    private Context mcontext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mcontext = MainActivity.getMycontext();
        fuzhuservice();
//        naozhongserver();
        notification();
//        floatingwindow();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        floatingwindow();
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
        String s = Http.get_http(MainConfig.url);
        JSONObject jsonObject = JsonHelper.getJsonObject(s);
        assert jsonObject != null;
        JsonString myjson = new JsonString(jsonObject);
        MyNotificationType.setMessagetext1(myjson.getContent());

        Binding binding = new Binding(MyNotificationType.case1, MyNotificationType.keytitle1,MyNotificationType.messagetitle1,MyNotificationType.keytext1, MyNotificationType.messagetext1);
        binding.doBindService();
    }

    public void floatingwindow() {
//        FloatingWindow floatingWindow = new FloatingWindow();
//        floatingWindow.chekPermission();
        FloatingWindow.start_float_windows();
    }
}
