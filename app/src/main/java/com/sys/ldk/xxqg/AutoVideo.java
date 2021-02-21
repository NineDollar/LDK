package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sys.ldk.xxqg.ReturnType.SUCCESS;
import static com.sys.ldk.xxqg.ReturnType.my_stop;
import static java.sql.Types.NULL;

public class AutoVideo {
    //    最大观看时间5分钟
    private final static int MAXshijina = 1 * 5;

    public static boolean auto_video() {
        AcessibilityApi.clickTextViewByText("电视台");
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        AcessibilityApi.clickTextViewByText("第一频道");
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        startvido(Objects.requireNonNull(di_yi_ping_dao()));
        if (ThreadSleepTime.sleep2()) {
            return false;
        }

//        联播频道
        AcessibilityApi.clickTextViewByText("联播频道");
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        return startvido(Objects.requireNonNull(XxqgFuntion.listinfo("cn.xuexi.android:id/general_card_title_id", "中央广播电视总台")));
    }

    public static boolean startvido(List<AccessibilityNodeInfo> accessibilityNodeInfos) {
        if (accessibilityNodeInfos.isEmpty()) {
            return false;
        }
        for (AccessibilityNodeInfo a : accessibilityNodeInfos
        ) {
            LogUtil.V("time: " + a.getText());
        }
        for (AccessibilityNodeInfo a : accessibilityNodeInfos
        ) {
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            LogUtil.V("text: " + a.getText());
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
            int shi_jian = 0;
            while (shi_jian++ < MAXshijina) {
                LogUtil.D("睡眠：" + shi_jian + "秒");
                if (watch_end() == SUCCESS) {
                    break;
                } else if (watch_end() == my_stop) {
                    return false;
                }
            }
            LogUtil.V("结束");
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
        LogUtil.V("第一视频总数：" + timeinfo.size());
        return timeinfo;
    }
}
