package com.sys.ldk.dg;

import android.content.Context;
import android.util.Log;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.Objects;

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
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        if (!isLearning_power()) {
            return false;
        }

//        自动阅读
       /* if (ThreadSleepTime.sleep1()) {
            return false;
        }*/
        if (LdkConfig.isRead()) {
            if (!AutoRead.auto_read()) {
                return false;
            }
        }

        //      打开四川频道
        if(LdkConfig.isSichuan()){
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            if (!opensichuan()) {
                return false;
            }
        }

//        视频
        if(LdkConfig.isVideo()){
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            if (!AutoVideo.auto_video()) {
                return false;
            }
        }

        //        联播频道
        if (LdkConfig.isXin_wen_lian_bo()) {
            LogUtil.D("开始观看新闻联播");
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            if (!AcessibilityApi.clickTextViewByText("联播频道")) {
                return false;
            }
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            if (!AutoVideo.startvideo(Objects.requireNonNull(XxqgFuntion.listinfo("cn.xuexi.android:id/general_card_title_id", "中央广播电视总台")))) {
                return false;
            }
        }

//       短视频
        if (LdkConfig.isDuan_video()) {
            LogUtil.D("开始短视频");
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            UiApi.clickNodeByTextWithTimeOut(2000, "百灵");
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            if (!AutoVideo.start_duan_video()) {
                return false;
            }
        }

//      进入积分页面，自动答题
        if (LdkConfig.isDati()) {
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            return Autoanswer.doactivity();
        } else {
            if(ThreadSleepTime.sleep2()){
                return false;
            }
            if(!Autoanswer.into_ji_fen_page()){
                return false;
            }
            if(ThreadSleepTime.sleep2()){
                return false;
            }
            if(!XxqgFuntion.fen_shu()){
                return false;
            }
            LogUtil.D("不答题");
        }
        return true;
    }

    public static boolean opensichuan() {
        LogUtil.D("点击四川频道");
        if (AcessibilityApi.clickTextViewByText("四川")) {
            LogUtil.D("四川点击成功");
            if (ThreadSleepTime.sleep0D2()) {
                return false;
            }
            if (UiApi.clickNodeByTextWithTimeOut(0, "四川学习平台")) {
                LogUtil.D("点击四川学习平台成功");
                if (ThreadSleepTime.sleep1D5()) {
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
//        判断是否进入软件
        for (int i = 0; i < 10; i++) {
            String s = AcessibilityApi.getEventPkg();
            if (s.equals("cn.xuexi.android")) {
                LogUtil.D("已打开大国");
                for (int m = 0; m < 20; m++) {
                    if (isPage()) {
                        LogUtil.D("进入大国首页");
                        return true;
                    }
                    if (ThreadSleepTime.sleep1()) {
                        return false;
                    }
                }
                return false;
            }
            if (ThreadSleepTime.sleep1()) {
                return false;
            }
        }

        LogUtil.D("进入学习强国失败");
        return false;
    }

    //    判断是否在学习强国首页
    public static boolean isPage() {
        String pageStr = "{maxMustMills:1000,"
                + "'maxOptionMills':1000,"
                + "'must':{'text':[],'id':[],'desc':[]},"
                + "'option':{'text':['推荐'],'id':[],'desc':[]}"
                + "}";

        return UiApi.isMyNeedPage(pageStr);
    }
}
