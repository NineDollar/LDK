package com.sys.ldk.dg;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;

import com.sys.ldk.MainActivity;
import com.sys.ldk.http.Http;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.http.JsonHelper;
import com.sys.ldk.http.JsonString;
import com.sys.ldk.serverset.Binding;
import com.sys.ldk.serverset.MainService;
import com.sys.ldk.serverset.MyNotificationType;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SandTimer {
    private Handler handler = new Handler();

    public void timerRun() {
        // 一天的毫秒数
        long daySpan = 24 * 60 * 60 * 1000;
//        long daySpan = 10 * 1000;
        // 规定的每天时间15:33:30运行
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 07:00:00");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 07:00:00");
        // 首次运行时间
        try {
            @SuppressLint("SimpleDateFormat") Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
            // 如果今天的已经过了 首次运行时间就改为明天
            if (System.currentTimeMillis() > startTime.getTime()) {
                startTime = new Date(startTime.getTime() + daySpan);
            }
            Timer t = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    LogUtil.D("定时器启动");
                    handler.post(runnable);
                }
            };
            // 以每24小时执行一次
            t.schedule(task, startTime, daySpan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Runnable runnable = () -> {
        MyNotificationType.setMessagetitle1("还未学习");
        MainService.notification();
    };
}