package com.sys.ldk.dg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sys.ldk.dg.LdkConfig.getRead_time_second;

public class AutoRead {
    //        观看次数
    private static int q = 0;
    private static int time = 5;
    private final static String tuijian = "推荐";
    private final static String yaowen = "要闻";
    public static List<String> textlist = new ArrayList<>();

    public static boolean auto_read() {
        LogUtil.D("要闻---开始阅读");

        /*if (!UiApi.clickNodeByTextWithTimeOut(0, tuijian)) {
            return false;
        }*/
        for (int i = 0; i < 3; i++) {
            if (!startread()) {
                return false;
            }
            if (ThreadSleepTime.sleep0D5()) {
                return false;
            }
            AcessibilityApi.ScrollNode(Objects.requireNonNull(AcessibilityApi.findViewByCls("android.widget.ListView")).get(3), 1);
        }
        LogUtil.D(tuijian + "观看完毕");
        if (ThreadSleepTime.sleep0D5()) {
            return false;
        }
        if (!UiApi.clickNodeByTextWithTimeOut(0, yaowen)) {
            return false;
        }
        for (int i = 0; i < 5; i++) {
            if (!startread()) {
                return false;
            }
            if (ThreadSleepTime.sleep0D5()) {
                return false;
            }
            AcessibilityApi.ScrollNode(Objects.requireNonNull(AcessibilityApi.findViewByCls("android.widget.ListView")).get(4), 1);
        }
        LogUtil.D(yaowen + "观看完毕");

        textlist.clear();
        q = 0;
        return true;
    }

    public static boolean startread() {
        boolean alreadyflag = false;
        if (ThreadSleepTime.sleep1()) {
            return false;
        }
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.findViewByid_list("cn.xuexi.android:id/general_card_title_id");
        assert accessibilityNodeInfoList != null;
        int m = accessibilityNodeInfoList.size();
        LogUtil.I("总共：" + m);

        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            LogUtil.D("text:" + a.getText());
        }

        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            if (q >= LdkConfig.getReading_times()) {
                LogUtil.I("到达最大文章阅读次数");
                break;
            }
            String atext = a.getText() + "";
            for (String s : textlist
            ) {
                if (atext.equals(s)) {
                    alreadyflag = true;
                    break;
                }
            }
            if (alreadyflag) {
                LogUtil.W("重复,跳过");
                alreadyflag = false;
                continue;
            }
            q++;
            LogUtil.D("观看第: " + q + " 次");
            LogUtil.D(atext);
            if (!AcessibilityApi.performViewClick(a)) {
                return false;
            }
//            分享
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            if (q < 3) {
                fenxiang();
            }
            if (ThreadSleepTime.sleep1()) {
                return false;
            }
//            如果页面有视频，则点击页面视频button

            isvideo();
//             毫秒
//            阅读时间
            LogUtil.I("阅读：" + getRead_time_second() + " 秒");
            int n = getRead_time_second() / time;
            for (int i = 0; i < n; i++) {
                if (ThreadSleepTime.sleep(time * 1000)) {
                    return false;
                }
                boolean isend = AcessibilityApi.ScrollNode(Objects.requireNonNull(AcessibilityApi.findViewByCls("android.webkit.WebView")).get(0), 1);
                if (!isend) {
                    LogUtil.D("观看结束");
                    break;
                }
            }

            LogUtil.I("第 " + q + " 次结束");
            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
        }

        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            textlist.add(a.getText() + "");
        }
        return true;
    }

    private static void isvideo() {
        List<AccessibilityNodeInfo> accessibilityNodeInfos = AcessibilityApi.findViewByCls("android.widget.Button");
        assert accessibilityNodeInfos != null;
        if (accessibilityNodeInfos.size() > 0) {
            AcessibilityApi.performViewClick(accessibilityNodeInfos.get(0));
        } else {
            LogUtil.D("页面没有视频");
        }
    }

    //  收藏和分享
    private static boolean fenxiang() {
        if (!User.findtext("欢迎发表你的观点")) {
            return false;
        }
        LogUtil.D("开始分享");
        List<AccessibilityNodeInfo> allInfo = AcessibilityApi.getAllNode(null, null);
        int size = allInfo.size();
        LogUtil.D("sixe: " + size);
        if (allInfo.isEmpty()) {
            return false;
        }

        AcessibilityApi.performViewClick(allInfo.get(size - 1));

        if (ThreadSleepTime.sleep0D5()) {
            return false;
        }
        UiApi.clickNodeByTextWithTimeOut(0, "分享到学习强国");
//        AcessibilityApi.clickTextViewByText("分享到学习强国");
        if (ThreadSleepTime.sleep0D5()) {
            return false;
        }
//        AcessibilityApi.clickTextViewByText("余波...");
        UiApi.clickNodeByIdWithTimeOut(0, "cn.xuexi.android:id/session_icon");

        if (ThreadSleepTime.sleep0D5()) {
            return false;
        }
        UiApi.clickNodeByTextWithTimeOut(0, "取消");
//        AcessibilityApi.clickTextViewByText("取消");
        if (ThreadSleepTime.sleep0D2()) {
            return false;
        }
        AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
        return true;
    }
}


