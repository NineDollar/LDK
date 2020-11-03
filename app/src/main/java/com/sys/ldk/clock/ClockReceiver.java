package com.sys.ldk.clock;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sys.ldk.accessibility.util.LogUtil;

public class ClockReceiver extends BroadcastReceiver {

    private int code;
    @Override
    public void onReceive(Context context, Intent intent) {
        code = intent.getIntExtra("requestcode", 0);
//            启动服务
        Intent intent1 = new Intent(context, ClockService.class);
        intent1.putExtra("flag", "ClockReceiver");
        intent1.putExtra("code", code + "");
        context.startService(intent1);
    }

}
