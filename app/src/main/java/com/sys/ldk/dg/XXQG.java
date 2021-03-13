package com.sys.ldk.dg;

import android.content.Context;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

public class XXQG {

    public static boolean openxxqj(Context context) {
        LogUtil.D("打开学习强国");

//        返回桌面
//        UiApi.backToDesk();

//        打开学习强国
        if (!startLearning_power(context, "cn.xuexi.android", "com.alibaba.android.rimet.biz.SplashActivity")) {
            return false;
        }

//      判断是否为当前页面
        if (ThreadSleepTime.sleep3()) {
            return false;
        }
        if (!isLearning_power()) {
            return false;
        }

//        自动阅读
        if (ThreadSleepTime.sleep3()) {
            return false;
        }
        if (!AutoRead.auto_read()) {
            return false;
        }

        //      打开四川频道
        if (ThreadSleepTime.sleep3()) {
            return false;
        }
        if (!opensichuan()) {
            return false;
        }

//        视频
        if (ThreadSleepTime.sleep3()) {
            return false;
        }
        if (!AutoVideo.auto_video()) {
            return false;
        }

//      进入积分页面，自动答题
        if (ThreadSleepTime.sleep3()) {
            return false;
        }
        if (!Autoanswer.doactivity()) {
            return false;
        }

        return true;
    }

    public static boolean opensichuan() {
        LogUtil.D("点击四川频道");
        if (UiApi.clickNodeByDesWithTimeOut(2000, "工作")) {
            LogUtil.D("首页点击成功");
            if (ThreadSleepTime.sleep3()) {
                return false;
            }
            /*UiApi.clickNodeByDesWithTimeOut(2000, "工作");*/
            /*if (ThreadSleepTime.sleep3()) {
                return false;
            }*/
            if (AcessibilityApi.clickTextViewByText("四川")) {
                LogUtil.D("四川点击成功");
                if (ThreadSleepTime.sleep3()) {
                    return false;
                }
                /*AcessibilityApi.clickTextViewByText("四川");
                if (ThreadSleepTime.sleep3()) {
                    return false;
                }*/
                if (AcessibilityApi.clickTextViewByText("四川学习平台")) {
                    LogUtil.D("点击四川学习平台成功");
                    if (ThreadSleepTime.sleep3()) {
                        return false;
                    }
                    AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
                    return true;
                } else {
                    LogUtil.D("点击四川学习平台失败");
                }
            } else {
                LogUtil.D("点击四川失败");
            }
        } else {
            LogUtil.D("点击首页失败");
            return false;
        }
        return true;
    }

    public static boolean startLearning_power(Context context, String pkg, String cls) {
        try {
            if (User.startAPP(context, pkg, cls)) {
                LogUtil.D("APP启动成功");
                return true;
            } else {
                LogUtil.E("包名不正确或未安装");
                return false;
            }
        } catch (Exception e) {
            LogUtil.E(e.toString());
        }
        return false;
    }

    public static boolean isLearning_power() {
//        判断页面次数
        int frequency = 3;
//        判断是否进入软件
        while (frequency > 0) {
            if (isPage()) {
                LogUtil.I("已进入学习强国");
                return true;
            } else {
                LogUtil.E("当前不在学习强国界面");
            }
            frequency--;
            LogUtil.E("判断次数： " + frequency);
            if (ThreadSleepTime.sleep1()) {
                return false;
            }
        }
        LogUtil.D("进入学习强国失败");
        return false;
    }

    //    判断是否在学习强国首页
    public static boolean isPage() {
        String pageStr = "{maxMustMills:10000,"
                + "'maxOptionMills':10000,"
                + "'must':{'text':[],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':['cn.xuexi.android:id/comm_head_title'],'desc':[]}"
                + "}";

        return UiApi.isMyNeedPage(pageStr);
    }
}
