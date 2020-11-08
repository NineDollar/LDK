package com.sys.ldk.apptype;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sys.ldk.MyApplication;
import com.sys.ldk.R;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.clock.ClockService;
import com.sys.ldk.power.PowerThread;
import com.sys.ldk.serverset.Binding;
import com.sys.ldk.serverset.MessengerService;
import com.sys.ldk.serverset.MyNotificationType;

/**
 * @author: Nine_Dollar
 * @date: 2020/11/5
 * @description 通过hundle启动app
 */
public class Startapp extends Service {
    private Context mcontext;

    public Startapp() {
        mcontext = MyApplication.getContext();
    }

    @Override
    public void onCreate() {
//        设置文件名
        LogUtil.setFilename(User.gettimelog()+".txt");
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            LogUtil.D("启动app");
            switch (msg.what) {
                case MyNotificationType.case3:
                    String apptype = msg.getData().getString("app");
                    LogUtil.D("apptype： " + apptype);
                    Binding binding = new Binding(MyNotificationType.case3, MyNotificationType.key3, MyNotificationType.value3);
                    binding.doBindService();
                    startappchoose(apptype);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    final Messenger mMessenger = new Messenger(new Startapp.IncomingHandler());

    /**
     * @description 启动app
     * @param 
     * @return 
     * @author Nine_Dollar
     * @time 2020/11/5 2:48
     */
    private void startappchoose(String apptype) {
        LogUtil.I("启动app： " + apptype);
        PowerThread powerThread = new PowerThread(mcontext);
        if (!powerThread.openphone()) {
            return;
        }
        User.Threadsleep500();
        Appchoose appchoose = new Appchoose(mcontext, apptype);
        appchoose.start();
    }

    /**
     * @description 停止服务
     * @param 
     * @return 
     * @author Nine_Dollar
     * @time 2020/11/5 2:48
     */
    public void doUnbindService() {

    }
}

