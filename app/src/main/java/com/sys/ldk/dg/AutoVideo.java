package com.sys.ldk.dg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sys.ldk.dg.LdkConfig.getVideo_time_second;
import static com.sys.ldk.dg.ReturnType.SUCCESS;
import static com.sys.ldk.dg.ReturnType.my_stop;
import static java.sql.Types.NULL;

public class AutoVideo {
    private static int q = 0;//次数
    private static int time = 0;

    public static boolean auto_video() {
        if (!AcessibilityApi.clickTextViewByText("电视台")) {
            return false;
        }
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        if (!AcessibilityApi.clickTextViewByText("第一频道")) {
            return false;
        }
        if (ThreadSleepTime.sleep2()) {

            return false;
        }
        if (!startvido(Objects.requireNonNull(di_yi_ping_dao()))) {
            return false;
        }

//        联播频道
        if (LdkConfig.isIs_xin_wen_lian_bo()) {
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
            if(!startvido(Objects.requireNonNull(XxqgFuntion.listinfo("cn.xuexi.android:id/general_card_title_id", "中央广播电视总台")))){
                return false;
            }
        }

//       短视频
        if(LdkConfig.isDuan_video()){
            LogUtil.D("开始短视频");
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
        }

        return true;
    }

    public static boolean startvido(List<AccessibilityNodeInfo> accessibilityNodeInfos) {

        if (accessibilityNodeInfos.isEmpty()) {
            return false;
        }
        LogUtil.D("总共：" + accessibilityNodeInfos.size() + " 个视频");
        for (AccessibilityNodeInfo a : accessibilityNodeInfos
        ) {
            LogUtil.D("text: " + a.getText());
        }

        for (AccessibilityNodeInfo a : accessibilityNodeInfos
        ) {
            q++;
            //最大观看时间
            if (q > LdkConfig.getVideoing_times()) {
                LogUtil.I("到达视频最大观看次数");
                if (ThreadSleepTime.sleep1()) {
                    return false;
                }
                break;
            }
            int video_max = getVideo_time_second();
            LogUtil.D("开始观看第 " + q + " 个视频");
            LogUtil.D("text: " + a.getText());
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            AcessibilityApi.performViewClick(a);
            if (UiApi.findNodeByTextWithTimeOut(2000, "欢迎发表你的观点") == null) {
                LogUtil.W("观看失败");
                return false;
            } else {
                LogUtil.I("正在观看");
            }
//            流量播放
            AcessibilityApi.performViewClick(UiApi.findNodeByTextWithTimeOut(2000, "继续播放"));
            if (ThreadSleepTime.sleep2()) {
                return false;
            }

            LogUtil.I("观看视频：" + video_max + "秒");
            while (true) {
                LogUtil.V("睡眠：" + time + "秒");

                int i = watch_end();
                if (time++ > video_max || i == SUCCESS) {
                    break;
                } else if (i == my_stop) {
                    return false;
                }

            }
            time = 0;
            LogUtil.D("第" + q + " 个视频结束");
            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
        }
        q = 0;
        LogUtil.D("视频观看完毕");
        return true;
    }

    //    观看是否结束
    private static int watch_end() {
        if (AcessibilityApi.findViewByText("重新播放") != null) {
            LogUtil.D("观看完毕");
            return SUCCESS;
        } else {
            if (ThreadSleepTime.sleep1()) {
                return my_stop;
            }
        }
        return NULL;
    }



    /**
     * 获取第一视频里的info
     *
     * @return
     */
    public static List<AccessibilityNodeInfo> di_yi_ping_dao() {
        int sum = 0;
        int j = 0;
        int i = 0;
        List<AccessibilityNodeInfo> timeinfo = new ArrayList<>();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        sum = accessibilityNodeInfoList.size() - 2;
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            if (j < sum) {
                i = j;
            }
            if (XxqgFuntion.is_video_time(a.getText() + "")) {
                if ("中央广播电视总台".equals(accessibilityNodeInfoList.get(i - 2).getText() + "") || "中央广播电视总台".equals(accessibilityNodeInfoList.get(i + 2).getText() + "")) {
                    timeinfo.add(a);
                }
            }
            i = 0;
            j++;
        }
        if (timeinfo.isEmpty()) {
            return null;
        }
        LogUtil.D("第一视频总数：" + timeinfo.size());
        return timeinfo;
    }
}
