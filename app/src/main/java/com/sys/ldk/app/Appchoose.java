package com.sys.ldk.app;

import android.content.Context;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.jrxy.JRXY;

import com.sys.ldk.weixin.WXDK;
import com.sys.ldk.dg.XXQG;

public class Appchoose extends Thread {
    private String apptype;
    private Context context;

    public Appchoose(Context context, String apptype) {
        this.apptype = apptype;
        this.context = context;
    }

    @Override
    public void run() {
        switch (apptype) {
            case "今日校园":
                if (JRXY.openjrxy(context)) {
                    LogUtil.D("打卡成功");
                } else {
                    LogUtil.E("打卡失败");
                }
                AcessibilityApi.performAction(AcessibilityApi.ActionType.POWER);
                break;
            case "学习强国":
                if (XXQG.openxxqj(context)) {
                    LogUtil.D("学习成功");
                } else {
                    LogUtil.E("学习失败");
                }
                AcessibilityApi.performAction(AcessibilityApi.ActionType.POWER);
                break;
            case "微信实习打卡":
                if(WXDK.startdgsx()){
                    LogUtil.D("打卡成功");
                }else {
                    LogUtil.E("打卡失败");
                }
//                AcessibilityApi.performAction(AcessibilityApi.ActionType.POWER);
                break;
            case  "test":
                test();
                break;
        }
    }
    private void test(){
        User.getallInfottext(true);
    }
}
