package com.sys.ldk.dg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.List;

import static com.sys.ldk.dg.LdkConfig.getRead_time_Mill;
import static com.sys.ldk.dg.LdkConfig.getRead_time_second;

public class AutoRead {
    //        观看次数
    private static int q = 0;

    public static boolean auto_read() {
        LogUtil.D("要闻---开始阅读");
        return yao_wen("要闻");
    }

    public static boolean yao_wen(String tuijianORyaowen) {
        AcessibilityApi.clickTextViewByText(tuijianORyaowen);
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        /*AcessibilityApi.clickTextViewByText(tuijianORyaowen);
        if (ThreadSleepTime.sleep2()) {
            return false;
        }*/
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.findViewByid_list("cn.xuexi.android:id/general_card_title_id");
        assert accessibilityNodeInfoList != null;
        int m = accessibilityNodeInfoList.size();
        LogUtil.I("总共： " + m);
    /*    for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            LogUtil.D("text: " + a.getText());
        }*/

        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            q++;
            if (q > LdkConfig.getReading_times()){
                LogUtil.I("到达最大文章阅读次数");
                break;
            }
            LogUtil.D("观看第: " + q + " 次");
            LogUtil.D(a.getText() + "");
            if (!AcessibilityApi.performViewClick(a)) {
                return false;
            }
//            如果页面有视频，则点击页面视频button
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            if (q < 3) {
                fenxiang();
            }
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
            isvideo();
//             毫秒
//            阅读时间
            LogUtil.I("阅读：" + getRead_time_second() + " 秒");
            if (ThreadSleepTime.sleep(getRead_time_Mill())) {
                return false;
            }
            LogUtil.I("第 " + q + " 次结束");
            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
            if (ThreadSleepTime.sleep2()) {
                return false;
            }
        }
        q = 0;
        LogUtil.D("观看完毕");
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
//        AcessibilityApi.clickTextViewByText("余波...");
        UiApi.clickNodeByIdWithTimeOut(3000, "cn.xuexi.android:id/session_icon");

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


