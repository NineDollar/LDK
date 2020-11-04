package com.sys.ldk.apptype;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.sys.ldk.MyApplication;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.power.PowerThread;

/**
 * @author: Nine_Dollar
 * @date: 2020/11/5
 * @description 通过hundle启动app
 */
public class Startapp {
    private Context mcontext;
    private String apptype;

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 30:
                    apptype = msg.getData().getString("app");
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private Startapp() {
        mcontext = MyApplication.getContext();

        this.apptype = apptype;
    }



    private void startappchoose() {
        LogUtil.I("时间到");
        PowerThread powerThread = new PowerThread(mcontext);
        if (!powerThread.openphone()) {
            return;
        }
        User.Threadsleep500();
        Appchoose appchoose = new Appchoose(mcontext, apptype);
        appchoose.start();
    }
}

