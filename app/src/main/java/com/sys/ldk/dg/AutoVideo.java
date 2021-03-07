package com.sys.ldk.dg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sys.ldk.dg.Config.getRead_time_second;
import static com.sys.ldk.dg.Config.getVideo_time_Mill;
import static com.sys.ldk.dg.Config.getVideo_time_second;
import static com.sys.ldk.dg.Config.is_xin_wen_lian_bo;
import static com.sys.ldk.dg.Config.video_time;
import static com.sys.ldk.dg.ReturnType.SUCCESS;
import static com.sys.ldk.dg.ReturnType.my_stop;
import static java.sql.Types.NULL;

public class AutoVideo {
    //    最大观看时间5分钟

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
        if (ThreadSleepTime.sleep2()) {
            return false;
        }

//        联播频道
        if (is_xin_wen_lian_bo) {
            LogUtil.D("开始观看新闻联播");
            if (!AcessibilityApi.clickTextViewByText("联播频道")) {
                return false;
            }
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            return startvido(Objects.requireNonNull(XxqgFuntion.listinfo("cn.xuexi.android:id/general_card_title_id", "中央广播电视总台")));
        }
        return true;
    }

    public static boolean startvido(List<AccessibilityNodeInfo> accessibilityNodeInfos) {
        int q = 0;
        int video_max = getVideo_time_second();//换成秒
        int shi_jian = 0;

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
            shi_jian = 1;
            LogUtil.D("开始观看第 " + q + " 个视频");
            LogUtil.D("text: " + a.getText());
            LogUtil.D("观看视频：" + video_max + "秒");
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
//                判断观看是否结束
            while (shi_jian++ < video_max) {
                LogUtil.V("睡眠：" + shi_jian + "秒");
                if (watch_end() == SUCCESS) {
                    break;
                } else if (watch_end() == my_stop) {
                    return false;
                }
            }
            LogUtil.D(a.getText() + "结束");
            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
        }
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

    public static boolean Data(String str) {
        String regex = "[0-9]{2}:[0-9]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        boolean dateFlag = m.matches();
        if (!dateFlag) {
            return false;
        }
        return true;
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
            if (AutoVideo.Data(a.getText() + "")) {
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
