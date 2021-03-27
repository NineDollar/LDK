package com.sys.ldk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.easyfloat.EasyFloat;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        if(intent.getAction().equals("MessengerService")){
            LogUtil.D("点击了通知栏");
            if(!EasyFloat.appFloatIsShow("1")){
                EasyFloat.showAppFloat("1");
                Toast.makeText(context, "打开悬浮窗", Toast.LENGTH_SHORT).show();
            }else {
                LogUtil.D("无需打开");
            }
        }
   }
}
