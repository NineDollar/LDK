package com.sys.ldk.apptype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.power.PowerThread;
public class StartAppReceiver extends BroadcastReceiver {
    private String apptype;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.sys.ldk")) {
            apptype = intent.getStringExtra("apptype");
            LogUtil.E("接收到广播打开: "+apptype);
            startappchoose(context,apptype);
        }
    }

    private void startappchoose(Context context, String apptype){
        LogUtil.I("时间到");
        PowerThread powerThread = new PowerThread(context);
        if(!powerThread.openphone()){
            return;
        }
        User.Threadsleep500();
        Appchoose appchoose = new Appchoose(context,apptype);
        appchoose.start();
    }
}
