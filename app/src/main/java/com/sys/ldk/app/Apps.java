package com.sys.ldk.app;

import android.content.Context;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.jrxy.JRXY;
import com.sys.ldk.QQ;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import org.jetbrains.annotations.NotNull;

public class Apps {
    public final static String xuexi = "大国";
    public final static String Qq = "QQ";
    public final static String jinri = "今日校园";
    public final static String mayi = "蚂蚁森林";
    public final static String weixin = "微信";

    public void startapp(@NotNull String appstr, Context context) {
        switch (appstr) {
            case xuexi:
                LogUtil.D("打开学习强国");
                break;
            case Qq:
                ThreadSleepTime.sleep2();

                LogUtil.D("打开QQ");
                QQ.openQQ(context);
                break;
            case jinri:
                ThreadSleepTime.sleep3();
                LogUtil.D("打开今日校园");
                JRXY.openjrxy(context);
                break;
            case weixin:

            default:
                LogUtil.E("未找到" + appstr);
        }
    }

    public static String[] getapps() {
        String[] strings = {xuexi, jinri, mayi, weixin};
        return strings;
    }

    public static String getXuexi() {
        return xuexi;
    }

    public static String getQq() {
        return Qq;
    }

    public static String getJinri() {
        return jinri;
    }

    public static String getMayi() {
        return mayi;
    }
}
