package com.sys.ldk.apptype;

import android.content.Context;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.jrxy.JRXY;

import com.sys.ldk.xxqg.XXQG;

public class Appchoose extends Thread {
    private String apptype;
    private Context context;

    public Appchoose(Context context, String apptype) {
        this.apptype = apptype;
        this.context = context;
    }

    @Override
    public  void run() {
        switch (apptype) {
            case "今日校园":
                if (JRXY.openjrxy(context)) {
                    LogUtil.E("打卡成功");
                } else {
                    LogUtil.D("打卡失败");
                }
                AcessibilityApi.performAction(AcessibilityApi.ActionType.POWER);
                break;
            case "学习强国":
                if (XXQG.openxxqj(context)) {
                    LogUtil.E("学习成功");
                } else {
                    LogUtil.D("学习失败");
                }
                AcessibilityApi.performAction(AcessibilityApi.ActionType.POWER);
                break;
            case "蚂蚁森林":
        }
    }
}
