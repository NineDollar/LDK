package com.sys.ldk.dg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.List;

import static com.sys.ldk.dg.Config.read_time;

public class AutoRead {

    public static boolean auto_read() {
        LogUtil.D("要闻---开始阅读");
        return yao_wen("要闻");
    }

    public static boolean yao_wen(String tuijianORyaowen) {
//        观看次数
        int q = 1;
        AcessibilityApi.clickTextViewByText(tuijianORyaowen);
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        AcessibilityApi.clickTextViewByText(tuijianORyaowen);
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.findViewByid_list("cn.xuexi.android:id/general_card_title_id");
        assert accessibilityNodeInfoList != null;
        int m = accessibilityNodeInfoList.size();
        LogUtil.I("size： " + m);
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            LogUtil.D("text: " + a.getText());
        }

        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            LogUtil.D("观看: " + m);
            LogUtil.D(a.getText() + "");
            if (!AcessibilityApi.performViewClick(a)) {
                return false;
            }
//            如果页面有视频，则点击页面视频button
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            fenxiang();
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            video();
//            阅读时间
            long read_time1 = read_time;
            LogUtil.D("阅读：" + (double) read_time1 / 1000 + "秒");
//             毫秒
            if (ThreadSleepTime.sleep(read_time)) {
                return false;
            }

            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
        }
        LogUtil.D("观看出错");
        return false;
    }

    private static void video() {
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
        if (ThreadSleepTime.sleep1()) {
            return false;
        }

        if (ThreadSleepTime.sleep1()) {
            return false;
        }
        AcessibilityApi.clickTextViewByText("分享到学习强国");
        if (ThreadSleepTime.sleep1()) {
            return false;
        }
        AcessibilityApi.clickTextViewByText("余波...");
        if (ThreadSleepTime.sleep1()) {
            return false;
        }
        AcessibilityApi.clickTextViewByText("取消");
        if (ThreadSleepTime.sleep1()) {
            return false;
        }
        AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
        return true;
    }
}


